package com.eg.matching.exception.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class ErrorMessage {

  @JsonProperty("Message")
  private Object message;

  @JsonProperty("ErrorCode")
  private ErrorCode errorCode;

  @JsonProperty("Timestamp")
  private LocalDateTime timestamp;
}
