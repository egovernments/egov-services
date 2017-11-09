package org.egov.inv.api;

import org.egov.inv.model.ErrorRes;
import org.egov.inv.model.RequestInfo;
import org.egov.inv.model.ScrapRequest;
import org.egov.inv.model.ScrapResponse;

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
public class ScrapsApiController implements ScrapsApi {



    public ResponseEntity<ScrapResponse> scrapsCreatePost( @NotNull@ApiParam(value = "Unique id for a tenant.", required = true) @RequestParam(value = "tenantId", required = true) String tenantId,
        @ApiParam(value = "Create  new"  )  @Valid @RequestBody ScrapRequest scrapRequest) {
        // do some magic!
        return new ResponseEntity<ScrapResponse>(HttpStatus.OK);
    }

    public ResponseEntity<ScrapResponse> scrapsSearchPost( @NotNull@ApiParam(value = "Unique id for a tenant.", required = true) @RequestParam(value = "tenantId", required = true) String tenantId,
        @ApiParam(value = "Parameter to carry Request metadata in the request body"  )  @Valid @RequestBody RequestInfo requestInfo,
         @Size(max=50)@ApiParam(value = "comma seperated list of Ids") @RequestParam(value = "ids", required = false) List<String> ids,
        @ApiParam(value = "store of the Scrap ") @RequestParam(value = "store", required = false) Long store,
        @ApiParam(value = "scrapNumber  Auto generated number, read only ") @RequestParam(value = "scrapNumber", required = false) String scrapNumber,
        @ApiParam(value = "scrap date of the Scrap ") @RequestParam(value = "scrapDate", required = false) Long scrapDate,
        @ApiParam(value = "description of the Scrap ") @RequestParam(value = "description", required = false) String description,
        @ApiParam(value = "scrap status of the Scrap ", allowableValues = "CREATED, APPROVED, REJECTED, CANCELED") @RequestParam(value = "scrapStatus", required = false) String scrapStatus,
        @ApiParam(value = "state id of the Scrap ") @RequestParam(value = "stateId", required = false) Long stateId,
         @Min(0) @Max(100)@ApiParam(value = "Number of records returned.", defaultValue = "20") @RequestParam(value = "pageSize", required = false, defaultValue="20") Integer pageSize,
        @ApiParam(value = "Page number", defaultValue = "1") @RequestParam(value = "pageNumber", required = false, defaultValue="1") Integer pageNumber,
        @ApiParam(value = "This takes any field from the Object seperated by comma and asc,desc keywords. example name asc,code desc or name,code or name,code desc", defaultValue = "id") @RequestParam(value = "sortBy", required = false, defaultValue="id") String sortBy) {
        // do some magic!
        return new ResponseEntity<ScrapResponse>(HttpStatus.OK);
    }

    public ResponseEntity<ScrapResponse> scrapsUpdatePost( @NotNull@ApiParam(value = "Unique id for a tenant.", required = true) @RequestParam(value = "tenantId", required = true) String tenantId,
        @ApiParam(value = "common Request info"  )  @Valid @RequestBody ScrapRequest scrapRequest) {
        // do some magic!
        return new ResponseEntity<ScrapResponse>(HttpStatus.OK);
    }

}
