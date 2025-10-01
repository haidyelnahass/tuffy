package com.eg.tracking.model.request;

import com.eg.common.model.enums.DriverStatusEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UpdateLocationRequest {

  @JsonProperty("Latitude")
  @NotNull
  private Double latitude;

  @JsonProperty("Longitude")
  @NotNull
  private Double longitude;

  @JsonProperty("Accuracy")
  private Integer accuracy;

  @JsonProperty("Timestamp")
  @NotNull
  private String timestamp;

  @JsonProperty("DriverStatus")
  private DriverStatusEnum driverStatus;
}
