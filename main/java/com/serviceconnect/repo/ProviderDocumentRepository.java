package com.serviceconnect.repo;
import com.serviceconnect.model.ProviderDocument; import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;
public interface ProviderDocumentRepository extends JpaRepository<ProviderDocument,Long>{List<ProviderDocument> findByProviderId(Long id);}
