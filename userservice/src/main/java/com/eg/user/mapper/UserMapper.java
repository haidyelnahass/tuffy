package com.eg.user.mapper;

import com.eg.user.model.entity.UserEntity;
import com.eg.user.model.request.UserRegistrationRequest;
import com.eg.user.model.response.ProfileDetailsResponse;
import com.eg.user.util.Util;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

  UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

  @Mapping(target = "password", qualifiedByName = "encodePassword")
  @Mapping(target = "loginAttempts", constant = "0")
  UserEntity map(UserRegistrationRequest request);

  @Mapping(target = "customerStatus", source = "user.customerStatus.value")
  @Mapping(target = "registrationDate", qualifiedByName = "mapDate", source = "createDate")
  ProfileDetailsResponse map(UserEntity user);

  @Named("encodePassword")
  static String encodePassword(String password) {
    return Util.encodePassword(password);
  }

  @Named("mapDate")
  static String mapDate(LocalDateTime date) {
    return date.toLocalDate().toString();
  }
}
