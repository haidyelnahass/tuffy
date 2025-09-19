package com.eg.ride.mapper;

import com.eg.ride.client.model.response.ProfileDetailsResponse;
import com.eg.ride.entity.RideEntity;
import com.eg.ride.model.request.LocationDetails;
import com.eg.ride.model.response.RideDetailsResponse;
import com.eg.ride.model.response.UserDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RideDetailsMapper {

  RideDetailsMapper INSTANCE = Mappers.getMapper(RideDetailsMapper.class);


  @Mapping(target = "riderDetails", expression = "java(map(riderDetails))")
  @Mapping(target = "driverDetails", expression = "java(map(driverDetails))")
  @Mapping(target = "price", source = "rideEntity.price")
  @Mapping(target = "pickup", qualifiedByName = "MapLocation", source = "rideEntity.pickup")
  @Mapping(target = "dropOff", qualifiedByName = "MapLocation", source = "rideEntity.dropOff")
  RideDetailsResponse map(RideEntity rideEntity,
                          ProfileDetailsResponse riderDetails,
                          ProfileDetailsResponse driverDetails);

  UserDetails map(ProfileDetailsResponse profileDetails);

  @Named("MapLocation")
  @Mapping(target = "latitude", expression = "java(RideDetailsMapper.splitLocation(location, \",\", 0))")
  @Mapping(target = "longitude", expression = "java(RideDetailsMapper.splitLocation(location, \",\", 1))")
  LocationDetails map(String location);


  static String splitLocation(String input, String splitter, int index) {
    return input.split(splitter)[index];
  }
}
