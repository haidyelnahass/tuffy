package com.eg.account.model.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ProfileDetailsMessage {

  @JsonProperty("Username")
  private String username;

  @JsonProperty("Name")
  private String name;

  @JsonProperty("RegistrationDate")
  private String registrationDate;

  @JsonProperty("CustomerStatus")
  private String customerStatus;
}
