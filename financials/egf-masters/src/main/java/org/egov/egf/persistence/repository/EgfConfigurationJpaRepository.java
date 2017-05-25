package org.egov.egf.persistence.repository;

import org.egov.egf.persistence.entity.EgfConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface EgfConfigurationJpaRepository extends JpaRepository<EgfConfiguration, java.lang.Long>,
		JpaSpecificationExecutor<EgfConfiguration> {

}