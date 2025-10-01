package com.eg.matching.client.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class NearbyLocationsResponse {

  @JsonProperty("NearbyLocations")
  private List<NearbyDriverLocation> nearbyLocations;
}
