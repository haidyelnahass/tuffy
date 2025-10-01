package com.eg.ride.client.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class DriverLocationResponse {

  @JsonProperty("Latitude")
  private double latitude;

  @JsonProperty("Longitude")
  private double longitude;

  @JsonProperty("LastUpdateDate")
  private String lastUpdateDate;

  @JsonProperty("DriverStatus")
  private String driverStatus;
}
