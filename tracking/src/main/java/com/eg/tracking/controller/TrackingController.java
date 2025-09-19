package com.eg.tracking.controller;

import com.eg.tracking.service.TrackingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/tracking")
@RequiredArgsConstructor
public class TrackingController {

  private final TrackingService trackingService;

  @PutMapping("/drivers/location")
  public void updateDriverLocation() {

  }
}
