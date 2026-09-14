package com.example.demo.dto;

public class SingleProblemGradeRequest {
    private String problemId;
    private String userAnswer;

    // 기본 생성자
    public SingleProblemGradeRequest() {}

    public String getProblemId() {
        return problemId;
    }

    public void setProblemId(String problemId) {
        this.problemId = problemId;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }
}
