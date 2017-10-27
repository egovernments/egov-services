package org.egov.inv.web.controller;

import org.egov.common.contract.request.RequestInfo;
import org.egov.inv.domain.model.Pagination;
import org.egov.inv.domain.model.Store;
import org.egov.inv.domain.service.InventoryUtilityService;
import org.egov.inv.domain.service.StoreService;
import org.egov.inv.web.contract.StoreGetRequest;
import org.egov.inv.web.contract.StoreRequest;
import org.egov.inv.web.contract.StoreResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/stores")
public class StoreController {

    @Autowired
    private StoreService storeService;

    @Autowired
    private InventoryUtilityService inventoryUtilityService;

    @PostMapping("/_create")
    @ResponseStatus(HttpStatus.CREATED)
    public StoreResponse create(@RequestBody @Valid StoreRequest storeRequest,
                                @RequestParam(value = "tenantId") String tenantId, final BindingResult error) {
        List<Store> stores = storeService.create(storeRequest, tenantId);
        return buildMaterialResponse(stores, storeRequest.getRequestInfo());

    }

    @PostMapping("/_update")
    @ResponseStatus(HttpStatus.CREATED)
    public StoreResponse update(@RequestBody @Valid final StoreRequest storeRequest,
                                @RequestParam(value = "tenantId") String tenantId, final BindingResult error) {
        List<Store> stores = storeService.update(storeRequest, tenantId);
        return buildMaterialResponse(stores, storeRequest.getRequestInfo());
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
        response.setResponseInfo(inventoryUtilityService.getResponseInfo(requestInfo));
        return response;
    }

    private StoreResponse buildMaterialResponse(List<Store> stores, RequestInfo requestInfo) {
        return StoreResponse.builder()
                .responseInfo(inventoryUtilityService.getResponseInfo(requestInfo))
                .stores(stores)
                .build();
    }


}
