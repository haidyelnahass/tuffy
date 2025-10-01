package com.eg.ride.event;

import com.eg.ride.model.message.RideMatchFailedMessage;
import com.eg.ride.service.RideService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RideMatchFailedListener {

  private final RideService rideService;

  @KafkaListener(topics = "${spring.kafka.ride-match-failed}", groupId = "${spring.kafka.group-id}",
    containerFactory = "rideMatchFailedConsumerContainerFactory")
  public void handleRideMatchFailed(@Payload RideMatchFailedMessage message) {
    rideService.expireRide(message);
  }

}
