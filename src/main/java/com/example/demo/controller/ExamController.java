package com.example.demo.controller;

import com.example.demo.dto.ExamGenerateRequest;
import com.example.demo.dto.ExamGenerateResponse;
import com.example.demo.dto.ExamSubmitRequest;
import com.example.demo.dto.ExamSubmitResponse;
import com.example.demo.service.ExamService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/exams")
public class ExamController {

    private final ExamService examService;

    public ExamController(ExamService examService) {
        this.examService = examService;
    }

    @PostMapping("/generate")
    public ResponseEntity<ExamGenerateResponse> generateExam(@Valid @RequestBody ExamGenerateRequest request) {

        if (request.getGenerationType() == null || request.getGenerationType().trim().isEmpty()) {
            throw new IllegalArgumentException("출제 타입(generationType)은 필수입니다. (CHAPTER 또는 MOCK_EXAM)");
        }

        if ("CHAPTER".equalsIgnoreCase(request.getGenerationType())) {
            if (request.getDomainId() == null || request.getDomainId().trim().isEmpty()) {
                throw new IllegalArgumentException("단원별 출제(CHAPTER) 모드에서는 domainId가 필수입니다.");
            }
            // [추가된 검증] AI에게 알려줄 단원명도 필수
            if (request.getDomainName() == null || request.getDomainName().trim().isEmpty()) {
                throw new IllegalArgumentException("단원별 출제(CHAPTER) 모드에서는 domainName(단원명)이 필수입니다.");
            }
            if (request.getProblemCount() == null || request.getProblemCount() <= 0) {
                throw new IllegalArgumentException("단원별 출제(CHAPTER) 모드에서는 출제할 문제 수(problemCount)를 1 이상 명시해야 합니다.");
            }
        }

        ExamGenerateResponse response = examService.generateExam(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/submit")
    public ResponseEntity<ExamSubmitResponse> submitExam(@Valid @RequestBody ExamSubmitRequest request) {
        if (request.getExamId() == null || request.getExamId().trim().isEmpty()) {
            throw new IllegalArgumentException("시험지 ID(examId)는 필수입니다.");
        }
        if (request.getAnswers() == null || request.getAnswers().isEmpty()) {
            throw new IllegalArgumentException("제출할 답안 목록(answers)이 비어 있습니다.");
        }

        for (ExamSubmitRequest.AnswerDto answer : request.getAnswers()) {
            if (answer.getProblemId() == null || answer.getProblemId().trim().isEmpty()) {
                throw new IllegalArgumentException("답안 데이터에 문제 ID(problemId)가 누락되었습니다.");
            }
            if (answer.getUserAnswer() == null) {
                throw new IllegalArgumentException("문제 ID " + answer.getProblemId() + "의 userAnswer가 null입니다.");
            }
        }

        ExamSubmitResponse response = examService.submitAndGradeExam(request);
        return ResponseEntity.ok(response);
    }
}
