package com.eg.user.repository;

import com.eg.user.model.entity.UserEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

  Optional<UserEntity> findByEmailOrPhone(String email, String phone);

  Optional<UserEntity> findByPhone(String phone);


  @EntityGraph(attributePaths = {"customerStatus", "previousCustomerStatus"})
  Optional<UserEntity> findById(@Nullable Long id);
}
