package com.eg.common.util;

import com.eg.common.model.TokenDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

  @Value("${security.jwt.access.secret}")
  private String accessSecretKey;

  @Value("${security.jwt.refresh.secret}")
  private String refreshSecretKey;

  @Value("${security.jwt.access.expiration-ms}")
  private long accessTokenExpirationMs;

  @Value("${security.jwt.refresh.expiration-ms}")
  private long refreshTokenExpirationMs;

  public TokenDetails generateAccessToken(String userId, String name,
                                          String userType, String customerStatus) {
    String token = Jwts.builder()
      .setSubject(userId)
      .claim("name", name)
      .claim("role", userType)
      .claim("status", customerStatus)
      .setIssuedAt(new Date())
      .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpirationMs))
      .signWith(jwtSigningKey(accessSecretKey))
      .compact();
    return TokenDetails.builder()
      .token(token)
      .expiresIn(accessTokenExpirationMs)
      .build();
  }

  public TokenDetails generateRefreshToken(String userId) {
    return TokenDetails.builder().token(Jwts.builder()
      .setSubject(userId)
      .setIssuedAt(new Date())
      .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpirationMs))
      .signWith(jwtSigningKey(accessSecretKey))
      .compact())
      .expiresIn(refreshTokenExpirationMs)
      .build();
  }

  public Key jwtSigningKey(String jwtSecret) {
    // ensure it's at least 256-bit for HS256
    return Keys.hmacShaKeyFor(jwtSecret.getBytes());
  }

  public boolean validateToken(String token, boolean isAccess) {
    return !isTokenExpired(token, isAccess);
  }

  public Claims extractClaims(String token, boolean isAccess) {
    return Jwts.parserBuilder()
      .setSigningKey(jwtSigningKey(isAccess ? accessSecretKey: refreshSecretKey))
      .build()
      .parseClaimsJws(token)
      .getBody();
  }

  private boolean isTokenExpired(String token, boolean isAccess) {
    Date expiration = Jwts.parserBuilder()
      .setSigningKey(jwtSigningKey(isAccess? accessSecretKey: refreshSecretKey))
      .build()
      .parseClaimsJws(token)
      .getBody()
      .getExpiration();
    return expiration.before(new Date());
  }
}
