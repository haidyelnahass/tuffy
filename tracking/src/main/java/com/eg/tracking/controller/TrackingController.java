package com.eg.tracking.controller;

import com.eg.tracking.model.request.UpdateLocationRequest;
import com.eg.tracking.model.response.DriverLocationResponse;
import com.eg.tracking.model.response.NearbyLocationsResponse;
import com.eg.tracking.service.TrackingService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/tracking")
@RequiredArgsConstructor
public class TrackingController {

  private final TrackingService trackingService;

  @PutMapping("/drivers/{driverId}/location")
  @PreAuthorize("hasRole('DRIVER')")
  public void updateDriverLocation(
    @PathVariable Long driverId,
    @RequestBody UpdateLocationRequest request) {
    trackingService.updateDriverLocation(driverId, request);
  }

  @GetMapping("/drivers/{driverId}/location")
  @PreAuthorize("hasRole('RIDER')")
  public ResponseEntity<DriverLocationResponse> getDriverLocation(@PathVariable Long driverId) {
    return ResponseEntity.ok(trackingService.getCurrentDriverLocation(driverId));
  }

  @GetMapping("/drivers/location")
  public ResponseEntity<NearbyLocationsResponse> getNearbyDriverLocations(@NotNull @RequestParam double lat,
                                                                          @NotNull @RequestParam double lon,
                                                                          @NotNull @RequestParam int radius,
                                                                          @NotNull @RequestParam int limit) {
    return ResponseEntity.ok(trackingService.getNearbyDriverLocations(lat, lon, radius, limit));
  }
}
