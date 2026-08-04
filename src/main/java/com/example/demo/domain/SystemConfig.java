package com.example.demo.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "systemConfig")
public class SystemConfig {

    @Id
    private String id;

    @Field("ai_model_name")
    private String aiModelName = "gemini-3.1-pro-preview"; // 최신 Pro 모델 팩트 반영

    @Field("pass_score")
    private Integer passScore = 80;

    @Field("updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    // 기본 생성자
    public SystemConfig() {}

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getAiModelName() { return aiModelName; }
    public void setAiModelName(String aiModelName) { this.aiModelName = aiModelName; }
    public Integer getPassScore() { return passScore; }
    public void setPassScore(Integer passScore) { this.passScore = passScore; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}