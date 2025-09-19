package com.eg.ride.controller;

import com.eg.ride.model.request.DriverAssignmentRequest;
import com.eg.ride.model.request.RideRequest;
import com.eg.ride.model.request.UpdateRideStatusRequest;
import com.eg.ride.model.response.RideDetailsResponse;
import com.eg.ride.model.response.RideResponse;
import com.eg.ride.service.RideService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/rides")
@RequiredArgsConstructor
public class RideController {

  private final RideService rideService;

  @PostMapping
  public ResponseEntity<RideResponse> requestRide(@RequestHeader("UserId") Long userId,
                                                  @RequestHeader("Authorization") String authorization,
                                                  @RequestBody RideRequest rideRequest) {
    return ResponseEntity.ok(rideService.requestRide(authorization, userId, rideRequest));
  }

  @PostMapping("/{rideId}/assign-driver")
  public ResponseEntity<Void> assignDriver(@PathVariable Long rideId,
                                           @RequestHeader("Authorization") String authorization,
                                           @RequestBody DriverAssignmentRequest request) {
    rideService.assignDriver(authorization, rideId, request);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/{rideId}")
  public ResponseEntity<RideDetailsResponse> getRideDetails(@PathVariable Long rideId,
                                                            @RequestHeader("Authorization") String authorization) {
    return ResponseEntity.ok(rideService.getRideDetails(authorization, rideId));
  }

  @PutMapping("/{rideId}")
  public void updateRideStatus(@PathVariable Long rideId,
                               @RequestBody UpdateRideStatusRequest request) {
    rideService.updateRideStatus(rideId, request);
  }

  // TODO Ride Payment APIs

}
