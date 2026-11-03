package com.serviceconnect.model;
import jakarta.persistence.*;
import java.time.LocalTime;
@Entity public class AvailabilitySettings {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
 @OneToOne(optional=false) public ProviderProfile provider;
 public String workingDays="MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY,SATURDAY";
 public LocalTime startTime=LocalTime.of(9,0); public LocalTime endTime=LocalTime.of(18,0);
 public LocalTime breakStart=LocalTime.of(13,0); public LocalTime breakEnd=LocalTime.of(14,0);
 public Integer dailyCapacity=10; public Integer slotDurationMinutes=30;
}
