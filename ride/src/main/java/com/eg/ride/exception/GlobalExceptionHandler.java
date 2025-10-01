package com.eg.ride.exception;

import com.eg.ride.exception.model.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.eg.ride.exception.model.ErrorCode.ACCESS_DENIED;
import static com.eg.ride.exception.model.ErrorCode.HTTP_MESSAGE_NOT_READABLE;
import static com.eg.ride.exception.model.ErrorCode.UNKNOWN_EXCEPTION;
import static com.eg.ride.exception.model.ErrorCode.WRONG_REQUEST_BODY;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorMessage> handleMessageNotReadable(HttpMessageNotReadableException exception) {
    return ResponseEntity.badRequest().body(ErrorMessage.builder()
      .message(exception.getMessage().split(":")[0])
      .errorCode(HTTP_MESSAGE_NOT_READABLE)
      .timestamp(LocalDateTime.now()).build());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorMessage> handleMessageNotReadable(MethodArgumentNotValidException exception) {
    List<String> fieldErrors = new ArrayList<>();
    exception.getBindingResult().getFieldErrors().forEach(error ->
      fieldErrors.add(error.getField() + " " + error.getDefaultMessage())
    );
    return ResponseEntity.badRequest().body(ErrorMessage.builder()
      .message(fieldErrors)
      .errorCode(WRONG_REQUEST_BODY)
      .timestamp(LocalDateTime.now())
      .build());
  }

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<ErrorMessage> handleBadRequestException(BadRequestException exception) {
    return ResponseEntity.badRequest().body(ErrorMessage.builder()
      .message(exception.getMessage())
      .errorCode(exception.getErrorCode())
      .timestamp(LocalDateTime.now())
      .build());
  }

  @ExceptionHandler(InternalServerException.class)
  public ResponseEntity<ErrorMessage> handleInternalServerException(InternalServerException exception) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.builder()
      .message(exception.getMessage())
      .errorCode(exception.getErrorCode())
      .timestamp(LocalDateTime.now())
      .build());
  }


  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorMessage> handleGeneric(Exception ex) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.builder()
      .message(ex.getMessage())
      .timestamp(LocalDateTime.now())
      .errorCode(UNKNOWN_EXCEPTION)
      .build());
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ErrorMessage> handleAccessDenied(AccessDeniedException exception) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN)
      .body(ErrorMessage.builder()
        .errorCode(ACCESS_DENIED)
        .message(exception.getMessage())
        .timestamp(LocalDateTime.now())
        .build());
  }
}
