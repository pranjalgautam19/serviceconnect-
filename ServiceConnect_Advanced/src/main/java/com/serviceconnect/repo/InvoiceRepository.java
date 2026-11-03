package com.serviceconnect.repo;
import com.serviceconnect.model.Invoice; import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;
public interface InvoiceRepository extends JpaRepository<Invoice,Long>{ List<Invoice> findByAppointmentCustomerIdOrderByIssuedAtDesc(Long id); List<Invoice> findByAppointmentProviderIdOrderByIssuedAtDesc(Long id); Optional<Invoice> findByAppointmentId(Long id); }
