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

@Entity
@Table(name = "USERS")
@Builder
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class UserEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  private String password;

  @Column
  private String email;

  @Column
  private String name;

  @Column
  private Integer loginAttempts;

  @Column
  private LocalDateTime createDate;

  @Column
  private LocalDateTime updateDate;

  @JoinColumn(name = "CUSTOMER_STATUS", referencedColumnName = "id")
  @ManyToOne
  private CustomerStatusEntity customerStatus;

  @JoinColumn(name = "PREVIOUS_CUSTOMER_STATUS", referencedColumnName = "id")
  @ManyToOne
  private CustomerStatusEntity previousCustomerStatus;


  @JoinColumn(name = "USER_TYPE", referencedColumnName = "id")
  @ManyToOne
  private UserTypeEntity userTypeEntity;

  @Column
  private String phone;

  @PrePersist
  protected void onCreate() {
    this.createDate = LocalDateTime.now();
  }

  @PreUpdate
  protected void onUpdate() {
    this.updateDate = LocalDateTime.now();
  }

}
