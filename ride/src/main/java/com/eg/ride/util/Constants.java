package com.eg.ride.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {
  public static final String USER_ID_HEADER = "UserId";
  public static final String STATUS_NOT_FOUND = "Status entity not found";
  public static final String RIDE_ALREADY_IN_PROGRESS = "A ride request is already in progress";
  public static final String RIDE_NOT_FOUND_MESSAGE = "Ride not found";
  public static final String INCORRECT_RIDE_STATUS_MESSAGE = "Ride has wrong status";
  public static final String RIDE_STATUS_CANNOT_BE_UPDATED = "This ride status cannot be updated as status is ";
  public static final String WRONG_RIDE_STATUS_TO_UPDATE = "Cannot update ride status to requested";

}
