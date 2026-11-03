package com.serviceconnect.model;
import jakarta.persistence.*;
@Entity public class ServiceOffering {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
 @ManyToOne(optional=false) public ProviderProfile provider;
 @Column(nullable=false) public String title; public String category;
 @Column(length=3000) public String description;
 public Double price=0.0; public Integer durationMinutes=30; public boolean active=true;
}
