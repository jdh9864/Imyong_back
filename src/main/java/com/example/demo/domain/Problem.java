package com.example.demo.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Document(collection = "problem")
public class Problem {

    @Id
    private String id;

    // RDBMS의 외래 키(Foreign Key) 참조 매핑
    @Field("exam_id")
    private String examId;

    @Field("domain_id")
    private String domainId;

    // [LLM 출제 정보]
    @Field("problem_number")
    private Integer problemNumber;

    @Field("question_type")
    private String questionType;

    private String title;
    private String content;

    // JSONB: 객관식 보기
    private List<String> options = new ArrayList<>();

    @Field("reference_answer")
    private String referenceAnswer;

    // JSONB: 채점 기준표 (예: [{"criterion": "설명", "points": 10}])
    private List<Map<String, Object>> rubric = new ArrayList<>();

    // [유저 풀이 정보]
    @Field("user_answer")
    private String userAnswer;

    // [AI 채점 및 약점 분석 정보]
    private Integer score;

    @Field("is_correct")
    private Boolean isCorrect;

    // JSONB: AI 피드백 (총평, 강점, 약점 구조화)
    @Field("ai_feedback")
    private Map<String, Object> aiFeedback = new HashMap<>();

    // JSONB: 누락된 핵심 키워드 리스트
    @Field("missing_keywords")
    private List<String> missingKeywords = new ArrayList<>();

    // [오답노트 관리]
    @Field("is_reviewed")
    private Boolean isReviewed = false;

    @Field("review_notes")
    private String reviewNotes;

    public Problem() {}

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getExamId() { return examId; }
    public void setExamId(String examId) { this.examId = examId; }
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
    public List<String> getOptions() { return options; }
    public void setOptions(List<String> options) { this.options = options; }
    public String getReferenceAnswer() { return referenceAnswer; }
    public void setReferenceAnswer(String referenceAnswer) { this.referenceAnswer = referenceAnswer; }
    public List<Map<String, Object>> getRubric() { return rubric; }
    public void setRubric(List<Map<String, Object>> rubric) { this.rubric = rubric; }
    public String getUserAnswer() { return userAnswer; }
    public void setUserAnswer(String userAnswer) { this.userAnswer = userAnswer; }
    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }
    public Boolean getIsCorrect() { return isCorrect; }
    public void setIsCorrect(Boolean isCorrect) { this.isCorrect = isCorrect; }
    public Map<String, Object> getAiFeedback() { return aiFeedback; }
    public void setAiFeedback(Map<String, Object> aiFeedback) { this.aiFeedback = aiFeedback; }
    public List<String> getMissingKeywords() { return missingKeywords; }
    public void setMissingKeywords(List<String> missingKeywords) { this.missingKeywords = missingKeywords; }
    public Boolean getIsReviewed() { return isReviewed; }
    public void setIsReviewed(Boolean isReviewed) { this.isReviewed = isReviewed; }
    public String getReviewNotes() { return reviewNotes; }
    public void setReviewNotes(String reviewNotes) { this.reviewNotes = reviewNotes; }
}
