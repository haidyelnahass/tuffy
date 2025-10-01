package com.eg.matching.client.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class NearbyDriverLocation {

  @JsonProperty("DriverId")
  private Long driverId;

  @JsonProperty("Latitude")
  private double latitude;

  @JsonProperty("Longitude")
  private double longitude;

  @JsonProperty("Distance")
  private double distance;
}
