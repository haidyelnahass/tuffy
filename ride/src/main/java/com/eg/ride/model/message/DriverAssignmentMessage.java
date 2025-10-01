package com.eg.ride.model.message;

import com.eg.common.model.LocationDetails;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverAssignmentMessage {

  @JsonProperty("RideId")
  @NotNull
  private Long rideId;

  @JsonProperty("DriverId")
  @NotNull
  private Long driverId;

  @JsonProperty("DriverLocation")
  @NotNull
  private @Valid LocationDetails driverLocation;

}
