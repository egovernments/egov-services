package org.egov.inv.web.controller;

import java.util.List;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.inv.domain.model.Pagination;
import org.egov.inv.domain.model.Store;
import org.egov.inv.domain.service.StoreService;
import org.egov.inv.web.contract.StoreGetRequest;
import org.egov.inv.web.contract.StoreRequest;
import org.egov.inv.web.contract.StoreResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stores")
public class StoreController {

    @Autowired
    private StoreService storeService;

    @PostMapping("/_create")
    @ResponseStatus(HttpStatus.CREATED)
    public StoreResponse create(@RequestBody @Valid StoreRequest storeRequest,
            @RequestParam(value = "tenantId") String tenantId) {
        StoreResponse storeResponse = new StoreResponse();
        storeResponse.setResponseInfo(getResponseInfo(storeRequest.getRequestInfo()));
        List<Store> stores = storeService.create(storeRequest, tenantId);
        storeResponse.setStores(stores);
        return storeResponse;
    }

    @PostMapping("/_update")
    @ResponseStatus(HttpStatus.CREATED)
    public StoreResponse update(@RequestBody @Valid final StoreRequest storeRequest,
            @RequestParam(value = "tenantId") String tenantId) {
        StoreResponse storeResponse = new StoreResponse();
        storeResponse.setResponseInfo(getResponseInfo(storeRequest.getRequestInfo()));
        List<Store> stores = storeService.update(storeRequest, tenantId);
        storeResponse.setStores(stores);
        return storeResponse;
    }

    @PostMapping("/_search")
    @ResponseStatus(HttpStatus.OK)
    public StoreResponse search(@RequestParam(value = "tenantId") String tenantId,
            @RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "department", required = false) String department,
            @RequestParam(value = "contactNo1", required = false) String contactNo1,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "isCentralStore", required = false) Boolean isCentralStore,
            @RequestParam(value = "storeInCharge", required = false) String storeInCharge,
            @RequestParam(value = "active", required = false) Boolean active,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @RequestParam(value = "offset", required = false) Integer offset,
            @RequestParam(value = "ids", required = false) List<String> ids,
            @RequestBody @Valid final RequestInfo requestInfo) {
        StoreGetRequest storeGetRequest = StoreGetRequest.builder().ids(ids).code(code).name(name).active(active)
                .tenantId(tenantId).description(description).department(department)
                .contactNo1(contactNo1).email(email).isCentralStore(isCentralStore).storeInCharge(storeInCharge).sortBy(sortBy)
                .pageSize(pageSize).offset(offset).build();
        Pagination<Store> storesList = storeService.search(storeGetRequest);
        StoreResponse response = new StoreResponse();
        response.setStores(storesList.getPagedData());
        response.setResponseInfo(getResponseInfo(requestInfo));
        return response;
    }

    private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
        return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer())
                .resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
    }
}
