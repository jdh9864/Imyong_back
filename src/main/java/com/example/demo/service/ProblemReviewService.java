package com.example.demo.service;

import com.example.demo.domain.Problem;
import com.example.demo.dto.ProblemReviewResponse;
import com.example.demo.repository.ProblemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProblemReviewService {

    private final ProblemRepository problemRepository;

    public ProblemReviewService(ProblemRepository problemRepository) {
        this.problemRepository = problemRepository;
    }

    public ProblemReviewResponse getProblemsForReview(String domainId) {
        List<Problem> targetProblems;
        boolean isFallback = false;

        // 1. 도메인 ID가 파라미터로 제공된 경우
        if (domainId != null && !domainId.trim().isEmpty()) {
            targetProblems = problemRepository.findReviewTargetsByDomainId(domainId);

            // 2. 해당 도메인에 오답/미풀이 문제가 없는 경우 전체 문제로 대체
            if (targetProblems.isEmpty()) {
                targetProblems = problemRepository.findAllReviewTargets();
                isFallback = true;
            }
        }
        // 3. 도메인 ID 자체가 제공되지 않은 경우 바로 전체 문제 조회
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
}
