package com.eg.user.controller;

import com.eg.user.model.request.TokenRequest;
import com.eg.user.model.request.UserLoginRequest;
import com.eg.user.model.response.UserTokenResponse;
import com.eg.user.service.UserAuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserAuthenticationController {

  private final UserAuthenticationService userAuthenticationService;

  @PostMapping("/login")
  public ResponseEntity<UserTokenResponse> login(@RequestBody @Valid UserLoginRequest request) {
    return ResponseEntity.ok(userAuthenticationService.login(request));
  }

  @PostMapping("/refresh")
  public ResponseEntity<UserTokenResponse> refreshToken(@RequestBody @Valid TokenRequest request) {
    return ResponseEntity.ok(userAuthenticationService.refreshToken(request));
  }


  @PostMapping("/logout")
  public void logout(@RequestBody @Valid TokenRequest request) {
    userAuthenticationService.logout(request);
  }
}
