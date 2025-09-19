package com.eg.user.repository;

import com.eg.user.model.entity.CustomerStatusEntity;
import com.eg.user.model.entity.UserTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserTypeRepository extends JpaRepository<UserTypeEntity, Long> {

  Optional<UserTypeEntity> findByValue(String value);
}
