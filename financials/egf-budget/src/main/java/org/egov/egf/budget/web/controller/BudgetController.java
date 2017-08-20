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
package org.egov.egf.budget.web.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.common.domain.model.Pagination;
import org.egov.common.web.contract.PaginationContract;
import org.egov.egf.budget.domain.model.Budget;
import org.egov.egf.budget.domain.model.BudgetSearch;
import org.egov.egf.budget.domain.service.BudgetService;
import org.egov.egf.budget.web.contract.BudgetContract;
import org.egov.egf.budget.web.contract.BudgetRequest;
import org.egov.egf.budget.web.contract.BudgetResponse;
import org.egov.egf.budget.web.contract.BudgetSearchContract;
import org.egov.egf.budget.web.mapper.BudgetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/budgets")
public class BudgetController {

    public static final String ACTION_CREATE = "create";
    public static final String ACTION_UPDATE = "update";
    public static final String ACTION_DELETE = "delete";
    public static final String PLACEHOLDER = "placeholder";

    @Autowired
    private BudgetService budgetService;

    @PostMapping("/_create")
    @ResponseStatus(HttpStatus.CREATED)
    public BudgetResponse create(@RequestBody final BudgetRequest budgetRequest, final BindingResult errors) {

        final BudgetMapper mapper = new BudgetMapper();
        final BudgetResponse budgetResponse = new BudgetResponse();
        budgetResponse.setResponseInfo(getResponseInfo(budgetRequest.getRequestInfo()));
        List<Budget> budgets = new ArrayList<>();
        Budget budget = null;
        final List<BudgetContract> budgetContracts = new ArrayList<BudgetContract>();
        BudgetContract contract = null;

        budgetRequest.getRequestInfo().setAction(ACTION_CREATE);

        for (final BudgetContract budgetContract : budgetRequest.getBudgets()) {
            budget = mapper.toDomain(budgetContract);
            budget.setCreatedBy(budgetRequest.getRequestInfo().getUserInfo());
            budget.setLastModifiedBy(budgetRequest.getRequestInfo().getUserInfo());
            budgets.add(budget);
        }

        budgets = budgetService.create(budgets, errors, budgetRequest.getRequestInfo());

        for (final Budget b : budgets) {
            contract = mapper.toContract(b);
            budgetContracts.add(contract);
        }

        budgetResponse.setBudgets(budgetContracts);

        return budgetResponse;
    }

    @PostMapping("/_update")
    @ResponseStatus(HttpStatus.CREATED)
    public BudgetResponse update(@RequestBody @Valid final BudgetRequest budgetRequest, final BindingResult errors) {

        final BudgetMapper mapper = new BudgetMapper();
        final BudgetResponse budgetResponse = new BudgetResponse();
        budgetResponse.setResponseInfo(getResponseInfo(budgetRequest.getRequestInfo()));
        List<Budget> budgets = new ArrayList<>();
        Budget budget = null;
        BudgetContract contract = null;
        final List<BudgetContract> budgetContracts = new ArrayList<BudgetContract>();

        budgetRequest.getRequestInfo().setAction(ACTION_UPDATE);

        for (final BudgetContract budgetContract : budgetRequest.getBudgets()) {
            budget = mapper.toDomain(budgetContract);
            budget.setLastModifiedBy(budgetRequest.getRequestInfo().getUserInfo());
            budgets.add(budget);
        }

        budgets = budgetService.update(budgets, errors, budgetRequest.getRequestInfo());

        for (final Budget b : budgets) {
            contract = mapper.toContract(b);
            budgetContracts.add(contract);
        }

        budgetResponse.setBudgets(budgetContracts);

        return budgetResponse;
    }
    
    @PostMapping("/_delete")
    @ResponseStatus(HttpStatus.CREATED)
    public BudgetResponse delete(@RequestBody @Valid final BudgetRequest budgetRequest, final BindingResult errors) {

        final BudgetMapper mapper = new BudgetMapper();
        final BudgetResponse budgetResponse = new BudgetResponse();
        budgetResponse.setResponseInfo(getResponseInfo(budgetRequest.getRequestInfo()));
        List<Budget> budgets = new ArrayList<>();
        Budget budget = null;
        BudgetContract contract = null;
        final List<BudgetContract> budgetContracts = new ArrayList<BudgetContract>();

        budgetRequest.getRequestInfo().setAction(ACTION_DELETE);

        for (final BudgetContract budgetContract : budgetRequest.getBudgets()) {
            budget = mapper.toDomain(budgetContract);
            budgets.add(budget);
        }

        budgets = budgetService.delete(budgets, errors, budgetRequest.getRequestInfo());

        for (final Budget b : budgets) {
            contract = mapper.toContract(b);
            budgetContracts.add(contract);
        }

        budgetResponse.setBudgets(budgetContracts);

        return budgetResponse;
    }

    @PostMapping("/_search")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public BudgetResponse search(@ModelAttribute final BudgetSearchContract budgetSearchContract,
            @RequestBody final RequestInfo requestInfo, final BindingResult errors) {

        final BudgetMapper mapper = new BudgetMapper();
        final BudgetSearch domain = mapper.toSearchDomain(budgetSearchContract);
        BudgetContract contract = null;
        final List<BudgetContract> budgetContracts = new ArrayList<BudgetContract>();
        final Pagination<Budget> budgets = budgetService.search(domain);

        for (final Budget budget : budgets.getPagedData()) {
            contract = mapper.toContract(budget);
            budgetContracts.add(contract);
        }

        final BudgetResponse response = new BudgetResponse();
        response.setBudgets(budgetContracts);
        response.setPage(new PaginationContract(budgets));
        response.setResponseInfo(getResponseInfo(requestInfo));

        return response;

    }

    private ResponseInfo getResponseInfo(final RequestInfo requestInfo) {
        return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer())
                .ts(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date())).resMsgId(requestInfo.getMsgId())
                .resMsgId(PLACEHOLDER).status(PLACEHOLDER).build();
    }

}
