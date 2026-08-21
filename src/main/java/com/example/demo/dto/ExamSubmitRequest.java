package com.example.demo.dto;

import java.util.List;

public class ExamSubmitRequest {
    private String examId;
    private List<AnswerDto> answers;

    public ExamSubmitRequest() {}

    public String getExamId() { return examId; }
    public void setExamId(String examId) { this.examId = examId; }
    public List<AnswerDto> getAnswers() { return answers; }
    public void setAnswers(List<AnswerDto> answers) { this.answers = answers; }

    public static class AnswerDto {
        private String problemId;
        private String userAnswer;

        public AnswerDto() {}
        public String getProblemId() { return problemId; }
        public void setProblemId(String problemId) { this.problemId = problemId; }
        public String getUserAnswer() { return userAnswer; }
        public void setUserAnswer(String userAnswer) { this.userAnswer = userAnswer; }
    }
}
