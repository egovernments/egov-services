/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *      accountability and the service delivery of the government  organizations.
 *  
 *       Copyright (C) <2015>  eGovernments Foundation
 *  
 *       The updated version of eGov suite of products as by eGovernments Foundation
 *       is available at http://www.egovernments.org
 *  
 *       This program is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published by
 *       the Free Software Foundation, either version 3 of the License, or
 *       any later version.
 *  
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU General Public License for more details.
 *  
 *       You should have received a copy of the GNU General Public License
 *       along with this program. If not, see http://www.gnu.org/licenses/ or
 *       http://www.gnu.org/licenses/gpl.html .
 *  
 *       In addition to the terms of the GPL license to be adhered to in using this
 *       program, the following additional terms are to be complied with:
 *  
 *           1) All versions of this program, verbatim or modified must carry this
 *              Legal Notice.
 *  
 *           2) Any misrepresentation of the origin of the material is prohibited. It
 *              is required that all modified versions of this material be marked in
 *              reasonable ways as different from the original version.
 *  
 *           3) This license does not grant any rights to any user of the program
 *              with regards to rights under trademark law for use of the trade names
 *              or trademarks of eGovernments Foundation.
 *  
 *     In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
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
import org.egov.egf.budget.web.contract.BudgetReAppropriationSearchContract;
import org.egov.egf.budget.web.mapper.BudgetReAppropriationMapper;
import org.egov.egf.master.web.repository.FinancialConfigurationContractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BudgetReAppropriationRepository {

    private final BudgetReAppropriationJdbcRepository budgetReAppropriationJdbcRepository;

    private final BudgetReAppropriationQueueRepository budgetReAppropriationQueueRepository;

    private final String persistThroughKafka;

    private FinancialConfigurationContractRepository financialConfigurationContractRepository;

    private BudgetReAppropriationESRepository budgetReAppropriationESRepository;


    @Autowired
    public BudgetReAppropriationRepository(final BudgetReAppropriationJdbcRepository budgetReAppropriationJdbcRepository,
                                           final BudgetReAppropriationQueueRepository budgetReAppropriationQueueRepository,
                                           @Value("${persist.through.kafka}") final String persistThroughKafka,
                                           FinancialConfigurationContractRepository financialConfigurationContractRepository,
                                           BudgetReAppropriationESRepository budgetReAppropriationESRepository
    ) {
        this.budgetReAppropriationJdbcRepository = budgetReAppropriationJdbcRepository;
        this.budgetReAppropriationQueueRepository = budgetReAppropriationQueueRepository;
        this.persistThroughKafka = persistThroughKafka;
        this.financialConfigurationContractRepository = financialConfigurationContractRepository;
        this.budgetReAppropriationESRepository = budgetReAppropriationESRepository;

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
    public List<BudgetReAppropriation> delete(final List<BudgetReAppropriation> budgetReAppropriations,
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
                resultList.add(delete(iac));

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
    
    @Transactional
    public BudgetReAppropriation delete(final BudgetReAppropriation entity) {
        return budgetReAppropriationJdbcRepository.delete(new BudgetReAppropriationEntity().toEntity(entity))
                .toDomain();
    }

    public Pagination<BudgetReAppropriation> search(final BudgetReAppropriationSearch domain) {

        if (financialConfigurationContractRepository.fetchDataFrom() != null
                && financialConfigurationContractRepository.fetchDataFrom().equalsIgnoreCase("es")) {

            BudgetReAppropriationMapper mapper = new BudgetReAppropriationMapper();
            BudgetReAppropriationSearchContract budgetReAppropriationSearchContract = new BudgetReAppropriationSearchContract();
            budgetReAppropriationSearchContract = mapper.toSearchContract(domain);
            Pagination<BudgetReAppropriation> budgetReAppropriations = budgetReAppropriationESRepository.search(budgetReAppropriationSearchContract);
            return budgetReAppropriations;
        } else {
            return budgetReAppropriationJdbcRepository.search(domain);
        }

    }

	public boolean uniqueCheck(String fieldName, BudgetReAppropriation budgetReAppropriation) {
		return budgetReAppropriationJdbcRepository.uniqueCheck(fieldName, new BudgetReAppropriationEntity().toEntity(budgetReAppropriation));
	}

}
