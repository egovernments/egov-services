package org.egov.egf.master.domain.repository;

import org.egov.common.domain.model.Pagination;
import org.egov.common.web.contract.CommonRequest;
import org.egov.egf.master.domain.model.BudgetGroup;
import org.egov.egf.master.domain.model.BudgetGroupSearch;
import org.egov.egf.master.persistence.entity.BudgetGroupEntity;
import org.egov.egf.master.persistence.queue.MastersQueueRepository;
import org.egov.egf.master.persistence.repository.BudgetGroupJdbcRepository;
import org.egov.egf.master.web.contract.BudgetGroupContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BudgetGroupRepository {

	@Autowired
	private BudgetGroupJdbcRepository budgetGroupJdbcRepository;
	@Autowired
	private MastersQueueRepository budgetGroupQueueRepository;

	public BudgetGroup findById(BudgetGroup budgetGroup) {
		return budgetGroupJdbcRepository.findById(new BudgetGroupEntity().toEntity(budgetGroup)).toDomain();

	}

	public BudgetGroup save(BudgetGroup budgetGroup) {
		return budgetGroupJdbcRepository.create(new BudgetGroupEntity().toEntity(budgetGroup)).toDomain();
	}

	public BudgetGroup update(BudgetGroup entity) {
		return budgetGroupJdbcRepository.update(new BudgetGroupEntity().toEntity(entity)).toDomain();
	}

	public void add(CommonRequest<BudgetGroupContract> request) {
		budgetGroupQueueRepository.add(request);
	}

	public Pagination<BudgetGroup> search(BudgetGroupSearch domain) {

		return budgetGroupJdbcRepository.search(domain);

	}

}