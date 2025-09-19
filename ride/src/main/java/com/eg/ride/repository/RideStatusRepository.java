package com.eg.ride.repository;


import com.eg.ride.entity.RideStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RideStatusRepository extends JpaRepository<RideStatusEntity, Long> {

  Optional<RideStatusEntity> findByValue(String value);
}
