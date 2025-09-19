package com.eg.ride.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class DriverAssignmentRequest {

  @JsonProperty("DriverId")
  private Long driverId;
}
