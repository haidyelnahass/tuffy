package com.eg.tracking.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {

  public static final String DRIVERS_LOCATION_KEY = "drivers:locations";

  public static final String DRIVERS_LAST_UPDATE_KEY = "drivers:lastUpdate";
  public static final String DRIVER_ACTIVE_KEY = "drivers:active";
  public static final String DRIVER_META_KEY_PREFIX = "driver:";

  public static final String STATUS_KEY = "status";
  public static final String LAST_UPDATE_KEY = "lastUpdate";

  public static final String NO_RECENT_LOCATION_MESSAGE = "No recent location found for driver";


  // Config
  public static final Integer STALE_THRESHOLD_MINUTES = 5;
}
