package com.eg.user.service;


import com.eg.common.model.TokenDetails;
import com.eg.common.model.enums.CustomerStatusEnum;
import com.eg.common.util.JwtUtil;
import com.eg.user.exception.BadRequestException;
import com.eg.user.exception.NotFoundException;
import com.eg.user.model.entity.UserEntity;
import com.eg.user.model.response.UserTokenResponse;
import com.eg.user.repository.CustomerStatusRepository;
import com.eg.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.Optional;

import static com.eg.common.model.enums.UserTypeEnum.RIDER;
import static com.eg.user.util.TestConstants.buildCustomerStatusEntity;
import static com.eg.user.util.TestConstants.buildUserEntity;
import static com.eg.user.util.TestConstants.buildUserLoginRequest;
import static com.eg.user.util.TestConstants.buildWrongUserLoginRequest;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserAuthenticationServiceTest {

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
    when(jwtUtil.generateAccessToken(any(), any(),
      any(), any())).thenReturn(TokenDetails.builder().token("xxx").build());
    UserTokenResponse userTokenResponse
    = userAuthenticationService.login(buildUserLoginRequest());
    assertNotNull(userTokenResponse.getAccessToken());
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
