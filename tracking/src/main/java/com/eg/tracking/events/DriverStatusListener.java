package com.eg.tracking.events;

import com.eg.tracking.model.message.StatusUpdateMessage;
import com.eg.tracking.service.TrackingService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DriverStatusListener {

  private final TrackingService trackingService;

  @KafkaListener(topics = "${spring.kafka.driver-status-updated}", groupId = "${spring.kafka.group-id}")
  public void consumeStatusUpdate(StatusUpdateMessage message) {
    // TODO to be produced by ride service and tracking service
    trackingService.updateDriverStatus(message);
  }
}
