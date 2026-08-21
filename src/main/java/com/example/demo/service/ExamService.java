package com.example.demo.service;

import com.example.demo.dto.ExamGenerateRequest;
import com.example.demo.dto.ExamGenerateResponse;
import com.example.demo.dto.ExamSubmitRequest;
import com.example.demo.dto.ExamSubmitResponse;

public interface ExamService {
    ExamGenerateResponse generateExam(ExamGenerateRequest request);
    ExamSubmitResponse submitAndGradeExam(ExamSubmitRequest request);
}