package com.eg.ride.controller;

import com.eg.ride.service.RideStatusSseService;
import com.eg.common.util.RedisCacheManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/v1/rides")
@RequiredArgsConstructor
public class RideStatusSseController {

  private final RideStatusSseService rideStatusSseService;
  private final RedisCacheManager redisCacheManager;

  @GetMapping(value = "/{rideId}/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Flux<String> streamRideStatus(@RequestHeader Long userId, @PathVariable Long rideId) {
    return rideStatusSseService.inquireRideStatus(rideId);
  }
}
