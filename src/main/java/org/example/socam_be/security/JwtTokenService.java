package org.example.socam_be.security;

import org.example.socam_be.util.JwtUtils;
import org.springframework.stereotype.Service;

@Service
public class JwtTokenService {

    public String refreshAccessToken(String refreshToken) {
        try {
            return JwtUtils.refreshAccessToken(refreshToken);
        } catch (Exception e) {
            throw new RuntimeException("Error refreshing access token", e);
        }
    }

    public String generateAccessToken(String email, String role, String name) {
        return JwtUtils.generateAccessToken(email, role, name);
    }

    public String generateRefreshToken(String email, String role, String name) {
        return JwtUtils.generateRefreshToken(email, role, name);
    }
}