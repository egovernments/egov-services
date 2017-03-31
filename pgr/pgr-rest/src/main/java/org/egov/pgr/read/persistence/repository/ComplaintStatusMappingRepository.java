package org.egov.pgr.read.persistence.repository;

import org.egov.pgr.common.entity.ComplaintStatusMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComplaintStatusMappingRepository extends JpaRepository<ComplaintStatusMapping, Long> {
}
