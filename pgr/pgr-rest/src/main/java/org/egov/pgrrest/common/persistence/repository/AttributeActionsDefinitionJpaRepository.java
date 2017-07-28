package org.egov.pgrrest.common.persistence.repository;


import org.egov.pgrrest.common.persistence.entity.AttributeActionsDefinition;
import org.egov.pgrrest.common.persistence.entity.AttributeActionsDefinitionKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AttributeActionsDefinitionJpaRepository extends JpaRepository<AttributeActionsDefinition, AttributeActionsDefinitionKey> {

    @Query("select a from AttributeActionsDefinition a where a.id.attributeCode in :attributeCodes and a.id.tenantId = :tenantId and a.id.serviceCode =:serviceCode")
    List<AttributeActionsDefinition> find(@Param("attributeCodes") List<String> attributeCodes,
                                        @Param("tenantId") String tenantId,
                                        @Param("serviceCode") String serviceCode);
}
