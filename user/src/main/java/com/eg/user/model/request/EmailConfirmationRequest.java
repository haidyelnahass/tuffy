package com.eg.user.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmailConfirmationRequest {

  @JsonProperty("ProfileId")
  private Long profileId;
}
