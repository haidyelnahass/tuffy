package com.eg.user.service;

import com.eg.user.exception.BadRequestException;
import com.eg.user.exception.NotFoundException;
import com.eg.user.exception.model.ErrorCode;
import com.eg.user.mapper.UserMapper;
import com.eg.user.mapper.VehicleMapper;
import com.eg.user.model.entity.UserEntity;
import com.eg.user.model.entity.VehicleEntity;
import com.eg.common.model.enums.UserTypeEnum;
import com.eg.user.model.request.ProfileUpdateRequest;
import com.eg.user.model.response.ProfileDetailsResponse;
import com.eg.user.repository.UserRepository;
import com.eg.user.repository.VehicleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.eg.user.util.Constants.MISSING_VEHICLE_DETAILS;
import static com.eg.user.util.Constants.USER_NOT_FOUND_MESSAGE;

@Service
@RequiredArgsConstructor
public class UserProfileService {

  private final UserRepository userRepository;
  private final VehicleRepository vehicleRepository;

  public ProfileDetailsResponse getProfileDetails(Long profileId) {
    UserEntity user = checkExistenceOfUser(profileId);
    ProfileDetailsResponse response = UserMapper.INSTANCE.map(user);
    if (user.getUserTypeEntity().getValue().equals(UserTypeEnum.DRIVER.name())) {
      VehicleEntity vehicleEntity = vehicleRepository.findByUserEntity(user)
        .orElseThrow(() -> new BadRequestException(MISSING_VEHICLE_DETAILS,
          ErrorCode.DATA_NOT_FOUND));
      response.setVehicleDetails(VehicleMapper.INSTANCE.map(vehicleEntity));
    }
    return response;
  }

  private UserEntity checkExistenceOfUser(Long userId) {
    return userRepository.findById(userId)
      .orElseThrow(() -> new BadRequestException(USER_NOT_FOUND_MESSAGE,
        ErrorCode.USER_NOT_FOUND));
  }

  @Transactional
  public void updateProfileDetails(Long profileId, ProfileUpdateRequest request) {
    UserEntity user = checkExistenceOfUser(profileId);
    user.setName(request.getName());
  }
}
