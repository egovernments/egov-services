package org.egov.inv.api;

import org.egov.inv.model.ErrorRes;
import org.egov.inv.model.RequestInfo;
import org.egov.inv.model.TransferInwardRequest;
import org.egov.inv.model.TransferInwardResponse;

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
public class TransferinwardsApiController implements TransferinwardsApi {



    public ResponseEntity<TransferInwardResponse> transferinwardsCreatePost( @NotNull@ApiParam(value = "Unique id for a tenant.", required = true) @RequestParam(value = "tenantId", required = true) String tenantId,
        @ApiParam(value = "Create  new"  )  @Valid @RequestBody TransferInwardRequest transferInwardRequest) {
        // do some magic!
        return new ResponseEntity<TransferInwardResponse>(HttpStatus.OK);
    }

    public ResponseEntity<TransferInwardResponse> transferinwardsSearchPost( @NotNull@ApiParam(value = "Unique id for a tenant.", required = true) @RequestParam(value = "tenantId", required = true) String tenantId,
        @ApiParam(value = "Parameter to carry Request metadata in the request body"  )  @Valid @RequestBody RequestInfo requestInfo,
         @Size(max=50)@ApiParam(value = "comma seperated list of Ids") @RequestParam(value = "ids", required = false) List<String> ids,
        @ApiParam(value = "receipt date of the TransferInward ") @RequestParam(value = "receiptDate", required = false) Long receiptDate,
        @ApiParam(value = "transfer out ward of the TransferInward ") @RequestParam(value = "transferOutWard", required = false) Long transferOutWard,
        @ApiParam(value = "description of the TransferInward ") @RequestParam(value = "description", required = false) String description,
        @ApiParam(value = "inward note number of the TransferInward ") @RequestParam(value = "inwardNoteNumber", required = false) String inwardNoteNumber,
        @ApiParam(value = "inward note status of the TransferInward ", allowableValues = "CREATED, APPROVED, REJECTED, CANCELED") @RequestParam(value = "inwardNoteStatus", required = false) String inwardNoteStatus,
        @ApiParam(value = "state id of the TransferInward ") @RequestParam(value = "stateId", required = false) Long stateId,
         @Min(0) @Max(100)@ApiParam(value = "Number of records returned.", defaultValue = "20") @RequestParam(value = "pageSize", required = false, defaultValue="20") Integer pageSize,
        @ApiParam(value = "Page number", defaultValue = "1") @RequestParam(value = "pageNumber", required = false, defaultValue="1") Integer pageNumber,
        @ApiParam(value = "This takes any field from the Object seperated by comma and asc,desc keywords. example name asc,code desc or name,code or name,code desc", defaultValue = "id") @RequestParam(value = "sortBy", required = false, defaultValue="id") String sortBy) {
        // do some magic!
        return new ResponseEntity<TransferInwardResponse>(HttpStatus.OK);
    }

    public ResponseEntity<TransferInwardResponse> transferinwardsUpdatePost( @NotNull@ApiParam(value = "Unique id for a tenant.", required = true) @RequestParam(value = "tenantId", required = true) String tenantId,
        @ApiParam(value = "common Request info"  )  @Valid @RequestBody TransferInwardRequest transferInwardRequest) {
        // do some magic!
        return new ResponseEntity<TransferInwardResponse>(HttpStatus.OK);
    }

}
