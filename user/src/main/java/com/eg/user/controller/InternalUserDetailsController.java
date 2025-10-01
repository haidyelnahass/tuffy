package com.eg.user.controller;


import com.eg.user.model.response.ProfileDetailsResponse;
import com.eg.user.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.eg.user.util.Constants.USER_ID_HEADER;

@RestController
@RequestMapping("/internal/v1/users")
@RequiredArgsConstructor
public class InternalUserDetailsController {

  private final UserProfileService userProfileService;

  @GetMapping("/details")
  public ResponseEntity<ProfileDetailsResponse> getUserDetails(@RequestHeader(USER_ID_HEADER) Long userId) {
    return ResponseEntity.ok(userProfileService.getProfileDetails(userId));
  }
}
