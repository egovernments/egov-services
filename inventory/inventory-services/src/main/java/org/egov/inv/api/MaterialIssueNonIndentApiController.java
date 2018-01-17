package org.egov.inv.api;

import java.math.BigDecimal;

import io.swagger.annotations.*;

import org.egov.common.contract.request.RequestInfo;
import org.egov.inv.domain.service.NonIndentMaterialIssueService;
import org.egov.inv.model.MaterialIssueRequest;
import org.egov.inv.model.MaterialIssueResponse;
import org.egov.inv.model.MaterialIssueSearchContract;
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

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-12-06T06:54:07.407Z")

@Controller
public class MaterialIssueNonIndentApiController implements MaterialIssueNonIndentApi {

	@Autowired
	private NonIndentMaterialIssueService nonIndentMaterialIssueService;

	public ResponseEntity<MaterialIssueResponse> materialissuesNonIndentCreatePost(
			@NotNull @ApiParam(value = "Unique id for a tenant.", required = true) @RequestParam(value = "tenantId", required = true) String tenantId,
			@ApiParam(value = "Create  new") @Valid @RequestBody MaterialIssueRequest nonIndentIssueRequest) {
		MaterialIssueResponse materialIssueResponse = nonIndentMaterialIssueService.create(nonIndentIssueRequest);
		return new ResponseEntity(materialIssueResponse, HttpStatus.OK);
	}

	public ResponseEntity<MaterialIssueResponse> materialissuesNonIndentSearchPost(
			@NotNull @ApiParam(value = "Unique id for a tenant.", required = true) @RequestParam(value = "tenantId", required = true) String tenantId,
			@ApiParam(value = "Parameter to carry Request metadata in the request body") @Valid @RequestBody RequestInfo requestInfo,
			@Size(max = 50) @ApiParam(value = "comma seperated list of Ids") @RequestParam(value = "ids", required = false) List<String> ids,
			@ApiParam(value = "issuing store of the MaterialIssue ") @RequestParam(value = "fromStore", required = false) String fromStore,
			@ApiParam(value = "receiving store of the MaterialIssue ") @RequestParam(value = "toStore", required = false) String toStore,
			@ApiParam(value = "issueNoteNumber  Auto generated number, read only ") @RequestParam(value = "issueNoteNumber", required = false) String issueNoteNumber,
			@ApiParam(value = "issue date of the MaterialIssue ") @RequestParam(value = "issueDate", required = false) Long issueDate,
			@ApiParam(value = "material issue status of the MaterialIssue ", allowableValues = "CREATED, APPROVED, REJECTED, CANCELED") @RequestParam(value = "materialIssueStatus", required = false) String materialIssueStatus,
			@ApiParam(value = "issue purpose status of the materialissue ") @RequestParam(value = "issuePurpose", required = false) String issuePurpose,
			@ApiParam(value = "description of the MaterialIssue ") @RequestParam(value = "description", required = false) String description,
			@ApiParam(value = "total issue value of the MaterialIssue ") @RequestParam(value = "totalIssueValue", required = false) BigDecimal totalIssueValue,
			@Min(0) @Max(100) @ApiParam(value = "Number of records returned.") @RequestParam(value = "pageSize", required = false) Integer pageSize,
			@ApiParam(value = "Page number") @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
			@ApiParam(value = "This takes any field from the Object seperated by comma and asc,desc keywords. example name asc,code desc or name,code or name,code desc") @RequestParam(value = "sortBy", required = false) String sortBy,
			@ApiParam(value = "This takes purpose of issuesearch") @RequestParam(value = "purpose", required = false) String purpose) {
		MaterialIssueSearchContract searchContract = new MaterialIssueSearchContract(tenantId, ids, fromStore, toStore,
				issueNoteNumber, issuePurpose,issueDate, null, materialIssueStatus, description, totalIssueValue,null, pageNumber, sortBy,
				pageSize, purpose);
		MaterialIssueResponse materialIssueResponse = nonIndentMaterialIssueService.search(searchContract);
		return new ResponseEntity(materialIssueResponse, HttpStatus.OK);
	}

	public ResponseEntity<MaterialIssueResponse> materialissuesNonIndentUpdatePost(
			@NotNull @ApiParam(value = "Unique id for a tenant.", required = true) @RequestParam(value = "tenantId", required = true) String tenantId,
			@ApiParam(value = "common Request info") @Valid @RequestBody MaterialIssueRequest nonIndentIssueRequest) {
		MaterialIssueResponse materialIssueResponse = nonIndentMaterialIssueService.update(nonIndentIssueRequest, tenantId);
		return new ResponseEntity(materialIssueResponse, HttpStatus.OK);

	}

}
