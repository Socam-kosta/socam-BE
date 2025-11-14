package org.example.socam_be.config;

import lombok.RequiredArgsConstructor;
import org.example.socam_be.domain.admin.Admin;
import org.example.socam_be.repository.AdminRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Configuration
@RequiredArgsConstructor
public class AdminInitConfig {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initAdmin() {
        return args -> {

            // ★ 여기 이메일만 팀장이 원하는 값으로 바꾸면 됨 (지금은 혜니꺼 이메일로 임시 ㅎㅎ)
            String adminEmail = "hy3782@gmail.com";

            // 이미 존재하면 생성 X
            if (!adminRepository.existsById(adminEmail)) {

                Admin admin = Admin.builder()
                        .adminEmail(adminEmail)
                        .password(passwordEncoder.encode("socam1234!"))
                        .name("관리자")
                        .nickname("관리자")
                        .role("ADMIN")
                        .approvalStatus("APPROVED")
                        .createdAt(LocalDateTime.now())
                        .build();

                adminRepository.save(admin);
                System.out.println("✔ 기본 관리자 계정 생성됨: " + adminEmail);
            } else {
                System.out.println("✔ 관리자 계정 이미 존재함: " + adminEmail);
            }
        };
    }
}
