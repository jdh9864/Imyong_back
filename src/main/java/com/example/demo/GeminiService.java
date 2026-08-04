package com.example.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Service
public class GeminiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String apiUrl;

    private final RestClient restClient;

    public GeminiService() {
        this.restClient = RestClient.create();
    }

    /**
     * Gemini 1.5 Flash 모델에 프롬프트를 전송하고 텍스트 응답을 반환합니다.
     *
     * @param promptText AI에 전달할 질문 또는 지시문
     * @return 생성된 답변 문자열
     */
    public String generateContent(String promptText) {
        // 1. Google Gemini API 요청 페이로드 규격 생성
        Map<String, Object> requestBody = Map.of(
                "contents", List.of(
                        Map.of(
                                "parts", List.of(
                                        Map.of("text", promptText)
                                )
                        )
                )
        );

        // 2. HTTP POST 요청 전송 및 Map 형태로 응답 바인딩
        Map<?, ?> response = restClient.post()
                .uri(apiUrl + "?key=" + apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestBody)
                .retrieve()
                .body(Map.class);

        // 3. 응답 JSON 트리 파싱
        return extractTextFromResponse(response);
    }

    @SuppressWarnings("unchecked")
    private String extractTextFromResponse(Map<?, ?> response) {
        try {
            if (response == null || !response.containsKey("candidates")) {
                return "Gemini API 응답 결과가 비어있습니다.";
            }

            List<Map<String, Object>> candidates = (List<Map<String, Object>>) response.get("candidates");
            if (candidates == null || candidates.isEmpty()) {
                return "생성된 후보 답변(candidates)이 존재하지 않습니다.";
            }

            Map<String, Object> firstCandidate = candidates.get(0);
            Map<String, Object> content = (Map<String, Object>) firstCandidate.get("content");
            List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");

            if (parts != null && !parts.isEmpty()) {
                return (String) parts.get(0).get("text");
            }

            return "텍스트 추출 실패: parts 구조가 올바르지 않습니다.";
        } catch (Exception e) {
            return "Gemini 응답 파싱 중 예외 발생: " + e.getMessage();
        }
    }
}
