package com.eg.common.model.message;

import com.eg.common.model.enums.DriverStatusEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatusUpdateMessage {
  @JsonProperty("DriverId")
  private Long driverId;

  @JsonProperty("Status")
  private DriverStatusEnum driverStatus;

  @JsonProperty("LastUpdateDate")
  private String lastUpdateDate;
}
