package com.eg.user.service;


import com.eg.common.model.enums.CustomerStatusEnum;
import com.eg.common.model.enums.UserTypeEnum;
import com.eg.user.exception.BadRequestException;
import com.eg.user.model.response.ProfileDetailsResponse;
import com.eg.user.repository.UserRepository;
import com.eg.user.repository.VehicleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.eg.common.model.enums.UserTypeEnum.DRIVER;
import static com.eg.common.model.enums.UserTypeEnum.RIDER;
import static com.eg.user.util.TestConstants.buildProfileUpdateRequest;
import static com.eg.user.util.TestConstants.buildUserEntity;
import static com.eg.user.util.TestConstants.buildVehicleEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserProfileServiceTest {

  @InjectMocks
  private UserProfileService userProfileService;

  @Mock
  private UserRepository userRepository;

  @Mock
  private VehicleRepository vehicleRepository;

  @Test
  void getRiderProfileDetailsSuccessfully() {
    when(userRepository.findById(any()))
      .thenReturn(Optional.of(buildUserEntity(1L, RIDER, CustomerStatusEnum.ACTIVE)));

    ProfileDetailsResponse profileDetailsResponse =
      userProfileService.getProfileDetails(1L);

    assertEquals(RIDER.name(), profileDetailsResponse.getUserType());
  }

  @Test
  void getDriverProfileDetailsSuccessfully() {
    when(userRepository.findById(any()))
      .thenReturn(Optional.of(buildUserEntity(1L, DRIVER, CustomerStatusEnum.ACTIVE)));
    when(vehicleRepository.findByUserEntity(any()))
      .thenReturn(Optional.ofNullable(buildVehicleEntity()));

    ProfileDetailsResponse profileDetailsResponse =
      userProfileService.getProfileDetails(1L);

    assertEquals(DRIVER.name(), profileDetailsResponse.getUserType());
  }

  @Test
  void getDriverProfileDetailsUserNotFound() {
    when(userRepository.findById(any()))
      .thenReturn(Optional.empty());

    assertThrows(BadRequestException.class, () ->
      userProfileService.getProfileDetails(1L));
  }

  @Test
  void getDriverProfileDetailsVehicleDetailsNotFound() {
    when(userRepository.findById(any()))
      .thenReturn(Optional.of(buildUserEntity(1L, DRIVER, CustomerStatusEnum.ACTIVE)));
    when(vehicleRepository.findByUserEntity(any()))
      .thenReturn(Optional.empty());

    assertThrows(BadRequestException.class, () ->
      userProfileService.getProfileDetails(1L));
  }

  @Test
  void updateProfileDetailsSuccessfully() {
    when(userRepository.findById(any()))
      .thenReturn(Optional.of(buildUserEntity(1L, RIDER, CustomerStatusEnum.PENDING)));

    userProfileService.updateProfileDetails(1L, buildProfileUpdateRequest());
    verify(userRepository, times(1)).findById(any());
  }
}
