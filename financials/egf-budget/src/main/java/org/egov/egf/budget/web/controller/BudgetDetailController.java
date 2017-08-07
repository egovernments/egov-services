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