package com.eg.ride.model.response;


import com.eg.common.model.LocationDetails;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RideDetailsResponse {

  @JsonProperty("RiderDetails")
  private UserDetails riderDetails;

  @JsonProperty("DriverDetails")
  private UserDetails driverDetails;

  @JsonProperty("Pickup")
  private LocationDetails pickup;

  @JsonProperty("Dropoff")
  private LocationDetails dropOff;

  @JsonProperty("Price")
  private Double price;

  @JsonProperty("EstimatedPickupTime")
  private Integer estimatedPickupTime;
}
