package com.eg.ride.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RidePriceRequest {

  @JsonProperty("Pickup")
  @NotNull
  private @Valid LocationDetails pickup;

  @JsonProperty("Dropoff")
  @NotNull
  private @Valid LocationDetails dropoff;

}
