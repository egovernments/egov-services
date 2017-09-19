package org.egov.egf.bill.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.common.constants.Constants;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.common.domain.exception.CustomBindException;
import org.egov.common.domain.model.Pagination;
import org.egov.common.web.contract.PaginationContract;
import org.egov.egf.bill.domain.model.BillChecklist;
import org.egov.egf.bill.domain.model.BillChecklistSearch;
import org.egov.egf.bill.domain.service.BillChecklistService;
import org.egov.egf.bill.web.contract.BillChecklistContract;
import org.egov.egf.bill.web.contract.BillChecklistSearchContract;
import org.egov.egf.bill.web.requests.BillChecklistRequest;
import org.egov.egf.bill.web.requests.BillChecklistResponse;
import org.modelmapper.ModelMapper;
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
@RequestMapping("/billchecklists")
public class BillChecklistController {

    @Autowired
    private BillChecklistService billChecklistService;

    @PostMapping("/_create")
    @ResponseStatus(HttpStatus.CREATED)
    public BillChecklistResponse create(@RequestBody BillChecklistRequest billChecklistRequest, BindingResult errors) {

    	if (errors.hasErrors()) {
            throw new CustomBindException(errors);
        }

        ModelMapper model = new ModelMapper();
        BillChecklistResponse billChecklistResponse = new BillChecklistResponse();
        billChecklistResponse.setResponseInfo(getResponseInfo(billChecklistRequest.getRequestInfo()));
        List<BillChecklist> billChecklists = new ArrayList<>();
        BillChecklist billChecklist;
        List<BillChecklistContract> billChecklistContracts = new ArrayList<>();
        BillChecklistContract contract;

        billChecklistRequest.getRequestInfo().setAction(Constants.ACTION_CREATE);

        for (BillChecklistContract billChecklistContract : billChecklistRequest.getBillChecklists()) {
            billChecklist = new BillChecklist();
            model.map(billChecklistContract, billChecklist);
            billChecklist.setCreatedDate(new Date());
            billChecklist.setCreatedBy(billChecklistRequest.getRequestInfo().getUserInfo());
            billChecklist.setLastModifiedBy(billChecklistRequest.getRequestInfo().getUserInfo());
            billChecklists.add(billChecklist);
        }

        billChecklists = billChecklistService.create(billChecklists, errors, billChecklistRequest.getRequestInfo());

        for (BillChecklist f : billChecklists) {
            contract = new BillChecklistContract();
            contract.setCreatedDate(new Date());
            model.map(f, contract);
            billChecklistContracts.add(contract);
        }

        billChecklistResponse.setBillChecklists(billChecklistContracts);

        return billChecklistResponse;
    }

    @PostMapping("/_update")
    @ResponseStatus(HttpStatus.CREATED)
    public BillChecklistResponse update(@RequestBody BillChecklistRequest billChecklistRequest, BindingResult errors) {

        if (errors.hasErrors()) {
            throw new CustomBindException(errors);
        }
        billChecklistRequest.getRequestInfo().setAction(Constants.ACTION_UPDATE);
        ModelMapper model = new ModelMapper();
        BillChecklistResponse billChecklistResponse = new BillChecklistResponse();
        List<BillChecklist> billChecklists = new ArrayList<>();
        billChecklistResponse.setResponseInfo(getResponseInfo(billChecklistRequest.getRequestInfo()));
        BillChecklist billChecklist;
        BillChecklistContract contract;
        List<BillChecklistContract> billChecklistContracts = new ArrayList<>();

        for (BillChecklistContract billChecklistContract : billChecklistRequest.getBillChecklists()) {
            billChecklist = new BillChecklist();
            model.map(billChecklistContract, billChecklist);
            billChecklist.setLastModifiedBy(billChecklistRequest.getRequestInfo().getUserInfo());
            billChecklist.setLastModifiedDate(new Date());
            billChecklists.add(billChecklist);
        }

        billChecklists = billChecklistService.update(billChecklists, errors, billChecklistRequest.getRequestInfo());

        for (BillChecklist billChecklistObj : billChecklists) {
            contract = new BillChecklistContract();
            model.map(billChecklistObj, contract);
            billChecklistObj.setLastModifiedDate(new Date());
            billChecklistContracts.add(contract);
        }

        billChecklistResponse.setBillChecklists(billChecklistContracts);

        return billChecklistResponse;
    }
    
    @PostMapping("/_search")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public BillChecklistResponse search(@ModelAttribute BillChecklistSearchContract billChecklistSearchContract, RequestInfo requestInfo,
            BindingResult errors) {

        ModelMapper mapper = new ModelMapper();
        BillChecklistSearch domain = new BillChecklistSearch();
        mapper.map(billChecklistSearchContract, domain);
        BillChecklistContract contract;
        ModelMapper model = new ModelMapper();
        List<BillChecklistContract> billBillChecklistContracts = new ArrayList<>();
        Pagination<BillChecklist> billBillChecklists = billChecklistService.search(domain, errors);

        if (billBillChecklists.getPagedData() != null) {
            for (BillChecklist billBillChecklist : billBillChecklists.getPagedData()) {
                contract = new BillChecklistContract();
                model.map(billBillChecklist, contract);
                billBillChecklistContracts.add(contract);
            }
        }

        BillChecklistResponse response = new BillChecklistResponse();
        response.setBillChecklists(billBillChecklistContracts);
        response.setPage(new PaginationContract(billBillChecklists));
        response.setResponseInfo(getResponseInfo(requestInfo));

        return response;

    }

    private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
        return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer())
                .resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
    }

}