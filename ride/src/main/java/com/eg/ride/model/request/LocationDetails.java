package com.eg.ride.model.request;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LocationDetails {

  @JsonProperty("Latitude")
  private String latitude;

  @JsonProperty("Longitude")
  private String longitude;
}
