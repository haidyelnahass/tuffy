package com.eg.ride.model.request;

import com.eg.ride.model.enums.RideStatusEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UpdateRideStatusRequest {

  @JsonProperty
  @NotBlank
  private RideStatusEnum rideStatus;
}
