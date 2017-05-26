package org.egov.egf.persistence.repository;

import org.egov.egf.persistence.entity.FinancialYear;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface FinancialYearJpaRepository
		extends JpaRepository<FinancialYear, java.lang.Long>, JpaSpecificationExecutor<FinancialYear> {

}