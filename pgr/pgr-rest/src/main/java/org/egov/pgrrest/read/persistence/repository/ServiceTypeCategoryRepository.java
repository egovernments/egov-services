package org.egov.pgrrest.read.persistence.repository;

import java.util.List;

import org.egov.pgrrest.common.persistence.entity.ServiceTypeCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceTypeCategoryRepository extends JpaRepository<ServiceTypeCategory, Long> {

    ServiceTypeCategory findByName(String name);
    
    List<ServiceTypeCategory> findAllByTenantIdOrderByNameAsc(String tenantId);

}
