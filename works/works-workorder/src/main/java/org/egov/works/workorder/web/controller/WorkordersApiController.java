package org.egov.works.workorder.web.controller;

import io.swagger.annotations.ApiParam;
import org.egov.works.workorder.domain.service.WorkOrderService;
import org.egov.works.workorder.web.contract.RequestInfo;
import org.egov.works.workorder.web.contract.WorkOrderRequest;
import org.egov.works.workorder.web.contract.WorkOrderResponse;
import org.egov.works.workorder.web.contract.WorkOrderSearchContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-27T05:41:29.315Z")

@Controller
public class WorkordersApiController implements WorkordersApi {

    @Autowired
    private WorkOrderService workOrderService;

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public ResponseEntity<WorkOrderResponse> workordersCreatePost(
            @ApiParam(value = "Details of new Work Order(s) + RequestInfo meta data.", required = true) @Valid @RequestBody WorkOrderRequest workOrderRequest) {
        WorkOrderResponse workOrderResponse = workOrderService.create(workOrderRequest);
        return new ResponseEntity(workOrderResponse, HttpStatus.OK);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public ResponseEntity<WorkOrderResponse> workordersSearchPost(
            @NotNull @ApiParam(value = "Unique id for a tenant.", required = true) @RequestParam(value = "tenantId", required = true) String tenantId,
            @ApiParam(value = "Parameter to carry Request metadata in the request body") @Valid @RequestBody RequestInfo requestInfo,
            @Min(0) @Max(100) @ApiParam(value = "Number of records returned.", defaultValue = "20") @RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize,
            @ApiParam(value = "Page number", defaultValue = "1") @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
            @ApiParam(value = "This takes any field from the Object seperated by comma and asc,desc keywords. example name asc,code desc or name,code or name,code desc", defaultValue = "id") @RequestParam(value = "sortBy", required = false, defaultValue = "id") String sortBy,
            @Size(max = 50) @ApiParam(value = "Comma separated list of Work Order Numbers") @RequestParam(value = "workOrderNumbers", required = false) List<String> workOrderNumbers,
            @ApiParam(value = "Matches exact or like string given regardless of case-sensitive.") @RequestParam(value = "workOrderNumberLike", required = false) String workOrderNumberLike,
            @Size(max = 50) @ApiParam(value = "Comma separated list of Ids of Work Order to get the Work Orders") @RequestParam(value = "ids", required = false) List<String> ids,
            @Size(max = 50) @ApiParam(value = "Comma separated list of LOA Numbers") @RequestParam(value = "loaNumbers", required = false) List<String> loaNumbers,
            @ApiParam(value = "Matches exact or like string given regardless of case-sensitive.") @RequestParam(value = "loaNumberLike", required = false) String loaNumberLike,
            @Size(max = 50) @ApiParam(value = "Comma separated list of Detailed Estimate Numbers") @RequestParam(value = "detailedEstimateNumbers", required = false) List<String> detailedEstimateNumbers,
            @ApiParam(value = "Matches exact or like string given regardless of case-sensitive.") @RequestParam(value = "detailedEstimateNumberLike", required = false) String detailedEstimateNumberLike,
            @Size(max = 50) @ApiParam(value = "Comma separated list of Work Identification Numbers") @RequestParam(value = "workIdentificationNumbers", required = false) List<String> workIdentificationNumbers,
            @ApiParam(value = "Matches exact or like string given regardless of case-sensitive.") @RequestParam(value = "workIdentificationNumberLike", required = false) String workIdentificationNumberLike,
            @ApiParam(value = "Epoch time for Work Order when it is created in the system") @RequestParam(value = "fromDate", required = false) Long fromDate,
            @ApiParam(value = "Epoch time for Work Order when it is created in the system") @RequestParam(value = "toDate", required = false) Long toDate,
            @Size(max = 50) @ApiParam(value = "Comma separated list of the Department for which Work Order belongs to.") @RequestParam(value = "department", required = false) List<String> department,
            @Size(max = 50) @ApiParam(value = "Comma separated list of the Work Order Status") @RequestParam(value = "statuses", required = false) List<String> statuses,
            @Size(max = 50) @ApiParam(value = "Comma separated list of Names of the contractor to which Work Order belongs to.") @RequestParam(value = "contractorNames", required = false) List<String> contractorNames,
            @Size(max = 50) @ApiParam(value = "Comma separated list of codes of the contractor to which Work Order belongs to.") @RequestParam(value = "contractorCodes", required = false) List<String> contractorCodes,
            @Size(max = 50) @ApiParam(value = "Comma separated list of ids of the Letter Of Acceptance to which Work Order belongs to.") @RequestParam(value = "letterOfAcceptances", required = false) List<String> letterOfAcceptances,
            @ApiParam(value = "Matches exact or like string given regardless of case-sensitive.") @RequestParam(value = "contractorCodeLike", required = false) String contractorCodeLike,
            @ApiParam(value = "Matches exact or like string given regardless of case-sensitive.") @RequestParam(value = "contractorNameLike", required = false) String contractorNameLike,
            @ApiParam(value = "if this value is true, API returns all the WOs where Offline Status is WORK_COMMENCED. if this values is false, API returns all the WOs where Offline Status is not created. If no value then it returns all the WOs.") @RequestParam(value = "isOfflineStatusCreated", required = false) Boolean isOfflineStatusCreated,
            @ApiParam(value = "if this value is true, API returns all the WOs where Milestone is created. if this values is false, API returns all the WOs where Milestone is not created. If no value then it returns all the WOs.") @RequestParam(value = "isMilestoneCreated", required = false) Boolean isMilestoneCreated,
            @ApiParam(value = "if this value is true, API returns all the WOs where bill are approved. if this values is false, API returns all the WOs where bill is not created. If no value then it returns all the WOs.'") @RequestParam(value = "isBillCreated", required = false) Boolean isBillCreated,
            @ApiParam(value = "if this value is true, API returns all the WOs where ARF is created. if this values is false, API returns all the WOs where ARF is not created. If no value then it returns all the WOs.") @RequestParam(value = "isARFCreated", required = false) Boolean isARFCreated,
            @ApiParam(value = "if this value is true, API returns all the WOs where MB is approved. if this values is false, API returns all the WOs where MB is not approved. If no value then it returns all the WOs.") @RequestParam(value = "isMBApproved", required = false) Boolean isMBApproved) {
        WorkOrderSearchContract workOrderSearchContract = WorkOrderSearchContract.builder().tenantId(tenantId)
                .contractorCodes(contractorCodes).contractorNames(contractorNames)
                .pageNumber(pageNumber).pageSize(pageSize).department(department).detailedEstimateNumbers(detailedEstimateNumbers)
                .fromDate(fromDate).toDate(toDate).ids(ids).loaNumbers(loaNumbers).sortBy(sortBy).statuses(statuses)
                .workIdentificationNumbers(workIdentificationNumbers).letterOfAcceptances(letterOfAcceptances).build();

        WorkOrderResponse workOrderResponse = workOrderService.search(workOrderSearchContract, requestInfo);
        return new ResponseEntity(workOrderResponse, HttpStatus.OK);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public ResponseEntity<WorkOrderResponse> workordersUpdatePost(
            @ApiParam(value = "Details of Work Order(s) + RequestInfo meta data.", required = true) @Valid @RequestBody WorkOrderRequest workOrderRequest) {
        WorkOrderResponse workOrderResponse = workOrderService.update(workOrderRequest);
        return new ResponseEntity(workOrderResponse, HttpStatus.OK);
    }

}
