package com.eg.tracking.util.jobs;


import com.eg.tracking.util.RedisCacheManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Set;

import static com.eg.tracking.util.Constants.DRIVERS_LOCATION_KEY;
import static com.eg.tracking.util.Constants.DRIVER_META_KEY_PREFIX;
import static com.eg.tracking.util.Constants.LAST_UPDATE_KEY;
import static com.eg.tracking.util.Constants.STALE_THRESHOLD_MINUTES;

@Component
@Slf4j
@RequiredArgsConstructor
public class StaleDriverJob {

  private final RedisCacheManager redisCacheManager;

  @Scheduled(fixedDelay = 60000)
  private void runStaleDriverJob() {
    Set<String> metaKeys = redisCacheManager.getKeys(DRIVER_META_KEY_PREFIX + "*");

    for (String metaKey : metaKeys) {
      Map<String, String> metaData = redisCacheManager.getMetaDataFromCache(metaKey);
      String lastUpdateStr = metaData.get(LAST_UPDATE_KEY);

      if (lastUpdateStr == null) {
        log.warn("No lastUpdate found for key: {}", metaKey);
        continue;
      }

      Instant lastUpdate = Instant.parse(lastUpdateStr);
      long minutes = Duration.between(lastUpdate, Instant.now()).toMinutes();

      if (minutes > STALE_THRESHOLD_MINUTES) {
        String driverId = metaKey.replace(DRIVER_META_KEY_PREFIX, "");

        redisCacheManager.removeGeoLocation(DRIVERS_LOCATION_KEY, driverId);
        redisCacheManager.deleteFromCache(metaKey);

        log.info("Removed stale driver {} (last update {} minutes ago)", driverId, minutes);
      }
    }
  }

}
