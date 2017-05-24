package org.egov.egf.persistence.repository;

import org.egov.egf.persistence.entity.FinancialConfigurationValues;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface FinancialConfigurationValuesJpaRepository extends
		JpaRepository<FinancialConfigurationValues, java.lang.Long>, JpaSpecificationExecutor<FinancialConfigurationValues> {

}