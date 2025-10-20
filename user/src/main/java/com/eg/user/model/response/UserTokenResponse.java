package com.eg.user.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserTokenResponse {

  @JsonProperty("AccessToken")
  private String accessToken;

  @JsonProperty("RefreshToken")
  private String refreshToken;

  @JsonProperty("ExpiresIn")
  private long expiresIn;

  @JsonProperty("IssuedAt")
  private long issuedAt;

  @JsonProperty("TokenType")
  private String tokenType;


}
