package com.eg.ride.service;

import com.eg.ride.entity.RideEntity;
import com.eg.ride.model.enums.RideStatusEnum;
import com.eg.ride.repository.RideRepository;
import com.eg.ride.repository.RideStatusRepository;
import com.eg.ride.util.RedisCacheManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.misc.Interval;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Objects;


@Service
@RequiredArgsConstructor
@Slf4j
public class RideStatusSseService {

  private final RedisCacheManager redisCacheManager;
  private final RideRepository rideRepository;
  private final RideStatusRepository rideStatusRepository;

  public Flux<String> inquireRideStatus(Long rideId) {
    return Flux.interval(Duration.ofSeconds(5))
      .onBackpressureDrop()
      .map(tick -> {
        String status = redisCacheManager.getFromCache(rideId.toString()).toString();
        if (status == null) {
          throw new IllegalStateException("No ride status found!");
        }
        return status;
      })
      .takeUntil(status -> !status.equals(RideStatusEnum.REQUESTED.name()))
      .take(Duration.ofSeconds(30))
      .concatWith(Mono.fromRunnable(() -> {
        RideEntity rideEntity = rideRepository.findById(rideId)
          .orElse(null);
        if (Objects.nonNull(rideEntity)) {
          rideEntity.setStatus(rideStatusRepository
            .findByValue(RideStatusEnum.EXPIRED.name()).orElse(null));
          rideRepository.save(rideEntity);
        }

      }).then(Mono.just(RideStatusEnum.EXPIRED.name())));
  }

  public void sendStatusUpdate(Long rideId, String status) {
    redisCacheManager.saveToCache(rideId.toString(), status);
  }
}
