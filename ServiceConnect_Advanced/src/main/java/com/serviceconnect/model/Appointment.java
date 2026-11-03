package com.serviceconnect.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity public class Appointment {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
 @Column(nullable=false,unique=true) public String verificationCode;
 @ManyToOne(optional=false) public UserAccount customer;
 @ManyToOne(optional=false) public ProviderProfile provider;
 @ManyToOne(optional=false) public ServiceOffering service;
 public LocalDateTime startAt; public LocalDateTime endAt;
 @Column(length=4000) public String problemDescription;
 public String serviceMode; // HOME or PROVIDER
 public String serviceAddress; public Double customerLatitude; public Double customerLongitude;
 public String status="PENDING"; // PENDING, APPROVED, REJECTED, CANCELLED, EXPIRED, COMPLETED
 public String providerResponse; public LocalDateTime createdAt=LocalDateTime.now();
 public LocalDateTime approvalDeadline; public LocalDateTime updatedAt=LocalDateTime.now();
}
