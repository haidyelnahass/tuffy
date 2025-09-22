package com.eg.user.model.request;

import com.eg.common.model.enums.UserTypeEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;


@Builder
@Data
public class UserRegistrationRequest {

  @JsonProperty("Email")
  @NotNull
  @Email
  private String email;

  @JsonProperty("Password")
  @NotNull
  private String password;

  @JsonProperty("UserType")
  @NotNull
  private UserTypeEnum userType;

  @JsonProperty("PhoneNumber")
  @NotNull
  private String phone;

  @JsonProperty("VehicleDetails")
  private VehicleDetailsRequest vehicleDetails;

  @JsonProperty("Name")
  private String name;
}
