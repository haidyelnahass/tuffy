package com.eg.user.service;

import com.eg.common.model.TokenDetails;
import com.eg.user.exception.BadRequestException;
import com.eg.user.exception.NotFoundException;
import com.eg.user.exception.model.ErrorCode;
import com.eg.user.model.entity.RefreshTokenEntity;
import com.eg.user.model.entity.UserEntity;
import com.eg.common.model.enums.CustomerStatusEnum;
import com.eg.user.model.request.TokenRequest;
import com.eg.user.model.request.UserLoginRequest;
import com.eg.user.model.response.UserTokenResponse;
import com.eg.user.repository.CustomerStatusRepository;
import com.eg.user.repository.RefreshTokenRepository;
import com.eg.user.repository.UserRepository;
import com.eg.common.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.annotation.web.oauth2.resourceserver.OpaqueTokenDsl;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static com.eg.user.util.Constants.CUSTOMER_STATUS_NOT_FOUND;
import static com.eg.user.util.Constants.INCORRECT_PASSWORD_ENTERED;
import static com.eg.user.util.Constants.TOKEN_TYPE;
import static com.eg.user.util.Constants.USER_NOT_ACTIVE;
import static com.eg.user.util.Constants.USER_NOT_FOUND_MESSAGE;
import static com.eg.user.util.Constants.WRONG_PASSWORD_ENTERED_TOO_MANY_TIMES;
import static com.eg.user.util.Util.checkHashMatch;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserAuthenticationService {

  private final UserRepository userRepository;
  private final CustomerStatusRepository customerStatusRepository;
  private final RefreshTokenRepository refreshTokenRepository;
  private final JwtUtil jwtUtil;

  @Value("${spring.login-trials-allowed:3}")
  private Integer loginTrialsAllowed;

  public UserTokenResponse login(UserLoginRequest request) {
    UserEntity user = checkExistenceOfUser(request.getPhone());
    log.info("login:: check if user {} has status = active", user.getPhone());
    if (!user.getCustomerStatus().getValue().equals(CustomerStatusEnum.ACTIVE.name())) {
      log.error("login:: user status is not active, throw error");
      throw new BadRequestException(USER_NOT_ACTIVE,
        ErrorCode.DATA_NOT_FOUND);
    }
    checkIfActiveRefreshTokenExists(user).ifPresent(entity -> {
      log.info("login:: revoke active token to generate new one");
      refreshTokenRepository.delete(entity);
    });
    user.setLoginAttempts(user.getLoginAttempts() + 1);
    log.info("login:: check hash match");
    if (!checkHashMatch(request.getPassword(), user.getPassword())) {
      String exceptionMessage;
      ErrorCode errorCode;
      log.error("login:: check if login trials exceeded");
      if (user.getLoginAttempts() >= loginTrialsAllowed) {
        log.error("login:: login trials exceeded, change user status to blocked");
        user.setCustomerStatus(customerStatusRepository.findByValue(CustomerStatusEnum.BLOCKED.name())
          .orElseThrow(() -> new BadRequestException(CUSTOMER_STATUS_NOT_FOUND,
            ErrorCode.DATA_NOT_FOUND)));
        exceptionMessage = WRONG_PASSWORD_ENTERED_TOO_MANY_TIMES;
        errorCode = ErrorCode.ACCOUNT_BLOCKED;
      } else {
        log.error("login:: login trials not exceeded, return normal error");
        exceptionMessage = INCORRECT_PASSWORD_ENTERED;
        errorCode = ErrorCode.INCORRECT_PASSWORD_ENTERED;
      }
      userRepository.save(user);
      throw new BadRequestException(exceptionMessage, errorCode);
    }
    user.setLoginAttempts(0);
    userRepository.save(user);
    log.info("login:: prepare access token data");
    TokenDetails accessTokenDetails = jwtUtil
      .generateAccessToken(user.getId().toString(), user.getName(),
        user.getUserTypeEntity().getValue(), user.getCustomerStatus().getValue());
    log.info("login:: prepare refresh token data");
    TokenDetails refreshTokenDetails
      = jwtUtil.generateRefreshToken(user.getId().toString());
    saveRefreshTokenData(refreshTokenDetails, user);
    log.info("login:: return response");
    return UserTokenResponse.builder()
      .accessToken(accessTokenDetails.getToken())
      .refreshToken(refreshTokenDetails.getToken())
      .issuedAt(Instant.now().getEpochSecond())
      .expiresIn(accessTokenDetails.getExpiresIn())
      .tokenType(TOKEN_TYPE)
      .build();
  }

  private Optional<RefreshTokenEntity> checkIfActiveRefreshTokenExists(UserEntity user) {
    log.info("checkIfActiveRefreshTokenExists by user");
    return refreshTokenRepository.findByUserAndExpiryDateAfter(user,
      LocalDateTime.now());
  }

  private Optional<RefreshTokenEntity> checkIfActiveRefreshTokenExists(String token) {
    log.info("checkIfActiveRefreshTokenExists by token");
    return refreshTokenRepository.findByValueAndExpiryDateAfter(token,
      LocalDateTime.now());
  }

  private void saveRefreshTokenData(TokenDetails refreshTokenDetails,
                                    UserEntity user) {
    refreshTokenRepository.save(RefreshTokenEntity.builder()
      .value(refreshTokenDetails.getToken())
      .expiryDate(LocalDateTime.now().plus(refreshTokenDetails.getExpiresIn(), ChronoUnit.MILLIS))
      .user(user)
      .build());
  }

  private UserEntity checkExistenceOfUser(String phone) {
    log.info("checkExistenceOfUser:: find user by phone {}", phone);
    return userRepository.findByPhone(phone)
      .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND_MESSAGE,
        ErrorCode.USER_NOT_FOUND));
  }


  public UserTokenResponse refreshToken(TokenRequest request) {
    Optional<RefreshTokenEntity> optionalRefreshToken
      = checkIfActiveRefreshTokenExists(request.getRefreshToken());
    if (optionalRefreshToken.isEmpty()) {
      throw new BadRequestException("Refresh token not found or expired",
        ErrorCode.DATA_NOT_FOUND);
    } else {
      log.info("refreshToken:: prepare access token data");
      UserEntity user = optionalRefreshToken.get().getUser();
      TokenDetails accessTokenDetails = jwtUtil
        .generateAccessToken(user.getId().toString(), user.getName(),
          user.getUserTypeEntity().getValue(), user.getCustomerStatus().getValue());
      log.info("refreshToken:: prepare refresh token data");
      TokenDetails refreshTokenDetails
        = jwtUtil.generateRefreshToken(user.getId().toString());
      saveRefreshTokenData(refreshTokenDetails, user);
      log.info("refreshToken:: revoke old refresh token");
      refreshTokenRepository.delete(optionalRefreshToken.get());
      log.info("refreshToken:: return response");
      return UserTokenResponse.builder()
        .accessToken(accessTokenDetails.getToken())
        .refreshToken(refreshTokenDetails.getToken())
        .issuedAt(Instant.now().getEpochSecond())
        .expiresIn(accessTokenDetails.getExpiresIn())
        .tokenType(TOKEN_TYPE)
        .build();
    }
  }

  public void logout(TokenRequest request) {
    Optional<RefreshTokenEntity> optionalRefreshToken
      = checkIfActiveRefreshTokenExists(request.getRefreshToken());
    if (optionalRefreshToken.isEmpty()) {
      throw new BadRequestException("Refresh token not found or expired",
        ErrorCode.DATA_NOT_FOUND);
    } else {
      refreshTokenRepository.delete(optionalRefreshToken.get());
    }
  }
}
