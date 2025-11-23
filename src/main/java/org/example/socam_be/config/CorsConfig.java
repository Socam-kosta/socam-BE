package org.example.socam_be.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins(
                        "http://localhost:3000",  // 너
                        "http://localhost:5173",  // 팀원(대표적으로 Vite)
                        "http://localhost:8080",  // 혹시 다른 팀원
                        "http://localhost:3306"   // 기존 팀원 코드 유지 (실제 의미는 없으나 '그대로 둠')
                ) //프론트 주소
                .allowedMethods("*")
                .allowCredentials(true);
    }
}