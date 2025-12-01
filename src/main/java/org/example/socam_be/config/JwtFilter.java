package org.example.socam_be.config;

import io.jsonwebtoken.ExpiredJwtException;
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

        // 1) 예외 경로 로그
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

            // 디버그용
            System.out.println("[JwtFilter] SKIP path = " + path);
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        System.out.println("[JwtFilter] path=" + path + ", Authorization=" + authHeader);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            try {
                String rawToken = authHeader.substring(7);

                String email = JwtUtils.getEmailFromToken(rawToken);
                String role  = JwtUtils.getRoleFromToken(rawToken);

                System.out.println("[JwtFilter] token parsed. email=" + email + ", role=" + role);

                if (email != null && !email.isBlank()) {
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
                    System.out.println("[JwtFilter] Authentication set. authorities=" + authorities);
                } else {
                    System.out.println("[JwtFilter] email is null. Authentication NOT set.");
                }

            } catch (ExpiredJwtException e) {
                System.out.println("[JwtFilter] token expired: " + e.getMessage());
                SecurityContextHolder.clearContext();
            } catch (JwtException | IllegalArgumentException e) {
                System.out.println("[JwtFilter] token invalid: " + e.getMessage());
                SecurityContextHolder.clearContext();
            }
        } else {
            System.out.println("[JwtFilter] no Authorization header for path=" + path);
        }

        filterChain.doFilter(request, response);
    }
}