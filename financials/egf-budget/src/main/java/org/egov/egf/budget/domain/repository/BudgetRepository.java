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

    private final BudgetJdbcRepository budgetJdbcRepository;

    private final BudgetQueueRepository budgetQueueRepository;

    private final String persistThroughKafka;

    @Autowired
    public BudgetRepository(final BudgetJdbcRepository budgetJdbcRepository, final BudgetQueueRepository budgetQueueRepository,
            @Value("${persist.through.kafka}") final String persistThroughKafka) {
        this.budgetJdbcRepository = budgetJdbcRepository;
        this.budgetQueueRepository = budgetQueueRepository;
        this.persistThroughKafka = persistThroughKafka;

    }

    public Budget findById(final Budget budget) {

        final BudgetEntity entity = budgetJdbcRepository.findById(new BudgetEntity().toEntity(budget));

        if (entity != null)
            return entity.toDomain();

        return null;
    }

    @Transactional
    public List<Budget> save(final List<Budget> budgets, final RequestInfo requestInfo) {

        final BudgetMapper mapper = new BudgetMapper();

        if (persistThroughKafka != null && !persistThroughKafka.isEmpty()
                && persistThroughKafka.equalsIgnoreCase("yes")) {

            final BudgetRequest request = new BudgetRequest();
            request.setRequestInfo(requestInfo);
            request.setBudgets(new ArrayList<>());

            for (final Budget iac : budgets)
                request.getBudgets().add(mapper.toContract(iac));

            budgetQueueRepository.addToQue(request);

            return budgets;
        } else {

            final List<Budget> resultList = new ArrayList<Budget>();

            for (final Budget iac : budgets)
                resultList.add(save(iac));

            final BudgetRequest request = new BudgetRequest();
            request.setRequestInfo(requestInfo);
            request.setBudgets(new ArrayList<>());

            for (final Budget iac : resultList)
                request.getBudgets().add(mapper.toContract(iac));

            budgetQueueRepository.addToSearchQue(request);

            return resultList;
        }

    }

    @Transactional
    public List<Budget> update(final List<Budget> budgets, final RequestInfo requestInfo) {

        final BudgetMapper mapper = new BudgetMapper();

        if (persistThroughKafka != null && !persistThroughKafka.isEmpty()
                && persistThroughKafka.equalsIgnoreCase("yes")) {

            final BudgetRequest request = new BudgetRequest();
            request.setRequestInfo(requestInfo);
            request.setBudgets(new ArrayList<>());

            for (final Budget iac : budgets)
                request.getBudgets().add(mapper.toContract(iac));

            budgetQueueRepository.addToQue(request);

            return budgets;
        } else {

            final List<Budget> resultList = new ArrayList<Budget>();

            for (final Budget iac : budgets)
                resultList.add(update(iac));

            final BudgetRequest request = new BudgetRequest();
            request.setRequestInfo(requestInfo);
            request.setBudgets(new ArrayList<>());

            for (final Budget iac : resultList)
                request.getBudgets().add(mapper.toContract(iac));

            budgetQueueRepository.addToSearchQue(request);

            return resultList;
        }

    }

    @Transactional
    public Budget save(final Budget budget) {
        return budgetJdbcRepository.create(new BudgetEntity().toEntity(budget)).toDomain();
    }

    @Transactional
    public Budget update(final Budget entity) {
        return budgetJdbcRepository.update(new BudgetEntity().toEntity(entity)).toDomain();
    }

    public Pagination<Budget> search(final BudgetSearch domain) {

        return budgetJdbcRepository.search(domain);

    }

}