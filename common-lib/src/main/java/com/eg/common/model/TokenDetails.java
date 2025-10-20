package com.eg.common.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenDetails {

  @JsonProperty("Token")
  private String token;

  @JsonProperty("ExpiresIn")
  private long expiresIn;
}
