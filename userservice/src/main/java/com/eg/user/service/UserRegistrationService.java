package com.eg.user.service;

import com.eg.user.exception.BadRequestException;
import com.eg.user.mapper.UserMapper;
import com.eg.user.mapper.VehicleMapper;
import com.eg.user.model.entity.CustomerStatusEntity;
import com.eg.user.model.entity.UserEntity;
import com.eg.user.model.entity.UserTypeEntity;
import com.eg.user.model.entity.VehicleEntity;
import com.eg.user.model.enums.CustomerStatusEnum;
import com.eg.user.model.enums.UserTypeEnum;
import com.eg.user.model.request.EmailConfirmationRequest;
import com.eg.user.model.request.ProfileConfirmationRequest;
import com.eg.user.model.request.UserRegistrationRequest;
import com.eg.user.model.response.ProfileDetailsResponse;
import com.eg.user.model.response.UserRegistrationResponse;
import com.eg.user.repository.CustomerStatusRepository;
import com.eg.user.repository.UserRepository;
import com.eg.user.repository.UserTypeRepository;
import com.eg.user.repository.VehicleRepository;
import com.eg.user.util.KafkaProducerUtil;
import com.eg.user.util.RedisCacheManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

import static com.eg.user.exception.model.ErrorCode.DATA_NOT_FOUND;
import static com.eg.user.exception.model.ErrorCode.EMAIL_ALREADY_CONFIRMED;
import static com.eg.user.exception.model.ErrorCode.USER_ALREADY_EXISTS;
import static com.eg.user.exception.model.ErrorCode.USER_NOT_FOUND;
import static com.eg.user.exception.model.ErrorCode.WRONG_CONFIRMATION_CODE_ENTERED;
import static com.eg.user.exception.model.ErrorCode.WRONG_REQUEST_BODY;
import static com.eg.user.util.Constants.CUSTOMER_STATUS_NOT_FOUND;
import static com.eg.user.util.Constants.MISSING_VEHICLE_DETAILS;
import static com.eg.user.util.Constants.USER_ALREADY_CONFIRMED_HIS_EMAIL;
import static com.eg.user.util.Constants.USER_ALREADY_EXISTS_MESSAGE;
import static com.eg.user.util.Constants.USER_NOT_FOUND_MESSAGE;
import static com.eg.user.util.Constants.USER_TYPE_NOT_FOUND;
import static com.eg.user.util.Constants.WRONG_CONFIRMATION_CODE;
import static com.eg.user.util.Util.generateRandomConfirmationCode;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserRegistrationService {

  private final UserRepository userRepository;
  private final CustomerStatusRepository customerStatusRepository;
  private final RedisCacheManager redisCacheManager;
  private final MailService mailService;
  private final UserTypeRepository userTypeRepository;
  private final VehicleRepository vehicleRepository;

  @Value("${spring.kafka.account-topic}")
  private String accountTopic;

  private final KafkaProducerUtil kafkaProducerUtil;

  public UserRegistrationResponse registerUser(UserRegistrationRequest request) {
    log.info("registerUser:: validating user request");
    validateRegistrationRequest(request);
    log.info("registerUser:: check if an existing user already has email or phone");
    Optional<UserEntity> optionalUser = userRepository.findByEmailOrPhone(request.getEmail(),
      request.getPhone());
    if (optionalUser.isPresent()) {
      log.error("registerUser:: credentials already present, throw error");
      throw new BadRequestException(USER_ALREADY_EXISTS_MESSAGE, USER_ALREADY_EXISTS);
    } else {
      log.info("registerUser:: prepare user entity");
      UserEntity user = UserMapper.INSTANCE.map(request);
      CustomerStatusEntity statusEntity = findStatus(CustomerStatusEnum.PENDING.name());
      user.setCustomerStatus(statusEntity);
      user.setUserTypeEntity(findUserType(request.getUserType().toString()));
      log.info("registerUser:: saving user entity");
      user = userRepository.save(user);
      if (request.getUserType().equals(UserTypeEnum.DRIVER)) {
        log.info("registerUser:: prepare vehicle details");
        VehicleEntity vehicleEntity = VehicleMapper.INSTANCE.map(request.getVehicleDetails());
        vehicleEntity.setUserEntity(user);
        log.info("registerUser:: save vehicle details");
        vehicleRepository.save(vehicleEntity);
      }
      log.info("registerUser:: return success response");
      return UserRegistrationResponse.builder()
        .userId(user.getId())
        .build();
    }
  }

  public void sendConfirmationEmail(EmailConfirmationRequest request) {
    log.info("sendConfirmationEmail:: check if user exists");
    UserEntity user = checkExistenceOfUser(request.getProfileId());
    if (!user.getCustomerStatus().getValue().equals(CustomerStatusEnum.PENDING.name())) {
      log.error("sendConfirmationEmail:: user already confirmed his email");
      throw new BadRequestException(USER_ALREADY_CONFIRMED_HIS_EMAIL,
        EMAIL_ALREADY_CONFIRMED);
    }
    log.info("sendConfirmationEmail:: generate code and send email");
    Integer confirmationCode = generateRandomConfirmationCode();
    redisCacheManager.saveToCache(user.getId().toString(), confirmationCode);
    mailService.sendMail(confirmationCode, user.getEmail());
  }

  public void confirmProfile(ProfileConfirmationRequest request) {
    UserEntity user = checkExistenceOfUser(request.getProfileId());
    Integer confirmationCode = (Integer) redisCacheManager.getFromCache(user.getId().toString());

    if (!request.getConfirmationCode().equals(confirmationCode)) {
      throw new BadRequestException(WRONG_CONFIRMATION_CODE,
        WRONG_CONFIRMATION_CODE_ENTERED);
    }
    user.setPreviousCustomerStatus(user.getCustomerStatus());
    user.setCustomerStatus(findStatus(CustomerStatusEnum.ACTIVE.name()));
    userRepository.save(user);
    ProfileDetailsResponse details = UserMapper.INSTANCE.map(user);
    kafkaProducerUtil.sendMessage(accountTopic, details);
    // TODO maybe account ready topic is not required
  }


  private UserEntity checkExistenceOfUser(Long userId) {
    return userRepository.findById(userId)
      .orElseThrow(() -> new BadRequestException(USER_NOT_FOUND_MESSAGE,
        USER_NOT_FOUND));
  }

  private CustomerStatusEntity findStatus(String status) {
    return customerStatusRepository
      .findByValue(status).orElseThrow(() -> new BadRequestException(CUSTOMER_STATUS_NOT_FOUND,
        DATA_NOT_FOUND));
  }

  private UserTypeEntity findUserType(String type) {
    return userTypeRepository
      .findByValue(type).orElseThrow(() -> new BadRequestException(USER_TYPE_NOT_FOUND,
        DATA_NOT_FOUND));
  }

  private void validateRegistrationRequest(UserRegistrationRequest request) {
    if (request.getUserType().equals(UserTypeEnum.DRIVER) &&
      Objects.isNull(request.getVehicleDetails())) {
      throw new BadRequestException(MISSING_VEHICLE_DETAILS,
        WRONG_REQUEST_BODY);
    }
  }
}
