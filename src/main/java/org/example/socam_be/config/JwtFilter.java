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
import java.util.Collections;

@Component
public class JwtFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
          throws ServletException, IOException {

    String path = request.getRequestURI();

    // ✅ 회원가입 / 로그인 / 토큰 갱신은 필터 통과
    if (path.startsWith("/api/users/register") ||
            path.startsWith("/api/users/login") ||
            path.startsWith("/api/auth/refresh")) {
      filterChain.doFilter(request, response);
      return;
    }

    String token = request.getHeader("Authorization");

    if (token != null && token.startsWith("Bearer ")) {
      try {
        String email = JwtUtils.getEmailFromToken(token.substring(7));
        if (email != null) {
          UsernamePasswordAuthenticationToken auth =
                  new UsernamePasswordAuthenticationToken(
                          email, null, Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
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