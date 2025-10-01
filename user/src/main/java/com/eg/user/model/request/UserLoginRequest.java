package com.eg.user.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;


@Builder
@Data
public class UserLoginRequest {

  @JsonProperty("PhoneNumber")
  @NotNull
  private String phone;

  @JsonProperty("Password")
  @NotNull
  private String password;
}
