package com.eg.matching.events;

import com.eg.matching.model.message.RideRequestedMessage;
import com.eg.matching.service.MatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RideRequestedListener {

  private final MatchingService matchingService;

  @KafkaListener(topics = "${spring.kafka.ride-requested}", groupId = "${spring.kafka.group-id}")
  public void consumeRideRequested(RideRequestedMessage message) {
    matchingService.matchRide(message);
  }
}
