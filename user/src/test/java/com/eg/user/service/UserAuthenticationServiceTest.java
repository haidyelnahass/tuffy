package com.eg.user.service;


import com.eg.common.model.enums.CustomerStatusEnum;
import com.eg.common.util.JwtUtil;
import com.eg.user.exception.BadRequestException;
import com.eg.user.exception.NotFoundException;
import com.eg.user.model.entity.UserEntity;
import com.eg.user.model.response.ProfileDetailsResponse;
import com.eg.user.model.response.UserLoginResponse;
import com.eg.user.repository.CustomerStatusRepository;
import com.eg.user.repository.UserRepository;
import com.eg.user.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.lang.reflect.Field;
import java.util.Optional;

import static com.eg.common.model.enums.UserTypeEnum.DRIVER;
import static com.eg.common.model.enums.UserTypeEnum.RIDER;
import static com.eg.user.util.TestConstants.buildCustomerStatusEntity;
import static com.eg.user.util.TestConstants.buildProfileUpdateRequest;
import static com.eg.user.util.TestConstants.buildUserEntity;
import static com.eg.user.util.TestConstants.buildUserLoginRequest;
import static com.eg.user.util.TestConstants.buildVehicleEntity;
import static com.eg.user.util.TestConstants.buildWrongUserLoginRequest;
import static com.eg.user.util.Util.encodePassword;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserAuthenticationServiceTest {

  @InjectMocks
  private UserAuthenticationService userAuthenticationService;

  @Mock
  private UserRepository userRepository;

  @Mock
  private CustomerStatusRepository customerStatusRepository;

  @Mock
  private JwtUtil jwtUtil;

  private static final Integer TEST_LOGIN_TRIALS_ALLOWED = 5;

  @BeforeEach
  void setUp() throws Exception {
    Field field = UserAuthenticationService.class.getDeclaredField("loginTrialsAllowed");
    field.setAccessible(true);
    field.set(userAuthenticationService, TEST_LOGIN_TRIALS_ALLOWED);
  }

  @Test
  void loginSuccessfully() {
    UserEntity user = buildUserEntity(2L, RIDER, CustomerStatusEnum.ACTIVE);
    when(userRepository.findByPhone(any()))
      .thenReturn(Optional.of(user));
    when(userRepository.save(any()))
      .thenReturn(user);
    when(jwtUtil.generateToken(any(), any(),
      any(), any())).thenReturn(encodePassword("112233"));
    UserLoginResponse userLoginResponse
    = userAuthenticationService.login(buildUserLoginRequest());
    assertNotNull(userLoginResponse.getAccessToken());
  }

  @Test
  void loginUserNotFound() {
    when(userRepository.findByPhone(any()))
      .thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () ->
      userAuthenticationService.login(buildUserLoginRequest()));
  }

  @Test
  void loginCustomerInActive() {
    UserEntity user = buildUserEntity(2L, RIDER, CustomerStatusEnum.PENDING);
    when(userRepository.findByPhone(any()))
      .thenReturn(Optional.of(user));
    assertThrows(BadRequestException.class, () ->
      userAuthenticationService.login(buildUserLoginRequest()));
  }

  @Test
  void loginWrongPasswordEntered() {
    UserEntity user = buildUserEntity(2L, RIDER, CustomerStatusEnum.ACTIVE);
    when(userRepository.findByPhone(any()))
      .thenReturn(Optional.of(user));
    when(userRepository.save(any()))
      .thenReturn(user);
    assertThrows(BadRequestException.class, () ->
      userAuthenticationService.login(buildWrongUserLoginRequest()));
  }

  @Test
  void loginWrongPasswordEnteredTooManyTimes() {
    UserEntity user = buildUserEntity(2L, RIDER, CustomerStatusEnum.ACTIVE,
      5);
    when(userRepository.findByPhone(any()))
      .thenReturn(Optional.of(user));
    when(customerStatusRepository.findByValue(any()))
      .thenReturn(Optional.ofNullable(buildCustomerStatusEntity(CustomerStatusEnum.BLOCKED)));
    when(userRepository.save(any()))
      .thenReturn(user);
    assertThrows(BadRequestException.class, () ->
      userAuthenticationService.login(buildWrongUserLoginRequest()));
  }
}
