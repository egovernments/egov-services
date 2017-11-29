package org.egov.works.measurementbook.web.controller;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.egov.works.measurementbook.domain.service.MeasurementBookService;
import org.egov.works.measurementbook.web.contract.MeasurementBookRequest;
import org.egov.works.measurementbook.web.contract.MeasurementBookResponse;
import org.egov.works.measurementbook.web.contract.MeasurementBookSearchContract;
import org.egov.works.measurementbook.web.contract.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.annotations.ApiParam;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-19T06:59:20.916Z")

@Controller
public class MeasurementbooksApiController implements MeasurementbooksApi {

	@Autowired
	private MeasurementBookService measurementBookService;

	public ResponseEntity<MeasurementBookResponse> measurementbooksCreatePost(
			@ApiParam(value = "Details of new Measurement Book(s) + RequestInfo meta data.", required = true) @RequestBody MeasurementBookRequest measurementBookRequest) {
		return new ResponseEntity<>(measurementBookService.create(measurementBookRequest),
				HttpStatus.OK);
	}

	public ResponseEntity<MeasurementBookResponse> measurementbooksSearchPost(
			@NotNull @ApiParam(value = "Unique id for a tenant.", required = true) @RequestParam(value = "tenantId", required = true) String tenantId,
			@ApiParam(value = "Parameter to carry Request metadata in the request body") @RequestBody RequestInfo requestInfo,
			@Min(0) @Max(100) @ApiParam(value = "Number of records returned.", defaultValue = "20") @RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize,
			@ApiParam(value = "Page number", defaultValue = "1") @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@ApiParam(value = "This takes any field from the Object seperated by comma and asc,desc keywords. example name asc,code desc or name,code or name,code desc", defaultValue = "id") @RequestParam(value = "sortBy", required = false, defaultValue = "id") String sortBy,
			@Size(max = 50) @ApiParam(value = "Comma separated list of Ids of Measurement Book to get the Measurement Books") @RequestParam(value = "ids", required = false) List<String> ids,
			@Size(max = 50) @ApiParam(value = "Comma separated list of the Measurement Book Status") @RequestParam(value = "statuses", required = false) List<String> statuses,
			@Size(max = 50) @ApiParam(value = "Comma separated list of Work Order Numbers") @RequestParam(value = "workOrderNumbers", required = false) List<String> workOrderNumbers,
			@Size(max = 50) @ApiParam(value = "Comma separated list of MB Reference Numbers") @RequestParam(value = "mbRefNumbers", required = false) List<String> mbRefNumbers,
			@Size(max = 50) @ApiParam(value = "Comma separated list of LOA Numbers") @RequestParam(value = "loaNumbers", required = false) List<String> loaNumbers,
			@Size(max = 50) @ApiParam(value = "Comma separated list of Detailed Estimate Numbers") @RequestParam(value = "detailedEstimateNumbers", required = false) List<String> detailedEstimateNumbers,
			@Size(max = 50) @ApiParam(value = "Comma separated list of Work Identification Numbers") @RequestParam(value = "workIdentificationNumbers", required = false) List<String> workIdentificationNumbers,
			@ApiParam(value = "Epoch time from when the Measurement Book is created") @RequestParam(value = "fromDate", required = false) Long fromDate,
			@ApiParam(value = "Epoch time till when the Measurement Book is created") @RequestParam(value = "toDate", required = false) Long toDate,
			@Size(max = 50) @ApiParam(value = "Comma separated list of the Department for which Measurement Book belongs to") @RequestParam(value = "department", required = false) List<String> department,
			@ApiParam(value = "The user who created the Measurement Book") @RequestParam(value = "createdBy", required = false) String createdBy,
			@Size(max = 50) @ApiParam(value = "Comma separated list of Names of the contractor to which Measurement Book belongs to") @RequestParam(value = "contractorNames", required = false) List<String> contractorNames,
			@Size(max = 50) @ApiParam(value = "Comma separated list of codes of the contractor to which Measurement Book belongs to") @RequestParam(value = "contractorCodes", required = false) List<String> contractorCodes) {
		MeasurementBookSearchContract measurementBookSearchContract = MeasurementBookSearchContract.builder()
				.tenantId(tenantId).pageSize(pageSize).pageNumber(pageNumber).sortProperty(sortBy).ids(ids)
				.statuses(statuses).workOrderNumbers(workOrderNumbers).mbRefNumbers(mbRefNumbers).loaNumbers(loaNumbers)
				.detailedEstimateNumbers(detailedEstimateNumbers).workIdentificationNumbers(workIdentificationNumbers)
				.fromDate(fromDate).toDate(toDate).department(department).createdBy(createdBy)
				.contractorNames(contractorNames).contractorCodes(contractorCodes).build();
        MeasurementBookResponse measurementBookResponse = measurementBookService.search(measurementBookSearchContract, requestInfo);
        return new ResponseEntity<>(measurementBookResponse, HttpStatus.OK);
	}

	public ResponseEntity<MeasurementBookResponse> measurementbooksUpdatePost(
			@ApiParam(value = "Details of Measurement Book(s) + RequestInfo meta data.", required = true) @Valid @RequestBody MeasurementBookRequest measurementBookRequest) {
		return new ResponseEntity<>(measurementBookService.update(measurementBookRequest),
				HttpStatus.OK);
	}

}
