package com.eg.tracking.mapper;

import com.eg.tracking.model.response.DriverLocationResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.data.geo.Point;

@Mapper
public interface TrackingMapper {

  TrackingMapper INSTANCE = Mappers.getMapper(TrackingMapper.class);

  @Mapping(target = "latitude", source = "y")
  @Mapping(target = "longitude", source = "x")
  DriverLocationResponse map(Point point);
}
