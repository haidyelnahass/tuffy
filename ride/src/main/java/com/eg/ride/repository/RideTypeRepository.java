package com.eg.ride.repository;

import com.eg.ride.model.entity.RideTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RideTypeRepository extends JpaRepository<RideTypeEntity, Long> {

  Optional<RideTypeEntity> findByType(String type);
}
