package org.egov.egf.budget.domain.repository;

import org.egov.common.domain.model.Pagination;
import org.egov.egf.budget.domain.model.BudgetReAppropriation;
import org.egov.egf.budget.domain.model.BudgetReAppropriationSearch;
import org.egov.egf.budget.persistence.entity.BudgetReAppropriationEntity;
import org.egov.egf.budget.persistence.repository.BudgetReAppropriationJdbcRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BudgetReAppropriationRepository {

	@Autowired
	private BudgetReAppropriationJdbcRepository budgetReAppropriationJdbcRepository;

	public BudgetReAppropriation findById(BudgetReAppropriation budgetReAppropriation) {

		BudgetReAppropriationEntity entity = budgetReAppropriationJdbcRepository
				.findById(new BudgetReAppropriationEntity().toEntity(budgetReAppropriation));

		if (entity != null)
			return entity.toDomain();

		return null;

	}

	@Transactional
	public BudgetReAppropriation save(BudgetReAppropriation budgetReAppropriation) {
		return budgetReAppropriationJdbcRepository
				.create(new BudgetReAppropriationEntity().toEntity(budgetReAppropriation)).toDomain();
	}

	@Transactional
	public BudgetReAppropriation update(BudgetReAppropriation entity) {
		return budgetReAppropriationJdbcRepository.update(new BudgetReAppropriationEntity().toEntity(entity))
				.toDomain();
	}

	public Pagination<BudgetReAppropriation> search(BudgetReAppropriationSearch domain) {

		return budgetReAppropriationJdbcRepository.search(domain);

	}

}