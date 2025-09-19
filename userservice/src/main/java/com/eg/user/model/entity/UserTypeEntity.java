package com.eg.user.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Entity
@Table(name = "USER_TYPE")
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class UserTypeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  private String value;

  @Column
  private String description;

  @Column
  private LocalDateTime createDate;

  @Column
  private LocalDateTime updateDate;

  public UserTypeEntity(String value) {
    this.value = value;
  }
}
