package org.egov.egf.budget.domain.repository;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.domain.model.Pagination;
import org.egov.egf.budget.domain.model.Budget;
import org.egov.egf.budget.domain.model.BudgetSearch;
import org.egov.egf.budget.persistence.entity.BudgetEntity;
import org.egov.egf.budget.persistence.queue.repository.BudgetQueueRepository;
import org.egov.egf.budget.persistence.repository.BudgetJdbcRepository;
import org.egov.egf.budget.web.contract.BudgetRequest;
import org.egov.egf.budget.web.mapper.BudgetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BudgetRepository {

	private BudgetJdbcRepository budgetJdbcRepository;

	private BudgetQueueRepository budgetQueueRepository;

	private String persistThroughKafka;

	@Autowired
	public BudgetRepository(BudgetJdbcRepository budgetJdbcRepository, BudgetQueueRepository budgetQueueRepository,
			@Value("${persist.through.kafka}") String persistThroughKafka) {
		this.budgetJdbcRepository = budgetJdbcRepository;
		this.budgetQueueRepository = budgetQueueRepository;
		this.persistThroughKafka = persistThroughKafka;

	}

	public Budget findById(Budget budget) {

		BudgetEntity entity = budgetJdbcRepository.findById(new BudgetEntity().toEntity(budget));

		if (entity != null)
			return entity.toDomain();

		return null;
	}

	@Transactional
	public List<Budget> save(List<Budget> budgets, RequestInfo requestInfo) {

		BudgetMapper mapper = new BudgetMapper();

		if (persistThroughKafka != null && !persistThroughKafka.isEmpty()
				&& persistThroughKafka.equalsIgnoreCase("yes")) {

			BudgetRequest request = new BudgetRequest();
			request.setRequestInfo(requestInfo);
			request.setBudgets(new ArrayList<>());

			for (Budget iac : budgets) {

				request.getBudgets().add(mapper.toContract(iac));

			}

			budgetQueueRepository.addToQue(request);

			return budgets;
		} else {

			List<Budget> resultList = new ArrayList<Budget>();

			for (Budget iac : budgets) {

				resultList.add(save(iac));
			}

			BudgetRequest request = new BudgetRequest();
			request.setRequestInfo(requestInfo);
			request.setBudgets(new ArrayList<>());

			for (Budget iac : resultList) {

				request.getBudgets().add(mapper.toContract(iac));

			}

			budgetQueueRepository.addToSearchQue(request);

			return resultList;
		}

	}

	@Transactional
	public List<Budget> update(List<Budget> budgets, RequestInfo requestInfo) {

		BudgetMapper mapper = new BudgetMapper();

		if (persistThroughKafka != null && !persistThroughKafka.isEmpty()
				&& persistThroughKafka.equalsIgnoreCase("yes")) {

			BudgetRequest request = new BudgetRequest();
			request.setRequestInfo(requestInfo);
			request.setBudgets(new ArrayList<>());

			for (Budget iac : budgets) {

				request.getBudgets().add(mapper.toContract(iac));

			}

			budgetQueueRepository.addToQue(request);

			return budgets;
		} else {

			List<Budget> resultList = new ArrayList<Budget>();

			for (Budget iac : budgets) {

				resultList.add(update(iac));
			}

			BudgetRequest request = new BudgetRequest();
			request.setRequestInfo(requestInfo);
			request.setBudgets(new ArrayList<>());

			for (Budget iac : resultList) {

				request.getBudgets().add(mapper.toContract(iac));

			}

			budgetQueueRepository.addToSearchQue(request);

			return resultList;
		}

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