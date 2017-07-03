package org.egov.pgrrest.common.persistence.repository;


import org.egov.pgrrest.common.persistence.entity.AttributeRolesDefinition;
import org.egov.pgrrest.common.persistence.entity.AttributeRolesDefinitionKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AttributeRolesDefinitionJpaRepository extends JpaRepository<AttributeRolesDefinition, AttributeRolesDefinitionKey> {

    @Query("select a from AttributeRolesDefinition a where a.id.attributeCode in :attributeCodes and a.id.tenantId = :tenantId and a.id.serviceCode =:serviceCode")
    List<AttributeRolesDefinition> find(@Param("attributeCodes") List<String> attributeCodes,
                               @Param("tenantId") String tenantId,
                               @Param("serviceCode") String serviceCode);
}
