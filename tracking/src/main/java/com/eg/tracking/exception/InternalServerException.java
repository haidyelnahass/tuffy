package com.eg.tracking.exception;

import com.eg.tracking.exception.model.ErrorCode;
import lombok.Getter;

@Getter
public class InternalServerException extends RuntimeException {

  private final ErrorCode errorCode;

  public InternalServerException(String message, ErrorCode errorCode) {
    super(message);
    this.errorCode = errorCode;
  }
}
