package org.egov.egf.persistence.repository;

import org.egov.egf.persistence.entity.FiscalPeriod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface FiscalPeriodJpaRepository
		extends JpaRepository<FiscalPeriod, java.lang.Long>, JpaSpecificationExecutor<FiscalPeriod> {

	FiscalPeriod findByName(String name);

}