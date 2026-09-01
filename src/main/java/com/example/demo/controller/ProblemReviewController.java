package com.example.demo.controller;

import com.example.demo.dto.ProblemReviewResponse;
import com.example.demo.service.ProblemReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

    // GET /api/problems/review?domainId={id}
    @GetMapping("/review")
    public ResponseEntity<ProblemReviewResponse> getReviewProblems(
            @RequestParam(required = false) String domainId) {

        ProblemReviewResponse response = problemReviewService.getProblemsForReview(domainId);
        return ResponseEntity.ok(response);
    }
}
