package com.example.demo.service;

import com.example.demo.GeminiService;
import com.example.demo.domain.Problem;
import com.example.demo.dto.ProblemReviewResponse;
import com.example.demo.repository.ProblemRepository;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProblemReviewService {

    private final ProblemRepository problemRepository;
    private final GeminiService geminiService; // 단일 문제 재채점을 위한 AI 서비스 추가

    public ProblemReviewService(ProblemRepository problemRepository, GeminiService geminiService) {
        this.problemRepository = problemRepository;
        this.geminiService = geminiService;
    }

    /**
     * 조건에 따른 복습 대상 문제 목록 조회
     */
    @Transactional(readOnly = true)
    public ProblemReviewResponse getProblemsForReview(String domainId) {
        List<Problem> targetProblems;
        boolean isFallback = false;

        // 1. 도메인 ID가 파라미터로 제공된 경우
        if (domainId != null && !domainId.trim().isEmpty()) {
            targetProblems = problemRepository.findReviewTargetsByDomainId(domainId);

            // 2. 해당 단원에 오답/미풀이 문제가 없는 경우, 전체 범위의 문제로 대체 (Fallback)
            if (targetProblems.isEmpty()) {
                targetProblems = problemRepository.findAllReviewTargets();
                isFallback = true;
            }
        }
        // 3. 도메인 ID가 제공되지 않은 경우, 전체 범위의 오답/미풀이 문제 조회
        else {
            targetProblems = problemRepository.findAllReviewTargets();
            isFallback = true;
        }

        // 엔티티를 DTO로 변환
        List<ProblemReviewResponse.ProblemDto> dtos = targetProblems.stream()
                .map(p -> new ProblemReviewResponse.ProblemDto(
                        p.getId(),
                        p.getDomainId(),
                        p.getProblemNumber(),
                        p.getQuestionType(),
                        p.getTitle(),
                        p.getContent(),
                        p.getReferenceAnswer(),
                        p.getUserAnswer(),
                        p.getIsCorrect(),
                        p.getAiFeedback()
                ))
                .collect(Collectors.toList());

        return new ProblemReviewResponse(domainId, isFallback, dtos);
    }

    /**
     * 단일 문제 AI 재채점 수행 및 결과 저장
     *
     * @param problemId  채점할 대상 문제의 DB 식별자
     * @param userAnswer 사용자가 새롭게 작성한 답안
     * @return 채점 결과가 반영된 문제 DTO
     */
    @Transactional
    public ProblemReviewResponse.ProblemDto gradeSingleProblem(String problemId, String userAnswer) {
        // 1. 대상 문제 조회
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new IllegalArgumentException("해당 문제를 찾을 수 없습니다. ID: " + problemId));

        // 2. AI 평가 프롬프트 구성 (ExamServiceImpl과 동일한 엄격한 기준표 기반 채점)
        String gradingPrompt = String.format(
                "당신은 생명과학 전공 임용고시의 엄격한 채점관입니다. 유저의 답안을 모범 답안 및 채점 기준표에 대조하여 100점 만점으로 평가하세요.\n" +
                        "반드시 아래의 JSON 포맷으로만 응답해야 하며, 마크다운이나 부가 설명은 절대 추가하지 마세요.\n\n" +
                        "출력 포맷: {\"score\": 점수(정수), \"summary\": \"총평 요약\", \"strengths\": \"잘 작성된 점\", \"deductions\": \"감점 사유\", \"missingKeywords\": [\"누락키워드1\", \"누락키워드2\"]}\n\n" +
                        "[모범 답안]: %s\n" +
                        "[채점 기준표]: %s\n" +
                        "[유저 답안]: %s",
                problem.getReferenceAnswer(),
                problem.getRubric() != null ? problem.getRubric().toString() : "별도 채점 기준 없음",
                userAnswer
        );

        // 3. Gemini API 호출
        String rawAiResponse = geminiService.generateContent(gradingPrompt);

        // 4. 응답 데이터 파싱 및 정제
        JsonParser springJsonParser = JsonParserFactory.getJsonParser();
        int parsedScore = 0;
        Map<String, Object> feedback = new HashMap<>();
        List<String> missingKeywords = new ArrayList<>();
        boolean isCorrect = false;

        try {
            String cleanJson = rawAiResponse.replaceAll("```json", "").replaceAll("```", "").trim();
            Map<String, Object> parsedMap = springJsonParser.parseMap(cleanJson);

            Number scoreObj = (Number) parsedMap.getOrDefault("score", 0);
            parsedScore = scoreObj.intValue();

            feedback.put("summary", parsedMap.getOrDefault("summary", "평가 요약 없음"));
            feedback.put("strengths", parsedMap.getOrDefault("strengths", "-"));
            feedback.put("deductions", parsedMap.getOrDefault("deductions", "-"));

            Object keywordsObj = parsedMap.get("missingKeywords");
            if (keywordsObj instanceof List) {
                @SuppressWarnings("unchecked")
                List<String> castedKeywords = (List<String>) keywordsObj;
                missingKeywords = castedKeywords;
            }
            // 80점 이상을 정답으로 간주하는 내부 규격 적용
            isCorrect = parsedScore >= 80;

        } catch (Exception e) {
            parsedScore = 0;
            feedback.put("summary", "AI JSON 파싱 중 시스템 에러 발생");
            feedback.put("strengths", "-");
            feedback.put("deductions", "AI 응답 규격 불량으로 인한 자동 채점 실패: " + rawAiResponse);
        }

        // 5. DB 엔티티 상태 갱신
        problem.setUserAnswer(userAnswer);
        problem.setScore(parsedScore);
        problem.setIsCorrect(isCorrect);
        problem.setAiFeedback(feedback);
        problem.setMissingKeywords(missingKeywords);

        // 영속성 컨텍스트를 통한 업데이트 (트랜잭션 커밋 시 플러시)
        problemRepository.save(problem);

        // 6. 갱신된 데이터를 프론트엔드 소비용 DTO로 변환하여 반환
        return new ProblemReviewResponse.ProblemDto(
                problem.getId(),
                problem.getDomainId(),
                problem.getProblemNumber(),
                problem.getQuestionType(),
                problem.getTitle(),
                problem.getContent(),
                problem.getReferenceAnswer(),
                problem.getUserAnswer(),
                problem.getIsCorrect(),
                problem.getAiFeedback()
        );
    }
}