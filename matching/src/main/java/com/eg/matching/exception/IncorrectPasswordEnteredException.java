package com.eg.matching.exception;

public class IncorrectPasswordEnteredException extends RuntimeException {

  public IncorrectPasswordEnteredException(String message) {
    super(message);
  }
}
