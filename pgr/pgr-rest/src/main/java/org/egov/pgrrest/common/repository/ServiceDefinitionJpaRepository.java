package org.egov.pgrrest.common.repository;

import org.egov.pgrrest.common.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceDefinitionJpaRepository extends JpaRepository<ServiceDefinition, ServiceDefinitionKey> {

}

