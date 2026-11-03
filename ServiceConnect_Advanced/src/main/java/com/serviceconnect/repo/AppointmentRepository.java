package com.serviceconnect.repo;
import com.serviceconnect.model.Appointment; import org.springframework.data.jpa.repository.JpaRepository; import java.time.LocalDateTime; import java.util.*;
public interface AppointmentRepository extends JpaRepository<Appointment,Long>{
 List<Appointment> findByCustomerIdOrderByCreatedAtDesc(Long id);
 List<Appointment> findByProviderIdOrderByStartAtDesc(Long id);
 List<Appointment> findByProviderIdAndStartAtBetween(Long id,LocalDateTime from,LocalDateTime to);
 boolean existsByVerificationCode(String code);
}
