package com.eg.ride.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RideRequest {

  @JsonProperty("Pickup")
  private LocationDetails pickup;

  @JsonProperty("Dropoff")
  private LocationDetails dropoff;

  @JsonProperty("Price")
  private Double price;
}
