package io.swagger.api;

import io.swagger.model.ErrorRes;
import io.swagger.model.Store;
import io.swagger.model.StoreResponse;
import io.swagger.model.Supplier;
import io.swagger.model.SupplierRequest;
import io.swagger.model.SupplierResponse;

import io.swagger.annotations.*;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.inv.domain.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import javax.validation.constraints.*;
import javax.validation.Valid;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-01T09:47:46.371Z")

@Controller
public class SuppliersApiController implements SuppliersApi {
	
	@Autowired
	private SupplierService supplierService;



    public ResponseEntity<SupplierResponse> suppliersCreatePost( @NotNull@ApiParam(value = "Unique id for a tenant.", required = true) @RequestParam(value = "tenantId", required = true) String tenantId,
        @ApiParam(value = "Create  new"  )  @Valid @RequestBody SupplierRequest supplierRequest , BindingResult errors) {
    	
    	List<Supplier> suppliers = supplierService.create(supplierRequest, tenantId , errors);
    	SupplierResponse storeResponse = buildSupplierResponse(suppliers, supplierRequest.getRequestInfo());
    	return new ResponseEntity(storeResponse, HttpStatus.OK);
    }

    public ResponseEntity<SupplierResponse> suppliersSearchPost( @NotNull@ApiParam(value = "Unique id for a tenant.", required = true) @RequestParam(value = "tenantId", required = true) String tenantId,
        @ApiParam(value = "Parameter to carry Request metadata in the request body"  )  @Valid @RequestBody RequestInfo requestInfo,
         @Size(max=50)@ApiParam(value = "comma seperated list of Ids") @RequestParam(value = "ids", required = false) List<String> ids,
        @ApiParam(value = "code of the Supplier ") @RequestParam(value = "code", required = false) String code,
         @Min(0) @Max(100)@ApiParam(value = "Number of records returned.", defaultValue = "20") @RequestParam(value = "pageSize", required = false, defaultValue="20") Integer pageSize,
        @ApiParam(value = "offset") @RequestParam(value = "offset", required = false) Integer offset,
        @ApiParam(value = "Page number", defaultValue = "1") @RequestParam(value = "pageNumber", required = false, defaultValue="1") Integer pageNumber,
        @ApiParam(value = "This takes any field from the Object seperated by comma and asc,desc keywords. example name asc,code desc or name,code or name,code desc", defaultValue = "id") @RequestParam(value = "sortBy", required = false, defaultValue="id") String sortBy) {
        // do some magic!
        return new ResponseEntity<SupplierResponse>(HttpStatus.OK);
    }

    public ResponseEntity<SupplierResponse> suppliersUpdatePost( @NotNull@ApiParam(value = "Unique id for a tenant.", required = true) @RequestParam(value = "tenantId", required = true) String tenantId,
        @ApiParam(value = "common Request info"  )  @Valid @RequestBody SupplierRequest supplierRequest) {
        // do some magic!
        return new ResponseEntity<SupplierResponse>(HttpStatus.OK);
    }
    
	private SupplierResponse buildSupplierResponse(List<Supplier> suppliers, RequestInfo requestInfo) {
		return SupplierResponse.builder().responseInfo(getResponseInfo(requestInfo)).suppliers(suppliers).build();
	}

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer())
				.resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
	}



	
}