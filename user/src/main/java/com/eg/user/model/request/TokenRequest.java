package com.eg.user.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenRequest {

  @JsonProperty("RefreshToken")
  @NotNull
  private String refreshToken;
}
