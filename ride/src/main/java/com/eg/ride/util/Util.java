package com.eg.ride.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Util {

  private static final int EARTH_RADIUS_KM = 6371;

  public static Double calculateDistance(Double lat1, Double lon1, Double lat2, Double lon2) {
    double dLat = Math.toRadians(lat2 - lat1);
    double dLon = Math.toRadians(lon2 - lon1);

    double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
      + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
      * Math.sin(dLon / 2) * Math.sin(dLon / 2);

    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

    return EARTH_RADIUS_KM * c;
  }

  public static int calculateEstimatedTime(Double distance, Double avgSpeedPerKm) {
    if (avgSpeedPerKm <= 0) throw new IllegalArgumentException("Speed must be > 0");
    return (int) Math.round((distance / avgSpeedPerKm) * 60);
  }

  public static Double splitLocation(String input, String splitter, int index) {
    return Objects.nonNull(input)? Double.parseDouble(input.split(splitter)[index]) : null;
  }
}
