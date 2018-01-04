package org.egov.works.workorder.web.controller;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.egov.works.workorder.web.contract.ContractorAdvanceRequisitionRequest;
import org.egov.works.workorder.web.contract.ContractorAdvanceRequisitionResponse;
import org.egov.works.workorder.web.contract.RequestInfo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.annotations.ApiParam;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-01-04T13:02:13.606Z")

@Controller
public class ContractoradvanceApiController implements ContractoradvanceApi {



    public ResponseEntity<ContractorAdvanceRequisitionResponse> contractoradvanceCreatePost(@ApiParam(value = "Details for the new Contractor Advance Requisition Form(s) + RequestInfo meta data." ,required=true )  @Valid @RequestBody ContractorAdvanceRequisitionRequest contractorAdvanceRequisitionRequest) {
        // do some magic!
        return new ResponseEntity<ContractorAdvanceRequisitionResponse>(HttpStatus.OK);
    }

    public ResponseEntity<ContractorAdvanceRequisitionResponse> contractoradvanceSearchPost( @NotNull@ApiParam(value = "Unique id for a tenant.", required = true) @RequestParam(value = "tenantId", required = true) String tenantId,
        @ApiParam(value = "Parameter to carry Request metadata in the request body"  )  @Valid @RequestBody RequestInfo requestInfo,
         @Min(0) @Max(100)@ApiParam(value = "Number of records returned.", defaultValue = "20") @RequestParam(value = "pageSize", required = false, defaultValue="20") Integer pageSize,
        @ApiParam(value = "Page number", defaultValue = "1") @RequestParam(value = "pageNumber", required = false, defaultValue="1") Integer pageNumber,
        @ApiParam(value = "This takes any field from the Object seperated by comma and asc,desc keywords. example name asc,code desc or name,code or name,code desc", defaultValue = "id") @RequestParam(value = "sortBy", required = false, defaultValue="id") String sortBy,
         @Size(max=50)@ApiParam(value = "Comma separated list of Ids of Contractor Advance Requistion to get the Contractor Advance Requistions") @RequestParam(value = "ids", required = false) List<String> ids,
         @Size(max=50)@ApiParam(value = "Comma separated list of Contractor Advance Requistion Numbers of  Contractor Advance Requistion to get the Contractor Advance Requistions") @RequestParam(value = "advanceRequisitionNumbers", required = false) List<String> advanceRequisitionNumbers,
         @Size(max=50)@ApiParam(value = "Comma separated list of Letter of Acceptance Numbers") @RequestParam(value = "loaNumbers", required = false) List<String> loaNumbers,
        @ApiParam(value = "Epoch time from Advance Requisition Created Date") @RequestParam(value = "fromDate", required = false) Long fromDate,
        @ApiParam(value = "Epoch time till the Advance Requisition Created Date") @RequestParam(value = "toDate", required = false) Long toDate,
         @Size(max=50)@ApiParam(value = "Comma separated list of Status of the Advance Requisition") @RequestParam(value = "statuses", required = false) List<String> statuses,
         @Size(max=50)@ApiParam(value = "Comma separated list of Advance Requisition Bill Numbers") @RequestParam(value = "advanceBillNumbers", required = false) List<String> advanceBillNumbers,
         @Size(max=50)@ApiParam(value = "Comma separated list of Names of the contractor to which Advance Requisition belongs to") @RequestParam(value = "contractorNames", required = false) List<String> contractorNames,
         @Size(max=50)@ApiParam(value = "Comma separated list of codes of the contractor to which Advance Requisition belongs to") @RequestParam(value = "contractorCodes", required = false) List<String> contractorCodes) {
        // do some magic!
        return new ResponseEntity<ContractorAdvanceRequisitionResponse>(HttpStatus.OK);
    }

    public ResponseEntity<ContractorAdvanceRequisitionResponse> contractoradvanceUpdatePost(@ApiParam(value = "Details of the Contractor Advance Requisition Form(s) + RequestInfo meta data." ,required=true )  @Valid @RequestBody ContractorAdvanceRequisitionRequest contractorAdvanceRequisitionRequest) {
        // do some magic!
        return new ResponseEntity<ContractorAdvanceRequisitionResponse>(HttpStatus.OK);
    }

}
