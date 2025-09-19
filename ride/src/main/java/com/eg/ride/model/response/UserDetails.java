package com.eg.ride.model.response;


import com.eg.ride.client.model.response.VehicleDetailsResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserDetails {

  @JsonProperty("Id")
  private Long id;

  @JsonProperty("Name")
  private String name;

  @JsonProperty("PhoneNumber")
  private String phone;

  @JsonProperty("VehicleDetails")
  private VehicleDetailsResponse vehicleDetails;
}
