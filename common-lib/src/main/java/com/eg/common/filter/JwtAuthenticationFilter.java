package com.eg.common.filter;

import com.eg.common.model.enums.CustomerStatusEnum;
import com.eg.common.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

import static com.eg.common.model.enums.UserTypeEnum.DRIVER;
import static com.eg.common.model.enums.UserTypeEnum.RIDER;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtUtil jwtUtil;

  @Override
  protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain filterChain)
    throws ServletException, IOException {

    final String authHeader = request.getHeader("Authorization");

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      filterChain.doFilter(request, response); // no token → continue
      return;
    }

    String token = authHeader.substring(7);

    try {
      if (!jwtUtil.validateToken(token, true)) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return;
      }
      Claims claims = jwtUtil.extractClaims(token, true);

      String userId = claims.getSubject();
      String role = claims.get("role", String.class); // DRIVER, RIDER
      String status = claims.get("status", String.class); // ACTIVE, BLOCKED

      if (status == null) {
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Status not found");
        return;
      } else if (!CustomerStatusEnum.ACTIVE.name().equals(status)) {
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "User not active");
        return;
      }

      if (role == null) {
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Role not found");
        return;
      } else if (!List.of(RIDER.name(), DRIVER.name()).contains(role)) {
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Wrong role found");
        return;
      }

      List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));


      UsernamePasswordAuthenticationToken authentication =
        new UsernamePasswordAuthenticationToken(userId, null, authorities);
      authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); // adds ip address/ http
      // session id
      SecurityContextHolder.getContext().setAuthentication(authentication);


    } catch (JwtException e) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      return;
    }

    filterChain.doFilter(request, response);
  }
}
