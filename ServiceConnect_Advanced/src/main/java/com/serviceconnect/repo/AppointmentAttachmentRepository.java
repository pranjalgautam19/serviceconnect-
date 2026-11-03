package com.serviceconnect.repo;
import com.serviceconnect.model.AppointmentAttachment; import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;
public interface AppointmentAttachmentRepository extends JpaRepository<AppointmentAttachment,Long>{ List<AppointmentAttachment> findByAppointmentId(Long id); }
