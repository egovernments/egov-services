package org.egov.works.workorder.web.controller;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.egov.works.workorder.domain.service.NoticeService;
import org.egov.works.workorder.web.contract.NoticeRequest;
import org.egov.works.workorder.web.contract.NoticeResponse;
import org.egov.works.workorder.web.contract.NoticeSearchContract;
import org.egov.works.workorder.web.contract.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.annotations.ApiParam;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-12-20T06:10:08.407Z")

@Controller
public class NoticesApiController implements NoticesApi {

    @Autowired
    private NoticeService noticeService;

    public ResponseEntity<NoticeResponse> noticesCreatePost(
            @ApiParam(value = "Details of new Notice(s) + RequestInfo meta data.", required = true) @Valid @RequestBody NoticeRequest noticeRequest) {
        NoticeResponse noticeResponse = noticeService.create(noticeRequest);
        return new ResponseEntity<NoticeResponse>(noticeResponse, HttpStatus.OK);
    }

    public ResponseEntity<NoticeResponse> noticesSearchPost(
            @NotNull @ApiParam(value = "Unique id for a tenant.", required = true) @RequestParam(value = "tenantId", required = true) String tenantId,
            @ApiParam(value = "Parameter to carry Request metadata in the request body") @RequestBody RequestInfo requestInfo,
            @Min(0) @Max(100) @ApiParam(value = "Number of records returned.", defaultValue = "20") @RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize,
            @ApiParam(value = "Page number", defaultValue = "1") @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
            @ApiParam(value = "This takes any field from the Object seperated by comma and asc,desc keywords. example name asc,code desc or name,code or name,code desc", defaultValue = "id") @RequestParam(value = "sortBy", required = false, defaultValue = "id") String sortBy,
            @Size(max = 50) @ApiParam(value = "Comma separated list of Work Order Numbers") @RequestParam(value = "workOrderNumbers", required = false) List<String> workOrderNumbers,
            @Size(max = 50) @ApiParam(value = "Comma separated list of Ids of Notice(s)") @RequestParam(value = "ids", required = false) List<String> ids,
            @Size(max = 50) @ApiParam(value = "Comma separated list of LOA Numbers") @RequestParam(value = "loaNumbers", required = false) List<String> loaNumbers,
            @Size(max = 50) @ApiParam(value = "Comma separated list of Detailed Estimate Numbers") @RequestParam(value = "detailedEstimateNumbers", required = false) List<String> detailedEstimateNumbers,
            @Size(max = 50) @ApiParam(value = "Comma separated list of Work Identification Numbers") @RequestParam(value = "workIdentificationNumbers", required = false) List<String> workIdentificationNumbers,
            @Size(max = 50) @ApiParam(value = "Comma separated list of Names of the contractor to whom Notice is issued") @RequestParam(value = "contractorNames", required = false) List<String> contractorNames,
            @Size(max = 50) @ApiParam(value = "Comma separated list of codes of the contractor to whom Notice is issued") @RequestParam(value = "contractorCodes", required = false) List<String> contractorCodes,
            @Size(max = 50) @ApiParam(value = "Comma separated list of the Notice Status") @RequestParam(value = "statuses", required = false) List<String> statuses) {
        NoticeSearchContract noticeSearchContract = NoticeSearchContract.builder().contractorCodes(contractorCodes)
                .contractorNames(contractorNames).detailedEstimateNumbers(detailedEstimateNumbers).ids(ids)
                .loaNumbers(loaNumbers).pageNumber(pageNumber).pageSize(pageSize).sortBy(sortBy).tenantId(tenantId)
                .workIdentificationNumbers(workIdentificationNumbers).workOrderNumbers(workOrderNumbers)
                .statuses(statuses).build();
        NoticeResponse noticeResponse = noticeService.search(noticeSearchContract, requestInfo);
        return new ResponseEntity<NoticeResponse>(noticeResponse, HttpStatus.OK);
    }

    public ResponseEntity<NoticeResponse> noticesUpdatePost(
            @ApiParam(value = "Details of Notice(s) + RequestInfo meta data.", required = true) @Valid @RequestBody NoticeRequest noticeRequest) {
        NoticeResponse noticeResponse = noticeService.update(noticeRequest);
        return new ResponseEntity<NoticeResponse>(noticeResponse, HttpStatus.OK);
    }

}
