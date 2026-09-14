package com.example.demo.controller;

import com.example.demo.dto.ProblemReviewResponse;
import com.example.demo.dto.SingleProblemGradeRequest;
import com.example.demo.service.ProblemReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/problems")
public class ProblemReviewController {

    private final ProblemReviewService problemReviewService;

    public ProblemReviewController(ProblemReviewService problemReviewService) {
        this.problemReviewService = problemReviewService;
    }

    /**
     * 특정 단원(domainId) 또는 전체 범위의 오답 및 미풀이 문제 조회
     * GET /api/problems/review?domainId={id}
     */
    @GetMapping("/review")
    public ResponseEntity<ProblemReviewResponse> getReviewProblems(
            @RequestParam(required = false) String domainId) {

        ProblemReviewResponse response = problemReviewService.getProblemsForReview(domainId);
        return ResponseEntity.ok(response);
    }

    /**
     * 단일 문제 AI 재채점 수행 및 결과 즉시 반환
     * POST /api/problems/review/grade
     */
    @PostMapping("/review/grade")
    public ResponseEntity<ProblemReviewResponse.ProblemDto> gradeSingleProblem(
            @RequestBody SingleProblemGradeRequest request) {

        // ProblemReviewService에 새로 추가한 gradeSingleProblem 메서드 호출
        ProblemReviewResponse.ProblemDto updatedProblem = problemReviewService.gradeSingleProblem(
                request.getProblemId(),
                request.getUserAnswer()
        );

        return ResponseEntity.ok(updatedProblem);
    }
}