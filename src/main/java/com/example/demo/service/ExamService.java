package com.example.demo.service;

import com.example.demo.dto.ExamGenerateRequest;
import com.example.demo.dto.ExamGenerateResponse;
import com.example.demo.dto.ExamSubmitRequest;
import com.example.demo.dto.ExamSubmitResponse;

public interface ExamService {

    // 1. 문제 출제
    ExamGenerateResponse generateExam(ExamGenerateRequest request);

    // 2. 기존 동기 채점 방식 (내부 스레드용)
    ExamSubmitResponse submitAndGradeExam(ExamSubmitRequest request);

    // --------------------------------------------------------
    // [추가된 비동기(폴링) 방식 채점 메서드 선언부]
    // --------------------------------------------------------

    // 비동기 채점 작업 할당 및 Job ID 반환
    String submitExamAsync(ExamSubmitRequest request);

    // 채점 진행 상태 조회 ("PROCESSING", "COMPLETED", "FAILED")
    String getSubmitStatus(String jobId);

    // 채점 완료 결과 반환
    ExamSubmitResponse getSubmitResult(String jobId);
}