package com.example.demo.dto;

import java.util.List;
import java.util.Map;

public class ProblemReviewResponse {
    private String requestedDomainId;
    private Boolean isFallbackToAll;
    private List<ProblemDto> problems;

    public ProblemReviewResponse() {}

    public ProblemReviewResponse(String requestedDomainId, Boolean isFallbackToAll, List<ProblemDto> problems) {
        this.requestedDomainId = requestedDomainId;
        this.isFallbackToAll = isFallbackToAll;
        this.problems = problems;
    }

    public String getRequestedDomainId() { return requestedDomainId; }
    public void setRequestedDomainId(String requestedDomainId) { this.requestedDomainId = requestedDomainId; }
    public Boolean getIsFallbackToAll() { return isFallbackToAll; }
    public void setIsFallbackToAll(Boolean isFallbackToAll) { this.isFallbackToAll = isFallbackToAll; }
    public List<ProblemDto> getProblems() { return problems; }
    public void setProblems(List<ProblemDto> problems) { this.problems = problems; }

    public static class ProblemDto {
        private String problemId;
        private String domainId;
        private Integer problemNumber;
        private String questionType;
        private String title;
        private String content;
        private String referenceAnswer;
        private String userAnswer;
        private Boolean isCorrect;
        private Object aiFeedback; // DB에 저장된 타입에 따라 Map<String, Object> 또는 String 할당

        public ProblemDto() {}

        public ProblemDto(String problemId, String domainId, Integer problemNumber, String questionType,
                          String title, String content, String referenceAnswer, String userAnswer,
                          Boolean isCorrect, Object aiFeedback) {
            this.problemId = problemId;
            this.domainId = domainId;
            this.problemNumber = problemNumber;
            this.questionType = questionType;
            this.title = title;
            this.content = content;
            this.referenceAnswer = referenceAnswer;
            this.userAnswer = userAnswer;
            this.isCorrect = isCorrect;
            this.aiFeedback = aiFeedback;
        }

        public String getProblemId() { return problemId; }
        public void setProblemId(String problemId) { this.problemId = problemId; }
        public String getDomainId() { return domainId; }
        public void setDomainId(String domainId) { this.domainId = domainId; }
        public Integer getProblemNumber() { return problemNumber; }
        public void setProblemNumber(Integer problemNumber) { this.problemNumber = problemNumber; }
        public String getQuestionType() { return questionType; }
        public void setQuestionType(String questionType) { this.questionType = questionType; }
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
        public String getReferenceAnswer() { return referenceAnswer; }
        public void setReferenceAnswer(String referenceAnswer) { this.referenceAnswer = referenceAnswer; }
        public String getUserAnswer() { return userAnswer; }
        public void setUserAnswer(String userAnswer) { this.userAnswer = userAnswer; }
        public Boolean getIsCorrect() { return isCorrect; }
        public void setIsCorrect(Boolean isCorrect) { this.isCorrect = isCorrect; }
        public Object getAiFeedback() { return aiFeedback; }
        public void setAiFeedback(Object aiFeedback) { this.aiFeedback = aiFeedback; }
    }
}