package com.eg.ride.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RideResponse {

  @JsonProperty("RideId")
  private Long rideId;
}
