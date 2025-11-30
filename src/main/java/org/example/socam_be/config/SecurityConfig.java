package org.example.socam_be.config;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource; // 🔵 추가
import org.springframework.web.cors.UrlBasedCorsConfigurationSource; // 🔵 추가

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    // 🔵 추가: CORS 설정
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList(
                "http://localhost:3000",
                "http://localhost:5173",
                "https://socam-fe.vercel.app",  // 프론트
                "https://socam.kro.kr",          // 백엔드 자기 자신 호출 시 대비
                "https://*.vercel.app"
        ));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 🔵 핵심!! "/api/**" 말고 전체 적용
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 🔵 추가: cors() 활성화
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // 완전 오픈
                        .requestMatchers(
                                // 일반 유저
                                "/api/users/register",
                                "/api/users/login",
                                "/api/users/check-email",
                                "/api/users/check-nickname",
                                "/api/users/reset-password",
                                "/api/users/password-reset-request",

                                // 토큰 재발급
                                "/api/auth/**",

//                                // 운영기관 비인증 접근 허용
                                "/api/org/register",
                                "/api/org/register/**",
                                "/api/org/login",
                                "/api/org/check-email",
                                "/api/org/password-reset-request",
                                "/api/org/reset-password",

                                // 운영기관 공개 API
                                "/api/org/public/**",

                                // 관리자 로그인
                                "/api/admin/login",

                                // 이미지 업로드 ←
                                "/api/image/upload",

                                // 강의 조회 (공개 API)
                                "/api/lecture/**",

                                // 리뷰 조회 (공개 API - 강의별 리뷰 조회만)
                                "/api/review/lecture/**",

                                // 공지사항 조회 (공개 API)
                                "/api/notices/**",

                                // swagger
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/v3/api-docs.yaml"
                        ).permitAll()

                        // 관리자 보호
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        // 운영기관 보호: 로그인 이후 사용할 API만 지정
                        .requestMatchers("/api/org/lecture/**").hasRole("ORG")
                        .requestMatchers("/api/org/me").hasRole("ORG")
                        .requestMatchers("/api/org/delete/**").hasRole("ORG")

                        // 사용자 찜하기 기능 (USER 권한 필요)
                        .requestMatchers("/api/wishlist/**").hasRole("USER")

                        // 그 외 전체 인증 필요
                        .anyRequest().authenticated()
                )
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .exceptionHandling(ex -> ex.authenticationEntryPoint((req, res, excep) -> {
                    res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    res.setContentType("application/json;charset=UTF-8");
                    res.getWriter().write("{\"error\": \"인증 필요 or 권한 부족\"}");
                }));

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
