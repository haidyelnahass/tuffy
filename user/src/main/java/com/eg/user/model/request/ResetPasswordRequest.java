package com.eg.user.model.request;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

@Builder
@Data
public class ResetPasswordRequest {

  @JsonProperty("Token")
  @NotBlank
  private String token;

  @JsonProperty("NewPassword")
  @NotBlank
  private String newPassword;


}
