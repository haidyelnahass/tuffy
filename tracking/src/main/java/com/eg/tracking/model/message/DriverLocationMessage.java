package com.eg.tracking.model.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class DriverLocationMessage {

  @JsonProperty("DriverId")
  private Long driverId;

  @JsonProperty("Latitude")
  private double latitude;

  @JsonProperty("Longitude")
  private double longitude;

  @JsonProperty("LastUpdateDate")
  private String lastUpdateDate;
}
