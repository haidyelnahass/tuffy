package com.eg.user.mapper;

import com.eg.user.model.entity.VehicleEntity;
import com.eg.user.model.request.VehicleDetailsRequest;
import com.eg.user.model.response.VehicleDetailsResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface VehicleMapper {

  VehicleMapper INSTANCE = Mappers.getMapper(VehicleMapper.class);

  VehicleEntity map(VehicleDetailsRequest request);

  VehicleDetailsResponse map(VehicleEntity request);
}
