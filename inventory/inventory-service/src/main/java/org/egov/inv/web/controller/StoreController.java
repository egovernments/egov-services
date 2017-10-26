package org.egov.inv.web.controller;

import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.inv.domain.model.Pagination;
import org.egov.inv.domain.model.Store;
import org.egov.inv.domain.service.StoreService;
import org.egov.inv.errorhandlers.ErrorResponse;
import org.egov.inv.factory.ResponseInfoFactory;
import org.egov.inv.util.ValidatorUtils;
import org.egov.inv.web.contract.StoreGetRequest;
import org.egov.inv.web.contract.StoreRequest;
import org.egov.inv.web.contract.StoreResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
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
    
    @Autowired
    private ValidatorUtils validatorUtils;
    
    @Autowired
    private ResponseInfoFactory responseInfoFactory;

    @PostMapping("/_create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> create(@RequestBody @Valid StoreRequest storeRequest,
            @RequestParam(value = "tenantId") String tenantId,final BindingResult error) {
      
            if(error.hasErrors())
            {
               final ErrorResponse errRes = validatorUtils.populateErrors(error);
               return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
           }  
            final List<ErrorResponse> errorResponses = validatorUtils.validateStoresRequest(storeRequest,tenantId);
            if (!errorResponses.isEmpty())
                return new ResponseEntity<>(errorResponses, HttpStatus.BAD_REQUEST);
        
   
        List<Store> stores = storeService.create(storeRequest, tenantId);
        return getSuccessResponse(stores,"Created",storeRequest.getRequestInfo());

    }

    @PostMapping("/_update")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> update(@RequestBody @Valid final StoreRequest storeRequest,
            @RequestParam(value = "tenantId") String tenantId,final BindingResult error) {
        if(error.hasErrors())
        {
           final ErrorResponse errRes = validatorUtils.populateErrors(error);
           return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
       }  
        final List<ErrorResponse> errorResponses = validatorUtils.validateStoresRequest(storeRequest,tenantId);
        if (!errorResponses.isEmpty())
            return new ResponseEntity<>(errorResponses, HttpStatus.BAD_REQUEST);
    

    List<Store> stores = storeService.update(storeRequest, tenantId);
    return getSuccessResponse(stores,"Created",storeRequest.getRequestInfo());
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
    
    private ResponseEntity<?> getSuccessResponse(final List<Store> stores,
            final String mode, final RequestInfo requestInfo) {
        final StoreResponse storeResponse = new StoreResponse();
        storeResponse.setStores(stores);
        final ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
        if (StringUtils.isNotBlank(mode))
            responseInfo.setStatus(HttpStatus.CREATED.toString());
        else
            responseInfo.setStatus(HttpStatus.OK.toString());
        storeResponse.setResponseInfo(responseInfo);
        return new ResponseEntity<>(storeResponse, HttpStatus.OK);

    }
}
