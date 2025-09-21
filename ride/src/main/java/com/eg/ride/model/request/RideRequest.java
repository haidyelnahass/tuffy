package com.eg.ride.model.request;

import com.eg.ride.model.enums.RideTypeEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RideRequest {

  @JsonProperty("Pickup")
  @NotNull
  private @Valid LocationDetails pickup;

  @JsonProperty("Dropoff")
  @NotNull
  private @Valid LocationDetails dropoff;

  @JsonProperty("Price")
  @NotNull
  private Double price;

  @JsonProperty("RideType")
  @NotNull
  private RideTypeEnum rideType;
}
