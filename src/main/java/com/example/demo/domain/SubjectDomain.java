package com.example.demo.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "subjectDomain")
public class SubjectDomain {

    @Id
    private String id;

    @Field("subject_type")
    private String subjectType;

    @Field("major_category")
    private String majorCategory;

    @Field("sub_category")
    private String subCategory;

    // JSONB: 빈 배열을 기본값으로 할당하여 제약 조건 완화 반영
    @Field("core_keywords")
    private List<String> coreKeywords = new ArrayList<>();

    @Field("reference_text")
    private String referenceText;

    @Field("created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public SubjectDomain() {}

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getSubjectType() { return subjectType; }
    public void setSubjectType(String subjectType) { this.subjectType = subjectType; }
    public String getMajorCategory() { return majorCategory; }
    public void setMajorCategory(String majorCategory) { this.majorCategory = majorCategory; }
    public String getSubCategory() { return subCategory; }
    public void setSubCategory(String subCategory) { this.subCategory = subCategory; }
    public List<String> getCoreKeywords() { return coreKeywords; }
    public void setCoreKeywords(List<String> coreKeywords) { this.coreKeywords = coreKeywords; }
    public String getReferenceText() { return referenceText; }
    public void setReferenceText(String referenceText) { this.referenceText = referenceText; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
