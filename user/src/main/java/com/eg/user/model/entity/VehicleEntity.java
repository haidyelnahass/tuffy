package com.eg.user.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Entity
@Table(name = "VEHICLE")
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class VehicleEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  private String model;

  @Column
  private String capacity;

  @Column
  private String plateNumber;

  @Column
  private LocalDateTime createDate;

  @Column
  private LocalDateTime updateDate;

  @JoinColumn(name = "DRIVER_ID", referencedColumnName = "id")
  @ManyToOne
  private UserEntity userEntity;

  @PrePersist
  protected void onCreate() {
    this.createDate = LocalDateTime.now();
  }

  @PreUpdate
  protected void onUpdate() {
    this.updateDate = LocalDateTime.now();
  }
}
