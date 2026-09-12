package com.example.demo.service;

import com.example.demo.GeminiService;
import com.example.demo.domain.Exam;
import com.example.demo.domain.Problem;
import com.example.demo.domain.QuestionFormatTemplate;
import com.example.demo.dto.ExamGenerateRequest;
import com.example.demo.dto.ExamGenerateResponse;
import com.example.demo.dto.ExamSubmitRequest;
import com.example.demo.dto.ExamSubmitResponse;
import com.example.demo.repository.ExamRepository;
import com.example.demo.repository.ProblemRepository;
import com.example.demo.repository.QuestionFormatTemplateRepository;

import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
public class ExamServiceImpl implements ExamService {

    private final ProblemRepository problemRepository;
    private final ExamRepository examRepository;
    private final QuestionFormatTemplateRepository templateRepository;
    private final GeminiService geminiService;

    // 비동기 작업 상태 및 결과를 저장하는 Thread-Safe Map
    private final Map<String, String> statusMap = new ConcurrentHashMap<>();
    private final Map<String, ExamSubmitResponse> resultMap = new ConcurrentHashMap<>();

    private record DomainData(String id, String name) {}

    public ExamServiceImpl(ProblemRepository problemRepository,
                           ExamRepository examRepository,
                           QuestionFormatTemplateRepository templateRepository,
                           GeminiService geminiService) {
        this.problemRepository = problemRepository;
        this.examRepository = examRepository;
        this.templateRepository = templateRepository;
        this.geminiService = geminiService;
    }

    @Override
    @Transactional
    public ExamGenerateResponse generateExam(ExamGenerateRequest request) {
        String examTitle;
        List<DomainData> targetDomains = new ArrayList<>();

        if ("CHAPTER".equalsIgnoreCase(request.getGenerationType())) {
            examTitle = request.getDomainName() + " 단원별 평가";

            for (int i = 0; i < request.getProblemCount(); i++) {
                targetDomains.add(new DomainData(request.getDomainId(), request.getDomainName()));
            }
        } else {
            examTitle = "전범위 AI 모의고사";
            Random random = new Random();
            int targetCount = random.nextBoolean() ? 11 : 12;

            String[] mockDomainNames = {
                    "분자생물학", "세포학", "유전학", "동물생리학",
                    "식물생리학", "생태학", "진화생물학", "미생물학", "면역학", "발생학"
            };

            for (int i = 0; i < targetCount; i++) {
                String randomName = mockDomainNames[random.nextInt(mockDomainNames.length)];
                String fakeId = UUID.randomUUID().toString();
                targetDomains.add(new DomainData(fakeId, randomName));
            }
            Collections.shuffle(targetDomains);
        }

        Exam exam = new Exam();
        exam.setExamType(request.getGenerationType());
        exam.setTitle(examTitle);
        exam.setTotalScore(targetDomains.size() * 100);
        exam = examRepository.save(exam);

        List<QuestionFormatTemplate> formats = templateRepository.findAll();
        if (formats.isEmpty()) {
            QuestionFormatTemplate fallback = new QuestionFormatTemplate();
            fallback.setTypeName("기본 서술형");
            fallback.setQuestionExample("제시된 개념에 대해 상세히 서술하시오.");
            fallback.setAnswerExample("개념 정의 및 특징을 명확히 작성");
            formats = Collections.singletonList(fallback);
        } else {
            Collections.shuffle(formats);
        }

        int threadCount = Math.min(targetDomains.size(), 10);
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        List<CompletableFuture<Problem>> futures = new ArrayList<>();
        JsonParser springJsonParser = JsonParserFactory.getJsonParser();

        for (int i = 0; i < targetDomains.size(); i++) {
            final int index = i;
            final DomainData currentDomainData = targetDomains.get(i);
            final QuestionFormatTemplate assignedFormat = formats.get(i % formats.size());
            final String examId = exam.getId();

            CompletableFuture<Problem> future = CompletableFuture.supplyAsync(() -> {
                Problem newProblem = new Problem();
                newProblem.setExamId(examId);
                newProblem.setDomainId(currentDomainData.id());
                newProblem.setProblemNumber(index + 1);
                newProblem.setQuestionType(assignedFormat.getTypeName());
                newProblem.setOptions(new ArrayList<>());

                String generatePrompt = String.format(
                        "당신은 생명과학 전공 임용고시 출제 위원입니다. 다음 '출제 단원'과 '출제 양식'을 엄격히 반영하여 실제 임용고시(필요시 인터넷을 참고하세요) 수준의 새로운 문제를 1개 출제하세요.\n" +
                                "또한 출제 양식을 준수하고 답안이 너무 길어지는 것을 방지하세요.(한문제의 핵심키워드에 대한 설명 수준)\n" +
                                "반드시 아래의 JSON 포맷으로만 응답해야 하며, 마크다운이나 부가 설명은 절대 추가하지 마세요.\n\n" +
                                "출력 포맷: {\"title\": \"문제 제목\", \"content\": \"문제 지문 내용\", \"referenceAnswer\": \"모범 답안\", \"rubric\": [\"채점 기준 1\", \"채점 기준 2\"]}\n\n" +
                                "[출제 단원]: %s\n" +
                                "[요구되는 출제 양식]: %s\n" +
                                "[답안 작성 예시]: %s",
                        currentDomainData.name(), assignedFormat.getQuestionExample(), assignedFormat.getAnswerExample()
                );

                int maxRetries = 2;
                int attempt = 0;
                boolean success = false;

                while (attempt < maxRetries && !success) {
                    try {
                        String rawAiResponse = geminiService.generateContent(generatePrompt);
                        String cleanJson = rawAiResponse.replaceAll("```json", "").replaceAll("```", "").trim();
                        Map<String, Object> parsedMap = springJsonParser.parseMap(cleanJson);

                        newProblem.setTitle((String) parsedMap.getOrDefault("title", currentDomainData.name() + " 문제"));
                        newProblem.setContent((String) parsedMap.getOrDefault("content", "문제 지문 생성 실패"));
                        newProblem.setReferenceAnswer((String) parsedMap.getOrDefault("referenceAnswer", "모범 답안 생성 실패"));

                        List<Map<String, Object>> rubricMapList = new ArrayList<>();
                        Object rubricObj = parsedMap.get("rubric");

                        if (rubricObj instanceof List) {
                            List<?> rawList = (List<?>) rubricObj;
                            for (Object item : rawList) {
                                Map<String, Object> mapItem = new HashMap<>();
                                mapItem.put("criteria", String.valueOf(item));
                                rubricMapList.add(mapItem);
                            }
                        } else {
                            Map<String, Object> defaultMap = new HashMap<>();
                            defaultMap.put("criteria", "채점 기준 없음");
                            rubricMapList.add(defaultMap);
                        }
                        newProblem.setRubric(rubricMapList);
                        success = true;

                    } catch (Exception e) {
                        attempt++;
                        if (attempt >= maxRetries) {
                            System.err.println("문제 생성 최종 실패 (문제 번호: " + (index + 1) + ") - " + e.getMessage());
                            newProblem.setTitle(currentDomainData.name() + " 문제 (생성 오류)");
                            newProblem.setContent("AI 서버 응답 지연으로 인해 문제 지문을 생성하지 못했습니다.");
                            newProblem.setReferenceAnswer("오류");

                            List<Map<String, Object>> errorRubricList = new ArrayList<>();
                            Map<String, Object> errorMap = new HashMap<>();
                            errorMap.put("criteria", "오류");
                            errorRubricList.add(errorMap);
                            newProblem.setRubric(errorRubricList);
                        } else {
                            try { Thread.sleep(1000); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
                        }
                    }
                }
                return newProblem;
            }, executor);

            futures.add(future);
        }

        List<Problem> actualProblemsToSave = futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());

        executor.shutdown();

        List<Problem> savedProblems = problemRepository.saveAll(actualProblemsToSave);
        savedProblems.sort(Comparator.comparingInt(Problem::getProblemNumber));

        List<ExamGenerateResponse.ProblemDto> problemDtos = new ArrayList<>();
        for (Problem p : savedProblems) {
            problemDtos.add(new ExamGenerateResponse.ProblemDto(
                    p.getId(),
                    p.getProblemNumber(),
                    p.getQuestionType(),
                    p.getTitle(),
                    p.getContent(),
                    p.getOptions()
            ));
        }

        return new ExamGenerateResponse(
                exam.getId(),
                exam.getTitle(),
                request.getGenerationType(),
                problemDtos
        );
    }

    // [추가] 비동기 채점 작업 할당 및 JobID 반환
    @Override
    public String submitExamAsync(ExamSubmitRequest request) {
        String jobId = UUID.randomUUID().toString();
        statusMap.put(jobId, "PROCESSING");

        // CompletableFuture를 이용해 백그라운드 스레드에서 채점 진행
        CompletableFuture.runAsync(() -> {
            try {
                ExamSubmitResponse response = submitAndGradeExam(request);
                resultMap.put(jobId, response);
                statusMap.put(jobId, "COMPLETED");
            } catch (Exception e) {
                System.err.println("비동기 채점 중 오류 발생: " + e.getMessage());
                statusMap.put(jobId, "FAILED");
            }
        });

        return jobId;
    }

    // [추가] 상태 조회
    @Override
    public String getSubmitStatus(String jobId) {
        return statusMap.getOrDefault(jobId, "NOT_FOUND");
    }

    // [추가] 결과 조회
    @Override
    public ExamSubmitResponse getSubmitResult(String jobId) {
        return resultMap.get(jobId);
    }

    // 기존의 채점 로직 (내부 스레드에서 호출됨)
    @Override
    @Transactional
    public ExamSubmitResponse submitAndGradeExam(ExamSubmitRequest request) {
        Exam exam = examRepository.findById(request.getExamId())
                .orElseThrow(() -> new IllegalArgumentException("시험지를 찾을 수 없습니다."));

        exam.setIsCompleted(true);
        exam.setSubmittedAt(LocalDateTime.now());

        int obtainedScore = 0;
        List<ExamSubmitResponse.ResultDto> results = new ArrayList<>();
        JsonParser springJsonParser = JsonParserFactory.getJsonParser();

        for (ExamSubmitRequest.AnswerDto answerDto : request.getAnswers()) {
            Problem problem = problemRepository.findById(answerDto.getProblemId())
                    .orElseThrow(() -> new IllegalArgumentException("문제를 찾을 수 없습니다."));

            String gradingPrompt = String.format(
                    "당신은 엄격한 채점관입니다. 유저의 답안을 모범 답안 및 채점 기준표에 대조하여 100점 만점으로 평가하세요.\n" +
                            "반드시 아래의 JSON 포맷으로만 응답해야 하며, 마크다운이나 부가 설명은 절대 추가하지 마세요.\n\n" +
                            "출력 포맷: {\"score\": 점수(정수), \"summary\": \"총평 요약\", \"strengths\": \"잘 작성된 점\", \"deductions\": \"감점 사유\", \"missingKeywords\": [\"누락키워드1\", \"누락키워드2\"]}\n\n" +
                            "[모범 답안]: %s\n" +
                            "[채점 기준표]: %s\n" +
                            "[유저 답안]: %s",
                    problem.getReferenceAnswer(), problem.getRubric().toString(), answerDto.getUserAnswer()
            );

            String rawAiResponse = geminiService.generateContent(gradingPrompt);
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
                feedback.put("strengths", parsedMap.getOrDefault("strengths", "강점 데이터 없음"));
                feedback.put("deductions", parsedMap.getOrDefault("deductions", "감점 데이터 없음"));

                Object keywordsObj = parsedMap.get("missingKeywords");
                if (keywordsObj instanceof List) {
                    @SuppressWarnings("unchecked")
                    List<String> castedKeywords = (List<String>) keywordsObj;
                    missingKeywords = castedKeywords;
                }
                isCorrect = parsedScore >= 80;

            } catch (Exception e) {
                parsedScore = 0;
                feedback.put("summary", "AI 응답 파싱 에러 발생");
                feedback.put("strengths", "-");
                feedback.put("deductions", "AI 응답 규격 불량으로 인한 채점 불가: " + rawAiResponse);
            }

            problem.setUserAnswer(answerDto.getUserAnswer());
            problem.setScore(parsedScore);
            problem.setIsCorrect(isCorrect);
            problem.setAiFeedback(feedback);
            problem.setMissingKeywords(missingKeywords);

            problemRepository.save(problem);
            obtainedScore += parsedScore;

            results.add(new ExamSubmitResponse.ResultDto(
                    problem.getId(),
                    problem.getProblemNumber(),
                    problem.getScore(),
                    problem.getIsCorrect(),
                    problem.getAiFeedback(),
                    problem.getMissingKeywords()
            ));
        }

        exam.setObtainedScore(obtainedScore);
        examRepository.save(exam);

        return new ExamSubmitResponse(
                exam.getId(),
                exam.getTotalScore(),
                exam.getObtainedScore(),
                exam.getIsCompleted(),
                exam.getSubmittedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                results
        );
    }
}