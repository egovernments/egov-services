package org.egov.egf.voucher.domain.repository;

import org.egov.common.domain.model.Pagination;
import org.egov.common.web.contract.CommonRequest;
import org.egov.egf.voucher.domain.model.Budget;
import org.egov.egf.voucher.domain.model.BudgetSearch;
import org.egov.egf.voucher.persistence.entity.BudgetEntity;
import org.egov.egf.voucher.persistence.queue.MastersQueueRepository;
import org.egov.egf.voucher.persistence.repository.BudgetJdbcRepository;
import org.egov.egf.voucher.web.contract.BudgetContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BudgetRepository {

	@Autowired
	private BudgetJdbcRepository budgetJdbcRepository;
	@Autowired
	private MastersQueueRepository budgetQueueRepository;

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

	public void add(CommonRequest<BudgetContract> request) {
		budgetQueueRepository.add(request);
	}

	public Pagination<Budget> search(BudgetSearch domain) {

		return budgetJdbcRepository.search(domain);

	}

}