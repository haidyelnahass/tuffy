package com.eg.ride.repository;


import com.eg.ride.model.entity.RideEntity;
import com.eg.ride.model.entity.RideStatusEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RideRepository extends JpaRepository<RideEntity, Long> {

  @EntityGraph(attributePaths = {"type", "status"})
  Optional<RideEntity> findByRiderIdAndStatus(Long riderId, RideStatusEntity status);

  @EntityGraph(attributePaths = {"type", "status"})
  Optional<RideEntity> findById(Long id);
}
