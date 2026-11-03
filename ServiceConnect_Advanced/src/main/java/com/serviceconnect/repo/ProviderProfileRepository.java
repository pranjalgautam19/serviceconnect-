package com.serviceconnect.repo;
import com.serviceconnect.model.*; import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;
public interface ProviderProfileRepository extends JpaRepository<ProviderProfile,Long>{ Optional<ProviderProfile> findByUserId(Long id); List<ProviderProfile> findByVerificationStatusIgnoreCase(String status); }
