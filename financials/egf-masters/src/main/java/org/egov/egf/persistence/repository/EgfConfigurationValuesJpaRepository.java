package org.egov.egf.persistence.repository;

import org.egov.egf.persistence.entity.EgfConfigurationValues;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface EgfConfigurationValuesJpaRepository extends
		JpaRepository<EgfConfigurationValues, java.lang.Long>, JpaSpecificationExecutor<EgfConfigurationValues> {

}