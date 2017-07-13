package org.egov.egf.budget.domain.repository;

import org.egov.common.domain.model.Pagination;
import org.egov.egf.budget.domain.model.Budget;
import org.egov.egf.budget.domain.model.BudgetSearch;
import org.egov.egf.budget.persistence.entity.BudgetEntity;
import org.egov.egf.budget.persistence.repository.BudgetJdbcRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BudgetRepository {

	@Autowired
	private BudgetJdbcRepository budgetJdbcRepository;

	public Budget findById(Budget budget) {
		return budgetJdbcRepository.findById(new BudgetEntity().toEntity(budget)).toDomain();

	}

	@Transactional
	public Budget save(Budget budget) {
		return budgetJdbcRepository.create(new BudgetEntity().toEntity(budget)).toDomain();
	}

	@Transactional
	public Budget update(Budget entity) {
		return budgetJdbcRepository.update(new BudgetEntity().toEntity(entity)).toDomain();
	}

	public Pagination<Budget> search(BudgetSearch domain) {

		return budgetJdbcRepository.search(domain);

	}

}