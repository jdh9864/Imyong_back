package com.example.demo;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/gemini")
public class GeminiTestController {

    private final GeminiService geminiService;

    public GeminiTestController(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    @PostMapping("/test")
    public Map<String, Object> testGemini(@RequestBody Map<String, String> request) {
        String prompt = request.getOrDefault("prompt", "안녕하세요. 간단히 자기소개 부탁드립니다.");

        try {
            String aiResponse = geminiService.generateContent(prompt);
            return Map.of(
                    "status", "SUCCESS",
                    "prompt", prompt,
                    "result", aiResponse
            );
        } catch (Exception e) {
            e.printStackTrace();
            return Map.of(
                    "status", "FAIL",
                    "error_type", e.getClass().getName(),
                    "error_reason", e.getMessage() != null ? e.getMessage() : "Gemini 통신 중 오류 발생"
            );
        }
    }
}
