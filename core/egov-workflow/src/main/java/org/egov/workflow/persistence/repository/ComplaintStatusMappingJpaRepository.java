package org.egov.workflow.persistence.repository;

import org.egov.workflow.persistence.entity.ComplaintStatusMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComplaintStatusMappingJpaRepository extends JpaRepository<ComplaintStatusMapping, Long> {

    List<ComplaintStatusMapping> findByCurrentStatusIdAndRoleInAndTenantIdOrderByOrderNoAsc(Long currentStatus, List<Long> role, String tenantId);
}
