package com.eg.common.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationDetails {

  @JsonProperty("Latitude")
  @NotNull
  private Double latitude;

  @JsonProperty("Longitude")
  @NotNull
  private Double longitude;
}
