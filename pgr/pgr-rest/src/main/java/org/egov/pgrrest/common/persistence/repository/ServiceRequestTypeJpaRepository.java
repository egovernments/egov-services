package org.egov.pgrrest.common.persistence.repository;

import org.egov.pgrrest.common.persistence.entity.ServiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRequestTypeJpaRepository extends JpaRepository<ServiceType, Long> {
    @Query("select c from ServiceType c where c.category.id = :categoryId and c.active = 't' and c.tenantId = :tenantId order by c.name")
    List<ServiceType> findActiveServiceTypes(@Param("categoryId") Long categoryId, @Param("tenantId") String tenantId);

    ServiceType findByCodeAndTenantId(String code, String tenantId);

    List<ServiceType> findByCodeInAndTenantId(List<String> codes, String tenantId);

    List<ServiceType> findByTenantId(String tenantId);

    @Query("select c from ServiceType c where c.code in :codes and c.active = 't' and c.tenantId = :tenantId order by c.name")
    List<ServiceType> findActiveServiceTypes(@Param("codes") List<String> codes, @Param("tenantId") String tenantId);
}
