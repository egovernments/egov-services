package org.egov.egf.budget.domain.repository;

import org.egov.common.domain.model.Pagination;
import org.egov.common.web.contract.CommonRequest;
import org.egov.egf.budget.domain.model.Budget;
import org.egov.egf.budget.domain.model.BudgetSearch;
import org.egov.egf.budget.persistence.entity.BudgetEntity;
import org.egov.egf.budget.persistence.repository.BudgetJdbcRepository;
import org.egov.egf.budget.web.contract.BudgetContract;
import org.egov.egf.master.persistence.queue.MastersQueueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BudgetRepository {

	@Autowired
	private BudgetJdbcRepository budgetJdbcRepository;
	@Autowired
	private MastersQueueRepository budgetQueueRepository;

	public Budget findById(Budget budget) {
		return budgetJdbcRepository.findById(new BudgetEntity().toEntity(budget)).toDomain();

	}

	public Budget save(Budget budget) {
		return budgetJdbcRepository.create(new BudgetEntity().toEntity(budget)).toDomain();
	}

	public Budget update(Budget entity) {
		return budgetJdbcRepository.update(new BudgetEntity().toEntity(entity)).toDomain();
	}

	public void add(CommonRequest<BudgetContract> request) {
		budgetQueueRepository.add(request);
	}

	public Pagination<Budget> search(BudgetSearch domain) {

		return budgetJdbcRepository.search(domain);

	}

}