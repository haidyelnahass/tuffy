package com.eg.matching.exception;

import com.eg.matching.exception.model.ErrorCode;
import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {

  private final ErrorCode errorCode;

  public NotFoundException(String message, ErrorCode errorCode) {
    super(message);
    this.errorCode = errorCode;
  }
}
