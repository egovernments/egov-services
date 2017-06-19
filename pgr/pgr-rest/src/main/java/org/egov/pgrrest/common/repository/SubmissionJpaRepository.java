package org.egov.pgrrest.common.repository;

import org.egov.pgrrest.common.entity.Submission;
import org.egov.pgrrest.common.entity.SubmissionKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubmissionJpaRepository extends JpaRepository<Submission, SubmissionKey> {
    @Query("select s from Submission s where s.id.crn in :crns and s.id.tenantId = :tenantId")
    List<Submission> findCRNList(@Param("crns") List<String> crns, @Param("tenantId") String tenantId);
}
