package com.eg.ride.event;

import com.eg.ride.model.message.DriverAssignmentMessage;
import com.eg.ride.service.RideService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RideAssignedListener {

  private final RideService rideService;

  @KafkaListener(topics = "${spring.kafka.ride-assigned}", groupId = "${spring.kafka.group-id}",
    containerFactory = "driverAssignmentKafkaListenerContainerFactory")
  public void assignRide(@Payload DriverAssignmentMessage message) {
    rideService.assignDriver(message);
  }

}
