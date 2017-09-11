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
import org.egov.egf.budget.domain.model.BudgetDetail;
import org.egov.egf.budget.domain.model.BudgetDetailSearch;
import org.egov.egf.budget.persistence.entity.BudgetDetailEntity;
import org.egov.egf.budget.persistence.queue.repository.BudgetDetailQueueRepository;
import org.egov.egf.budget.persistence.repository.BudgetDetailJdbcRepository;
import org.egov.egf.budget.web.contract.BudgetDetailRequest;
import org.egov.egf.budget.web.contract.BudgetDetailSearchContract;
import org.egov.egf.budget.web.mapper.BudgetDetailMapper;
import org.egov.egf.master.web.repository.FinancialConfigurationContractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BudgetDetailRepository {

    private final BudgetDetailJdbcRepository budgetDetailJdbcRepository;

    private final BudgetDetailQueueRepository budgetDetailQueueRepository;

    private final String persistThroughKafka;

    private FinancialConfigurationContractRepository financialConfigurationContractRepository;

    private BudgetDetailESRepository budgetDetailESRepository;

    @Autowired
    public BudgetDetailRepository(final BudgetDetailJdbcRepository budgetDetailJdbcRepository,
                                  final BudgetDetailQueueRepository budgetDetailQueueRepository,
                                  @Value("${persist.through.kafka}") final String persistThroughKafka,
                                  FinancialConfigurationContractRepository financialConfigurationContractRepository,
                                  BudgetDetailESRepository budgetDetailESRepository
    ) {
        this.budgetDetailJdbcRepository = budgetDetailJdbcRepository;
        this.budgetDetailQueueRepository = budgetDetailQueueRepository;
        this.persistThroughKafka = persistThroughKafka;
        this.financialConfigurationContractRepository = financialConfigurationContractRepository;
        this.budgetDetailESRepository = budgetDetailESRepository;

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
    public List<BudgetDetail> delete(final List<BudgetDetail> budgetDetails, final RequestInfo requestInfo) {

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
                resultList.add(delete(iac));

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
    
    @Transactional
    public BudgetDetail delete(final BudgetDetail entity) {
        return budgetDetailJdbcRepository.delete(new BudgetDetailEntity().toEntity(entity)).toDomain();
    }

    public Pagination<BudgetDetail> search(final BudgetDetailSearch domain) {

        if (financialConfigurationContractRepository.fetchDataFrom() != null
                && financialConfigurationContractRepository.fetchDataFrom().equalsIgnoreCase("es")) {

            BudgetDetailMapper mapper = new BudgetDetailMapper();
            BudgetDetailSearchContract budgetDetailSearchContract = new BudgetDetailSearchContract();
            budgetDetailSearchContract = mapper.toSearchContract(domain);
            Pagination<BudgetDetail> budgetDetails = budgetDetailESRepository.search(budgetDetailSearchContract);
            return budgetDetails;
        } else {

            return budgetDetailJdbcRepository.search(domain);
        }

    }

	public boolean uniqueCheck(String fieldName, BudgetDetail budgetDetail) {
		return budgetDetailJdbcRepository.uniqueCheck(fieldName, new BudgetDetailEntity().toEntity(budgetDetail));
	}

}
