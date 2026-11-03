package com.serviceconnect.service;
import com.serviceconnect.model.*; import com.serviceconnect.repo.*; import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional;
import java.time.*; import java.util.*;
@Service public class BookingService {
 private final AppointmentRepository appointments; private final AvailabilitySettingsRepository settings; private final InvoiceRepository invoices; private final ServiceOfferingRepository offerings;
 public BookingService(AppointmentRepository a,AvailabilitySettingsRepository s,InvoiceRepository i,ServiceOfferingRepository o){appointments=a;settings=s;invoices=i;offerings=o;}
 public AvailabilitySettings getSettings(ProviderProfile p){return settings.findByProviderId(p.id).orElseGet(()->{AvailabilitySettings x=new AvailabilitySettings();x.provider=p;return settings.save(x);});}
 public List<Map<String,Object>> slots(Long serviceId,LocalDate date){ServiceOffering svc=offerings.findById(serviceId).orElseThrow(()->new IllegalArgumentException("Service not found")); AvailabilitySettings a=getSettings(svc.provider); List<Map<String,Object>> out=new ArrayList<>(); DayOfWeek day=date.getDayOfWeek(); if(!a.workingDays.contains(day.name()))return out;
  int duration=svc.durationMinutes==null?a.slotDurationMinutes:svc.durationMinutes; LocalDateTime cursor=date.atTime(a.startTime), end=date.atTime(a.endTime);
  List<Appointment> booked=appointments.findByProviderIdAndStartAtBetween(svc.provider.id,date.atStartOfDay(),date.plusDays(1).atStartOfDay());
  while(!cursor.plusMinutes(duration).isAfter(end)){LocalDateTime slotEnd=cursor.plusMinutes(duration); boolean inBreak=a.breakStart!=null&&a.breakEnd!=null&&cursor.toLocalTime().isBefore(a.breakEnd)&&slotEnd.toLocalTime().isAfter(a.breakStart); boolean past=!cursor.isAfter(LocalDateTime.now()); final LocalDateTime slotStart=cursor; boolean overlap=booked.stream().anyMatch(b->List.of("PENDING","APPROVED").contains(b.status)&&slotStart.isBefore(b.endAt)&&slotEnd.isAfter(b.startAt)); long active=booked.stream().filter(b->List.of("PENDING","APPROVED").contains(b.status)).count(); Map<String,Object> row=new LinkedHashMap<>();row.put("startAt",cursor.toString());row.put("endAt",slotEnd.toString());row.put("available",!past&&!inBreak&&!overlap&&active<a.dailyCapacity);row.put("reason",past?"PAST":inBreak?"BREAK":overlap?"TAKEN":active>=a.dailyCapacity?"DAILY_CAPACITY":"AVAILABLE");out.add(row);cursor=cursor.plusMinutes(duration); }
  return out;
 }
 @Transactional public Appointment create(UserAccount customer,ServiceOffering svc,LocalDateTime start,String problem,String mode,String address,Double lat,Double lon){
  if(!svc.active||!svc.provider.verificationStatus.equals("VERIFIED"))throw new IllegalArgumentException("This provider is not currently accepting bookings.");
  if(!List.of("HOME","PROVIDER").contains(mode))throw new IllegalArgumentException("Choose HOME or PROVIDER service.");
  if(mode.equals("HOME")&&!List.of("HOME","BOTH").contains(svc.provider.deliveryModes))throw new IllegalArgumentException("This provider does not offer home visits.");
  if(mode.equals("PROVIDER")&&!List.of("PROVIDER","BOTH").contains(svc.provider.deliveryModes))throw new IllegalArgumentException("This provider does not accept visits at their location.");
  if(mode.equals("HOME")&&lat!=null&&lon!=null&&svc.provider.latitude!=null&&svc.provider.longitude!=null&&Geo.km(lat,lon,svc.provider.latitude,svc.provider.longitude)>svc.provider.serviceRadiusKm)throw new IllegalArgumentException("Your address is outside this provider's home-service radius.");
  if(mode.equals("HOME")&&(address==null||address.isBlank()))throw new IllegalArgumentException("Enter the service address for a home visit.");
  AvailabilitySettings a=settings.lockByProviderId(svc.provider.id).orElseGet(()->getSettings(svc.provider)); LocalDate date=start.toLocalDate(); if(!a.workingDays.contains(date.getDayOfWeek().name()))throw new IllegalArgumentException("Provider is closed on this day.");
  int duration=svc.durationMinutes==null?a.slotDurationMinutes:svc.durationMinutes; LocalDateTime end=start.plusMinutes(duration);
  if(!start.isAfter(LocalDateTime.now()))throw new IllegalArgumentException("Choose a future appointment time.");
  if(start.toLocalTime().isBefore(a.startTime)||end.toLocalTime().isAfter(a.endTime))throw new IllegalArgumentException("Selected time is outside working hours.");
  if(java.time.Duration.between(a.startTime,start.toLocalTime()).toMinutes()%duration!=0)throw new IllegalArgumentException("Choose one of the displayed appointment time slots.");
  if(a.breakStart!=null&&a.breakEnd!=null&&start.toLocalTime().isBefore(a.breakEnd)&&end.toLocalTime().isAfter(a.breakStart))throw new IllegalArgumentException("Selected time overlaps the provider's break.");
  List<Appointment> day=appointments.findByProviderIdAndStartAtBetween(svc.provider.id,date.atStartOfDay(),date.plusDays(1).atStartOfDay());
  long active=day.stream().filter(b->List.of("PENDING","APPROVED").contains(b.status)).count(); if(active>=a.dailyCapacity)throw new IllegalArgumentException("Provider's daily appointment capacity is full.");
  boolean overlap=day.stream().filter(b->List.of("PENDING","APPROVED").contains(b.status)).anyMatch(b->start.isBefore(b.endAt)&&end.isAfter(b.startAt)); if(overlap)throw new IllegalArgumentException("This time slot has already been reserved. Please choose another slot.");
  Appointment ap=new Appointment(); ap.customer=customer;ap.provider=svc.provider;ap.service=svc;ap.startAt=start;ap.endAt=end;ap.problemDescription=problem;ap.serviceMode=mode;ap.serviceAddress=address;ap.customerLatitude=lat;ap.customerLongitude=lon;ap.status="PENDING";ap.approvalDeadline=LocalDateTime.now().plusHours(24);ap.verificationCode=code();return appointments.save(ap);
 }
 private String code(){String c; do{c="SC-"+(100000+(int)(Math.random()*900000));}while(appointments.existsByVerificationCode(c));return c;}
 @Transactional public void expire(){LocalDateTime now=LocalDateTime.now(); for(Appointment a:appointments.findAll())if("PENDING".equals(a.status)&&a.approvalDeadline!=null&&a.approvalDeadline.isBefore(now)){a.status="EXPIRED";a.providerResponse="Automatically expired after 24 hours without provider approval.";a.updatedAt=now;}}
}
