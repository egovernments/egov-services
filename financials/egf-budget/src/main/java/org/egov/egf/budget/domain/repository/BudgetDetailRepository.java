package org.egov.egf.budget.domain.repository;

import org.egov.common.domain.model.Pagination;
import org.egov.egf.budget.domain.model.BudgetDetail;
import org.egov.egf.budget.domain.model.BudgetDetailSearch;
import org.egov.egf.budget.persistence.entity.BudgetDetailEntity;
import org.egov.egf.budget.persistence.repository.BudgetDetailJdbcRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BudgetDetailRepository {

	@Autowired
	private BudgetDetailJdbcRepository budgetDetailJdbcRepository;

	public BudgetDetail findById(BudgetDetail budgetDetail) {
		return budgetDetailJdbcRepository.findById(new BudgetDetailEntity().toEntity(budgetDetail)).toDomain();

	}

	@Transactional
	public BudgetDetail save(BudgetDetail budgetDetail) {
		return budgetDetailJdbcRepository.create(new BudgetDetailEntity().toEntity(budgetDetail)).toDomain();
	}

	@Transactional
	public BudgetDetail update(BudgetDetail entity) {
		return budgetDetailJdbcRepository.update(new BudgetDetailEntity().toEntity(entity)).toDomain();
	}

	public Pagination<BudgetDetail> search(BudgetDetailSearch domain) {

		return budgetDetailJdbcRepository.search(domain);

	}

}