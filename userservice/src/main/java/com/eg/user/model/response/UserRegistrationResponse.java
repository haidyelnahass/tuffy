package com.eg.user.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserRegistrationResponse {

  @JsonProperty("GeneratedUserId")
  private Long userId;
}
