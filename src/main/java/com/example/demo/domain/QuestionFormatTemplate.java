package com.example.demo.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "QuestionFormatTemplate")
public class QuestionFormatTemplate {

    @Id
    private String id;

    // [문제 유형] 예: "기입형(빈칸채우기)", "서술형(대화문 분석)", "논술형(조건 제시)"
    @Field("type_name")
    private String typeName;

    // [문제 텍스트 예시] 특정 과목에 국한되지 않는 템플릿 텍스트
    // 예: "다음은 교사와 학생의 대화이다. 괄호 (가), (나)에 들어갈 알맞은 말을 쓰시오."
    @Field("question_example")
    private String questionExample;

    // [답안 텍스트 예시] AI가 출력 형식을 모방할 수 있도록 제공하는 정답 예시
    // 예: "(가): 자연선택, (나): 생물다양성"
    @Field("answer_example")
    private String answerExample;

    @Field("created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public QuestionFormatTemplate() {}

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTypeName() { return typeName; }
    public void setTypeName(String typeName) { this.typeName = typeName; }
    public String getQuestionExample() { return questionExample; }
    public void setQuestionExample(String questionExample) { this.questionExample = questionExample; }
    public String getAnswerExample() { return answerExample; }
    public void setAnswerExample(String answerExample) { this.answerExample = answerExample; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
