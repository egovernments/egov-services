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
import org.egov.egf.budget.domain.model.BudgetDetail;
import org.egov.egf.budget.domain.model.BudgetDetailSearch;
import org.egov.egf.budget.domain.service.BudgetDetailService;
import org.egov.egf.budget.web.contract.BudgetDetailContract;
import org.egov.egf.budget.web.contract.BudgetDetailRequest;
import org.egov.egf.budget.web.contract.BudgetDetailResponse;
import org.egov.egf.budget.web.contract.BudgetDetailSearchContract;
import org.egov.egf.budget.web.mapper.BudgetDetailMapper;
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
@RequestMapping("/budgetdetails")
public class BudgetDetailController {

    public static final String ACTION_CREATE = "create";
    public static final String ACTION_UPDATE = "update";
    public static final String ACTION_DELETE = "delete";
    public static final String PLACEHOLDER = "placeholder";

    @Autowired
    private BudgetDetailService budgetDetailService;

    @PostMapping("/_create")
    @ResponseStatus(HttpStatus.CREATED)
    public BudgetDetailResponse create(@RequestBody final BudgetDetailRequest budgetDetailRequest, final BindingResult errors) {

        final BudgetDetailMapper mapper = new BudgetDetailMapper();
        final BudgetDetailResponse budgetDetailResponse = new BudgetDetailResponse();
        budgetDetailResponse.setResponseInfo(getResponseInfo(budgetDetailRequest.getRequestInfo()));
        List<BudgetDetail> budgetdetails = new ArrayList<>();
        BudgetDetail budgetDetail = null;
        final List<BudgetDetailContract> budgetDetailContracts = new ArrayList<BudgetDetailContract>();
        BudgetDetailContract contract = null;

        budgetDetailRequest.getRequestInfo().setAction(ACTION_CREATE);

        for (final BudgetDetailContract budgetDetailContract : budgetDetailRequest.getBudgetDetails()) {
            budgetDetail = mapper.toDomain(budgetDetailContract);
            budgetDetail.setCreatedBy(budgetDetailRequest.getRequestInfo().getUserInfo());
            budgetDetail.setLastModifiedBy(budgetDetailRequest.getRequestInfo().getUserInfo());
            budgetdetails.add(budgetDetail);
        }

        budgetdetails = budgetDetailService.create(budgetdetails, errors, budgetDetailRequest.getRequestInfo());

        for (final BudgetDetail bd : budgetdetails) {
            contract = mapper.toContract(bd);
            budgetDetailContracts.add(contract);
        }

        budgetDetailResponse.setBudgetDetails(budgetDetailContracts);

        return budgetDetailResponse;
    }

    @PostMapping("/_update")
    @ResponseStatus(HttpStatus.CREATED)
    public BudgetDetailResponse update(@RequestBody @Valid final BudgetDetailRequest budgetDetailRequest,
            final BindingResult errors) {

        final BudgetDetailMapper mapper = new BudgetDetailMapper();
        budgetDetailRequest.getRequestInfo().setAction(ACTION_UPDATE);
        final BudgetDetailResponse budgetDetailResponse = new BudgetDetailResponse();
        budgetDetailResponse.setResponseInfo(getResponseInfo(budgetDetailRequest.getRequestInfo()));
        List<BudgetDetail> budgetdetails = new ArrayList<>();
        BudgetDetail budgetDetail = null;
        BudgetDetailContract contract = null;
        final List<BudgetDetailContract> budgetDetailContracts = new ArrayList<BudgetDetailContract>();

        for (final BudgetDetailContract budgetDetailContract : budgetDetailRequest.getBudgetDetails()) {
            budgetDetail = mapper.toDomain(budgetDetailContract);
            budgetDetail.setCreatedBy(budgetDetailRequest.getRequestInfo().getUserInfo());
            budgetDetail.setLastModifiedBy(budgetDetailRequest.getRequestInfo().getUserInfo());
            budgetdetails.add(budgetDetail);
        }

        budgetdetails = budgetDetailService.update(budgetdetails, errors, budgetDetailRequest.getRequestInfo());

        for (final BudgetDetail bd : budgetdetails) {
            contract = mapper.toContract(bd);
            budgetDetailContracts.add(contract);
        }

        budgetDetailResponse.setBudgetDetails(budgetDetailContracts);

        return budgetDetailResponse;
    }
    
    @PostMapping("/_delete")
    @ResponseStatus(HttpStatus.CREATED)
    public BudgetDetailResponse delete(@RequestBody @Valid final BudgetDetailRequest budgetDetailRequest,
            final BindingResult errors) {

        final BudgetDetailMapper mapper = new BudgetDetailMapper();
        budgetDetailRequest.getRequestInfo().setAction(ACTION_DELETE);
        final BudgetDetailResponse budgetDetailResponse = new BudgetDetailResponse();
        budgetDetailResponse.setResponseInfo(getResponseInfo(budgetDetailRequest.getRequestInfo()));
        List<BudgetDetail> budgetdetails = new ArrayList<>();
        BudgetDetail budgetDetail = null;
        BudgetDetailContract contract = null;
        final List<BudgetDetailContract> budgetDetailContracts = new ArrayList<BudgetDetailContract>();

        for (final BudgetDetailContract budgetDetailContract : budgetDetailRequest.getBudgetDetails()) {
            budgetDetail = mapper.toDomain(budgetDetailContract);
            budgetDetail.setCreatedBy(budgetDetailRequest.getRequestInfo().getUserInfo());
            budgetDetail.setLastModifiedBy(budgetDetailRequest.getRequestInfo().getUserInfo());
            budgetdetails.add(budgetDetail);
        }

        budgetdetails = budgetDetailService.delete(budgetdetails, errors, budgetDetailRequest.getRequestInfo());

        for (final BudgetDetail bd : budgetdetails) {
            contract = mapper.toContract(bd);
            budgetDetailContracts.add(contract);
        }

        budgetDetailResponse.setBudgetDetails(budgetDetailContracts);

        return budgetDetailResponse;
    }

    @PostMapping("/_search")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public BudgetDetailResponse search(@ModelAttribute final BudgetDetailSearchContract budgetDetailSearchContract,
            @RequestBody final RequestInfo requestInfo, final BindingResult errors) {

        final BudgetDetailMapper mapper = new BudgetDetailMapper();
        final BudgetDetailSearch domain = mapper.toSearchDomain(budgetDetailSearchContract);
        BudgetDetailContract contract = null;
        final List<BudgetDetailContract> budgetDetailContracts = new ArrayList<BudgetDetailContract>();

        final Pagination<BudgetDetail> budgetdetails = budgetDetailService.search(domain);

        for (final BudgetDetail budgetDetail : budgetdetails.getPagedData()) {
            contract = mapper.toContract(budgetDetail);
            budgetDetailContracts.add(contract);
        }

        final BudgetDetailResponse response = new BudgetDetailResponse();
        response.setBudgetDetails(budgetDetailContracts);
        response.setPage(new PaginationContract(budgetdetails));
        response.setResponseInfo(getResponseInfo(requestInfo));

        return response;

    }

    private ResponseInfo getResponseInfo(final RequestInfo requestInfo) {
        return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer())
                .ts(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date())).resMsgId(requestInfo.getMsgId())
                .resMsgId(PLACEHOLDER).status(PLACEHOLDER).build();
    }

}
