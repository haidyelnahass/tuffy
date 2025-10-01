package com.eg.user.controller;


import com.eg.user.model.request.EmailConfirmationRequest;
import com.eg.user.model.request.ProfileConfirmationRequest;
import com.eg.user.model.request.UserRegistrationRequest;
import com.eg.user.model.response.UserRegistrationResponse;
import com.eg.user.service.UserRegistrationService;
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
public class UserRegistrationController {

  private final UserRegistrationService userRegistrationService;

  @PostMapping("/register")
  public ResponseEntity<UserRegistrationResponse> registerUser(@RequestBody @Valid UserRegistrationRequest request) {
    return ResponseEntity.ok(userRegistrationService.registerUser(request));
  }

  @PostMapping("/register/send-confirmation")
  public void sendConfirmationEmail(
    @RequestBody @Valid EmailConfirmationRequest request) {
    userRegistrationService.sendConfirmationEmail(request);
  }

  @PostMapping("/register/confirm")
  public void confirmProfile(
    @RequestBody @Valid ProfileConfirmationRequest request) {
    userRegistrationService.confirmProfile(request);
  }

  // TODO Reset Password Capabilities
  // TODO OTP Notifications to verify mobile number
}
