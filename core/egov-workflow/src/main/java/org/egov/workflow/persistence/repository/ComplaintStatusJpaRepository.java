package org.egov.workflow.persistence.repository;

import org.egov.workflow.persistence.entity.ComplaintStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComplaintStatusJpaRepository extends JpaRepository<ComplaintStatus, Long> {
    ComplaintStatus findByName(String name);

    List<ComplaintStatus> findByCodeInAndTenantId(List<String> code, String tenantId);

    ComplaintStatus findByCodeAndTenantId(String code, String tenantId);
}
