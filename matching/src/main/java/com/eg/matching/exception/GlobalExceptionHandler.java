package com.eg.matching.exception;

import com.eg.matching.exception.model.ErrorMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.eg.matching.exception.model.ErrorCode.HTTP_MESSAGE_NOT_READABLE;
import static com.eg.matching.exception.model.ErrorCode.UNKNOWN_EXCEPTION;
import static com.eg.matching.exception.model.ErrorCode.WRONG_REQUEST_BODY;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorMessage> handleMessageNotReadable(HttpMessageNotReadableException exception) {
    log.error("handleMessageNotReadable::", exception);
    return ResponseEntity.badRequest().body(ErrorMessage.builder()
      .errorCode(HTTP_MESSAGE_NOT_READABLE)
      .message(exception.getMessage().split(":")[0])
      .timestamp(LocalDateTime.now()).build());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorMessage> handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
    log.error("handleMethodArgumentNotValid::", exception);
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
    log.error("handleBadRequestException::", exception);
    return ResponseEntity.badRequest().body(ErrorMessage.builder()
      .errorCode(exception.getErrorCode())
      .message(exception.getMessage())
      .timestamp(LocalDateTime.now())
      .build());
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ErrorMessage> handleNotFoundException(NotFoundException exception) {
    log.error("handleNotFoundException::", exception);
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorMessage.builder()
      .errorCode(exception.getErrorCode())
      .message(exception.getMessage())
      .timestamp(LocalDateTime.now())
      .build());
  }


  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorMessage> handleGeneric(Exception exception) {
    log.error("handleGeneric::", exception);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.builder()
      .message(exception.getMessage())
      .errorCode(UNKNOWN_EXCEPTION)
      .timestamp(LocalDateTime.now())
      .build());
  }
}
