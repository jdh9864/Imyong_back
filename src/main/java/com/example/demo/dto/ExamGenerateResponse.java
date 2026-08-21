package com.example.demo.dto;

import java.util.List;

public class ExamGenerateResponse {
    private String examId;
    private String title;
    private String generationType;
    private List<ProblemDto> problems;

    public ExamGenerateResponse() {}

    public ExamGenerateResponse(String examId, String title, String generationType, List<ProblemDto> problems) {
        this.examId = examId;
        this.title = title;
        this.generationType = generationType;
        this.problems = problems;
    }

    public String getExamId() { return examId; }
    public void setExamId(String examId) { this.examId = examId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getGenerationType() { return generationType; }
    public void setGenerationType(String generationType) { this.generationType = generationType; }
    public List<ProblemDto> getProblems() { return problems; }
    public void setProblems(List<ProblemDto> problems) { this.problems = problems; }

    public static class ProblemDto {
        private String problemId;
        private Integer problemNumber;
        private String questionType;
        private String title;
        private String content;
        private List<String> options;

        public ProblemDto() {}

        public ProblemDto(String problemId, Integer problemNumber, String questionType, String title, String content, List<String> options) {
            this.problemId = problemId;
            this.problemNumber = problemNumber;
            this.questionType = questionType;
            this.title = title;
            this.content = content;
            this.options = options;
        }

        public String getProblemId() { return problemId; }
        public void setProblemId(String problemId) { this.problemId = problemId; }
        public Integer getProblemNumber() { return problemNumber; }
        public void setProblemNumber(Integer problemNumber) { this.problemNumber = problemNumber; }
        public String getQuestionType() { return questionType; }
        public void setQuestionType(String questionType) { this.questionType = questionType; }
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
        public List<String> getOptions() { return options; }
        public void setOptions(List<String> options) { this.options = options; }
    }
}
