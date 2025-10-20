package com.eg.user.service;

import com.eg.user.exception.BadRequestException;
import com.eg.user.exception.model.ErrorCode;
import com.eg.user.model.entity.ResetTokenEntity;
import com.eg.user.model.entity.UserEntity;
import com.eg.user.model.request.ForgotPasswordRequest;
import com.eg.user.model.request.ResetPasswordRequest;
import com.eg.user.repository.ResetTokenRepository;
import com.eg.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

import static com.eg.user.util.Constants.ONLY_EMAIL_OR_PHONE_SHOULD_BE_SENT;
import static com.eg.user.util.Constants.RESET_PASSWORD_SUBJECT;
import static com.eg.user.util.Constants.RESET_PASSWORD_TEXT;
import static com.eg.user.util.Constants.TOKEN_EXPIRED;
import static com.eg.user.util.Constants.USER_NOT_FOUND_MESSAGE;
import static com.eg.user.util.Constants.WRONG_TOKEN_RECEIVED;
import static com.eg.user.util.Util.encodePassword;
import static com.eg.user.util.Util.generateToken;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserPasswordResetService {

  private final MailService mailService;

  private final ResetTokenRepository resetTokenRepository;

  private final UserRepository userRepository;

  @Value("${redirection.url}")
  private String redirectionUrl;

  public void sendForgetPasswordEmail(ForgotPasswordRequest request) {
    UserEntity user = validateAndRetrieveUserEntity(request);
    String generatedToken = generateToken();

    mailService.sendMail(RESET_PASSWORD_TEXT
        .replace("<reset_link>", redirectionUrl + "?token=" + generatedToken),
      RESET_PASSWORD_SUBJECT, user.getEmail());

    resetTokenRepository.save(ResetTokenEntity.builder()
      .value(generatedToken)
      .user(user)
      .expiryDate(LocalDateTime.now().plusMinutes(10))
      .build());
  }

  @Transactional
  public void resetPassword(ResetPasswordRequest request) {
    UserEntity user = validateAndRetrieveUserEntity(request);
    user.setPassword(encodePassword(request.getNewPassword()));
  }

  private UserEntity validateAndRetrieveUserEntity(ForgotPasswordRequest request) {
    if (Objects.nonNull(request.getEmail()) && Objects.nonNull(request.getPhone())) {
      throw new BadRequestException(ONLY_EMAIL_OR_PHONE_SHOULD_BE_SENT,
        ErrorCode.WRONG_REQUEST_BODY);
    }
    Optional<UserEntity> optionalUser = userRepository.findByEmailOrPhone(request.getEmail(), request.getPhone());
    if (optionalUser.isEmpty()) {
      throw new BadRequestException(USER_NOT_FOUND_MESSAGE,
        ErrorCode.USER_NOT_FOUND);
    }
    return optionalUser.get();
  }

  private UserEntity validateAndRetrieveUserEntity(ResetPasswordRequest request) {
    Optional<ResetTokenEntity> optionalToken = resetTokenRepository.findByValue(request.getToken());
    if (optionalToken.isEmpty()) {
      throw new BadRequestException(WRONG_TOKEN_RECEIVED,
        ErrorCode.DATA_NOT_FOUND);
    }
    ResetTokenEntity resetTokenEntity = optionalToken.get();

    if(resetTokenEntity.getExpiryDate().isBefore(LocalDateTime.now())) {
      throw new BadRequestException(TOKEN_EXPIRED,
        ErrorCode.WRONG_REQUEST_BODY);
    }

    return resetTokenEntity.getUser();
  }
}
