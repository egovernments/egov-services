package org.egov.inv.api;

import org.egov.inv.model.ErrorRes;
import org.egov.inv.model.RequestInfo;
import org.egov.inv.model.TransferIndentNoteRequest;
import org.egov.inv.model.TransferIndentNoteResponse;

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
public class TransferindentnotesApiController implements TransferindentnotesApi {



    public ResponseEntity<TransferIndentNoteResponse> transferindentnotesCreatePost( @NotNull@ApiParam(value = "Unique id for a tenant.", required = true) @RequestParam(value = "tenantId", required = true) String tenantId,
        @ApiParam(value = "Create  new"  )  @Valid @RequestBody TransferIndentNoteRequest transferIndentNoteRequest) {
        // do some magic!
        return new ResponseEntity<TransferIndentNoteResponse>(HttpStatus.OK);
    }

    public ResponseEntity<TransferIndentNoteResponse> transferindentnotesSearchPost( @NotNull@ApiParam(value = "Unique id for a tenant.", required = true) @RequestParam(value = "tenantId", required = true) String tenantId,
        @ApiParam(value = "Parameter to carry Request metadata in the request body"  )  @Valid @RequestBody RequestInfo requestInfo,
         @Size(max=50)@ApiParam(value = "comma seperated list of Ids") @RequestParam(value = "ids", required = false) List<String> ids,
        @ApiParam(value = "indent number of the Transfer Indent Note ") @RequestParam(value = "indentNumber", required = false) String indentNumber,
        @ApiParam(value = "indent store of the Transfer Indent Note ") @RequestParam(value = "indentStore", required = false) Long indentStore,
        @ApiParam(value = "state id of the TransferIndentNote ") @RequestParam(value = "stateId", required = false) Long stateId,
         @Min(0) @Max(100)@ApiParam(value = "Number of records returned.", defaultValue = "20") @RequestParam(value = "pageSize", required = false, defaultValue="20") Integer pageSize,
        @ApiParam(value = "Page number", defaultValue = "1") @RequestParam(value = "pageNumber", required = false, defaultValue="1") Integer pageNumber,
        @ApiParam(value = "This takes any field from the Object seperated by comma and asc,desc keywords. example name asc,code desc or name,code or name,code desc", defaultValue = "id") @RequestParam(value = "sortBy", required = false, defaultValue="id") String sortBy) {
        // do some magic!
        return new ResponseEntity<TransferIndentNoteResponse>(HttpStatus.OK);
    }

    public ResponseEntity<TransferIndentNoteResponse> transferindentnotesUpdatePost( @NotNull@ApiParam(value = "Unique id for a tenant.", required = true) @RequestParam(value = "tenantId", required = true) String tenantId,
        @ApiParam(value = "common Request info"  )  @Valid @RequestBody TransferIndentNoteRequest transferIndentNoteRequest) {
        // do some magic!
        return new ResponseEntity<TransferIndentNoteResponse>(HttpStatus.OK);
    }

}
