package com.serviceconnect.model;
import jakarta.persistence.*; import java.time.LocalDateTime;
@Entity public class ProviderDocument {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
 @ManyToOne(optional=false) public ProviderProfile provider;
 public String originalName; public String storedName; public String contentType; public Long sizeBytes;
 public LocalDateTime uploadedAt=LocalDateTime.now();
}
