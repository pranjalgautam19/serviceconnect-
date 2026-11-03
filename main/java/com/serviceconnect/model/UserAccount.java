package com.serviceconnect.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity @Table(name="users") public class UserAccount {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
 @Column(nullable=false) public String fullName;
 @Column(nullable=false,unique=true) public String email;
 @Column(nullable=false) public String passwordHash;
 public String phone;
 @Column(nullable=false) public String role; // CUSTOMER, PROVIDER, ADMIN
 public boolean enabled=true;
 public LocalDateTime createdAt=LocalDateTime.now();
}
