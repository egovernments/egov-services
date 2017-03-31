package org.egov.pgr.common.repository;

import org.egov.pgr.common.entity.ComplaintStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComplaintStatusJpaRepository extends JpaRepository<ComplaintStatus, Long> {
	ComplaintStatus findByName(String name);
}
