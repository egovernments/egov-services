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
import org.egov.egf.budget.domain.model.BudgetReAppropriation;
import org.egov.egf.budget.domain.model.BudgetReAppropriationSearch;
import org.egov.egf.budget.domain.service.BudgetReAppropriationService;
import org.egov.egf.budget.web.contract.BudgetReAppropriationContract;
import org.egov.egf.budget.web.contract.BudgetReAppropriationRequest;
import org.egov.egf.budget.web.contract.BudgetReAppropriationResponse;
import org.egov.egf.budget.web.contract.BudgetReAppropriationSearchContract;
import org.egov.egf.budget.web.mapper.BudgetReAppropriationMapper;
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
@RequestMapping("/budgetreappropriations")
public class BudgetReAppropriationController {

    public static final String ACTION_CREATE = "create";
    public static final String ACTION_UPDATE = "update";
    public static final String ACTION_DELETE = "delete";
    public static final String PLACEHOLDER = "placeholder";

    @Autowired
    private BudgetReAppropriationService budgetReAppropriationService;

    @PostMapping("/_create")
    @ResponseStatus(HttpStatus.CREATED)
    public BudgetReAppropriationResponse create(@RequestBody final BudgetReAppropriationRequest budgetReAppropriationRequest,
            final BindingResult errors) {

        final BudgetReAppropriationMapper mapper = new BudgetReAppropriationMapper();
        final BudgetReAppropriationResponse budgetReAppropriationResponse = new BudgetReAppropriationResponse();
        budgetReAppropriationResponse.setResponseInfo(getResponseInfo(budgetReAppropriationRequest.getRequestInfo()));
        List<BudgetReAppropriation> budgetreappropriations = new ArrayList<>();
        BudgetReAppropriation budgetReAppropriation = null;
        final List<BudgetReAppropriationContract> budgetReAppropriationContracts = new ArrayList<BudgetReAppropriationContract>();
        BudgetReAppropriationContract contract = null;

        budgetReAppropriationRequest.getRequestInfo().setAction(ACTION_CREATE);

        for (final BudgetReAppropriationContract budgetReAppropriationContract : budgetReAppropriationRequest
                .getBudgetReAppropriations()) {
            budgetReAppropriation = mapper.toDomain(budgetReAppropriationContract);
            budgetReAppropriation.setCreatedBy(budgetReAppropriationRequest.getRequestInfo().getUserInfo());
            budgetReAppropriation.setLastModifiedBy(budgetReAppropriationRequest.getRequestInfo().getUserInfo());
            budgetreappropriations.add(budgetReAppropriation);
        }

        budgetreappropriations = budgetReAppropriationService.create(budgetreappropriations, errors,
                budgetReAppropriationRequest.getRequestInfo());

        for (final BudgetReAppropriation bra : budgetreappropriations) {
            contract = mapper.toContract(bra);
            budgetReAppropriationContracts.add(contract);
        }

        budgetReAppropriationResponse.setBudgetReAppropriations(budgetReAppropriationContracts);

        return budgetReAppropriationResponse;
    }

    @PostMapping("/_update")
    @ResponseStatus(HttpStatus.CREATED)
    public BudgetReAppropriationResponse update(
            @RequestBody @Valid final BudgetReAppropriationRequest budgetReAppropriationRequest, final BindingResult errors) {

        final BudgetReAppropriationMapper mapper = new BudgetReAppropriationMapper();
        budgetReAppropriationRequest.getRequestInfo().setAction(ACTION_UPDATE);
        final BudgetReAppropriationResponse budgetReAppropriationResponse = new BudgetReAppropriationResponse();
        budgetReAppropriationResponse.setResponseInfo(getResponseInfo(budgetReAppropriationRequest.getRequestInfo()));
        List<BudgetReAppropriation> budgetreappropriations = new ArrayList<>();
        BudgetReAppropriation budgetReAppropriation = null;
        BudgetReAppropriationContract contract = null;
        final List<BudgetReAppropriationContract> budgetReAppropriationContracts = new ArrayList<BudgetReAppropriationContract>();

        for (final BudgetReAppropriationContract budgetReAppropriationContract : budgetReAppropriationRequest
                .getBudgetReAppropriations()) {
            budgetReAppropriation = mapper.toDomain(budgetReAppropriationContract);
            budgetReAppropriation.setLastModifiedBy(budgetReAppropriationRequest.getRequestInfo().getUserInfo());
            budgetreappropriations.add(budgetReAppropriation);
        }

        budgetreappropriations = budgetReAppropriationService.update(budgetreappropriations, errors,
                budgetReAppropriationRequest.getRequestInfo());

        for (final BudgetReAppropriation bra : budgetreappropriations) {
            contract = mapper.toContract(bra);
            budgetReAppropriationContracts.add(contract);
        }
        budgetReAppropriationResponse.setBudgetReAppropriations(budgetReAppropriationContracts);

        return budgetReAppropriationResponse;
    }
    
    @PostMapping("/_delete")
    @ResponseStatus(HttpStatus.CREATED)
    public BudgetReAppropriationResponse delete(
            @RequestBody @Valid final BudgetReAppropriationRequest budgetReAppropriationRequest, final BindingResult errors) {

        final BudgetReAppropriationMapper mapper = new BudgetReAppropriationMapper();
        budgetReAppropriationRequest.getRequestInfo().setAction(ACTION_DELETE);
        final BudgetReAppropriationResponse budgetReAppropriationResponse = new BudgetReAppropriationResponse();
        budgetReAppropriationResponse.setResponseInfo(getResponseInfo(budgetReAppropriationRequest.getRequestInfo()));
        List<BudgetReAppropriation> budgetreappropriations = new ArrayList<>();
        BudgetReAppropriation budgetReAppropriation = null;
        BudgetReAppropriationContract contract = null;
        final List<BudgetReAppropriationContract> budgetReAppropriationContracts = new ArrayList<BudgetReAppropriationContract>();

        for (final BudgetReAppropriationContract budgetReAppropriationContract : budgetReAppropriationRequest
                .getBudgetReAppropriations()) {
            budgetReAppropriation = mapper.toDomain(budgetReAppropriationContract);
            budgetreappropriations.add(budgetReAppropriation);
        }

        budgetreappropriations = budgetReAppropriationService.delete(budgetreappropriations, errors,
                budgetReAppropriationRequest.getRequestInfo());

        for (final BudgetReAppropriation bra : budgetreappropriations) {
            contract = mapper.toContract(bra);
            budgetReAppropriationContracts.add(contract);
        }
        budgetReAppropriationResponse.setBudgetReAppropriations(budgetReAppropriationContracts);

        return budgetReAppropriationResponse;
    }

    @PostMapping("/_search")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public BudgetReAppropriationResponse search(
            @ModelAttribute final BudgetReAppropriationSearchContract budgetReAppropriationSearchContract,
            @RequestBody final RequestInfo requestInfo, final BindingResult errors) {

        final BudgetReAppropriationMapper mapper = new BudgetReAppropriationMapper();
        final BudgetReAppropriationSearch domain = mapper.toSearchDomain(budgetReAppropriationSearchContract);
        BudgetReAppropriationContract contract = null;
        final List<BudgetReAppropriationContract> budgetReAppropriationContracts = new ArrayList<BudgetReAppropriationContract>();
        final Pagination<BudgetReAppropriation> budgetreappropriations = budgetReAppropriationService.search(domain);

        for (final BudgetReAppropriation budgetReAppropriation : budgetreappropriations.getPagedData()) {
            contract = mapper.toContract(budgetReAppropriation);
            budgetReAppropriationContracts.add(contract);
        }

        final BudgetReAppropriationResponse response = new BudgetReAppropriationResponse();
        response.setBudgetReAppropriations(budgetReAppropriationContracts);
        response.setPage(new PaginationContract(budgetreappropriations));
        response.setResponseInfo(getResponseInfo(requestInfo));

        return response;

    }

    private ResponseInfo getResponseInfo(final RequestInfo requestInfo) {
        return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer())
                .ts(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date())).resMsgId(requestInfo.getMsgId())
                .resMsgId(PLACEHOLDER).status(PLACEHOLDER).build();
    }

}
