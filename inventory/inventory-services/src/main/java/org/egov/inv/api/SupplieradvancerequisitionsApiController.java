package org.egov.inv.api;

import io.swagger.annotations.ApiParam;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.egov.inv.domain.service.SupplierAdvanceRequisitionService;
import org.egov.inv.model.RequestInfo;
import org.egov.inv.model.SupplierAdvanceRequisitionRequest;
import org.egov.inv.model.SupplierAdvanceRequisitionResponse;
import org.egov.inv.model.SupplierAdvanceRequisitionSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-01-03T10:57:16.070Z")

@Controller
public class SupplieradvancerequisitionsApiController implements SupplieradvancerequisitionsApi {

	@Autowired
	SupplierAdvanceRequisitionService supplierAdvanceRequisitionService;

    public ResponseEntity<SupplierAdvanceRequisitionResponse> supplieradvancerequisitionsCreatePost( @NotNull@ApiParam(value = "Unique id for a tenant.", required = true) @RequestParam(value = "tenantId", required = true) String tenantId,
        @ApiParam(value = "Create new supplieradvancerequisition")   @RequestBody SupplierAdvanceRequisitionRequest supplierAdvanceRequisitionRequest) {
    	SupplierAdvanceRequisitionResponse response = supplierAdvanceRequisitionService.create(supplierAdvanceRequisitionRequest);
    	return new ResponseEntity(response, HttpStatus.OK);
    }

    public ResponseEntity<SupplierAdvanceRequisitionResponse> supplieradvancerequisitionsSearchPost( @NotNull@ApiParam(value = "Unique id for a tenant.", required = true) @RequestParam(value = "tenantId", required = true) String tenantId,
        @ApiParam(value = "Parameter to carry Request metadata in the request body"  )   @RequestBody RequestInfo requestInfo,
        @ApiParam(value = "reference no of the supplier ") @RequestParam(value = "supplier", required = false) String supplier,
        @ApiParam(value = "reference no of the purchaseOrder placed with the supplier ") @RequestParam(value = "purchaseOrder", required = false) String purchaseOrder,
        @ApiParam(value = "status of the SupplierAdvanceRequisition ") @RequestParam(value = "sarStatus", required = false) String sarStatus,
        @ApiParam(value = "stateId of the SupplierAdvanceRequisition ") @RequestParam(value = "stateId", required = false) String stateId,
        @ApiParam(value = "Date on which purchaseOrder raised with supplier ") @RequestParam(value = "purchaseOrderDate", required = false) Long purchaseOrderDate) {
    	SupplierAdvanceRequisitionSearch sars = SupplierAdvanceRequisitionSearch.builder().supplier(supplier).purchaseOrder(purchaseOrder).sarStatus(sarStatus).stateId(stateId).build();
    	SupplierAdvanceRequisitionResponse response = supplierAdvanceRequisitionService.search(sars, new RequestInfo());
    	return new ResponseEntity(response, HttpStatus.OK);
    }

    public ResponseEntity<SupplierAdvanceRequisitionResponse> supplieradvancerequisitionsUpdatePost( @NotNull@ApiParam(value = "Unique id for a tenant.", required = true) @RequestParam(value = "tenantId", required = true) String tenantId,
        @ApiParam(value = "common Request info"  )   @RequestBody SupplierAdvanceRequisitionRequest supplierAdvanceRequisitionRequest) {
    	SupplierAdvanceRequisitionResponse response = supplierAdvanceRequisitionService.update(supplierAdvanceRequisitionRequest);
    	return new ResponseEntity(response, HttpStatus.OK);
    }

}