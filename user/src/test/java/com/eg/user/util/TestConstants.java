package com.eg.user.util;

import com.eg.common.model.enums.UserTypeEnum;
import com.eg.user.model.request.EmailConfirmationRequest;
import com.eg.user.model.request.ProfileConfirmationRequest;
import com.eg.user.model.request.ProfileUpdateRequest;
import com.eg.user.model.request.UserLoginRequest;
import com.eg.user.model.request.UserRegistrationRequest;
import com.eg.user.model.response.ProfileDetailsResponse;
import com.eg.user.model.response.UserLoginResponse;
import com.eg.user.model.response.UserRegistrationResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TestConstants {

  public static final String BASE_URL = "/v1/users";
  public static final String REGISTRATION_URL = "/register";
  public static final String SEND_CONFIRMATION_URL = "/register/send-confirmation";
  public static final String CONFIRMATION_URL = "/register/confirm";
  public static final String LOGIN_URL = "/login";
  public static final String PROFILE_URL = "/profile";
  public static final String DETAILS_URL = "/details";
  public static final String USER_ID_HEADER = "UserId";


  public static UserRegistrationRequest buildRiderUserRegistrationRequest() {
    return UserRegistrationRequest.builder()
      .userType(UserTypeEnum.RIDER)
      .email("aa@aa.com")
      .password("aa2233")
      .phone("01229933003")
      .build();
  }

  public static UserRegistrationResponse buildUserRegistrationResponse() {
    return UserRegistrationResponse.builder()
      .userId(1L)
      .build();
  }

  public static EmailConfirmationRequest buildEmailConfirmationRequest() {
    return EmailConfirmationRequest.builder()
      .profileId(1L)
      .build();
  }

  public static ProfileConfirmationRequest buildProfileConfirmationRequest() {
    return ProfileConfirmationRequest.builder()
      .profileId(1L)
      .confirmationCode(112233)
      .build();
  }

  public static UserLoginRequest buildUserLoginRequest() {
    return UserLoginRequest.builder()
      .password("112233")
      .phone("01222993939")
      .build();
  }

  public static UserLoginResponse buildUserLoginResponse() {
    return UserLoginResponse.builder()
      .accessToken("accessToken")
      .build();
  }

  public static ProfileUpdateRequest buildProfileUpdateRequest() {
    return ProfileUpdateRequest.builder()
      .name("xx")
      .build();
  }

  public static ProfileDetailsResponse buildProfileDetailsResponse() {
    return ProfileDetailsResponse.builder()
      .id(1L)
      .build();
  }
}
