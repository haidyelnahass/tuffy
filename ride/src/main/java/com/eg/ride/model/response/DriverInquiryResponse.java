package com.eg.ride.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class DriverInquiryResponse {

  @JsonProperty("Latitude")
  private double latitude;

  @JsonProperty("Longitude")
  private double longitude;

  @JsonProperty("LastUpdateDate")
  private String lastUpdateDate;

  @JsonProperty("EstimatedArrivalTime")
  private Integer estimatedArrivalTime;
}
