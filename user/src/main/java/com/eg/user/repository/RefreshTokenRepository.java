package com.eg.user.repository;

import com.eg.user.model.entity.RefreshTokenEntity;
import com.eg.user.model.entity.UserEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {

  Optional<RefreshTokenEntity> findByUserAndExpiryDateAfter(UserEntity user, LocalDateTime date);

  @EntityGraph(attributePaths = {"user"})
  Optional<RefreshTokenEntity> findByValueAndExpiryDateAfter(String value, LocalDateTime date);

  void deleteByExpiryDateBefore(LocalDateTime dateTime);
}
