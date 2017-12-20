package org.egov.works.qualitycontrol.web.controller;


import io.swagger.annotations.*;

import org.egov.works.qualitycontrol.domain.service.QualityTestingService;
import org.egov.works.qualitycontrol.web.contract.QualityTestingRequest;
import org.egov.works.qualitycontrol.web.contract.QualityTestingResponse;
import org.egov.works.qualitycontrol.web.contract.QualityTestingSearchContract;
import org.egov.works.qualitycontrol.web.contract.RequestInfo;
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
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-12-18T07:18:22.459Z")

@Controller
public class QualitytestingsApiController implements QualitytestingsApi {

    @Autowired
    private QualityTestingService qualityTestingService;

    public ResponseEntity<QualityTestingResponse> qualitytestingsCreatePost(@ApiParam(value = "Details of new Quality Testing(s) + RequestInfo meta data." ,required=true )  @Valid @RequestBody QualityTestingRequest qualityTestingRequest) {
        QualityTestingResponse qualityTestingResponse = qualityTestingService.create(qualityTestingRequest);
        return new ResponseEntity(qualityTestingResponse, HttpStatus.OK);
    }

    public ResponseEntity<QualityTestingResponse> qualitytestingsSearchPost( @NotNull@ApiParam(value = "Unique id for a tenant.", required = true) @RequestParam(value = "tenantId", required = true) String tenantId,
        @ApiParam(value = "Parameter to carry Request metadata in the request body"  )  @Valid @RequestBody RequestInfo requestInfo,
         @Min(0) @Max(100)@ApiParam(value = "Number of records returned.", defaultValue = "20") @RequestParam(value = "pageSize", required = false, defaultValue="20") Integer pageSize,
        @ApiParam(value = "Page number", defaultValue = "1") @RequestParam(value = "pageNumber", required = false, defaultValue="1") Integer pageNumber,
        @ApiParam(value = "This takes any field from the Object seperated by comma and asc,desc keywords. example name asc,code desc or name,code or name,code desc", defaultValue = "id") @RequestParam(value = "sortBy", required = false, defaultValue="id") String sortBy,
         @Size(max=50)@ApiParam(value = "Comma separated list of Work Order Numbers") @RequestParam(value = "workOrderNumbers", required = false) List<String> workOrderNumbers,
         @Size(max=50)@ApiParam(value = "Work Order Numbers") @RequestParam(value = "workOrderNumberLike", required = false) String workOrderNumberLike,
         @Size(max=50)@ApiParam(value = "Comma separated list of Ids of Quality Testing(s)") @RequestParam(value = "ids", required = false) List<String> ids,
         @Size(max=50)@ApiParam(value = "Comma separated list of LOA Numbers") @RequestParam(value = "loaNumbers", required = false) List<String> loaNumbers,
         @Size(max=50)@ApiParam(value = "LOA Number") @RequestParam(value = "loaNumberLike", required = false) String loaNumberLike,
         @Size(max=50)@ApiParam(value = "Comma separated list of Detailed Estimate Numbers") @RequestParam(value = "detailedEstimateNumbers", required = false) List<String> detailedEstimateNumbers,
         @Size(max=50)@ApiParam(value = "Detailed estimate Number") @RequestParam(value = "detailedEstimateNumberLike", required = false) String detailedEstimateNumberLike,
         @Size(max=50)@ApiParam(value = "Comma separated list of Work Identification Numbers") @RequestParam(value = "workIdentificationNumbers", required = false) List<String> workIdentificationNumbers,
         @Size(max=50)@ApiParam(value = "Work Identification Number") @RequestParam(value = "workIdentificationNumberLike", required = false) String workIdentificationNumberLike) {
        QualityTestingSearchContract qualityTestingSearchContract = QualityTestingSearchContract.builder()
                .tenantId(tenantId).ids(ids).pageNumber(pageNumber).pageSize(pageSize).sortBy(sortBy)
                .workIdentificationNumbers(workIdentificationNumbers).workIdentificationNumberLike(workIdentificationNumberLike)
                .loaNumbers(loaNumbers).loaNumberLike(loaNumberLike).detailedEstimateNumbers(detailedEstimateNumbers)
                .detailedEstimateNumberLike(detailedEstimateNumberLike).workOrderNumberLike(workOrderNumberLike)
                .workOrderNumbers(workOrderNumbers).build();
        QualityTestingResponse qualityTestingResponse = qualityTestingService.search(qualityTestingSearchContract, requestInfo);
        return new ResponseEntity(qualityTestingResponse, HttpStatus.OK);
    }

    public ResponseEntity<QualityTestingResponse> qualitytestingsUpdatePost(@ApiParam(value = "Details of Quality Testing(s) + RequestInfo meta data." ,required=true )  @Valid @RequestBody QualityTestingRequest qualityTestingRequest) {
        QualityTestingResponse qualityTestingResponse = qualityTestingService.update(qualityTestingRequest);
        return new ResponseEntity(qualityTestingResponse, HttpStatus.OK);
    }

}
