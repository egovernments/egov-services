package org.egov.workflow.persistence.repository;

import org.egov.workflow.persistence.entity.ComplaintStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComplaintStatusJpaRepository extends JpaRepository<ComplaintStatus, Long> {
}
