package org.egov.egf.voucher.domain.repository;

import org.egov.common.domain.model.Pagination;
import org.egov.common.web.contract.CommonRequest;
import org.egov.egf.voucher.domain.model.BudgetReAppropriation;
import org.egov.egf.voucher.domain.model.BudgetReAppropriationSearch;
import org.egov.egf.voucher.persistence.entity.BudgetReAppropriationEntity;
import org.egov.egf.voucher.persistence.queue.MastersQueueRepository;
import org.egov.egf.voucher.persistence.repository.BudgetReAppropriationJdbcRepository;
import org.egov.egf.voucher.web.contract.BudgetReAppropriationContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BudgetReAppropriationRepository {

	@Autowired
	private BudgetReAppropriationJdbcRepository budgetReAppropriationJdbcRepository;
	@Autowired
	private MastersQueueRepository budgetReAppropriationQueueRepository;

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