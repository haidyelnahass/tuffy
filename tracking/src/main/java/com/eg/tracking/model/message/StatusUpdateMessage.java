package com.eg.tracking.model.message;

import com.eg.tracking.model.enums.DriverStatusEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class StatusUpdateMessage {
  @JsonProperty("DriverId")
  private Long driverId;

  @JsonProperty("Status")
  private DriverStatusEnum driverStatus;

  @JsonProperty("LastUpdateDate")
  private String lastUpdateDate;
}
