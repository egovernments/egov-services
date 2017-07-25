package org.egov.pgrrest.common.persistence.repository;

import org.egov.pgrrest.common.persistence.entity.ComputeRuleDefinition;
import org.egov.pgrrest.common.persistence.entity.ComputeRuleDefinitionKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComputeRuleDefinitionJpaRepository extends
    JpaRepository<ComputeRuleDefinition, ComputeRuleDefinitionKey> {

    @Query("select c from ComputeRuleDefinition c where c.id.serviceCode = :serviceCode and " +
        "c.id.attributeCode in :attributeCodes and c.id.tenantId = :tenantId")
    List<ComputeRuleDefinition> findBy(@Param("serviceCode") String serviceCode,
                                      @Param("attributeCodes") List<String> attributeCodes,
                                      @Param("tenantId") String tenantId);
}
