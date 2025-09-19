package com.eg.user.mapper;

import com.eg.user.model.entity.UserEntity;
import com.eg.user.model.entity.VehicleEntity;
import com.eg.user.model.request.UserRegistrationRequest;
import com.eg.user.model.request.VehicleDetailsRequest;
import com.eg.user.model.response.ProfileDetailsResponse;
import com.eg.user.model.response.VehicleDetailsResponse;
import com.eg.user.util.Util;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface VehicleMapper {

  VehicleMapper INSTANCE = Mappers.getMapper(VehicleMapper.class);

  VehicleEntity map(VehicleDetailsRequest request);

  VehicleDetailsResponse map(VehicleEntity request);
}
