package org.egov.works.measurementbook.web.controller;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.egov.works.measurementbook.domain.service.ContractorBillService;
import org.egov.works.measurementbook.web.contract.ContractorBillRequest;
import org.egov.works.measurementbook.web.contract.ContractorBillResponse;
import org.egov.works.measurementbook.web.contract.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.annotations.ApiParam;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-12-12T07:40:13.283Z")

@Controller
public class ContractorbillsApiController implements ContractorbillsApi {
    
    @Autowired
    private ContractorBillService contractorBillService;

    public ResponseEntity<ContractorBillResponse> contractorbillsCreatePost(@ApiParam(value = "Details of new Contractor Bill(s) + RequestInfo meta data." ,required=true )  @Valid @RequestBody ContractorBillRequest contractorBillRequest) {
        // do some magic!
        return new ResponseEntity<>(contractorBillService.create(contractorBillRequest),HttpStatus.OK);
    }

    public ResponseEntity<ContractorBillResponse> contractorbillsSearchPost( @NotNull@ApiParam(value = "Unique id for a tenant.", required = true) @RequestParam(value = "tenantId", required = true) String tenantId,
        @ApiParam(value = "Parameter to carry Request metadata in the request body"  )  @Valid @RequestBody RequestInfo requestInfo,
         @Min(0) @Max(100)@ApiParam(value = "Number of records returned.", defaultValue = "20") @RequestParam(value = "pageSize", required = false, defaultValue="20") Integer pageSize,
        @ApiParam(value = "Page number", defaultValue = "1") @RequestParam(value = "pageNumber", required = false, defaultValue="1") Integer pageNumber,
        @ApiParam(value = "This takes any field from the Object seperated by comma and asc,desc keywords. example name asc,code desc or name,code or name,code desc", defaultValue = "id") @RequestParam(value = "sortBy", required = false, defaultValue="id") String sortBy,
         @Size(max=50)@ApiParam(value = "Comma separated list of Ids of Contractor Bills") @RequestParam(value = "ids", required = false) List<String> ids,
         @Size(max=50)@ApiParam(value = "Comma separated list of Letter of Acceptance Numbers") @RequestParam(value = "letterOfAcceptanceNumbers", required = false) List<String> letterOfAcceptanceNumbers,
        @ApiParam(value = "Epoch time from when bill is created") @RequestParam(value = "billFromDate", required = false) Long billFromDate,
        @ApiParam(value = "Epoch time till when bill is created") @RequestParam(value = "billToDate", required = false) Long billToDate,
         @Size(max=50)@ApiParam(value = "Comma separated list of bill types") @RequestParam(value = "billTypes", required = false) List<String> billTypes,
         @Size(max=50)@ApiParam(value = "Comma separated list of bill numbers") @RequestParam(value = "billNumbers", required = false) List<String> billNumbers,
         @Size(max=50)@ApiParam(value = "Comma separated list of Contractor Bill Status") @RequestParam(value = "statuses", required = false) List<String> statuses,
         @Size(max=50)@ApiParam(value = "Comma separated list of Work Identification Numbers") @RequestParam(value = "workIdentificationNumbers", required = false) List<String> workIdentificationNumbers,
         @Size(max=50)@ApiParam(value = "Comma separated list of contractor names") @RequestParam(value = "contractorNames", required = false) List<String> contractorNames,
         @Size(max=50)@ApiParam(value = "Comma separated list of department of the Contractor Bill") @RequestParam(value = "departmentCodes", required = false) List<String> departmentCodes,
        @ApiParam(value = "Boolean value to check whether its Spillover or not") @RequestParam(value = "spillOverFlag", required = false) Boolean spillOverFlag) {
        // do some magic!
        return new ResponseEntity<ContractorBillResponse>(HttpStatus.OK);
    }

    public ResponseEntity<ContractorBillResponse> contractorbillsUpdatePost(@ApiParam(value = "Details of Contractor Bill(s) + RequestInfo meta data." ,required=true )  @Valid @RequestBody ContractorBillRequest contractorBillRequest) {
        // do some magic!
        return new ResponseEntity<ContractorBillResponse>(HttpStatus.OK);
    }

}
