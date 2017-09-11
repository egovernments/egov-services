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

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.domain.model.Pagination;
import org.egov.egf.budget.domain.model.Budget;
import org.egov.egf.budget.domain.model.BudgetSearch;
import org.egov.egf.budget.persistence.entity.BudgetEntity;
import org.egov.egf.budget.persistence.queue.repository.BudgetQueueRepository;
import org.egov.egf.budget.persistence.repository.BudgetJdbcRepository;
import org.egov.egf.budget.web.contract.BudgetRequest;
import org.egov.egf.budget.web.contract.BudgetSearchContract;
import org.egov.egf.budget.web.mapper.BudgetMapper;
import org.egov.egf.master.web.repository.FinancialConfigurationContractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class BudgetRepository {

    private final BudgetJdbcRepository budgetJdbcRepository;

    private final BudgetQueueRepository budgetQueueRepository;

    private final String persistThroughKafka;

    private FinancialConfigurationContractRepository financialConfigurationContractRepository;

    private BudgetESRepository budgetESRepository;


    @Autowired
    public BudgetRepository(BudgetJdbcRepository budgetJdbcRepository, BudgetQueueRepository budgetQueueRepository,
                            @Value("${persist.through.kafka}") String persistThroughKafka,FinancialConfigurationContractRepository financialConfigurationContractRepository,
                            BudgetESRepository budgetESRepository) {
        this.budgetJdbcRepository = budgetJdbcRepository;
        this.budgetQueueRepository = budgetQueueRepository;
        this.persistThroughKafka = persistThroughKafka;
        this.financialConfigurationContractRepository = financialConfigurationContractRepository;
        this.budgetESRepository = budgetESRepository;

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
    public List<Budget> delete(final List<Budget> budgets, final RequestInfo requestInfo) {

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
                resultList.add(delete(iac));

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
    
    @Transactional
    public Budget delete(final Budget entity) {
        return budgetJdbcRepository.delete(new BudgetEntity().toEntity(entity)).toDomain();
    }

    public Pagination<Budget> search(final BudgetSearch domain) {
        if (financialConfigurationContractRepository.fetchDataFrom() != null
                && financialConfigurationContractRepository.fetchDataFrom().equalsIgnoreCase("es")) {

            BudgetMapper mapper = new BudgetMapper();
            BudgetSearchContract budgetSearchContract = new BudgetSearchContract();
            budgetSearchContract = mapper.toSearchContract(domain);
            Pagination<Budget> budgets = budgetESRepository.search(budgetSearchContract);
            return budgets;
        } else {

            return budgetJdbcRepository.search(domain);
        }

    }

	public boolean uniqueCheck(String fieldName, Budget budget) {
        return budgetJdbcRepository.uniqueCheck(fieldName, new BudgetEntity().toEntity(budget));

	}

}
