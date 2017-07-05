package org.egov.pgrrest.common.persistence.repository;

import org.egov.pgrrest.common.persistence.entity.ConstraintDefinition;
import org.egov.pgrrest.common.persistence.entity.ConstraintDefinitionKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConstraintDefinitionJpaRepository extends
    JpaRepository<ConstraintDefinition, ConstraintDefinitionKey> {

    @Query("select c from ConstraintDefinition c where c.id.serviceCode = :serviceCode and " +
        "c.id.attributeCode in :attributeCodes and c.id.tenantId = :tenantId")
    List<ConstraintDefinition> findBy(@Param("serviceCode") String serviceCode,
                                      @Param("attributeCodes") List<String> attributeCodes,
                                      @Param("tenantId") String tenantId);
}
