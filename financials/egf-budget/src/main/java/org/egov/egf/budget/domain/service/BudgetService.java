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
package org.egov.egf.budget.domain.service;

import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.domain.exception.CustomBindException;
import org.egov.common.domain.exception.InvalidDataException;
import org.egov.common.domain.model.Pagination;
import org.egov.egf.budget.domain.model.Budget;
import org.egov.egf.budget.domain.model.BudgetSearch;
import org.egov.egf.budget.domain.repository.BudgetRepository;
import org.egov.egf.master.web.contract.FinancialYearContract;
import org.egov.egf.master.web.contract.FinancialYearSearchContract;
import org.egov.egf.master.web.repository.FinancialYearContractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.SmartValidator;

@Service
@Transactional(readOnly = true)
public class BudgetService {

    public static final String ACTION_CREATE = "create";
    public static final String ACTION_UPDATE = "update";
    public static final String ACTION_VIEW = "view";
    public static final String ACTION_EDIT = "edit";
    public static final String ACTION_SEARCH = "search";

    private final SmartValidator validator;

    private final BudgetRepository budgetRepository;

    private final FinancialYearContractRepository financialYearContractRepository;

    @Autowired
    public BudgetService(final SmartValidator validator, final BudgetRepository budgetRepository,
            final FinancialYearContractRepository financialYearContractRepository) {
        this.validator = validator;
        this.budgetRepository = budgetRepository;
        this.financialYearContractRepository = financialYearContractRepository;
    }

    @Transactional
    public List<Budget> create(List<Budget> budgets, final BindingResult errors,
            final RequestInfo requestInfo) {

        try {

            budgets = fetchRelated(budgets);

            validate(budgets, ACTION_CREATE, errors);

            if (errors.hasErrors())
                throw new CustomBindException(errors);

        } catch (final CustomBindException e) {

            throw new CustomBindException(errors);
        }

        return budgetRepository.save(budgets, requestInfo);

    }

    @Transactional
    public List<Budget> update(List<Budget> budgets, final BindingResult errors,
            final RequestInfo requestInfo) {

        try {

            budgets = fetchRelated(budgets);

            validate(budgets, ACTION_UPDATE, errors);

            if (errors.hasErrors())
                throw new CustomBindException(errors);

        } catch (final CustomBindException e) {

            throw new CustomBindException(errors);
        }

        return budgetRepository.update(budgets, requestInfo);

    }

    public BindingResult validate(final List<Budget> budgets, final String method, final BindingResult errors) {

        try {
            switch (method) {
            case ACTION_VIEW:
                // validator.validate(budgetContractRequest.getBudget(),
                // errors);
                break;
            case ACTION_CREATE:
                Assert.notNull(budgets, "Budgets to create must not be null");
                for (final Budget budget : budgets)
                    validator.validate(budget, errors);
                break;
            case ACTION_UPDATE:
                Assert.notNull(budgets, "Budgets to update must not be null");
                for (final Budget budget : budgets){
                	Assert.notNull(budget.getId(), "Budget ID to update must not be null");
                	validator.validate(budget, errors);
                }
                break;
            default:

            }
        } catch (final IllegalArgumentException e) {
            errors.addError(new ObjectError("Missing data", e.getMessage()));
        }
        return errors;

    }

    public List<Budget> fetchRelated(final List<Budget> budgets) {
        if (budgets != null)
            for (final Budget budget : budgets) {
                // fetch related items

                if (budget.getFinancialYear() != null && budget.getFinancialYear().getId() != null
                        && budget.getFinancialYear().getTenantId() != null) {
                    final FinancialYearSearchContract contract = new FinancialYearSearchContract();
                    contract.setId(budget.getFinancialYear().getId());
                    contract.setTenantId(budget.getFinancialYear().getTenantId());
                    final FinancialYearContract financialYear = financialYearContractRepository.findById(contract);
                    if (financialYear == null)
                        throw new InvalidDataException("financialYear", "financialYear.invalid",
                                " Invalid financialYear");
                    budget.setFinancialYear(financialYear);
                }

                if (budget.getParent() != null && budget.getParent().getId() != null
                        && !budget.getParent().getId().isEmpty()) {
                    final Budget parent = budgetRepository.findById(budget.getParent());
                    if (parent == null)
                        throw new InvalidDataException("parent", "parent.invalid", " Invalid parent");
                    budget.setParent(parent);
                }
                if (budget.getReferenceBudget() != null && budget.getReferenceBudget().getId() != null
                        && !budget.getReferenceBudget().getId().isEmpty()) {
                    final Budget referenceBudget = budgetRepository.findById(budget.getReferenceBudget());
                    if (referenceBudget == null)
                        throw new InvalidDataException("referenceBudget", "referenceBudget.invalid",
                                " Invalid referenceBudget");
                    budget.setReferenceBudget(referenceBudget);
                }
            }

        return budgets;
    }

    public Pagination<Budget> search(final BudgetSearch budgetSearch) {
        return budgetRepository.search(budgetSearch);
    }

    @Transactional
    public Budget save(final Budget budget) {
        return budgetRepository.save(budget);
    }

    @Transactional
    public Budget update(final Budget budget) {
        return budgetRepository.update(budget);
    }

}
