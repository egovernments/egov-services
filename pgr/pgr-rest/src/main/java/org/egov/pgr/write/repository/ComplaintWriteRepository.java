package org.egov.pgr.write.repository;

import org.egov.pgr.write.entity.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComplaintWriteRepository extends JpaRepository<Complaint, Long> {

    Complaint findByCrn(String crn);

}
