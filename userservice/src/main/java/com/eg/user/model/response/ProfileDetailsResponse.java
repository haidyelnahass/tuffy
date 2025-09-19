package com.eg.user.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ProfileDetailsResponse {

  @JsonProperty("Name")
  private String name;

  @JsonProperty("RegistrationDate")
  private String registrationDate;

  @JsonProperty("CustomerStatus")
  private String customerStatus;

  @JsonProperty("PhoneNumber")
  private String phone;

  @JsonProperty("VehicleDetails")
  private VehicleDetailsResponse vehicleDetails;
}
