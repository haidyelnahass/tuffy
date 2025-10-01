package com.eg.ride.controller;

import com.eg.ride.model.request.RidePriceRequest;
import com.eg.ride.model.request.RideRequest;
import com.eg.ride.model.request.UpdateRideStatusRequest;
import com.eg.ride.model.response.RideDetailsResponse;
import com.eg.ride.model.response.RidePriceResponse;
import com.eg.ride.model.response.RideResponse;
import com.eg.ride.service.RideService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
  @PreAuthorize("hasRole('RIDER')")
  public ResponseEntity<RideResponse> requestRide(@RequestHeader("UserId") Long userId,
                                                  @RequestHeader("Authorization") String authorization,
                                                  @Valid @RequestBody RideRequest rideRequest) {
    return ResponseEntity.ok(rideService.requestRide(userId, rideRequest));
  }

  @GetMapping("/{rideId}")
  public ResponseEntity<RideDetailsResponse> getRideDetails(@PathVariable Long rideId) {
    // TODO how to track driver after he has been assigned?? (SSE for driver's time??)
    return ResponseEntity.ok(rideService.getRideDetails(rideId));
  }

  @PutMapping("/{rideId}")
  public void updateRideStatus(@PathVariable Long rideId,
                               @Valid @RequestBody UpdateRideStatusRequest request) {
    // TODO check this api maybe we don't need it anymore.
    rideService.updateRideStatus(rideId, request);
  }

  @PutMapping("/{rideId}/start")
  @PreAuthorize("hasRole('DRIVER')")
  public void startRide(@PathVariable Long rideId) {
    rideService.startRide(rideId);
  }

  @PutMapping("/{rideId}/complete")
  @PreAuthorize("hasRole('DRIVER')")
  public void completeRide(@PathVariable Long rideId) {
    rideService.completeRide(rideId);
  }

  @PutMapping("/{rideId}/cancel")
  public void cancelRide(@PathVariable Long rideId) {
    rideService.cancelRide(rideId);
  }

  @PostMapping("/pricing")
  @PreAuthorize("hasRole('RIDER')")
  public RidePriceResponse getRidePricingOptions(@Valid @RequestBody RidePriceRequest request) {
    return rideService.getRidePricingOptions(request);
  }

  // TODO Ride Payment API
  // TODO add logic for live fare computation
  // TODO job to check rides expired as expected

}
