package com.eg.ride.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class DriverAssignmentRequest {

  @JsonProperty("DriverId")
  @NotNull
  private Long driverId;
}
