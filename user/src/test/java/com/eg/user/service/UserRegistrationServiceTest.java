package com.eg.user.service;

import com.eg.common.model.enums.CustomerStatusEnum;
import com.eg.common.model.enums.UserTypeEnum;
import com.eg.common.util.KafkaProducerUtil;
import com.eg.common.util.RedisCacheManager;
import com.eg.user.exception.BadRequestException;
import com.eg.user.model.response.UserRegistrationResponse;
import com.eg.user.repository.CustomerStatusRepository;
import com.eg.user.repository.UserRepository;
import com.eg.user.repository.UserTypeRepository;
import com.eg.user.repository.VehicleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.eg.user.util.TestConstants.buildCustomerStatusEntity;
import static com.eg.user.util.TestConstants.buildDriverUserRegistrationRequest;
import static com.eg.user.util.TestConstants.buildEmailConfirmationRequest;
import static com.eg.user.util.TestConstants.buildProfileConfirmationRequest;
import static com.eg.user.util.TestConstants.buildRiderUserRegistrationRequest;
import static com.eg.user.util.TestConstants.buildUserEntity;
import static com.eg.user.util.TestConstants.buildUserTypeEntity;
import static com.eg.user.util.TestConstants.buildVehicleEntity;
import static com.eg.user.util.TestConstants.buildWrongDriverUserRegistrationRequest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserRegistrationServiceTest {

  @InjectMocks
  private UserRegistrationService userRegistrationService;

  @Mock
  private UserRepository userRepository;

  @Mock
  private CustomerStatusRepository customerStatusRepository;

  @Mock
  private RedisCacheManager redisCacheManager;

  @Mock
  private MailService mailService;

  @Mock
  private UserTypeRepository userTypeRepository;

  @Mock
  private VehicleRepository vehicleRepository;

  @Mock
  private KafkaProducerUtil kafkaProducerUtil;


  @Test
  void testRegisterRiderUserSuccessfully() {
    when(userRepository.findByEmailOrPhone(any(), any()))
      .thenReturn(Optional.empty());

    when(customerStatusRepository.findByValue(any()))
      .thenReturn(Optional.of(buildCustomerStatusEntity(CustomerStatusEnum.PENDING)));

    when(userTypeRepository.findByValue(any()))
      .thenReturn(Optional.of(buildUserTypeEntity(UserTypeEnum.RIDER)));
    when(userRepository.save(any()))
      .thenReturn(buildUserEntity(1L, UserTypeEnum.RIDER, CustomerStatusEnum.PENDING));

    UserRegistrationResponse response
      = userRegistrationService.registerUser(buildRiderUserRegistrationRequest());
    assertEquals(1L, response.getUserId());
  }

  @Test
  void testRegisterDriverUserFailed() {
    assertThrows(BadRequestException.class, () ->
      userRegistrationService.registerUser(buildWrongDriverUserRegistrationRequest()));
  }

  @Test
  void testRegisterDriverUserSuccessfully() {
    when(userRepository.findByEmailOrPhone(any(), any()))
      .thenReturn(Optional.empty());

    when(customerStatusRepository.findByValue(any()))
      .thenReturn(Optional.of(buildCustomerStatusEntity(CustomerStatusEnum.PENDING)));

    when(userTypeRepository.findByValue(any()))
      .thenReturn(Optional.of(buildUserTypeEntity(UserTypeEnum.DRIVER)));
    when(userRepository.save(any()))
      .thenReturn(buildUserEntity(1L, UserTypeEnum.DRIVER, CustomerStatusEnum.PENDING));

    when(vehicleRepository.save(any()))
      .thenReturn(buildVehicleEntity());

    UserRegistrationResponse response
      = userRegistrationService.registerUser(buildDriverUserRegistrationRequest());
    assertEquals(1L, response.getUserId());
  }

  @Test
  void testRegisterDriverUserAlreadyExists() {
    when(userRepository.findByEmailOrPhone(any(), any()))
      .thenReturn(Optional.of(buildUserEntity(1L, UserTypeEnum.DRIVER, CustomerStatusEnum.PENDING)));

    assertThrows(BadRequestException.class, () ->
      userRegistrationService.registerUser(buildDriverUserRegistrationRequest()));
  }

  @Test
  void testSendConfirmationEmailUserNotFound() {
    when(userRepository.findById(any()))
      .thenReturn(Optional.empty());

    assertThrows(BadRequestException.class, () ->
      userRegistrationService.sendConfirmationEmail(buildEmailConfirmationRequest()));

  }

  @Test
  void testSendConfirmationEmailSuccessfully() {
    when(userRepository.findById(any()))
      .thenReturn(Optional.of(buildUserEntity(1L, UserTypeEnum.RIDER, CustomerStatusEnum.PENDING)));

    doNothing().when(redisCacheManager).saveToCache(any(), any());
    doNothing().when(mailService).sendMail(any(), any(), any());

    userRegistrationService.sendConfirmationEmail(buildEmailConfirmationRequest());

    verify(userRepository, times(1)).findById(any());

  }

  @Test
  void testConfirmEmailSuccessfully() {
    when(userRepository.findById(any()))
      .thenReturn(Optional.of(buildUserEntity(1L, UserTypeEnum.RIDER, CustomerStatusEnum.PENDING)));

    when(redisCacheManager.getFromCache(any())).thenReturn(112233);
    when(userRepository.save(any())).thenReturn(buildUserEntity(1L,
      UserTypeEnum.RIDER, CustomerStatusEnum.ACTIVE));
    when(customerStatusRepository.findByValue(any()))
      .thenReturn(Optional.of(buildCustomerStatusEntity(CustomerStatusEnum.ACTIVE)));

    doNothing().when(kafkaProducerUtil).sendMessage(any(), any());

    userRegistrationService.confirmProfile(buildProfileConfirmationRequest());

    verify(userRepository, times(1)).save(any());

  }

  @Test
  void testConfirmEmailWrongConfirmationCode() {
    when(userRepository.findById(any()))
      .thenReturn(Optional.of(buildUserEntity(1L, UserTypeEnum.RIDER, CustomerStatusEnum.PENDING)));

    when(redisCacheManager.getFromCache(any())).thenReturn(115533);

    assertThrows(BadRequestException.class,
      () -> userRegistrationService.confirmProfile(buildProfileConfirmationRequest()));
  }




}
