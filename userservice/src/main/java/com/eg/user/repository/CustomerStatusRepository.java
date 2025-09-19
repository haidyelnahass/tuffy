package com.eg.user.repository;

import com.eg.user.model.entity.CustomerStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerStatusRepository extends JpaRepository<CustomerStatusEntity, Long> {

  Optional<CustomerStatusEntity> findByValue(String value);
}
