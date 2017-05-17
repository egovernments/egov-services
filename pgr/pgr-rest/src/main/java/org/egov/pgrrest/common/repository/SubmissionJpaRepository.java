package org.egov.pgrrest.common.repository;

import org.egov.pgrrest.common.entity.Complaint;
import org.egov.pgrrest.common.entity.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Repository
public interface SubmissionJpaRepository extends JpaRepository<Submission, String> {
    Submission findByIdAndTenantId(String crn, String TenantId);
}
