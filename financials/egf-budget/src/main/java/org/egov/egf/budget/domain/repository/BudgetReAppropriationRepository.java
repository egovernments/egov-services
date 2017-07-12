package org.egov.egf.budget.domain.repository;

import org.egov.common.domain.model.Pagination;
import org.egov.common.web.contract.CommonRequest;
import org.egov.egf.budget.domain.model.BudgetReAppropriation;
import org.egov.egf.budget.domain.model.BudgetReAppropriationSearch;
import org.egov.egf.budget.persistence.entity.BudgetReAppropriationEntity;
import org.egov.egf.budget.persistence.queue.BudgetServiceQueueRepository;
import org.egov.egf.budget.persistence.repository.BudgetReAppropriationJdbcRepository;
import org.egov.egf.budget.web.contract.BudgetReAppropriationContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BudgetReAppropriationRepository {

	@Autowired
	private BudgetReAppropriationJdbcRepository budgetReAppropriationJdbcRepository;
	@Autowired
	private BudgetServiceQueueRepository budgetReAppropriationQueueRepository;

	public BudgetReAppropriation findById(BudgetReAppropriation budgetReAppropriation) {
		return budgetReAppropriationJdbcRepository.findById(new BudgetReAppropriationEntity().toEntity(budgetReAppropriation)).toDomain();

	}
	@Transactional
	public BudgetReAppropriation save(BudgetReAppropriation budgetReAppropriation) {
		return budgetReAppropriationJdbcRepository.create(new BudgetReAppropriationEntity().toEntity(budgetReAppropriation)).toDomain();
	}
	@Transactional
	public BudgetReAppropriation update(BudgetReAppropriation entity) {
		return budgetReAppropriationJdbcRepository.update(new BudgetReAppropriationEntity().toEntity(entity)).toDomain();
	}

	public void add(CommonRequest<BudgetReAppropriationContract> request) {
		budgetReAppropriationQueueRepository.add(request);
	}

	public Pagination<BudgetReAppropriation> search(BudgetReAppropriationSearch domain) {

		return budgetReAppropriationJdbcRepository.search(domain);

	}

}