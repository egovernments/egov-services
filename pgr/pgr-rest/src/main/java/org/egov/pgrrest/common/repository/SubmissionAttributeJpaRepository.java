package org.egov.pgrrest.common.repository;

import org.egov.pgrrest.common.entity.SubmissionAttribute;
import org.egov.pgrrest.common.entity.SubmissionAttributeKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubmissionAttributeJpaRepository extends JpaRepository<SubmissionAttribute, SubmissionAttributeKey> {
    @Query("select a from SubmissionAttribute a where a.id.crn = :crn and a.id.tenantId = :tenantId")
    List<SubmissionAttribute> findByCrnAndTenantId(@Param("crn") String crn, @Param("tenantId") String tenantId);
}

