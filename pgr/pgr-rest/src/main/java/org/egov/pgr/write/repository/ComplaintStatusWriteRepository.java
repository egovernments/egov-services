package org.egov.pgr.write.repository;

import org.egov.pgr.write.entity.ComplaintStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComplaintStatusWriteRepository extends JpaRepository<ComplaintStatus, Long> {

	ComplaintStatus findByName(String name);
}
