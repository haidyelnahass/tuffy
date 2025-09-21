package com.eg.ride.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class RidePriceResponse {

  @JsonProperty("RideTypesDetails")
  public List<RideTypeDetails> rideTypesDetailsList;

  @JsonProperty("ExpectedArrivalTime")
  public Integer expectedArrivalTimeInMinutes;
}
