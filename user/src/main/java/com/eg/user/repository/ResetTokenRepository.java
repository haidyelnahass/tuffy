package com.eg.user.repository;

import com.eg.user.model.entity.ResetTokenEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResetTokenRepository extends JpaRepository<ResetTokenEntity, Long> {

  @EntityGraph(attributePaths = {"user"})
  Optional<ResetTokenEntity> findByValue(String value);
}
