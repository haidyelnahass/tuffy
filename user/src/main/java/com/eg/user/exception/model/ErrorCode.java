package com.eg.user.exception.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public enum ErrorCode {
  ACCOUNT_BLOCKED,
  HTTP_MESSAGE_NOT_READABLE,
  WRONG_REQUEST_BODY,
  INCORRECT_PASSWORD_ENTERED,
  UNKNOWN_EXCEPTION,
  USER_ALREADY_EXISTS,
  EMAIL_ALREADY_CONFIRMED,
  WRONG_CONFIRMATION_CODE_ENTERED,
  USER_NOT_FOUND,
  DATA_NOT_FOUND
}
