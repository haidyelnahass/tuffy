package com.eg.user.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserLoginResponse {

  @JsonProperty("AccessToken")
  private String accessToken;
}
