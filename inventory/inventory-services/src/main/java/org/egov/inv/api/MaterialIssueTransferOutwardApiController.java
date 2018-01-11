package org.egov.inv.api;

import org.egov.inv.domain.service.MaterialIssueService;
import org.egov.inv.model.MaterialIssue;
import org.egov.inv.model.MaterialIssue.IssueTypeEnum;
import org.egov.inv.model.MaterialIssueRequest;
import org.egov.inv.model.MaterialIssueResponse;
import org.egov.inv.model.MaterialIssueSearchContract;
import org.egov.inv.model.MaterialResponse;
import org.egov.inv.model.RequestInfo;

import io.swagger.annotations.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.*;
import javax.validation.Valid;

@javax.annotation.Generated(value = "org.egov.inv.codegen.languages.SpringCodegen", date = "2017-11-08T13:51:07.770Z")

@Controller
public class MaterialIssueTransferOutwardApiController implements MaterialIssueTransferOutwardApi {

	@Autowired
	private MaterialIssueService materialIssueService;

	@Override
	public ResponseEntity<MaterialIssueResponse> materialIssueCreatePost(
			@NotNull @ApiParam(value = "Unique id for a tenant.", required = true) @Valid @RequestParam(value = "tenantId", required = true) String tenantId,
			@ApiParam(value = "Create  new") @Valid @RequestBody MaterialIssueRequest indentIssueRequest) {
		MaterialIssueResponse materialIssueResponse = materialIssueService.create(indentIssueRequest, IssueTypeEnum.MATERIALOUTWARD.toString());
		return new ResponseEntity(materialIssueResponse, HttpStatus.OK);
	}

	public ResponseEntity<MaterialIssueResponse> materialIssueSearchPost(
			@NotNull @ApiParam(value = "Unique id for a tenant.", required = true) @RequestParam(value = "tenantId", required = true) String tenantId,
			@ApiParam(value = "Parameter to carry Request metadata in the request body") @Valid @RequestBody org.egov.common.contract.request.RequestInfo requestInfo,
			@Size(max = 50) @ApiParam(value = "comma seperated list of Ids") @RequestParam(value = "ids", required = false) List<String> ids,
			@ApiParam(value = "issuing store of the MaterialIssue ") @RequestParam(value = "fromStore", required = false) String fromStore,
			@ApiParam(value = "receiving store of the MaterialIssue ") @RequestParam(value = "toStore", required = false) String toStore,
			@ApiParam(value = "issueNoteNumber  Auto generated number, read only ") @RequestParam(value = "issueNoteNumber", required = false) String issueNoteNumber,
			@ApiParam(value = "issue date of the MaterialIssue ") @RequestParam(value = "issueDate", required = false) Long issueDate,
			@ApiParam(value = "material issue status of the MaterialIssue ", allowableValues = "CREATED, APPROVED, REJECTED, CANCELED") @RequestParam(value = "materialIssueStatus", required = false) String materialIssueStatus,
			@ApiParam(value = "description of the MaterialIssue ") @RequestParam(value = "description", required = false) String description,
			@ApiParam(value = "issue purpose status of the materialissue ") @RequestParam(value = "issuePurpose", required = false) String issuePurpose,
			@ApiParam(value = "total issue value of the MaterialIssue ") @RequestParam(value = "totalIssueValue", required = false) BigDecimal totalIssueValue,
			@Min(0) @Max(100) @ApiParam(value = "Number of records returned.") @RequestParam(value = "pageSize", required = false) Integer pageSize,
			@ApiParam(value = "Page number") @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
			@ApiParam(value = "This takes any field from the Object seperated by comma and asc,desc keywords. example name asc,code desc or name,code or name,code desc") @RequestParam(value = "sortBy", required = false) String sortBy) {
		MaterialIssueSearchContract searchContract = new MaterialIssueSearchContract(tenantId, ids, fromStore, toStore,
				issueNoteNumber,issuePurpose,issueDate,null, materialIssueStatus, description, totalIssueValue, null ,pageNumber, sortBy,
				pageSize);
		MaterialIssueResponse materialIssueResponse = materialIssueService.search(searchContract, IssueTypeEnum.MATERIALOUTWARD.toString());
		return new ResponseEntity(materialIssueResponse, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<MaterialIssueResponse> materialIssueUpdatePost(
			@NotNull @ApiParam(value = "Unique id for a tenant.", required = true) @RequestParam(value = "tenantId", required = true) String tenantId,
			@ApiParam(value = "common Request info") @Valid @RequestBody MaterialIssueRequest indentIssueRequest) {
		MaterialIssueResponse materialIssueResponse = materialIssueService.update(indentIssueRequest,tenantId, IssueTypeEnum.MATERIALOUTWARD.toString());
		return new ResponseEntity(materialIssueResponse,HttpStatus.OK);
	}
	
	
}
