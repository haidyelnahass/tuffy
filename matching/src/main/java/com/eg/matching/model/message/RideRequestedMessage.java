package com.eg.matching.model.message;

import com.eg.common.model.LocationDetails;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RideRequestedMessage {

  @JsonProperty("RideId")
  private Long rideId;

  @JsonProperty("RiderId")
  private Long riderId;

  @JsonProperty("Pickup")
  private LocationDetails pickup;

  @JsonProperty("DropOff")
  private LocationDetails dropOff;

  @JsonProperty("Price")
  private Double price;

  @JsonProperty("RideType")
  private String type;
}
