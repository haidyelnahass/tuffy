package com.eg.matching.exception.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public enum ErrorCode {
  HTTP_MESSAGE_NOT_READABLE,
  WRONG_REQUEST_BODY,
  UNKNOWN_EXCEPTION
}
