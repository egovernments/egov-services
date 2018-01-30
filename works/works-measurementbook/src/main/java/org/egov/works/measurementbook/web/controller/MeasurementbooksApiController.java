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

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-01-24T10:52:01.165Z")

@Controller
public class MeasurementbooksApiController implements MeasurementbooksApi {

	@Autowired
	private MeasurementBookService measurementBookService;

	public ResponseEntity<MeasurementBookResponse> measurementbooksCreatePost(@ApiParam(value = "Details of new Measurement Book(s) + RequestInfo meta data." ,required=true )  @Valid @RequestBody MeasurementBookRequest measurementBookRequest) {
		return new ResponseEntity<>(measurementBookService.create(measurementBookRequest),
				HttpStatus.OK);
	}

	public ResponseEntity<MeasurementBookResponse> measurementbooksSearchPost( @NotNull@ApiParam(value = "Unique id for a tenant.", required = true) @RequestParam(value = "tenantId", required = true) String tenantId,
																			   @ApiParam(value = "Parameter to carry Request metadata in the request body"  ) @RequestBody RequestInfo requestInfo,
																			   @Min(0) @Max(100)@ApiParam(value = "Number of records returned.", defaultValue = "20") @RequestParam(value = "pageSize", required = false, defaultValue="20") Integer pageSize,
																			   @ApiParam(value = "Page number", defaultValue = "1") @RequestParam(value = "pageNumber", required = false, defaultValue="1") Integer pageNumber,
																			   @ApiParam(value = "This takes any field from the Object seperated by comma and asc,desc keywords. example name asc,code desc or name,code or name,code desc", defaultValue = "id") @RequestParam(value = "sortBy", required = false, defaultValue="id") String sortBy,
																			   @Size(max=50)@ApiParam(value = "Comma separated list of Ids of Measurement Book to get the Measurement Books") @RequestParam(value = "ids", required = false) List<String> ids,
																			   @Size(max=50)@ApiParam(value = "Comma separated list of the Measurement Book Status") @RequestParam(value = "statuses", required = false) List<String> statuses,
																			   @Size(max=50)@ApiParam(value = "Comma separated list of Work Order Numbers") @RequestParam(value = "workOrderNumbers", required = false) List<String> workOrderNumbers,
																			   @ApiParam(value = "Matches exact or like string given regardless of case-sensitive.") @RequestParam(value = "workOrderNumberLike", required = false) String workOrderNumberLike,
																			   @Size(max=50)@ApiParam(value = "Comma separated list of MB Reference Numbers") @RequestParam(value = "mbRefNumbers", required = false) List<String> mbRefNumbers,
																			   @ApiParam(value = "Matches exact or like string given regardless of case-sensitive.") @RequestParam(value = "mbRefNumberLike", required = false) String mbRefNumberLike,
																			   @Size(max=50)@ApiParam(value = "Comma separated list of LOA Numbers") @RequestParam(value = "loaNumbers", required = false) List<String> loaNumbers,
																			   @ApiParam(value = "Matches exact or like string given regardless of case-sensitive.") @RequestParam(value = "loaNumberLike", required = false) String loaNumberLike,
																			   @Size(max=50)@ApiParam(value = "Comma separated list of Detailed Estimate Numbers") @RequestParam(value = "detailedEstimateNumbers", required = false) List<String> detailedEstimateNumbers,
																			   @ApiParam(value = "Matches exact or like string given regardless of case-sensitive.") @RequestParam(value = "detailedEstimateNumberLike", required = false) String detailedEstimateNumberLike,
																			   @Size(max=50)@ApiParam(value = "Comma separated list of Work Identification Numbers") @RequestParam(value = "workIdentificationNumbers", required = false) List<String> workIdentificationNumbers,
																			   @ApiParam(value = "Matches exact or like string given regardless of case-sensitive.") @RequestParam(value = "workIdentificationNumbersLike", required = false) String workIdentificationNumbersLike,
																			   @ApiParam(value = "Epoch time from when the Measurement Book is created") @RequestParam(value = "fromDate", required = false) Long fromDate,
																			   @ApiParam(value = "Epoch time till when the Measurement Book is created") @RequestParam(value = "toDate", required = false) Long toDate,
																			   @Size(max=50)@ApiParam(value = "Comma separated list of the Department for which Measurement Book belongs to") @RequestParam(value = "department", required = false) List<String> department,
																			   @ApiParam(value = "The user who created the Measurement Book") @RequestParam(value = "createdBy", required = false) String createdBy,
																			   @Size(max=50)@ApiParam(value = "Comma separated list of Names of the contractor to which Measurement Book belongs to") @RequestParam(value = "contractorNames", required = false) List<String> contractorNames,
																			   @ApiParam(value = "Matches exact or like string given regardless of case-sensitive.") @RequestParam(value = "contractorNameLike", required = false) String contractorNameLike,
																			   @Size(max=50)@ApiParam(value = "Comma separated list of codes of the contractor to which Measurement Book belongs to") @RequestParam(value = "contractorCodes", required = false) List<String> contractorCodes,
																			   @ApiParam(value = "Matches exact or like string given regardless of case-sensitive.") @RequestParam(value = "contractorCodeLike", required = false) String contractorCodeLike,
																			   @ApiParam(value = "if this value is true then API returns all the part rated MBs. If this values is false then API returns all the non part rated MBs. If no value then it returns all the MBs.") @RequestParam(value = "partRateExists", required = false) Boolean partRateExists,
																			   @ApiParam(value = "if this value is true, API returns all the MBs where bill is created. if this values is false, API returns all the MBs where bill is not created. If no value then it returns all the MBs.") @RequestParam(value = "billExists", required = false) Boolean billExists,
																			   @ApiParam(value = "Letter Of Acceptance Estimate id of MB.") @RequestParam(value = "loaEstimateId", required = false) String loaEstimateId) {
		MeasurementBookSearchContract measurementBookSearchContract = MeasurementBookSearchContract.builder()
				.tenantId(tenantId).pageSize(pageSize).pageNumber(pageNumber).sortProperty(sortBy).ids(ids)
				.statuses(statuses).workOrderNumbers(workOrderNumbers).mbRefNumbers(mbRefNumbers).loaNumbers(loaNumbers)
				.detailedEstimateNumbers(detailedEstimateNumbers).workIdentificationNumbers(workIdentificationNumbers)
				.fromDate(fromDate).toDate(toDate).department(department).createdBy(createdBy)
				.contractorNames(contractorNames).contractorCodes(contractorCodes).billExists(billExists)
				.partRateExists(partRateExists).loaEstimateId(loaEstimateId).build();
        MeasurementBookResponse measurementBookResponse = measurementBookService.search(measurementBookSearchContract, requestInfo);
        return new ResponseEntity<>(measurementBookResponse, HttpStatus.OK);
	}

	public ResponseEntity<MeasurementBookResponse> measurementbooksUpdatePost(@ApiParam(value = "Details of Measurement Book(s) + RequestInfo meta data." ,required=true )  @Valid @RequestBody MeasurementBookRequest measurementBookRequest) {
		return new ResponseEntity<>(measurementBookService.update(measurementBookRequest),
				HttpStatus.OK);
	}

}
