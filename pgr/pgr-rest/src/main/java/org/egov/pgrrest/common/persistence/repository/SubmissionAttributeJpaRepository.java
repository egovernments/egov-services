package org.egov.pgrrest.common.persistence.repository;

import org.egov.pgrrest.common.persistence.entity.SubmissionAttribute;
import org.egov.pgrrest.common.persistence.entity.SubmissionAttributeKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubmissionAttributeJpaRepository extends JpaRepository<SubmissionAttribute, SubmissionAttributeKey>, JpaSpecificationExecutor<SubmissionAttribute> {
    @Query("select a from SubmissionAttribute a where a.id.crn = :crn and a.id.tenantId = :tenantId")
    List<SubmissionAttribute> findByCrnAndTenantId(@Param("crn") String crn, @Param("tenantId") String tenantId);

    @Query("select a from SubmissionAttribute a where a.id.crn in :crns and a.id.tenantId = :tenantId")
    List<SubmissionAttribute> findByCrnListAndTenantId(@Param("crns") List<String> crns,
                                                       @Param("tenantId") String tenantId);
}

