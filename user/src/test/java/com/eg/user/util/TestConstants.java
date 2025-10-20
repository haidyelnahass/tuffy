package com.eg.user.util;

import com.eg.common.model.enums.CustomerStatusEnum;
import com.eg.common.model.enums.UserTypeEnum;
import com.eg.user.model.entity.CustomerStatusEntity;
import com.eg.user.model.entity.UserEntity;
import com.eg.user.model.entity.UserTypeEntity;
import com.eg.user.model.entity.VehicleEntity;
import com.eg.user.model.request.EmailConfirmationRequest;
import com.eg.user.model.request.ProfileConfirmationRequest;
import com.eg.user.model.request.ProfileUpdateRequest;
import com.eg.user.model.request.UserLoginRequest;
import com.eg.user.model.request.UserRegistrationRequest;
import com.eg.user.model.request.VehicleDetailsRequest;
import com.eg.user.model.response.ProfileDetailsResponse;
import com.eg.user.model.response.UserTokenResponse;
import com.eg.user.model.response.UserRegistrationResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.eg.user.util.Util.encodePassword;

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

  public static UserRegistrationRequest buildWrongDriverUserRegistrationRequest() {
    return UserRegistrationRequest.builder()
      .userType(UserTypeEnum.DRIVER)
      .email("aa@aa.com")
      .password("aa2233")
      .phone("01229933003")
      .build();
  }

  public static UserRegistrationRequest buildDriverUserRegistrationRequest() {
    return UserRegistrationRequest.builder()
      .userType(UserTypeEnum.DRIVER)
      .email("aa@aa.com")
      .password("aa2233")
      .phone("01229933003")
      .vehicleDetails(VehicleDetailsRequest.builder()
        .capacity("4")
        .plateNumber("434")
        .model("xx")
        .build())
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
  public static UserLoginRequest buildWrongUserLoginRequest() {
    return UserLoginRequest.builder()
      .password("112733")
      .phone("01222993939")
      .build();
  }

  public static UserTokenResponse buildUserLoginResponse() {
    return UserTokenResponse.builder()
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

  public static UserEntity buildUserEntity(Long userId, UserTypeEnum userType,
                                           CustomerStatusEnum customerStatus) {
    return UserEntity.builder()
      .id(userId)
      .userTypeEntity(buildUserTypeEntity(userType))
      .name("xx")
      .phone("01229933940")
      .customerStatus(buildCustomerStatusEntity(customerStatus))
      .createDate(LocalDateTime.now())
      .password(encodePassword("112233"))
      .loginAttempts(0)
      .build();
  }

  public static UserEntity buildUserEntity(Long userId, UserTypeEnum userType,
                                           CustomerStatusEnum customerStatus,
                                           Integer loginAttempts) {
    return UserEntity.builder()
      .id(userId)
      .userTypeEntity(buildUserTypeEntity(userType))
      .name("xx")
      .phone("01229933940")
      .customerStatus(buildCustomerStatusEntity(customerStatus))
      .createDate(LocalDateTime.now())
      .password(encodePassword("112233"))
      .loginAttempts(loginAttempts)
      .build();
  }

  public static CustomerStatusEntity buildCustomerStatusEntity(CustomerStatusEnum value) {
    return CustomerStatusEntity.builder()
      .id(1L)
      .value(value.name())
      .build();
  }

  public static UserTypeEntity buildUserTypeEntity(UserTypeEnum value) {
    return UserTypeEntity.builder()
      .id(1L)
      .value(value.name())
      .build();
  }

  public static VehicleEntity buildVehicleEntity() {
    return VehicleEntity.builder()
      .id(1L)
      .plateNumber("2222")
      .build();
  }

}
