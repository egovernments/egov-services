package org.egov.egf.bill.web.controller;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.egf.bill.constants.Constants;
import org.egov.egf.bill.domain.model.Checklist;
import org.egov.egf.bill.domain.model.ChecklistSearch;
import org.egov.egf.bill.domain.model.Pagination;
import org.egov.egf.bill.domain.service.ChecklistService;
import org.egov.egf.bill.web.requests.ChecklistRequest;
import org.egov.egf.bill.web.requests.ChecklistResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ChecklistResponse create(@RequestBody ChecklistRequest checklistRequest) {

        checklistRequest.getRequestInfo().setAction(Constants.ACTION_CREATE);

        checklistRequest = checklistService.create(checklistRequest);

        return ChecklistResponse.builder().responseInfo(getResponseInfo(checklistRequest.getRequestInfo()))
                .checklists(checklistRequest.getChecklists()).build();
    }

    @PostMapping("/_update")
    @ResponseStatus(HttpStatus.CREATED)
    public ChecklistResponse update(@RequestBody ChecklistRequest checklistRequest) {

        checklistRequest.getRequestInfo().setAction(Constants.ACTION_UPDATE);

        checklistRequest = checklistService.update(checklistRequest);

        return ChecklistResponse.builder().responseInfo(getResponseInfo(checklistRequest.getRequestInfo()))
                .checklists(checklistRequest.getChecklists()).build();
    }

    @PostMapping("/_search")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ChecklistResponse search(@ModelAttribute final ChecklistSearch checklistSearch,
            final RequestInfo requestInfo) {

        final Pagination<Checklist> checklistList = checklistService.search(checklistSearch);

        return ChecklistResponse.builder().responseInfo(getResponseInfo(requestInfo))
                .checklists(checklistList.getPagedData()).page(new Pagination(checklistList))
                .build();
    }

    private ResponseInfo getResponseInfo(final RequestInfo requestInfo) {
        return ResponseInfo.builder().apiId(requestInfo.getApiId())
                .ver(requestInfo.getVer()).resMsgId(requestInfo.getMsgId())
                .resMsgId("placeholder").status("placeholder").build();
    }

}