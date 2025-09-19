package com.eg.user.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class VehicleDetailsRequest {

  @JsonProperty("Model")
  private final String model;

  @JsonProperty("PlateNumber")
  private final String plateNumber;

  @JsonProperty("Capacity")
  private final String capacity;
}
