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
        String method = request.getMethod();

        // 디버그 로그 추가
        System.out.println("[JwtFilter] " + method + " " + path);

        // ================================
        // 인증 제외 경로 (로그인, 회원가입)
        // ================================
        if (path.equals("/api/admin/login") ||
                path.startsWith("/api/users/register") ||
                path.startsWith("/api/users/login") ||
                path.startsWith("/api/users/check-email") ||
                path.startsWith("/api/users/check-nickname") ||
                path.startsWith("/api/auth/refresh") ||
                path.startsWith("/api/org/register") ||
                path.startsWith("/api/org/login") ||
                path.startsWith("/api/org/check-email") ||
                path.startsWith("/api/org/public") ||
                path.startsWith("/api/notices") ||
                path.startsWith("/api/users/password-reset-request") ||
                path.startsWith("/api/users/reset-password") ||
                path.startsWith("/api/org/password-reset-request") ||
                path.startsWith("/api/org/reset-password")) {

            System.out.println("[JwtFilter] skip auth for path=" + path);
            filterChain.doFilter(request, response);
            return;
        }

        // ================================
        // JWT 인증 처리 시작
        // ================================
        String token = request.getHeader("Authorization");
        System.out.println("[JwtFilter] Authorization header=" + token);

        if (token != null && token.startsWith("Bearer ")) {
            try {
                String rawToken = token.substring(7);

                String email = JwtUtils.getEmailFromToken(rawToken);
                String role  = JwtUtils.getRoleFromToken(rawToken);
                System.out.println("[JwtFilter] parsed email=" + email + ", role=" + role);

                if (email != null) {
                    request.setAttribute("email", email);

                    List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    if ("ORG".equalsIgnoreCase(role)) {
                        authorities.add(new SimpleGrantedAuthority("ROLE_ORG"));
                    } else if ("ADMIN".equalsIgnoreCase(role)) {
                        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
                    } else {
                        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
                    }

                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(email, null, authorities);

                    SecurityContextHolder.getContext().setAuthentication(auth);
                    System.out.println("[JwtFilter] SecurityContext set: " + auth);
                }

            } catch (JwtException | IllegalArgumentException e) {
                System.err.println("[JwtFilter] JWT 토큰 파싱 실패: " + e.getMessage());
                SecurityContextHolder.clearContext();
            }
        } else {
            System.out.println("[JwtFilter] Authorization header is null or invalid format");
        }

        filterChain.doFilter(request, response);
    }
}