package com.eg.ride.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RideTypeDetails {

  @JsonProperty("RideTypeName")
  private String rideTypeName;

  @JsonProperty("RideTypeDescription")
  private String rideTypeDescription;

  @JsonProperty("Price")
  private Double price;
}
