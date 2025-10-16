package com.eg.user.repository;

import com.eg.user.model.entity.TokenEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<TokenEntity, Long> {

  @EntityGraph(attributePaths = {"user"})
  Optional<TokenEntity> findByValue(String value);
}
