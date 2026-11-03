package com.serviceconnect.repo;
import com.serviceconnect.model.ServiceOffering; import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;
public interface ServiceOfferingRepository extends JpaRepository<ServiceOffering,Long>{ List<ServiceOffering> findByActiveTrue(); List<ServiceOffering> findByProviderIdAndActiveTrue(Long id); }
