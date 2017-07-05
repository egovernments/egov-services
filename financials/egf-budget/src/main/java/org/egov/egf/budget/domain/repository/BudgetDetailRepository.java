package org.egov.egf.budget.domain.repository;

import org.egov.common.domain.model.Pagination;
import org.egov.common.web.contract.CommonRequest;
import org.egov.egf.budget.domain.model.BudgetDetail;
import org.egov.egf.budget.domain.model.BudgetDetailSearch;
import org.egov.egf.budget.persistence.entity.BudgetDetailEntity;
import org.egov.egf.budget.persistence.queue.BudgetServiceQueueRepository;
import org.egov.egf.budget.persistence.repository.BudgetDetailJdbcRepository;
import org.egov.egf.budget.web.contract.BudgetDetailContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BudgetDetailRepository {

	@Autowired
	private BudgetDetailJdbcRepository budgetDetailJdbcRepository;

	@Autowired
	private BudgetServiceQueueRepository budgetDetailQueueRepository;

	public BudgetDetail findById(BudgetDetail budgetDetail) {
		return budgetDetailJdbcRepository.findById(new BudgetDetailEntity().toEntity(budgetDetail)).toDomain();

	}

	public BudgetDetail save(BudgetDetail budgetDetail) {
		return budgetDetailJdbcRepository.create(new BudgetDetailEntity().toEntity(budgetDetail)).toDomain();
	}

	public BudgetDetail update(BudgetDetail entity) {
		return budgetDetailJdbcRepository.update(new BudgetDetailEntity().toEntity(entity)).toDomain();
	}

	public void add(CommonRequest<BudgetDetailContract> request) {
		budgetDetailQueueRepository.add(request);
	}

	public Pagination<BudgetDetail> search(BudgetDetailSearch domain) {

		return budgetDetailJdbcRepository.search(domain);

	}

}