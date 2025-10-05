package com.eg.tracking.service;

import com.eg.tracking.exception.BadRequestException;
import com.eg.tracking.mapper.TrackingMapper;
import com.eg.common.model.enums.DriverStatusEnum;
import com.eg.tracking.model.message.DriverLocationMessage;
import com.eg.common.model.message.StatusUpdateMessage;
import com.eg.tracking.model.request.UpdateLocationRequest;
import com.eg.tracking.model.response.DriverLocationResponse;
import com.eg.tracking.model.response.NearbyDriverLocation;
import com.eg.tracking.model.response.NearbyLocationsResponse;
import com.eg.common.util.KafkaProducerUtil;
import com.eg.common.util.RedisCacheManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.eg.tracking.exception.model.ErrorCode.NO_LOCATION_FOUND;
import static com.eg.tracking.util.Constants.DRIVERS_LOCATION_KEY;
import static com.eg.tracking.util.Constants.DRIVER_META_KEY_PREFIX;
import static com.eg.tracking.util.Constants.LAST_UPDATE_KEY;
import static com.eg.tracking.util.Constants.NO_RECENT_LOCATION_MESSAGE;
import static com.eg.tracking.util.Constants.STATUS_KEY;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrackingService {

  private final RedisCacheManager redisCacheManager;
  private final KafkaProducerUtil kafkaProducerUtil;

  @Value("${spring.kafka.location-updated}")
  private String locationUpdatedTopic;

  public void updateDriverLocation(Long driverId,
                                   UpdateLocationRequest request) {
    String driverKey = DRIVER_META_KEY_PREFIX + driverId;
    log.info("updateDriverLocation:: saving geo location in cache");

    redisCacheManager.saveGeoLocationToCache(DRIVERS_LOCATION_KEY,
      new Point(request.getLongitude(), request.getLatitude()), driverId.toString());
    Map<String, String> locationMetaData = new HashMap<>();
    locationMetaData.put(STATUS_KEY, request.getDriverStatus().toString());
    locationMetaData.put(LAST_UPDATE_KEY, request.getTimestamp());
    log.info("updateDriverLocation:: saving meta data in cache");
    redisCacheManager.saveMetaDataToCache(driverKey, locationMetaData);
    DriverLocationMessage driverLocationMessage = DriverLocationMessage.builder()
      .latitude(request.getLatitude())
      .longitude(request.getLongitude())
      .lastUpdateDate(request.getTimestamp())
      .driverId(driverId)
      .build();

    log.info("updateDriverLocation:: publish location message");
    kafkaProducerUtil.sendMessage(locationUpdatedTopic, driverLocationMessage);
  }

  public DriverLocationResponse getCurrentDriverLocation(Long driverId) {
    Point currentLocation = redisCacheManager.getGeoLocationFromCache(DRIVERS_LOCATION_KEY,
      driverId.toString());
    if (currentLocation == null) {
      throw new BadRequestException(NO_RECENT_LOCATION_MESSAGE, NO_LOCATION_FOUND);
    } else {
      DriverLocationResponse locationResponse = TrackingMapper.INSTANCE.map(currentLocation);
      Map<String, String> metaData = redisCacheManager.getMetaDataFromCache
          (DRIVER_META_KEY_PREFIX + driverId);
      locationResponse.setDriverStatus(metaData.getOrDefault(STATUS_KEY,
        DriverStatusEnum.INACTIVE.name()));
      locationResponse.setLastUpdateDate(metaData.getOrDefault(LAST_UPDATE_KEY,
        LocalDateTime.now().toString()));
      return locationResponse;
    }
  }

  public NearbyLocationsResponse getNearbyDriverLocations(double lat, double lon,
                                                          int radius, int limit) {
    log.info("getNearbyDriverLocations:: check cache");
    GeoResults<RedisGeoCommands.GeoLocation<Object>> results
    = redisCacheManager.getNearbyLocationsFromCache(DRIVERS_LOCATION_KEY, lon, lat, radius, limit);
    log.info("findBestDriverLocation:: check results");
    if (results == null) {
      return NearbyLocationsResponse.builder().nearbyLocations(List.of()).build();
    }
    log.info("findBestDriverLocation:: map results");
    List<NearbyDriverLocation> locations =  results.getContent().stream()
      .filter(result -> {
        String driverIdKey = result.getContent().getName().toString();
        Map<String, String> metaData =
          redisCacheManager.getMetaDataFromCache(DRIVER_META_KEY_PREFIX + driverIdKey);

        return DriverStatusEnum.ACTIVE.name().equals(metaData.get(STATUS_KEY));
      })
      .map(result -> {
        String driverIdKey = result.getContent().getName().toString();
        Long driverId = Objects.nonNull(driverIdKey) ?
          Long.parseLong(driverIdKey): 0L;
        Point coordinate = result.getContent().getPoint();
        double distance = result.getDistance().getValue();
        return NearbyDriverLocation.builder()
          .driverId(driverId)
          .latitude(coordinate.getY())
          .longitude(coordinate.getX())
          .distance(distance).build();
      })
      .toList();
    log.info("findBestDriverLocation:: locations {}", locations);
    return NearbyLocationsResponse.builder()
      .nearbyLocations(locations)
      .build();
  }

  public void updateDriverStatus(StatusUpdateMessage message) {
    String driverKey = DRIVER_META_KEY_PREFIX + message.getDriverId();
    Map<String, String> locationMetaData = new HashMap<>();
    locationMetaData.put(STATUS_KEY, message.getDriverStatus().toString());
    locationMetaData.put(LAST_UPDATE_KEY, message.getLastUpdateDate());
    log.info("updateDriverStatus:: saving meta data in cache");
    redisCacheManager.saveMetaDataToCache(driverKey, locationMetaData);
  }
}
