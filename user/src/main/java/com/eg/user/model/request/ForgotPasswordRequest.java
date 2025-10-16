package com.eg.user.model.request;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ForgotPasswordRequest {

  @JsonProperty("PhoneNumber")
  private String phone;

  @JsonProperty("Email")
  private String email;
}
