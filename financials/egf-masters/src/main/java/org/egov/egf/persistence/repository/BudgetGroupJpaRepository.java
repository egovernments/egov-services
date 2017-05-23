package org.egov.egf.persistence.repository;

import org.egov.egf.persistence.entity.BudgetGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BudgetGroupJpaRepository
		extends JpaRepository<BudgetGroup, java.lang.Long>, JpaSpecificationExecutor<BudgetGroup> {

	BudgetGroup findByName(String name);

}