package com.eg.ride.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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

@Entity
@Table(name = "RIDES")
@Builder
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class RideEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "RIDER_ID")
  private Long riderId;

  @Column(name = "DRIVER_ID")
  private Long driverId;

  @Column(name = "PICKUP", length = 500)
  private String pickup;

  @Column(name = "DROPOFF", length = 500)
  private String dropOff;

  // Foreign Key to RIDE_STATUS(ID)
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "STATUS", referencedColumnName = "ID")
  private RideStatusEntity status;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "TYPE", referencedColumnName = "ID")
  private RideTypeEntity type;

  @Column(name = "PRICE")
  private Double price;

  @Column(name = "CREATE_DATE")
  private LocalDateTime createDate;

  @Column(name = "UPDATE_DATE")
  private LocalDateTime updateDate;

  @PrePersist
  protected void onCreate() {
    this.createDate = LocalDateTime.now();
  }

  @PreUpdate
  protected void onUpdate() {
    this.updateDate = LocalDateTime.now();
  }
}
