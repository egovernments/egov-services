package org.egov.egf.persistence.repository;

import org.egov.egf.persistence.entity.FinancialConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface FinancialConfigurationJpaRepository extends JpaRepository<FinancialConfiguration, java.lang.Long>,
		JpaSpecificationExecutor<FinancialConfiguration> {

}