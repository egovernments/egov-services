package org.egov.inv.api;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.egov.inv.domain.service.IndentService;
import org.egov.inv.model.IndentRequest;
import org.egov.inv.model.IndentResponse;
import org.egov.inv.model.IndentSearch;
import org.egov.inv.model.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.annotations.ApiParam;
@javax.annotation.Generated(value = "org.egov.inv.codegen.languages.SpringCodegen", date = "2017-11-08T13:51:07.770Z")

@Controller
public class IndentsApiController implements IndentsApi {
    @Autowired
    private IndentService indentService;


    public ResponseEntity<IndentResponse> indentsCreatePost( @NotNull@ApiParam(value = "Unique id for a tenant.", required = true) @RequestParam(value = "tenantId", required = true) String tenantId,
        @ApiParam(value = "Create  new Indent"  )  @Valid @RequestBody IndentRequest indentRequest) {
        IndentResponse response = indentService.create(indentRequest);
        return   new ResponseEntity(response,HttpStatus.OK);
        
    }

    public ResponseEntity<IndentResponse> indentsSearchPost( @NotNull @ApiParam(value = "Unique id for a tenant.", required = true) @RequestParam(value = "tenantId", required = true) String tenantId,
        @ApiParam(value = "Parameter to carry Request metadata in the request body"  )  @Valid @RequestBody RequestInfo requestInfo,
         @Size(max=50)@ApiParam(value = "comma seperated list of Ids") @RequestParam(value = "ids", required = false) List<String> ids,
        @ApiParam(value = "issue store of the Indent ") @RequestParam(value = "issueStore", required = false) Long issueStore,
        @ApiParam(value = "indent date of the Indent ") @RequestParam(value = "indentDate", required = false) Long indentDate,
        @ApiParam(value = "indentNumber  Auto generated number, read only <ULB short code><Store Code><fin. Year><serial No.> ") @RequestParam(value = "indentNumber", required = false) String indentNumber,
        @ApiParam(value = "indent purpose of the Indent ", allowableValues = "Consumption, RepairsAndMaintenance, Capital, MaterialTransferNote") @RequestParam(value = "indentPurpose", required = false) String indentPurpose,
        @ApiParam(value = "description of the Indent ") @RequestParam(value = "description", required = false) String description,
        @ApiParam(value = "indent status of the Indent ", allowableValues = "CREATED, APPROVED, REJECTED, CANCELED") @RequestParam(value = "indentStatus", required = false) String indentStatus,
        @ApiParam(value = "totalIndentValue  denormalized value from Indent Material ") @RequestParam(value = "totalIndentValue", required = false) BigDecimal totalIndentValue,
         @Min(0) @Max(100)@ApiParam(value = "Number of records returned.", defaultValue = "20") @RequestParam(value = "pageSize", required = false, defaultValue="20") Integer pageSize,
        @ApiParam(value = "Page number", defaultValue = "1") @RequestParam(value = "pageNumber", required = false, defaultValue="1") Integer pageNumber,
        @ApiParam(value = "This takes any field from the Object seperated by comma and asc,desc keywords. example name asc,code desc or name,code or name,code desc", defaultValue = "id") @RequestParam(value = "sortBy", required = false, defaultValue="id") String sortBy) {
       
    	IndentSearch is=	new IndentSearch(tenantId,ids,issueStore,indentDate,indentNumber,
    			indentPurpose,description,indentStatus,totalIndentValue,pageSize,pageNumber,null, null, null, null, null, null);
    	 IndentResponse response = indentService.search(is);
         return   new ResponseEntity(response,HttpStatus.OK);
    }

    

	public ResponseEntity<IndentResponse> indentsUpdatePost( @NotNull@ApiParam(value = "Unique id for a tenant.", required = true) @RequestParam(value = "tenantId", required = true) String tenantId,
        @ApiParam(value = "common Request info"  )  @Valid @RequestBody IndentRequest indentRequest) {
		 IndentResponse response = indentService.update(indentRequest);
	        return   new ResponseEntity(response,HttpStatus.OK);
    }

}
