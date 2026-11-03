package com.serviceconnect.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity public class Invoice {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
 @OneToOne(optional=false) public Appointment appointment;
 public Double amount; public String status="UNPAID"; public LocalDateTime issuedAt=LocalDateTime.now();
}
