package com.eg.ride.service;

import com.eg.ride.client.UserClient;
import com.eg.ride.client.model.response.ProfileDetailsResponse;
import com.eg.ride.model.entity.RideEntity;
import com.eg.ride.model.entity.RideStatusEntity;
import com.eg.ride.exception.BadRequestException;
import com.eg.ride.exception.model.ErrorCode;
import com.eg.ride.mapper.RideMapper;
import com.eg.ride.model.entity.RideTypeEntity;
import com.eg.ride.model.enums.RideStatusEnum;
import com.eg.ride.model.enums.RideTypeEnum;
import com.eg.ride.model.enums.UserTypeEnum;
import com.eg.ride.model.request.DriverAssignmentRequest;
import com.eg.ride.model.request.RidePriceRequest;
import com.eg.ride.model.request.RideRequest;
import com.eg.ride.model.request.UpdateRideStatusRequest;
import com.eg.ride.model.response.RideDetailsResponse;
import com.eg.ride.model.response.RidePriceResponse;
import com.eg.ride.model.response.RideResponse;
import com.eg.ride.model.response.RideTypeDetails;
import com.eg.ride.repository.RideRepository;
import com.eg.ride.repository.RideStatusRepository;
import com.eg.ride.repository.RideTypeRepository;
import com.eg.ride.util.KafkaProducerUtil;
import com.eg.ride.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.eg.ride.exception.model.ErrorCode.DATA_NOT_FOUND;
import static com.eg.ride.exception.model.ErrorCode.INCORRECT_RIDE_STATUS;
import static com.eg.ride.exception.model.ErrorCode.RIDE_IN_PROGRESS;
import static com.eg.ride.exception.model.ErrorCode.RIDE_NOT_FOUND;
import static com.eg.ride.exception.model.ErrorCode.STATUS_UPDATE_FAILED;
import static com.eg.ride.model.enums.RideStatusEnum.CANCELLED;
import static com.eg.ride.model.enums.RideStatusEnum.COMPLETED;
import static com.eg.ride.model.enums.RideStatusEnum.EXPIRED;
import static com.eg.ride.util.Constants.AVERAGE_SPEED;
import static com.eg.ride.util.Constants.DRIVER_DETAILS_NOT_FOUND_MESSAGE;
import static com.eg.ride.util.Constants.INCORRECT_RIDE_STATUS_MESSAGE;
import static com.eg.ride.util.Constants.RIDE_ALREADY_IN_PROGRESS;
import static com.eg.ride.util.Constants.RIDE_NOT_FOUND_MESSAGE;
import static com.eg.ride.util.Constants.RIDE_STATUS_CANNOT_BE_UPDATED;
import static com.eg.ride.util.Constants.RIDE_TYPE_NOT_FOUND;
import static com.eg.ride.util.Constants.STATUS_NOT_FOUND;
import static com.eg.ride.util.Constants.WRONG_RIDE_STATUS_TO_UPDATE;
import static com.eg.ride.util.Constants.WRONG_USER_TYPE_MESSAGE;

@Service
@RequiredArgsConstructor
public class RideService {

  private final RideRepository rideRepository;
  private final RideStatusRepository rideStatusRepository;
  private final KafkaProducerUtil kafkaProducerUtil;
  private final RideStatusSseService rideStatusSseService;
  private final UserClient userClient;
  private final RideTypeRepository rideTypeRepository;

  @Value("${spring.kafka.ride-requested}")
  private String rideRequestedTopic;

  public RideResponse requestRide(Long userId, RideRequest rideRequest) {
    RideStatusEntity requestedEntity =
      rideStatusRepository.findByValue(RideStatusEnum.REQUESTED.name())
        .orElseThrow(() -> new BadRequestException(STATUS_NOT_FOUND,
          DATA_NOT_FOUND));
    Optional<RideEntity> optionalRideEntity = rideRepository.findByRiderIdAndStatus(userId,
      requestedEntity);
    if (optionalRideEntity.isPresent()) {
      throw new BadRequestException(RIDE_ALREADY_IN_PROGRESS, RIDE_IN_PROGRESS);
    }
    RideEntity rideEntity = RideEntity.builder()
      .riderId(userId)
      .pickup(rideRequest.getPickup().getLatitude() + "," +
        rideRequest.getPickup().getLongitude())
      .dropOff(rideRequest.getDropoff().getLatitude() + ","
        + rideRequest.getDropoff().getLongitude())
      .price(rideRequest.getPrice())
      .status(requestedEntity)
      .type(rideTypeRepository.findByType(rideRequest.getRideType().getName())
        .orElseThrow(() -> new BadRequestException(RIDE_TYPE_NOT_FOUND, DATA_NOT_FOUND)))
      .build();
    rideEntity = rideRepository.save(rideEntity);
    kafkaProducerUtil.sendMessage(rideRequestedTopic, rideEntity);
    rideStatusSseService.sendStatusUpdate(rideEntity.getId(), RideStatusEnum.REQUESTED.name());
    return RideResponse.builder().rideId(rideEntity.getId()).build();
  }

  public void assignDriver(String authorization, Long rideId, DriverAssignmentRequest request) {
    RideEntity rideEntity = rideRepository.findById(rideId)
      .orElseThrow(() -> new BadRequestException(RIDE_NOT_FOUND_MESSAGE, RIDE_NOT_FOUND));
    if (!rideEntity.getStatus().getValue().equals(RideStatusEnum.REQUESTED.name())) {
      throw new BadRequestException(INCORRECT_RIDE_STATUS_MESSAGE, INCORRECT_RIDE_STATUS);
    }
    ProfileDetailsResponse driverDetails = userClient.getUserDetails(authorization, request.getDriverId());
    if (Objects.nonNull(driverDetails)) {
      if (!driverDetails.getUserType().equals(UserTypeEnum.DRIVER.name())) {
        throw new BadRequestException(WRONG_USER_TYPE_MESSAGE, ErrorCode.WRONG_USER_TYPE);
      }
      rideEntity.setDriverId(request.getDriverId());
      rideEntity.setStatus(rideStatusRepository.findByValue(RideStatusEnum.ASSIGNED.name())
        .orElseThrow(() -> new BadRequestException(STATUS_NOT_FOUND, DATA_NOT_FOUND)));
      rideRepository.save(rideEntity);
      rideStatusSseService.sendStatusUpdate(rideId, RideStatusEnum.ASSIGNED.name());
    } else {
      throw new BadRequestException(DRIVER_DETAILS_NOT_FOUND_MESSAGE, DATA_NOT_FOUND);
    }

  }

  public RideDetailsResponse getRideDetails(String authorization, Long rideId) {
    RideEntity rideEntity = rideRepository.findById(rideId)
      .orElseThrow(() -> new BadRequestException(RIDE_NOT_FOUND_MESSAGE, RIDE_NOT_FOUND));
    ProfileDetailsResponse riderDetails = userClient.getUserDetails(authorization,
      rideEntity.getRiderId());
    ProfileDetailsResponse driverDetails = userClient.getUserDetails(authorization,
      rideEntity.getDriverId());

    return RideMapper.INSTANCE.map(rideEntity,
      riderDetails, driverDetails);
  }

  public void updateRideStatus(Long rideId, UpdateRideStatusRequest request) {
    RideEntity rideEntity = rideRepository.findById(rideId)
      .orElseThrow(() -> new BadRequestException(RIDE_NOT_FOUND_MESSAGE, RIDE_NOT_FOUND));
    validateUpdateRideRequest(rideEntity, request);
    rideEntity.setStatus(rideStatusRepository.findByValue(request.getRideStatus().name())
      .orElseThrow(() -> new BadRequestException(STATUS_NOT_FOUND, DATA_NOT_FOUND)));
    rideRepository.save(rideEntity);
    rideStatusSseService.sendStatusUpdate(rideId, request.getRideStatus().name());
  }

  public void validateUpdateRideRequest(RideEntity rideEntity,
                                        UpdateRideStatusRequest request) {
    if (List.of(CANCELLED.name(), EXPIRED.name(), COMPLETED.name())
      .contains(rideEntity.getStatus().getValue())) {
      throw new BadRequestException(RIDE_STATUS_CANNOT_BE_UPDATED
        + rideEntity.getStatus().getValue(), STATUS_UPDATE_FAILED);
    }
    if (request.getRideStatus().equals(RideStatusEnum.REQUESTED)) {
      throw new BadRequestException(WRONG_RIDE_STATUS_TO_UPDATE,
        STATUS_UPDATE_FAILED);
    }
  }

  public RidePriceResponse getRidePricingOptions(RidePriceRequest request) {
    List<RideTypeEntity> rideTypes = rideTypeRepository.findAll();
    Double distance = Util.calculateDistance(request.getPickup().getLatitude(),
      request.getPickup().getLongitude(), request.getDropoff().getLatitude(),
      request.getDropoff().getLongitude());
    return RidePriceResponse.builder()
      .rideTypesDetailsList(RideMapper.INSTANCE.map(rideTypes, distance))
      .expectedArrivalTimeInMinutes(Util.calculateEstimatedTime(distance, AVERAGE_SPEED))
      .build();

  }
}
