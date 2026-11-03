package com.serviceconnect.model;
import jakarta.persistence.*;
@Entity public class ProviderProfile {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
 @OneToOne(optional=false) public UserAccount user;
 public String businessName; @Column(length=3000) public String description;
 public String category; public Integer experienceYears=0; public String qualifications;
 public String address; public String city; public String state; public String pincode;
 public Double latitude; public Double longitude; public Integer serviceRadiusKm=10;
 public String deliveryModes="BOTH"; // HOME, PROVIDER, BOTH
 public String verificationStatus="PENDING"; // PENDING, UNDER_REVIEW, VERIFIED, REJECTED, RESUBMISSION_REQUIRED
 public String verificationNote; public boolean online=false;
}
