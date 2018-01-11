package org.egov.inv.api;

import io.swagger.annotations.ApiParam;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.egov.inv.domain.service.PurchaseOrderService;
import org.egov.inv.model.PurchaseOrderRequest;
import org.egov.inv.model.PurchaseOrderResponse;
import org.egov.inv.model.PurchaseOrderSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
@javax.annotation.Generated(value = "org.egov.inv.codegen.languages.SpringCodegen", date = "2017-11-08T13:51:07.770Z")

@Controller
public class PurchaseordersApiController implements PurchaseordersApi {

    @Autowired
    private PurchaseOrderService purchaseOrderService;


    public ResponseEntity<PurchaseOrderResponse> purchaseordersCreatePost( @NotNull@ApiParam(value = "Unique id for a tenant.", required = true) @RequestParam(value = "tenantId", required = true) String tenantId,
        @ApiParam(value = "Create  new"  )  @Valid @RequestBody PurchaseOrderRequest purchaseOrderRequest) {
    	PurchaseOrderResponse response = purchaseOrderService.create(purchaseOrderRequest,tenantId);
         return   new ResponseEntity(response,HttpStatus.OK);
    }

    public ResponseEntity<PurchaseOrderResponse> purchaseordersSearchPost( @NotNull@ApiParam(value = "Unique id for a tenant.", required = true) @RequestParam(value = "tenantId", required = true) String tenantId,
        @ApiParam(value = "Parameter to carry Request metadata in the request body"  )  @Valid @RequestBody org.egov.common.contract.request.RequestInfo requestInfo,
         @Size(max=50)@ApiParam(value = "comma seperated list of Ids") @RequestParam(value = "ids", required = false) List<String> ids,
        @ApiParam(value = "store of the PurchaseOrder ") @RequestParam(value = "store", required = false) String store,
        @ApiParam(value = "purchaseOrderNumber  Auto generated number, read only ") @RequestParam(value = "purchaseOrderNumber", required = false) String purchaseOrderNumber,
        @ApiParam(value = "purchase order date of the PurchaseOrder ") @RequestParam(value = "purchaseOrderDate", required = false) Long purchaseOrderDate,
        @ApiParam(value = "rate type of the PurchaseOrder ", allowableValues = "DGSC Rate Contract, ULB Rate Contract, One Time Tender, Quotation") @RequestParam(value = "rateType", required = false) String rateType,
        @ApiParam(value = "supplier code of the PurchaseOrder ") @RequestParam(value = "supplierCode", required = false) String supplierCode,
        @ApiParam(value = "status of the PurchaseOrder ") @RequestParam(value = "status", required = false) String status,
         @Min(0) @Max(100)@ApiParam(value = "Number of records returned.", defaultValue = "20") @RequestParam(value = "pageSize", required = false, defaultValue="20") Integer pageSize,
        @ApiParam(value = "Page number", defaultValue = "1") @RequestParam(value = "pageNumber", required = false, defaultValue="1") Integer pageNumber,
        @ApiParam(value = "This takes any field from the Object seperated by comma and asc,desc keywords. example name asc,code desc or name,code or name,code desc", defaultValue = "id") @RequestParam(value = "sortBy", required = false, defaultValue="id") String sortBy) {

        PurchaseOrderSearch purchaseOrderSearch = new PurchaseOrderSearch();
        purchaseOrderSearch.setIds(ids);
        purchaseOrderSearch.setStore(store);
        purchaseOrderSearch.setTenantId(tenantId);
        purchaseOrderSearch.setPurchaseOrderNumber(purchaseOrderNumber);
        purchaseOrderSearch.setPurchaseOrderDate(purchaseOrderDate);
        purchaseOrderSearch.setRateType(rateType);
        purchaseOrderSearch.setSupplier(supplierCode);
        purchaseOrderSearch.setStatus(status);
        purchaseOrderSearch.setPageSize(pageSize);
        purchaseOrderSearch.setPageNumber(pageNumber);

        return new ResponseEntity<PurchaseOrderResponse>(purchaseOrderService.search(purchaseOrderSearch),HttpStatus.OK);
    }

    public ResponseEntity<PurchaseOrderResponse> purchaseordersUpdatePost( @NotNull@ApiParam(value = "Unique id for a tenant.", required = true) @RequestParam(value = "tenantId", required = true) String tenantId,
        @ApiParam(value = "common Request info"  )  @Valid @RequestBody PurchaseOrderRequest purchaseOrderRequest) {
    	PurchaseOrderResponse response = purchaseOrderService.update(purchaseOrderRequest,tenantId);
        return   new ResponseEntity(response,HttpStatus.OK);
    }

    public ResponseEntity<PurchaseOrderResponse> purchaseordersPreparepofromindentsPost( @NotNull@ApiParam(value = "Unique id for a tenant.", required = true) @RequestParam(value = "tenantId", required = true) String tenantId,
            @ApiParam(value = "Create  new"  )  @RequestBody PurchaseOrderRequest purchaseOrderRequest) {
    	PurchaseOrderResponse response = purchaseOrderService.preparePoFromIndents(purchaseOrderRequest,tenantId);
        return   new ResponseEntity(response,HttpStatus.OK);
    }

    public ResponseEntity<PurchaseOrderResponse> purchaseordersSearchpoforadvancerequisitionPost( @NotNull@ApiParam(value = "Unique id for a tenant.", required = true) @RequestParam(value = "tenantId", required = true) String tenantId) {
    	PurchaseOrderResponse response = purchaseOrderService.getPOsForAdvanceRequisition(tenantId);
    	return new ResponseEntity<PurchaseOrderResponse>(HttpStatus.OK);
    }
}
