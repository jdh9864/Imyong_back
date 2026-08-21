package com.example.demo.dto;

import java.util.List;
import java.util.Map;

public class ExamSubmitResponse {
    private String examId;
    private Integer totalScore;
    private Integer obtainedScore;
    private Boolean isCompleted;
    private String submittedAt;
    private List<ResultDto> results;

    public ExamSubmitResponse() {}

    public ExamSubmitResponse(String examId, Integer totalScore, Integer obtainedScore, Boolean isCompleted, String submittedAt, List<ResultDto> results) {
        this.examId = examId;
        this.totalScore = totalScore;
        this.obtainedScore = obtainedScore;
        this.isCompleted = isCompleted;
        this.submittedAt = submittedAt;
        this.results = results;
    }

    public String getExamId() { return examId; }
    public void setExamId(String examId) { this.examId = examId; }
    public Integer getTotalScore() { return totalScore; }
    public void setTotalScore(Integer totalScore) { this.totalScore = totalScore; }
    public Integer getObtainedScore() { return obtainedScore; }
    public void setObtainedScore(Integer obtainedScore) { this.obtainedScore = obtainedScore; }
    public Boolean getIsCompleted() { return isCompleted; }
    public void setIsCompleted(Boolean isCompleted) { this.isCompleted = isCompleted; }
    public String getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(String submittedAt) { this.submittedAt = submittedAt; }
    public List<ResultDto> getResults() { return results; }
    public void setResults(List<ResultDto> results) { this.results = results; }

    public static class ResultDto {
        private String problemId;
        private Integer problemNumber;
        private Integer score;
        private Boolean isCorrect;
        private Map<String, Object> aiFeedback;
        private List<String> missingKeywords;

        public ResultDto() {}

        public ResultDto(String problemId, Integer problemNumber, Integer score, Boolean isCorrect, Map<String, Object> aiFeedback, List<String> missingKeywords) {
            this.problemId = problemId;
            this.problemNumber = problemNumber;
            this.score = score;
            this.isCorrect = isCorrect;
            this.aiFeedback = aiFeedback;
            this.missingKeywords = missingKeywords;
        }

        public String getProblemId() { return problemId; }
        public void setProblemId(String problemId) { this.problemId = problemId; }
        public Integer getProblemNumber() { return problemNumber; }
        public void setProblemNumber(Integer problemNumber) { this.problemNumber = problemNumber; }
        public Integer getScore() { return score; }
        public void setScore(Integer score) { this.score = score; }
        public Boolean getIsCorrect() { return isCorrect; }
        public void setIsCorrect(Boolean isCorrect) { this.isCorrect = isCorrect; }
        public Map<String, Object> getAiFeedback() { return aiFeedback; }
        public void setAiFeedback(Map<String, Object> aiFeedback) { this.aiFeedback = aiFeedback; }
        public List<String> getMissingKeywords() { return missingKeywords; }
        public void setMissingKeywords(List<String> missingKeywords) { this.missingKeywords = missingKeywords; }
    }
}
