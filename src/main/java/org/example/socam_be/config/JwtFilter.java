package org.example.socam_be.config;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.socam_be.util.JwtUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(
          HttpServletRequest request,
          HttpServletResponse response,
          FilterChain filterChain
  ) throws ServletException, IOException {

    String path = request.getRequestURI();

    // 인증이 필요 없는 API 목록
    if (path.startsWith("/api/users/register") ||
            path.startsWith("/api/users/login") ||
            path.startsWith("/api/auth/refresh") ||
            path.startsWith("/api/org/register") ||
            path.startsWith("/api/org/login") ||
            path.startsWith("/api/org/password-reset-request") ||
            path.startsWith("/api/org/reset-password")) {

      filterChain.doFilter(request, response);
      return;
    }

    String token = request.getHeader("Authorization");

    if (token != null && token.startsWith("Bearer ")) {
      token = token.substring(7);

      try {
        String email = JwtUtils.getEmailFromToken(token);

        if (email != null) {
          // Controller에서 request.getAttribute("email")로 사용
          request.setAttribute("email", email);
        }

      } catch (JwtException | IllegalArgumentException e) {
        SecurityContextHolder.clearContext();
      }
    }

    filterChain.doFilter(request, response);
  }
}
