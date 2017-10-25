package org.egov.inv.web.controller;

import java.util.List;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.inv.domain.model.Store;
import org.egov.inv.domain.service.StoreService;
import org.egov.inv.web.contract.StoreRequest;
import org.egov.inv.web.contract.StoreResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
   public StoreResponse create(@RequestBody @Valid StoreRequest storeRequest,@RequestParam(value = "tenantId") String tenantId)
   {   
       StoreResponse storeResponse = new StoreResponse();
       storeResponse.setResponseInfo(getResponseInfo(storeRequest.getRequestInfo()));
       List<Store> stores =  storeService.create(storeRequest,tenantId);
       storeResponse.setStores(stores);
       return storeResponse;
   }
   
   
   @PostMapping("/_update")
   public StoreResponse update(@RequestBody @Valid final StoreRequest storeRequest,@RequestParam(value = "tenantId") String tenantId)
   {   StoreResponse storeResponse = new StoreResponse();
       storeResponse.setResponseInfo(getResponseInfo(storeRequest.getRequestInfo()));
       List<Store> stores =  storeService.update(storeRequest,tenantId);
       storeResponse.setStores(stores);
       return storeResponse;
   }
   
   private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
       return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer())
                       .resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
}
}
