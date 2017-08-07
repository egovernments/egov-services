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

    private final BudgetReAppropriationJdbcRepository budgetReAppropriationJdbcRepository;

    private final BudgetReAppropriationQueueRepository budgetReAppropriationQueueRepository;

    private final String persistThroughKafka;

    @Autowired
    public BudgetReAppropriationRepository(final BudgetReAppropriationJdbcRepository budgetReAppropriationJdbcRepository,
            final BudgetReAppropriationQueueRepository budgetReAppropriationQueueRepository,
            @Value("${persist.through.kafka}") final String persistThroughKafka) {
        this.budgetReAppropriationJdbcRepository = budgetReAppropriationJdbcRepository;
        this.budgetReAppropriationQueueRepository = budgetReAppropriationQueueRepository;
        this.persistThroughKafka = persistThroughKafka;

    }

    public BudgetReAppropriation findById(final BudgetReAppropriation budgetReAppropriation) {

        final BudgetReAppropriationEntity entity = budgetReAppropriationJdbcRepository
                .findById(new BudgetReAppropriationEntity().toEntity(budgetReAppropriation));

        if (entity != null)
            return entity.toDomain();

        return null;

    }

    @Transactional
    public List<BudgetReAppropriation> save(final List<BudgetReAppropriation> budgetReAppropriations,
            final RequestInfo requestInfo) {

        final BudgetReAppropriationMapper mapper = new BudgetReAppropriationMapper();

        if (persistThroughKafka != null && !persistThroughKafka.isEmpty()
                && persistThroughKafka.equalsIgnoreCase("yes")) {

            final BudgetReAppropriationRequest request = new BudgetReAppropriationRequest();
            request.setRequestInfo(requestInfo);
            request.setBudgetReAppropriations(new ArrayList<>());

            for (final BudgetReAppropriation iac : budgetReAppropriations)
                request.getBudgetReAppropriations().add(mapper.toContract(iac));

            budgetReAppropriationQueueRepository.addToQue(request);

            return budgetReAppropriations;
        } else {

            final List<BudgetReAppropriation> resultList = new ArrayList<BudgetReAppropriation>();

            for (final BudgetReAppropriation iac : budgetReAppropriations)
                resultList.add(save(iac));

            final BudgetReAppropriationRequest request = new BudgetReAppropriationRequest();
            request.setRequestInfo(requestInfo);
            request.setBudgetReAppropriations(new ArrayList<>());

            for (final BudgetReAppropriation iac : resultList)
                request.getBudgetReAppropriations().add(mapper.toContract(iac));

            budgetReAppropriationQueueRepository.addToSearchQue(request);

            return resultList;
        }

    }

    @Transactional
    public List<BudgetReAppropriation> update(final List<BudgetReAppropriation> budgetReAppropriations,
            final RequestInfo requestInfo) {

        final BudgetReAppropriationMapper mapper = new BudgetReAppropriationMapper();

        if (persistThroughKafka != null && !persistThroughKafka.isEmpty()
                && persistThroughKafka.equalsIgnoreCase("yes")) {

            final BudgetReAppropriationRequest request = new BudgetReAppropriationRequest();
            request.setRequestInfo(requestInfo);
            request.setBudgetReAppropriations(new ArrayList<>());

            for (final BudgetReAppropriation iac : budgetReAppropriations)
                request.getBudgetReAppropriations().add(mapper.toContract(iac));

            budgetReAppropriationQueueRepository.addToQue(request);

            return budgetReAppropriations;
        } else {

            final List<BudgetReAppropriation> resultList = new ArrayList<BudgetReAppropriation>();

            for (final BudgetReAppropriation iac : budgetReAppropriations)
                resultList.add(update(iac));

            final BudgetReAppropriationRequest request = new BudgetReAppropriationRequest();
            request.setRequestInfo(requestInfo);
            request.setBudgetReAppropriations(new ArrayList<>());

            for (final BudgetReAppropriation iac : resultList)
                request.getBudgetReAppropriations().add(mapper.toContract(iac));

            budgetReAppropriationQueueRepository.addToSearchQue(request);

            return resultList;
        }

    }

    @Transactional
    public BudgetReAppropriation save(final BudgetReAppropriation budgetReAppropriation) {
        return budgetReAppropriationJdbcRepository
                .create(new BudgetReAppropriationEntity().toEntity(budgetReAppropriation)).toDomain();
    }

    @Transactional
    public BudgetReAppropriation update(final BudgetReAppropriation entity) {
        return budgetReAppropriationJdbcRepository.update(new BudgetReAppropriationEntity().toEntity(entity))
                .toDomain();
    }

    public Pagination<BudgetReAppropriation> search(final BudgetReAppropriationSearch domain) {

        return budgetReAppropriationJdbcRepository.search(domain);

    }

}