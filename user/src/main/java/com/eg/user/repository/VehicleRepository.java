package com.eg.user.repository;

import com.eg.user.model.entity.UserEntity;
import com.eg.user.model.entity.VehicleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<VehicleEntity, Long> {

  Optional<VehicleEntity> findByUserEntity(UserEntity userEntity);
}
