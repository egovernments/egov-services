package org.egov.pgrrest.common.persistence.repository;

import org.egov.pgrrest.common.persistence.entity.Submission;
import org.egov.pgrrest.common.persistence.entity.SubmissionKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubmissionJpaRepository extends JpaRepository<Submission, SubmissionKey>, JpaSpecificationExecutor<Submission> {
    @Query("select s from Submission s where s.id.crn in :crns and s.id.tenantId = :tenantId")
    List<Submission> findByCRNList(@Param("crns") List<String> crns, @Param("tenantId") String tenantId);

    @Query("select s.position from Submission s where s.id.crn = :crn and s.id.tenantId = :tenantId")
    Long findPosition(@Param("crn") String crn, @Param("tenantId") String tenantId);

    @Query("select s.loggedInRequester from Submission s where s.id.crn = :crn and s.id.tenantId = :tenantId")
    Long findLoggedInRequester(@Param("crn") String crn, @Param("tenantId") String tenantId);
}
