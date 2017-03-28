package org.egov.workflow.persistence.repository;

import org.egov.workflow.persistence.entity.Escalation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EscalationJpaRepository extends JpaRepository<Escalation, Long> {

    @Query("select hours from Escalation e where e.designation =:designationId and e.complaintType =:complaintTypeId " +
        "and e.tenantId = :tenantId")
    Integer findBy(@Param("designationId") long designationId,
                   @Param("complaintTypeId") long comTypeId,
                   @Param("tenantId") String tenantId);

}