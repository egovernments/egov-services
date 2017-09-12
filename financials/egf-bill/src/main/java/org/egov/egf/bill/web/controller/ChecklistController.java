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
import org.egov.egf.bill.domain.model.Checklist;
import org.egov.egf.bill.domain.model.ChecklistSearch;
import org.egov.egf.bill.domain.service.ChecklistService;
import org.egov.egf.bill.web.contract.ChecklistContract;
import org.egov.egf.bill.web.contract.ChecklistSearchContract;
import org.egov.egf.bill.web.requests.ChecklistRequest;
import org.egov.egf.bill.web.requests.ChecklistResponse;
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
@RequestMapping("/checklists")
public class ChecklistController {

    @Autowired
    private ChecklistService checklistService;

    @PostMapping("/_create")
    @ResponseStatus(HttpStatus.CREATED)
    public ChecklistResponse create(@RequestBody ChecklistRequest checklistRequest, BindingResult errors) {

    	if (errors.hasErrors()) {
            throw new CustomBindException(errors);
        }

        ModelMapper model = new ModelMapper();
        ChecklistResponse checklistResponse = new ChecklistResponse();
        checklistResponse.setResponseInfo(getResponseInfo(checklistRequest.getRequestInfo()));
        List<Checklist> checklists = new ArrayList<>();
        Checklist checklist;
        List<ChecklistContract> checklistContracts = new ArrayList<>();
        ChecklistContract contract;

        checklistRequest.getRequestInfo().setAction(Constants.ACTION_CREATE);

        for (ChecklistContract checklistContract : checklistRequest.getChecklists()) {
            checklist = new Checklist();
            model.map(checklistContract, checklist);
            checklist.setCreatedDate(new Date());
            checklist.setCreatedBy(checklistRequest.getRequestInfo().getUserInfo());
            checklist.setLastModifiedBy(checklistRequest.getRequestInfo().getUserInfo());
            checklists.add(checklist);
        }

        checklists = checklistService.create(checklists, errors, checklistRequest.getRequestInfo());

        for (Checklist f : checklists) {
            contract = new ChecklistContract();
            contract.setCreatedDate(new Date());
            model.map(f, contract);
            checklistContracts.add(contract);
        }

        checklistResponse.setChecklists(checklistContracts);

        return checklistResponse;
    }

    @PostMapping("/_update")
    @ResponseStatus(HttpStatus.CREATED)
    public ChecklistResponse update(@RequestBody ChecklistRequest checklistRequest, BindingResult errors) {

        if (errors.hasErrors()) {
            throw new CustomBindException(errors);
        }
        checklistRequest.getRequestInfo().setAction(Constants.ACTION_UPDATE);
        ModelMapper model = new ModelMapper();
        ChecklistResponse checklistResponse = new ChecklistResponse();
        List<Checklist> checklists = new ArrayList<>();
        checklistResponse.setResponseInfo(getResponseInfo(checklistRequest.getRequestInfo()));
        Checklist checklist;
        ChecklistContract contract;
        List<ChecklistContract> checklistContracts = new ArrayList<>();

        for (ChecklistContract checklistContract : checklistRequest.getChecklists()) {
            checklist = new Checklist();
            model.map(checklistContract, checklist);
            checklist.setLastModifiedBy(checklistRequest.getRequestInfo().getUserInfo());
            checklist.setLastModifiedDate(new Date());
            checklists.add(checklist);
        }

        checklists = checklistService.update(checklists, errors, checklistRequest.getRequestInfo());

        for (Checklist checklistObj : checklists) {
            contract = new ChecklistContract();
            model.map(checklistObj, contract);
            checklistObj.setLastModifiedDate(new Date());
            checklistContracts.add(contract);
        }

        checklistResponse.setChecklists(checklistContracts);

        return checklistResponse;
    }

    @PostMapping("/_search")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ChecklistResponse search(@ModelAttribute ChecklistSearchContract checklistSearchContract, RequestInfo requestInfo,
            BindingResult errors) {

        ModelMapper mapper = new ModelMapper();
        ChecklistSearch domain = new ChecklistSearch();
        mapper.map(checklistSearchContract, domain);
        ChecklistContract contract;
        ModelMapper model = new ModelMapper();
        List<ChecklistContract> checklistContracts = new ArrayList<>();
        Pagination<Checklist> checklists = checklistService.search(domain, errors);

        if (checklists.getPagedData() != null) {
            for (Checklist checklist : checklists.getPagedData()) {
                contract = new ChecklistContract();
                model.map(checklist, contract);
                checklistContracts.add(contract);
            }
        }

        ChecklistResponse response = new ChecklistResponse();
        response.setChecklists(checklistContracts);
        response.setPage(new PaginationContract(checklists));
        response.setResponseInfo(getResponseInfo(requestInfo));

        return response;

    }

    private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
        return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer())
                .resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
    }

}