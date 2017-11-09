package org.egov.inv.api;

import org.egov.inv.model.ErrorRes;
import org.egov.inv.model.RequestInfo;
import org.egov.inv.model.TransferOutwardRequest;
import org.egov.inv.model.TransferOutwardResponse;

import io.swagger.annotations.*;

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
public class TransferoutwardsApiController implements TransferoutwardsApi {



    public ResponseEntity<TransferOutwardResponse> transferoutwardsCreatePost( @NotNull@ApiParam(value = "Unique id for a tenant.", required = true) @RequestParam(value = "tenantId", required = true) String tenantId,
        @ApiParam(value = "Create  new"  )  @Valid @RequestBody TransferOutwardRequest transferOutwardRequest) {
        // do some magic!
        return new ResponseEntity<TransferOutwardResponse>(HttpStatus.OK);
    }

    public ResponseEntity<TransferOutwardResponse> transferoutwardsSearchPost( @NotNull@ApiParam(value = "Unique id for a tenant.", required = true) @RequestParam(value = "tenantId", required = true) String tenantId,
        @ApiParam(value = "Parameter to carry Request metadata in the request body"  )  @Valid @RequestBody RequestInfo requestInfo,
         @Size(max=50)@ApiParam(value = "comma seperated list of Ids") @RequestParam(value = "ids", required = false) List<String> ids,
        @ApiParam(value = "store of the TransferOutward ") @RequestParam(value = "store", required = false) Long store,
        @ApiParam(value = "issue date of the TransferOutward ") @RequestParam(value = "issueDate", required = false) Long issueDate,
        @ApiParam(value = "issued to employee of the TransferOutward ") @RequestParam(value = "issuedToEmployee", required = false) String issuedToEmployee,
        @ApiParam(value = "description of the TransferOutward ") @RequestParam(value = "description", required = false) String description,
        @ApiParam(value = "outward note number of the TransferOutward ") @RequestParam(value = "outwardNoteNumber", required = false) String outwardNoteNumber,
        @ApiParam(value = "outward note status of the TransferOutward ", allowableValues = "CREATED, APPROVED, REJECTED, CANCELED") @RequestParam(value = "outwardNoteStatus", required = false) String outwardNoteStatus,
        @ApiParam(value = "indent of the TransferOutward ") @RequestParam(value = "indent", required = false) Long indent,
        @ApiParam(value = "state id of the TransferOutward ") @RequestParam(value = "stateId", required = false) Long stateId,
         @Min(0) @Max(100)@ApiParam(value = "Number of records returned.", defaultValue = "20") @RequestParam(value = "pageSize", required = false, defaultValue="20") Integer pageSize,
        @ApiParam(value = "Page number", defaultValue = "1") @RequestParam(value = "pageNumber", required = false, defaultValue="1") Integer pageNumber,
        @ApiParam(value = "This takes any field from the Object seperated by comma and asc,desc keywords. example name asc,code desc or name,code or name,code desc", defaultValue = "id") @RequestParam(value = "sortBy", required = false, defaultValue="id") String sortBy) {
        // do some magic!
        return new ResponseEntity<TransferOutwardResponse>(HttpStatus.OK);
    }

    public ResponseEntity<TransferOutwardResponse> transferoutwardsUpdatePost( @NotNull@ApiParam(value = "Unique id for a tenant.", required = true) @RequestParam(value = "tenantId", required = true) String tenantId,
        @ApiParam(value = "common Request info"  )  @Valid @RequestBody TransferOutwardRequest transferOutwardRequest) {
        // do some magic!
        return new ResponseEntity<TransferOutwardResponse>(HttpStatus.OK);
    }

}
