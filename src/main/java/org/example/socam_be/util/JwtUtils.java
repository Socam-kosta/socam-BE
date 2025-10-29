package org.example.socam_be.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

public class JwtUtils {
  private static final String SECRET = "this-is-a-very-secure-key-that-is-at-least-64-bytes-long-1234567890!";
  private static final Key SECRET_KEY = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
  private static final long ACCESS_TOKEN_EXPIRATION_TIME = 3600000L * 100; // 100시간

  // ✅ 이메일 기반 Access Token 생성
  public static String generateAccessToken(String email, String role, String name) {
    return Jwts.builder()
            .setSubject(email) // subject를 email로
            .claim("email", email)
            .claim("role", role)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION_TIME))
            .signWith(SECRET_KEY, SignatureAlgorithm.HS512)
            .compact();
  }

  // ✅ 리프레시 토큰 생성
  public static String generateRefreshToken(String email, String role, String name) {
    long refreshExpiration = 7 * 24 * 3600_000; // 7일
    return Jwts.builder()
            .setSubject(email)
            .claim("email", email)
            .claim("role", role)
            .claim("name", name)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + refreshExpiration))
            .signWith(SECRET_KEY, SignatureAlgorithm.HS512)
            .compact();
  }

  // ✅ 이메일 추출
  public static String extractEmail(String jwtWithBearer) {
    try {
      String token = jwtWithBearer.startsWith("Bearer ") ? jwtWithBearer.substring(7) : jwtWithBearer;
      return getEmailFromToken(token);
    } catch (Exception e) {
      throw new RuntimeException("JWT에서 email 파싱 실패", e);
    }
  }

  public static String getEmailFromToken(String token) {
    return Jwts.parserBuilder()
            .setSigningKey(SECRET_KEY)
            .build()
            .parseClaimsJws(token)
            .getBody()
            .get("email", String.class);
  }

  public static String getRoleFromToken(String token) {
    return Jwts.parserBuilder()
            .setSigningKey(SECRET_KEY)
            .build()
            .parseClaimsJws(token)
            .getBody()
            .get("role", String.class);
  }

  public static String getNameFromToken(String token) {
    return Jwts.parserBuilder()
            .setSigningKey(SECRET_KEY)
            .build()
            .parseClaimsJws(token)
            .getBody()
            .get("name", String.class);
  }

  // ✅ 토큰 검증
  public static boolean validateToken(String token) {
    try {
      token = token.startsWith("Bearer ") ? token.substring(7) : token;
      Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token);
      return true;
    } catch (JwtException | IllegalArgumentException e) {
      return false;
    }
  }

  // ✅ 리프레시 토큰으로 새로운 Access Token 재발급
  public static String refreshAccessToken(String refreshToken) {
    try {
      // Bearer 제거
      String token = refreshToken.startsWith("Bearer ") ? refreshToken.substring(7) : refreshToken;

      // refreshToken에서 필요한 정보 추출
      Claims claims = Jwts.parserBuilder()
              .setSigningKey(SECRET_KEY)
              .build()
              .parseClaimsJws(token)
              .getBody();

      String email = claims.get("email", String.class);
      String role = claims.get("role", String.class);
      String name = claims.get("name", String.class);

      // 새로운 Access Token 생성
      return generateAccessToken(email, role, name);
    } catch (ExpiredJwtException e) {
      throw new RuntimeException("리프레시 토큰이 만료되었습니다. 다시 로그인하세요.", e);
    } catch (JwtException | IllegalArgumentException e) {
      throw new RuntimeException("리프레시 토큰이 유효하지 않습니다.", e);
    }
  }
}