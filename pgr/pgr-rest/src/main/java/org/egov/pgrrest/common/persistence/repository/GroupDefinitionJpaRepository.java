package org.egov.pgrrest.common.persistence.repository;

import org.egov.pgrrest.common.persistence.entity.GroupDefinition;
import org.egov.pgrrest.common.persistence.entity.GroupDefinitionKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupDefinitionJpaRepository extends JpaRepository<GroupDefinition, GroupDefinitionKey> {

    @Query("select g from GroupDefinition g where g.id.serviceCode = :serviceCode and g.id.tenantId = :tenantId")
    List<GroupDefinition> find(@Param("serviceCode") String serviceCode,
                                                           @Param("tenantId") String tenantId);
}

