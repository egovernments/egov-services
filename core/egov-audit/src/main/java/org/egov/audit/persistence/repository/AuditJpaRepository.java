package org.egov.audit.persistence.repository;

import org.egov.audit.persistence.entity.Audit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditJpaRepository extends JpaRepository<Audit, Long> {

}
