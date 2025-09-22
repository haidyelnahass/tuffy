package com.eg.user.service;

import com.eg.user.exception.BadRequestException;
import com.eg.user.exception.NotFoundException;
import com.eg.user.exception.model.ErrorCode;
import com.eg.user.model.entity.UserEntity;
import com.eg.common.model.enums.CustomerStatusEnum;
import com.eg.user.model.request.UserLoginRequest;
import com.eg.user.model.response.UserLoginResponse;
import com.eg.user.repository.CustomerStatusRepository;
import com.eg.user.repository.UserRepository;
import com.eg.common.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static com.eg.user.util.Constants.CUSTOMER_STATUS_NOT_FOUND;
import static com.eg.user.util.Constants.INCORRECT_PASSWORD_ENTERED;
import static com.eg.user.util.Constants.USER_NOT_ACTIVE;
import static com.eg.user.util.Constants.USER_NOT_FOUND_MESSAGE;
import static com.eg.user.util.Constants.WRONG_PASSWORD_ENTERED_TOO_MANY_TIMES;
import static com.eg.user.util.Util.checkHashMatch;

@Service
@RequiredArgsConstructor
public class UserAuthenticationService {

  private final UserRepository userRepository;
  private final CustomerStatusRepository customerStatusRepository;

  private final JwtUtil jwtUtil;

  @Value("${spring.login-trials-allowed:3}")
  private Integer loginTrialsAllowed;

  public UserLoginResponse login(UserLoginRequest request) {
    UserEntity user = checkExistenceOfUser(request.getPhone());
    if (!user.getCustomerStatus().getValue().equals(CustomerStatusEnum.ACTIVE.name())) {
      throw new BadRequestException(USER_NOT_ACTIVE,
        ErrorCode.DATA_NOT_FOUND);
    }
    user.setLoginAttempts(user.getLoginAttempts() + 1);
    if (!checkHashMatch(request.getPassword(), user.getPassword())) {
      String exceptionMessage;
      ErrorCode errorCode;
      if (user.getLoginAttempts().equals(loginTrialsAllowed)) {
        user.setCustomerStatus(customerStatusRepository.findByValue(CustomerStatusEnum.BLOCKED.name())
          .orElseThrow(() -> new BadRequestException(CUSTOMER_STATUS_NOT_FOUND,
            ErrorCode.DATA_NOT_FOUND)));
        exceptionMessage = WRONG_PASSWORD_ENTERED_TOO_MANY_TIMES;
        errorCode = ErrorCode.ACCOUNT_BLOCKED;
      } else {
        exceptionMessage = INCORRECT_PASSWORD_ENTERED;
        errorCode = ErrorCode.INCORRECT_PASSWORD_ENTERED;
      }
      userRepository.save(user);
      throw new BadRequestException(exceptionMessage, errorCode);
    }
    user.setLoginAttempts(0);
    userRepository.save(user);
    return UserLoginResponse.builder()
      .accessToken(jwtUtil
        .generateToken(user.getId().toString(), user.getName(),
          user.getUserTypeEntity().getValue(), user.getCustomerStatus().getValue()))
      .build();
  }

  private UserEntity checkExistenceOfUser(String email) {
    return userRepository.findByPhone(email)
      .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND_MESSAGE,
        ErrorCode.USER_NOT_FOUND));
  }
}
