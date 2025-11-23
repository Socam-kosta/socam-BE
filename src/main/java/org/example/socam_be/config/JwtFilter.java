package org.example.socam_be.config;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.socam_be.util.JwtUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String path = request.getRequestURI();

        // ================================
        // 인증 제외 경로 (로그인, 회원가입)
        // ================================
        if (path.equals("/api/admin/login") ||
                path.startsWith("/api/users/register") ||
                path.startsWith("/api/users/login") ||
                path.startsWith("/api/auth/refresh") ||
                path.startsWith("/api/org/register") ||
                path.startsWith("/api/org/login") ||
                path.startsWith("/api/users/password-reset-request") ||
                path.startsWith("/api/users/reset-password") ||
                path.startsWith("/api/org/password-reset-request") ||
                path.startsWith("/api/org/reset-password")) {

            filterChain.doFilter(request, response);
            return;
        }

        // ================================
        // JWT 인증 처리 시작
        // ================================
        String token = request.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            try {
                String rawToken = token.substring(7);

                // JWT에서 이메일과 역할 파싱
                String email = JwtUtils.getEmailFromToken(rawToken);
                String role = JwtUtils.getRoleFromToken(rawToken);   // ⭐ ROLE 파싱

                if (email != null) {
                    request.setAttribute("email", email); // 컨트롤러에서 사용 가능

                    // ================================
                    // ROLE 부여
                    // ================================
                    List<SimpleGrantedAuthority> authorities = new ArrayList<>();

                    if ("ORG".equalsIgnoreCase(role)) {
                        authorities.add(new SimpleGrantedAuthority("ROLE_ORG"));
                    } else if ("ADMIN".equalsIgnoreCase(role)) {
                        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
                    } else {
                        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
                    }

                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(
                                    email,
                                    null,
                                    authorities
                            );

                    SecurityContextHolder.getContext().setAuthentication(auth);
                }

            } catch (JwtException | IllegalArgumentException e) {
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }
}