package com.eg.user.controller;

import com.eg.user.model.request.UserLoginRequest;
import com.eg.user.model.response.UserLoginResponse;
import com.eg.user.service.UserAuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
  public UserLoginResponse login(@RequestBody @Valid UserLoginRequest request) {
    return userAuthenticationService.login(request);
  }

  // TODO Refresh Token // Sign out functionalities...
}
