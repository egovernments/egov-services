package org.egov.pgrrest.common.repository;

import org.egov.pgrrest.common.entity.ComplaintType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComplaintTypeJpaRepository extends JpaRepository<ComplaintType, Long> {
    @Query("select c from ComplaintType c where c.category.id = :categoryId and c.active = 't' and c.tenantId = :tenantId order by c.name")
    List<ComplaintType> findActiveComplaintTypes(@Param("categoryId") Long categoryId, @Param("tenantId") String tenantId);
    ComplaintType findByCodeAndTenantId(String code, String tenantId);
}
