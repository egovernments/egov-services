package org.egov.egf.budget.domain.repository;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.domain.model.Pagination;
import org.egov.egf.budget.domain.model.BudgetReAppropriation;
import org.egov.egf.budget.domain.model.BudgetReAppropriationSearch;
import org.egov.egf.budget.persistence.entity.BudgetReAppropriationEntity;
import org.egov.egf.budget.persistence.queue.repository.BudgetReAppropriationQueueRepository;
import org.egov.egf.budget.persistence.repository.BudgetReAppropriationJdbcRepository;
import org.egov.egf.budget.web.contract.BudgetReAppropriationRequest;
import org.egov.egf.budget.web.mapper.BudgetReAppropriationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BudgetReAppropriationRepository {

	private BudgetReAppropriationJdbcRepository budgetReAppropriationJdbcRepository;

	private BudgetReAppropriationQueueRepository budgetReAppropriationQueueRepository;

	private String persistThroughKafka;

	@Autowired
	public BudgetReAppropriationRepository(BudgetReAppropriationJdbcRepository budgetReAppropriationJdbcRepository,
			BudgetReAppropriationQueueRepository budgetReAppropriationQueueRepository,
			@Value("${persist.through.kafka}") String persistThroughKafka) {
		this.budgetReAppropriationJdbcRepository = budgetReAppropriationJdbcRepository;
		this.budgetReAppropriationQueueRepository = budgetReAppropriationQueueRepository;
		this.persistThroughKafka = persistThroughKafka;

	}

	public BudgetReAppropriation findById(BudgetReAppropriation budgetReAppropriation) {

		BudgetReAppropriationEntity entity = budgetReAppropriationJdbcRepository
				.findById(new BudgetReAppropriationEntity().toEntity(budgetReAppropriation));

		if (entity != null)
			return entity.toDomain();

		return null;

	}

	@Transactional
	public List<BudgetReAppropriation> save(List<BudgetReAppropriation> budgetReAppropriations,
			RequestInfo requestInfo) {

		BudgetReAppropriationMapper mapper = new BudgetReAppropriationMapper();

		if (persistThroughKafka != null && !persistThroughKafka.isEmpty()
				&& persistThroughKafka.equalsIgnoreCase("yes")) {

			BudgetReAppropriationRequest request = new BudgetReAppropriationRequest();
			request.setRequestInfo(requestInfo);
			request.setBudgetReAppropriations(new ArrayList<>());

			for (BudgetReAppropriation iac : budgetReAppropriations) {

				request.getBudgetReAppropriations().add(mapper.toContract(iac));

			}

			budgetReAppropriationQueueRepository.addToQue(request);

			return budgetReAppropriations;
		} else {

			List<BudgetReAppropriation> resultList = new ArrayList<BudgetReAppropriation>();

			for (BudgetReAppropriation iac : budgetReAppropriations) {

				resultList.add(save(iac));
			}

			BudgetReAppropriationRequest request = new BudgetReAppropriationRequest();
			request.setRequestInfo(requestInfo);
			request.setBudgetReAppropriations(new ArrayList<>());

			for (BudgetReAppropriation iac : resultList) {

				request.getBudgetReAppropriations().add(mapper.toContract(iac));

			}

			budgetReAppropriationQueueRepository.addToSearchQue(request);

			return resultList;
		}

	}

	@Transactional
	public List<BudgetReAppropriation> update(List<BudgetReAppropriation> budgetReAppropriations,
			RequestInfo requestInfo) {

		BudgetReAppropriationMapper mapper = new BudgetReAppropriationMapper();

		if (persistThroughKafka != null && !persistThroughKafka.isEmpty()
				&& persistThroughKafka.equalsIgnoreCase("yes")) {

			BudgetReAppropriationRequest request = new BudgetReAppropriationRequest();
			request.setRequestInfo(requestInfo);
			request.setBudgetReAppropriations(new ArrayList<>());

			for (BudgetReAppropriation iac : budgetReAppropriations) {

				request.getBudgetReAppropriations().add(mapper.toContract(iac));

			}

			budgetReAppropriationQueueRepository.addToQue(request);

			return budgetReAppropriations;
		} else {

			List<BudgetReAppropriation> resultList = new ArrayList<BudgetReAppropriation>();

			for (BudgetReAppropriation iac : budgetReAppropriations) {

				resultList.add(update(iac));
			}

			BudgetReAppropriationRequest request = new BudgetReAppropriationRequest();
			request.setRequestInfo(requestInfo);
			request.setBudgetReAppropriations(new ArrayList<>());

			for (BudgetReAppropriation iac : resultList) {

				request.getBudgetReAppropriations().add(mapper.toContract(iac));

			}

			budgetReAppropriationQueueRepository.addToSearchQue(request);

			return resultList;
		}

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