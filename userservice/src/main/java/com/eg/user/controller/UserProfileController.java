package com.eg.user.controller;


import com.eg.user.model.request.ProfileUpdateRequest;
import com.eg.user.model.response.ProfileDetailsResponse;
import com.eg.user.service.UserProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.eg.user.util.Constants.USER_ID_HEADER;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserProfileController {

  private final UserProfileService userProfileService;

  @PatchMapping("/profile")
  public void updateProfileDetails(
    @RequestHeader(USER_ID_HEADER) Long userId,
    @RequestBody @Valid ProfileUpdateRequest request) {
    userProfileService.updateProfileDetails(userId, request);
  }

  @GetMapping("/profile")
  public ResponseEntity<ProfileDetailsResponse> getProfileDetails(@RequestHeader(USER_ID_HEADER) Long userId) {
    return ResponseEntity.ok(userProfileService.getProfileDetails(userId));
  }


}
