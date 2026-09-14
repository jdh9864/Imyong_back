package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 1. /api/** 대신 /** 로 설정하여 모든 경로 허용
                .allowedOriginPatterns(
                        "https://imyong-front-yi5m-lime.vercel.app",  // 2. Vercel 메인 및 미리보기 배포 주소
                        "http://localhost:5175"   // 로컬 개발 환경
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
