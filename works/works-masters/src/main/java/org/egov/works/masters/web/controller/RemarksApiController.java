package org.egov.works.masters.web.controller;


import io.swagger.annotations.*;

import org.egov.works.masters.domain.service.RemarksService;
import org.egov.works.masters.web.contract.RemarksRequest;
import org.egov.works.masters.web.contract.RemarksResponse;
import org.egov.works.masters.web.contract.RemarksSearchContract;
import org.egov.works.masters.web.contract.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import javax.validation.constraints.*;
import javax.validation.Valid;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-01-02T12:11:15.167Z")

@Controller
public class RemarksApiController implements RemarksApi {

    @Autowired
    private RemarksService remarksService;


    public ResponseEntity<RemarksResponse> remarksCreatePost(@ApiParam(value = "Details of new Remarks(s) + RequestInfo meta data." ,required=true )  @Valid @RequestBody RemarksRequest remarksRequest) {
        RemarksResponse remarksResponse = remarksService.create(remarksRequest);
        return new ResponseEntity<>(remarksResponse, HttpStatus.OK);
    }

    public ResponseEntity<RemarksResponse> remarksSearchPost( @NotNull@ApiParam(value = "Unique id for a tenant.", required = true) @RequestParam(value = "tenantId", required = true) String tenantId,
        @ApiParam(value = "Parameter to carry Request metadata in the request body"  )  @RequestBody RequestInfo requestInfo,
         @Min(0) @Max(100)@ApiParam(value = "Number of records returned.", defaultValue = "20") @RequestParam(value = "pageSize", required = false, defaultValue="20") Integer pageSize,
        @ApiParam(value = "Page number", defaultValue = "1") @RequestParam(value = "pageNumber", required = false, defaultValue="1") Integer pageNumber,
        @ApiParam(value = "This takes any field from the Object seperated by comma and asc,desc keywords. example name asc,code desc or name,code or name,code desc", defaultValue = "id") @RequestParam(value = "sortBy", required = false, defaultValue="id") String sortBy,
         @Size(max=50)@ApiParam(value = "Comma separated list of Ids of Remarks to get the Remarks") @RequestParam(value = "ids", required = false) List<String> ids,
        @ApiParam(value = "The document type of the remarks.") @RequestParam(value = "typeOfDocument", required = false) String typeOfDocument,
        @ApiParam(value = "remarks type of the remarks.") @RequestParam(value = "remarksType", required = false) String remarksType,
        @ApiParam(value = "Remarks Condition") @RequestParam(value = "remarksDescription", required = false) String remarksDescription) {
        RemarksSearchContract remarksSearchContract = RemarksSearchContract.builder().ids(ids)
                .tenantId(tenantId)
                .remarksDescription(remarksDescription).remarksType(remarksType).pageNumber(pageNumber).sortBy(sortBy).pageSize(pageSize).typeOfDocument(typeOfDocument)
                .build();
        RemarksResponse remarksResponse = remarksService.search(remarksSearchContract, requestInfo);
        return new ResponseEntity<>(remarksResponse, HttpStatus.OK);
    }

    public ResponseEntity<RemarksResponse> remarksUpdatePost(@ApiParam(value = "Details of Remarks(s) + RequestInfo meta data." ,required=true )  @Valid @RequestBody RemarksRequest remarksRequest) {
        // do some magic!
        RemarksResponse remarksResponse = remarksService.update(remarksRequest);
        return new ResponseEntity<>(remarksResponse, HttpStatus.OK);
    }

}
