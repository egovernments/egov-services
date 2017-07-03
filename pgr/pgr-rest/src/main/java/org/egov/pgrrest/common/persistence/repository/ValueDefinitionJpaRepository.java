package org.egov.pgrrest.common.persistence.repository;

import org.egov.pgrrest.common.persistence.entity.ValueDefinition;
import org.egov.pgrrest.common.persistence.entity.ValueDefinitionKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ValueDefinitionJpaRepository extends JpaRepository<ValueDefinition, ValueDefinitionKey> {

    @Query("select v from ValueDefinition v where v.id.attributeCode in :attributeCodes and v.id.tenantId = :tenantId and v.serviceCode =:serviceCode")
    List<ValueDefinition> find(@Param("attributeCodes") List<String> attributeCodes,
                               @Param("tenantId") String tenantId,
                               @Param("serviceCode") String serviceCode);
}
