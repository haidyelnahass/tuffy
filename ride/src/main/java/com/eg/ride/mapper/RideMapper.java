package com.eg.ride.mapper;

import com.eg.ride.client.model.response.ProfileDetailsResponse;
import com.eg.ride.model.entity.RideEntity;
import com.eg.ride.model.entity.RideTypeEntity;
import com.eg.common.model.LocationDetails;
import com.eg.ride.model.message.RideRequestedMessage;
import com.eg.ride.model.response.RideDetailsResponse;
import com.eg.ride.model.response.RideTypeDetails;
import com.eg.ride.model.response.UserDetails;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Objects;

@Mapper
public interface RideMapper {

  RideMapper INSTANCE = Mappers.getMapper(RideMapper.class);


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
  @Mapping(target = "latitude", expression = "java(RideMapper.splitLocation(location, \",\", 0))")
  @Mapping(target = "longitude", expression = "java(RideMapper.splitLocation(location, \",\", 1))")
  LocationDetails map(String location);


  List<RideTypeDetails> map(List<RideTypeEntity> source, @Context Double distance);


  @Mapping(target = "rideTypeName", source = "source.type")
  @Mapping(target = "rideTypeDescription", source = "source.description")
  @Mapping(target = "price", expression = "java(RideMapper.calculatePrice(source.getBasePrice(), "
    + "source.getPricePerKm(), distance))")
  RideTypeDetails map(RideTypeEntity source, @Context Double distance);

  @Mapping(target = "rideId", source = "id")
  @Mapping(target = "pickup", qualifiedByName = "MapLocation", source = "source.pickup")
  @Mapping(target = "dropOff", qualifiedByName = "MapLocation", source = "source.dropOff")
  @Mapping(target = "type", source = "source.type.type")
  RideRequestedMessage map(RideEntity source);


  static Double calculatePrice(Double basePrice, Double pricePerKm,
                               Double distance) {
    double scale = Math.pow(10,2);
    double price = basePrice + (distance * pricePerKm);
    return Math.ceil(price * scale) / scale;
  }

  static Double splitLocation(String input, String splitter, int index) {
    return Objects.nonNull(input)? Double.parseDouble(input.split(splitter)[index]) : null;
  }
}
