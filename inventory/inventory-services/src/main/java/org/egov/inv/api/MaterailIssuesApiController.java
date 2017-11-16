package org.egov.inv.api;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.inv.domain.service.MaterialIssuesService;
import org.egov.inv.model.MaterialIssue;
import org.egov.inv.model.MaterialIssueRequest;
import org.egov.inv.model.MaterialIssueResponse;
import org.egov.inv.model.MaterialResponse;

import io.swagger.annotations.*;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

import javax.validation.constraints.*;
import javax.validation.Valid;

@javax.annotation.Generated(value = "org.egov.inv.codegen.languages.SpringCodegen", date = "2017-11-08T13:51:07.770Z")

@Controller
public class MaterailIssuesApiController implements MaterailIssuesApi {
	
	@Autowired
	private MaterialIssuesService materialIssueService;

	@Override
	public ResponseEntity<MaterialIssueResponse> materailIssuesCreatePost(
			@NotNull @ApiParam(value = "Unique id for a tenant.", required = true)@Valid @RequestParam(value = "tenantId", required = true) String tenantId,
			@ApiParam(value = "Create  new") @Valid @RequestBody MaterialIssueRequest indentIssueRequest) {
		MaterialIssueResponse materialIssueResponse = materialIssueService.create(indentIssueRequest);
		return new ResponseEntity(materialIssueResponse,HttpStatus.OK);
	}

	@Override
	public ResponseEntity<MaterialIssueResponse> materailIssuesSearchPost(
			@NotNull @ApiParam(value = "Unique id for a tenant.", required = true) @RequestParam(value = "tenantId", required = true) String tenantId,
			@ApiParam(value = "Parameter to carry Request metadata in the request body") @Valid @RequestBody RequestInfo requestInfo,
			@Size(max = 50) @ApiParam(value = "comma seperated list of Ids") @RequestParam(value = "ids", required = false) List<String> ids,
			@ApiParam(value = "material issue of the IndentIssue ") @RequestParam(value = "materialIssue", required = false) Long materialIssue,
			@ApiParam(value = "issued to employee of the IndentIssue ") @RequestParam(value = "issuedToEmployee", required = false) String issuedToEmployee,
			@ApiParam(value = "state id of the IndentIssue ") @RequestParam(value = "stateId", required = false) Long stateId,
			@Min(0) @Max(100) @ApiParam(value = "Number of records returned.", defaultValue = "20") @RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize,
			@ApiParam(value = "Page number", defaultValue = "1") @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@ApiParam(value = "This takes any field from the Object seperated by comma and asc,desc keywords. example name asc,code desc or name,code or name,code desc", defaultValue = "id") @RequestParam(value = "sortBy", required = false, defaultValue = "id") String sortBy) {
		// do some magic!
		return new ResponseEntity<MaterialIssueResponse>(HttpStatus.OK);
	}

	@Override
	public ResponseEntity<MaterialIssueResponse> materailIssuesUpdatePost(
			@NotNull @ApiParam(value = "Unique id for a tenant.", required = true) @RequestParam(value = "tenantId", required = true) String tenantId,
			@ApiParam(value = "common Request info") @Valid @RequestBody MaterialIssueRequest indentIssueRequest) {
		materialIssueService.update(indentIssueRequest);
		return new ResponseEntity<MaterialIssueResponse>(HttpStatus.OK);
	}
}
