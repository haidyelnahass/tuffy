package com.eg.ride.controller;

import com.eg.ride.model.response.DriverInquiryResponse;
import com.eg.ride.service.RideStatusSseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("/v1/rides")
@RequiredArgsConstructor
public class RideStatusSseController {

  private final RideStatusSseService rideStatusSseService;

  @GetMapping(value = "/{rideId}/status/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  @PreAuthorize("hasRole('RIDER')")
  public Flux<String> streamRideStatus(@RequestHeader Long userId, @PathVariable Long rideId) {
    return rideStatusSseService.inquireRideStatus(rideId);
  }

  @GetMapping(value = "/{rideId}/driver-location/stream")
  public Flux<DriverInquiryResponse> streamDriverLocation(
    @RequestHeader(AUTHORIZATION) String authorization,
    @PathVariable Long rideId) {
    return rideStatusSseService.inquireDriverLocation(authorization, rideId);
  }
}
