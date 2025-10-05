package com.eg.matching.service;

import com.eg.common.model.LocationDetails;
import com.eg.common.util.KafkaProducerUtil;
import com.eg.matching.client.TrackingClient;
import com.eg.matching.client.model.response.NearbyDriverLocation;
import com.eg.matching.model.message.DriverAssignmentMessage;
import com.eg.matching.model.message.RideMatchFailedMessage;
import com.eg.matching.model.message.RideRequestedMessage;
import com.eg.matching.util.Constants;
import com.eg.matching.client.model.response.NearbyLocationsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MatchingService {

  private final TrackingClient trackingClient;
  private final KafkaProducerUtil kafkaProducerUtil;

  @Value("${spring.kafka.ride-assigned}")
  private String rideAssignedTopic;

  @Value("${spring.kafka.ride-match-failed}")
  private String rideMatchFailedTopic;

  public void matchRide(RideRequestedMessage message) {
    log.info("matchRide:: finding best driver match");
    Optional<NearbyDriverLocation> bestMatch = findBestDriverLocation(
      message.getPickup().getLatitude(),
      message.getPickup().getLongitude()
    );
    log.info("matchRide:: check if best match found");
    if (bestMatch.isEmpty()) {
      log.error("matchRide:: could not find nearby drivers");
      kafkaProducerUtil.sendMessage(rideMatchFailedTopic,
        RideMatchFailedMessage.builder()
          .rideId(message.getRideId())
          .build());
      return;
    }

    NearbyDriverLocation driver = bestMatch.get();
    log.info("matchRide:: send assigned event");
    kafkaProducerUtil.sendMessage(rideAssignedTopic,
      DriverAssignmentMessage.builder()
        .rideId(message.getRideId())
        .driverLocation(LocationDetails.builder()
          .latitude(driver.getLatitude())
          .longitude(driver.getLongitude())
          .build())
        .driverId(driver.getDriverId())
        .build());

  }

  public Optional<NearbyDriverLocation> findBestDriverLocation(double lat, double lon) {
    for (int attempt = 1; attempt <= 10; attempt++) {
      for (int radius = 5; radius <= 40; radius += 5) {
      log.info("findBestDriverLocation:: attempt {}, radius {}", attempt, radius);
        NearbyLocationsResponse response =
          trackingClient.getNearbyDriverLocations(lat, lon, radius, Constants.LIMIT);
        log.info("findBestDriverLocation:: response {}", response);
        if (response != null && !response.getNearbyLocations().isEmpty()) {
          log.info("findBestDriverLocation:: return nearby locations");
          return response.getNearbyLocations().stream()
            .filter(loc -> loc.getDistance() > 0)
            .min(Comparator.comparingDouble(NearbyDriverLocation::getDistance));
        }
      }

      try {
        log.info("findBestDriverLocation:: wait 3 sec");
        Thread.sleep(3000); // 3 sec cool-off
      } catch (InterruptedException e) {
        log.error("findBestDriverLocation:: current thread interrupted");
        Thread.currentThread().interrupt();
      }
    }
    return Optional.empty();
  }
}
