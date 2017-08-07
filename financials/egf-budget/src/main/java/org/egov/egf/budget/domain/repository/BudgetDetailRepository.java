package org.egov.egf.budget.domain.repository;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.domain.model.Pagination;
import org.egov.egf.budget.domain.model.BudgetDetail;
import org.egov.egf.budget.domain.model.BudgetDetailSearch;
import org.egov.egf.budget.persistence.entity.BudgetDetailEntity;
import org.egov.egf.budget.persistence.queue.repository.BudgetDetailQueueRepository;
import org.egov.egf.budget.persistence.repository.BudgetDetailJdbcRepository;
import org.egov.egf.budget.web.contract.BudgetDetailRequest;
import org.egov.egf.budget.web.mapper.BudgetDetailMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BudgetDetailRepository {

    private final BudgetDetailJdbcRepository budgetDetailJdbcRepository;

    private final BudgetDetailQueueRepository budgetDetailQueueRepository;

    private final String persistThroughKafka;

    @Autowired
    public BudgetDetailRepository(final BudgetDetailJdbcRepository budgetDetailJdbcRepository,
            final BudgetDetailQueueRepository budgetDetailQueueRepository,
            @Value("${persist.through.kafka}") final String persistThroughKafka) {
        this.budgetDetailJdbcRepository = budgetDetailJdbcRepository;
        this.budgetDetailQueueRepository = budgetDetailQueueRepository;
        this.persistThroughKafka = persistThroughKafka;

    }

    public BudgetDetail findById(final BudgetDetail budgetDetail) {

        final BudgetDetailEntity entity = budgetDetailJdbcRepository
                .findById(new BudgetDetailEntity().toEntity(budgetDetail));

        if (entity != null)
            return entity.toDomain();

        return null;

    }

    @Transactional
    public List<BudgetDetail> save(final List<BudgetDetail> budgetDetails, final RequestInfo requestInfo) {

        final BudgetDetailMapper mapper = new BudgetDetailMapper();

        if (persistThroughKafka != null && !persistThroughKafka.isEmpty()
                && persistThroughKafka.equalsIgnoreCase("yes")) {

            final BudgetDetailRequest request = new BudgetDetailRequest();
            request.setRequestInfo(requestInfo);
            request.setBudgetDetails(new ArrayList<>());

            for (final BudgetDetail iac : budgetDetails)
                request.getBudgetDetails().add(mapper.toContract(iac));

            budgetDetailQueueRepository.addToQue(request);

            return budgetDetails;
        } else {

            final List<BudgetDetail> resultList = new ArrayList<BudgetDetail>();

            for (final BudgetDetail iac : budgetDetails)
                resultList.add(save(iac));

            final BudgetDetailRequest request = new BudgetDetailRequest();
            request.setRequestInfo(requestInfo);
            request.setBudgetDetails(new ArrayList<>());

            for (final BudgetDetail iac : resultList)
                request.getBudgetDetails().add(mapper.toContract(iac));

            budgetDetailQueueRepository.addToSearchQue(request);

            return resultList;
        }

    }

    @Transactional
    public List<BudgetDetail> update(final List<BudgetDetail> budgetDetails, final RequestInfo requestInfo) {

        final BudgetDetailMapper mapper = new BudgetDetailMapper();

        if (persistThroughKafka != null && !persistThroughKafka.isEmpty()
                && persistThroughKafka.equalsIgnoreCase("yes")) {

            final BudgetDetailRequest request = new BudgetDetailRequest();
            request.setRequestInfo(requestInfo);
            request.setBudgetDetails(new ArrayList<>());

            for (final BudgetDetail iac : budgetDetails)
                request.getBudgetDetails().add(mapper.toContract(iac));

            budgetDetailQueueRepository.addToQue(request);

            return budgetDetails;
        } else {

            final List<BudgetDetail> resultList = new ArrayList<BudgetDetail>();

            for (final BudgetDetail iac : budgetDetails)
                resultList.add(update(iac));

            final BudgetDetailRequest request = new BudgetDetailRequest();
            request.setRequestInfo(requestInfo);
            request.setBudgetDetails(new ArrayList<>());

            for (final BudgetDetail iac : resultList)
                request.getBudgetDetails().add(mapper.toContract(iac));

            budgetDetailQueueRepository.addToSearchQue(request);

            return resultList;
        }

    }

    @Transactional
    public BudgetDetail save(final BudgetDetail budgetDetail) {
        return budgetDetailJdbcRepository.create(new BudgetDetailEntity().toEntity(budgetDetail)).toDomain();
    }

    @Transactional
    public BudgetDetail update(final BudgetDetail entity) {
        return budgetDetailJdbcRepository.update(new BudgetDetailEntity().toEntity(entity)).toDomain();
    }

    public Pagination<BudgetDetail> search(final BudgetDetailSearch domain) {

        return budgetDetailJdbcRepository.search(domain);

    }

}