package org.egov.inv.api;

import org.egov.inv.domain.service.DisposalService;
import org.egov.inv.model.DisposalRequest;
import org.egov.inv.model.DisposalResponse;
import org.egov.inv.model.DisposalSearchContract;
import org.egov.inv.model.ErrorRes;
import org.egov.inv.model.MaterialIssueRequest;
import org.egov.inv.model.MaterialIssueResponse;
import org.egov.inv.model.RequestInfo;

import io.swagger.annotations.*;

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

@javax.annotation.Generated(value = "org.egov.inv.codegen.languages.SpringCodegen", date = "2017-11-08T13:51:07.770Z")

@Controller
public class DisposalsApiController implements DisposalsApi {

	@Autowired
	private DisposalService disposalService;

	public ResponseEntity<DisposalResponse> disposalsCreatePost(
			@NotNull @ApiParam(value = "Unique id for a tenant.", required = true) @RequestParam(value = "tenantId", required = true) String tenantId,
			@ApiParam(value = "Create  new") @Valid @RequestBody DisposalRequest disposalRequest) {
		DisposalResponse disposalResponse = disposalService.create(disposalRequest, tenantId);
		return new ResponseEntity(disposalResponse, HttpStatus.OK);
	}

	public ResponseEntity<DisposalResponse> disposalsSearchPost(
			@NotNull @ApiParam(value = "Unique id for a tenant.", required = true) @RequestParam(value = "tenantId", required = true) String tenantId,
			@ApiParam(value = "Parameter to carry Request metadata in the request body") @Valid @RequestBody org.egov.common.contract.request.RequestInfo requestInfo,
			@Size(max = 50) @ApiParam(value = "comma seperated list of Ids") @RequestParam(value = "ids", required = false) List<String> ids,
			@ApiParam(value = "store of the Disposal ") @RequestParam(value = "store", required = false) String store,
			@ApiParam(value = "disposal number of the Disposal ") @RequestParam(value = "disposalNumber", required = false) String disposalNumber,
			@ApiParam(value = "disposal date of the Disposal ") @RequestParam(value = "disposalDate", required = false) Long disposalDate,
			@ApiParam(value = "hand over to of the Disposal ") @RequestParam(value = "handOverTo", required = false) String handOverTo,
			@ApiParam(value = "auction number of the Disposal ") @RequestParam(value = "auctionNumber", required = false) String auctionNumber,
			@ApiParam(value = "disposal status of the Disposal ", allowableValues = "CREATED, APPROVED, REJECTED, CANCELED") @RequestParam(value = "disposalStatus", required = false) String disposalStatus,
			@ApiParam(value = "state id of the Disposal ") @RequestParam(value = "stateId", required = false) String stateId,
			@ApiParam(value = "totalDisposalValue  denormalized value from Disposal Details ") @RequestParam(value = "totalDisposalValue", required = false) Double totalDisposalValue,
			@Min(0) @Max(100) @ApiParam(value = "Number of records returned.") @RequestParam(value = "pageSize", required = false) Integer pageSize,
			@ApiParam(value = "Page number") @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
			@ApiParam(value = "This takes any field from the Object seperated by comma and asc,desc keywords. example name asc,code desc or name,code or name,code desc") @RequestParam(value = "sortBy", required = false) String sortBy,
			@ApiParam(value = "This takes the purpose of issue search") @RequestParam(value = "purpose", required = false) String purpose) {

		DisposalSearchContract disposalSearchContract = new DisposalSearchContract(ids,tenantId,store,disposalNumber,disposalDate,handOverTo,
				auctionNumber,disposalStatus,stateId,totalDisposalValue,pageSize,pageNumber,sortBy,purpose);
		DisposalResponse disposalResponse = disposalService.search(disposalSearchContract);
		return new ResponseEntity(disposalResponse,HttpStatus.OK);
	}

	public ResponseEntity<DisposalResponse> disposalsUpdatePost(
			@NotNull @ApiParam(value = "Unique id for a tenant.", required = true) @RequestParam(value = "tenantId", required = true) String tenantId,
			@ApiParam(value = "common Request info") @Valid @RequestBody DisposalRequest disposalRequest) {
		DisposalResponse disposalResponse = disposalService.update(disposalRequest, tenantId);
		return new ResponseEntity(disposalResponse, HttpStatus.OK);
	}
	
	public ResponseEntity<DisposalResponse> prepareDisposalFromScrap(
			@NotNull @ApiParam(value = "Unique id for a tenant.", required = true) @Valid @RequestParam(value = "tenantId", required = true) String tenantId,
			@ApiParam(value = "Create  new")  @RequestBody DisposalRequest disposalRequest) {
		DisposalResponse disposalResponse = disposalService.prepareDisposalFromScrap(disposalRequest, tenantId);
		return new ResponseEntity(disposalResponse, HttpStatus.OK);
	}

}
