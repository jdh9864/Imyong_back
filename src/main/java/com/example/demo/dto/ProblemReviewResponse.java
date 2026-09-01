package com.example.demo.dto;

import java.util.List;
import java.util.Map;

public class ProblemReviewResponse {

    private String requestedDomainId;
    private boolean isFallbackToAll; // 요청한 도메인에 틀린/안푼 문제가 없어 전체 범위로 대체되었는지 여부
    private List<ProblemDto> problems;

    public ProblemReviewResponse(String requestedDomainId, boolean isFallbackToAll, List<ProblemDto> problems) {
        this.requestedDomainId = requestedDomainId;
        this.isFallbackToAll = isFallbackToAll;
        this.problems = problems;
    }

    public String getRequestedDomainId() { return requestedDomainId; }
    public boolean getIsFallbackToAll() { return isFallbackToAll; }
    public List<ProblemDto> getProblems() { return problems; }

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
        private Map<String, Object> aiFeedback;

        public ProblemDto(String problemId, String domainId, Integer problemNumber, String questionType,
                          String title, String content, String referenceAnswer, String userAnswer,
                          Boolean isCorrect, Map<String, Object> aiFeedback) {
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
        public String getDomainId() { return domainId; }
        public Integer getProblemNumber() { return problemNumber; }
        public String getQuestionType() { return questionType; }
        public String getTitle() { return title; }
        public String getContent() { return content; }
        public String getReferenceAnswer() { return referenceAnswer; }
        public String getUserAnswer() { return userAnswer; }
        public Boolean getIsCorrect() { return isCorrect; }
        public Map<String, Object> getAiFeedback() { return aiFeedback; }
    }
}
