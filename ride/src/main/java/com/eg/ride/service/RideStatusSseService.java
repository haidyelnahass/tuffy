package com.eg.ride.service;

import com.eg.ride.client.TrackingClient;
import com.eg.ride.client.model.response.DriverLocationResponse;
import com.eg.ride.exception.BadRequestException;
import com.eg.ride.model.entity.RideEntity;
import com.eg.ride.model.enums.RideStatusEnum;
import com.eg.ride.model.response.DriverInquiryResponse;
import com.eg.ride.repository.RideRepository;
import com.eg.ride.repository.RideStatusRepository;
import com.eg.common.util.RedisCacheManager;
import com.eg.ride.util.Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Objects;

import static com.eg.ride.exception.model.ErrorCode.RIDE_NOT_FOUND;
import static com.eg.ride.model.enums.RideStatusEnum.REQUESTED;
import static com.eg.ride.util.Constants.AVERAGE_SPEED;
import static com.eg.ride.util.Constants.RIDE_NOT_FOUND_MESSAGE;
import static com.eg.ride.util.Util.calculateDistance;


@Service
@RequiredArgsConstructor
@Slf4j
public class RideStatusSseService {

  private final RedisCacheManager redisCacheManager;
  private final RideRepository rideRepository;
  private final RideStatusRepository rideStatusRepository;
  private final TrackingClient trackingClient;

  public Flux<String> inquireRideStatus(Long rideId) {
    validateRideEntity(rideId);
    return Flux.interval(Duration.ofSeconds(5))
      .onBackpressureDrop()
      .map(tick -> {
        Object status = redisCacheManager.getFromCache(rideId.toString());
        if (status == null) {
          throw new IllegalStateException("No ride status found!");
        }
        return status.toString();
      })
      .takeUntil(status -> !status.equals(REQUESTED.name()))
      .take(Duration.ofSeconds(120))
      .concatWith(Mono.defer(() -> {
        RideEntity rideEntity = rideRepository.findById(rideId)
          .orElse(null);
        if (Objects.nonNull(rideEntity) && rideEntity.getStatus().getValue()
          .equals(REQUESTED.name())) {
          rideEntity.setStatus(rideStatusRepository
            .findByValue(RideStatusEnum.EXPIRED.name()).orElse(null));
          rideRepository.save(rideEntity);
          return Mono.just(RideStatusEnum.EXPIRED.name());
        }
        return Mono.empty();
      }));
  }

  public Flux<DriverInquiryResponse> inquireDriverLocation(String authorization, Long rideId) {
    RideEntity rideEntity = validateRideEntity(rideId);
    return Flux.interval(Duration.ofSeconds(5))
      .onBackpressureDrop()
      .map(tick -> {
        DriverLocationResponse locationResponse
          = trackingClient.getDriverLocation(authorization, rideEntity.getRiderId(),
          rideEntity.getDriverId());
        Integer estimatedArrivalTime
          = Util.calculateEstimatedTime(calculateDistance(Util.splitLocation(rideEntity.getPickup(), ",",0),
            Util.splitLocation(rideEntity.getPickup(), ",",1),
            locationResponse.getLatitude(), locationResponse.getLongitude()), AVERAGE_SPEED);
        return DriverInquiryResponse.builder()
          .estimatedArrivalTime(estimatedArrivalTime)
          .latitude(locationResponse.getLatitude())
          .longitude(locationResponse.getLongitude())
          .lastUpdateDate(locationResponse.getLastUpdateDate())
          .build();
      })
      .takeUntil(response -> response.getEstimatedArrivalTime() < 1);
  }

  public void sendStatusUpdate(Long rideId, String status) {
    redisCacheManager.saveToCache(rideId.toString(), status);
  }

  private RideEntity validateRideEntity(Long rideId) {
    return rideRepository.findById(rideId)
      .orElseThrow(() -> new BadRequestException(RIDE_NOT_FOUND_MESSAGE, RIDE_NOT_FOUND));
  }
}
