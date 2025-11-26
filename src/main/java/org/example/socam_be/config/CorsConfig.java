package org.example.socam_be.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 모든 /api 경로에 대해 CORS 설정 적용
        registry.addMapping("/api/**") 
                // 허용할 Origins 목록
                .allowedOrigins(
                        // 개발 환경
                        "http://localhost:3000",
                        "http://localhost:5173",

                        // 운영 환경 (Vercel 및 최종 도메인 - HTTPS 필수)
                        "https://socam.kro.kr",
                        "https://socam-fe.vercel.app"
                )
                .allowedMethods("*") // 모든 HTTP 메서드 허용
                .allowedHeaders("*") // 모든 헤더 허용
                .allowCredentials(true); // 쿠키 및 자격 증명 허용
    }
}