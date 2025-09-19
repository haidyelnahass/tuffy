package com.eg.ride.repository;


import com.eg.ride.entity.RideEntity;
import com.eg.ride.entity.RideStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RideRepository extends JpaRepository<RideEntity, Long> {

  Optional<RideEntity> findByRiderIdAndStatus(Long riderId, RideStatusEntity status);
}
