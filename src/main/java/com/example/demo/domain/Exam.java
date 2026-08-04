package com.example.demo.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "exam")
public class Exam {

    @Id
    private String id;

    @Field("exam_type")
    private String examType;

    private String title;

    @Field("total_score")
    private Integer totalScore = 0;

    @Field("obtained_score")
    private Integer obtainedScore = 0;

    @Field("time_limit_minutes")
    private Integer timeLimitMinutes = 60;

    @Field("is_completed")
    private Boolean isCompleted = false;

    @Field("created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Field("submitted_at")
    private LocalDateTime submittedAt;

    public Exam() {}

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getExamType() { return examType; }
    public void setExamType(String examType) { this.examType = examType; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public Integer getTotalScore() { return totalScore; }
    public void setTotalScore(Integer totalScore) { this.totalScore = totalScore; }
    public Integer getObtainedScore() { return obtainedScore; }
    public void setObtainedScore(Integer obtainedScore) { this.obtainedScore = obtainedScore; }
    public Integer getTimeLimitMinutes() { return timeLimitMinutes; }
    public void setTimeLimitMinutes(Integer timeLimitMinutes) { this.timeLimitMinutes = timeLimitMinutes; }
    public Boolean getIsCompleted() { return isCompleted; }
    public void setIsCompleted(Boolean isCompleted) { this.isCompleted = isCompleted; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }
}
