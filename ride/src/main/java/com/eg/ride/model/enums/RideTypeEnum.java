package com.eg.ride.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum RideTypeEnum {
  MINI_TUFFY("Mini Tuffy"),
  TUFFY("Tuffy"),
  MEGA_TUFFY("Mega Tuffy");


  private final String name;

}
