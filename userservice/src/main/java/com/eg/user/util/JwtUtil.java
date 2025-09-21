package com.eg.user.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

  @Value("${security.jwt.secret}")
  private String secretKey;

  public String generateToken(String userId, String name,
                              String userType, String customerStatus) {
    // 1 hour
    long expirationMs = 3600000;
    return Jwts.builder()
      .setSubject(userId)
      .claim("name", name)
      .claim("role", userType)
      .claim("status", customerStatus)
      .setIssuedAt(new Date())
      .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
      .signWith(jwtSigningKey(secretKey))
      .compact();
  }

  public Key jwtSigningKey(String jwtSecret) {
    // ensure it's at least 256-bit for HS256
    return Keys.hmacShaKeyFor(jwtSecret.getBytes());
  }

  public boolean validateToken(String token) {
    return !isTokenExpired(token);
  }

  public Claims extractClaims(String token) {
    return Jwts.parserBuilder()
      .setSigningKey(jwtSigningKey(secretKey))
      .build()
      .parseClaimsJws(token)
      .getBody();
  }

  private boolean isTokenExpired(String token) {
    Date expiration = Jwts.parserBuilder()
      .setSigningKey(jwtSigningKey(secretKey))
      .build()
      .parseClaimsJws(token)
      .getBody()
      .getExpiration();
    return expiration.before(new Date());
  }
}
