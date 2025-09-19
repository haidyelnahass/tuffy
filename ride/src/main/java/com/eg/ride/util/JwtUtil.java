package com.eg.ride.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

  @Value("${security.jwt.secret}")
  private String secretKey;


  public boolean validateToken(String token, String username) {
    return username.equals(extractUsername(token)) &&
      !isTokenExpired(token);
  }

  public String extractUsername(String token) {
    return Jwts.parserBuilder()
      .setSigningKey(jwtSigningKey(secretKey))
      .build()
      .parseClaimsJws(token)
      .getBody()
      .getSubject();
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

  public Key jwtSigningKey(String jwtSecret) {
    // ensure it's at least 256-bit for HS256
    return Keys.hmacShaKeyFor(jwtSecret.getBytes());
  }
}
