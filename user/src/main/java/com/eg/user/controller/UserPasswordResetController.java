package com.eg.user.controller;


import com.eg.user.model.request.ForgotPasswordRequest;
import com.eg.user.model.request.ResetPasswordRequest;
import com.eg.user.service.UserPasswordResetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserPasswordResetController {

  private final UserPasswordResetService userPasswordResetService;


  @PostMapping("/forgot-password")
  public void sendForgotPasswordEmail(@RequestBody ForgotPasswordRequest request) {
    userPasswordResetService.sendForgetPasswordEmail(request);
  }

  @PutMapping("/reset-password")
  public void resetPassword(@RequestBody @Valid ResetPasswordRequest request) {
    userPasswordResetService.resetPassword(request);
  }
}
