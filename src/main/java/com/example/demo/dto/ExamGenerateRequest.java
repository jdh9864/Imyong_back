package com.example.demo.dto;

public class ExamGenerateRequest {
    private String generationType; // "CHAPTER" 또는 "MOCK_EXAM"
    private String domainId;       // MongoDB ObjectId (예: 6a7188a99...)
    private String domainName;     // 프롬프트용 단원명 (예: 분자생물학) 추가됨!
    private Integer problemCount;

    // Getters and Setters
    public String getGenerationType() { return generationType; }
    public void setGenerationType(String generationType) { this.generationType = generationType; }

    public String getDomainId() { return domainId; }
    public void setDomainId(String domainId) { this.domainId = domainId; }

    public String getDomainName() { return domainName; }
    public void setDomainName(String domainName) { this.domainName = domainName; }

    public Integer getProblemCount() { return problemCount; }
    public void setProblemCount(Integer problemCount) { this.problemCount = problemCount; }
}