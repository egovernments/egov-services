package org.egov.inv.api;

import org.egov.inv.model.ErrorRes;
import org.egov.inv.model.RequestInfo;
import org.egov.inv.model.StoreRequest;
import org.egov.inv.model.StoreResponse;

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
public class StoresApiController implements StoresApi {



    public ResponseEntity<StoreResponse> storesCreatePost( @NotNull@ApiParam(value = "Unique id for a tenant.", required = true) @RequestParam(value = "tenantId", required = true) String tenantId,
        @ApiParam(value = "Create  new"  )  @Valid @RequestBody StoreRequest storeRequest) {
        // do some magic!
        return new ResponseEntity<StoreResponse>(HttpStatus.OK);
    }

    public ResponseEntity<StoreResponse> storesSearchPost( @NotNull@ApiParam(value = "Unique id for a tenant.", required = true) @RequestParam(value = "tenantId", required = true) String tenantId,
        @ApiParam(value = "Parameter to carry Request metadata in the request body"  )  @Valid @RequestBody RequestInfo requestInfo,
         @Size(max=50)@ApiParam(value = "comma seperated list of Ids") @RequestParam(value = "ids", required = false) List<String> ids,
        @ApiParam(value = "code of the Store ") @RequestParam(value = "code", required = false) String code,
        @ApiParam(value = "name of the Store ") @RequestParam(value = "name", required = false) String name,
        @ApiParam(value = "description of the Store ") @RequestParam(value = "description", required = false) String description,
        @ApiParam(value = "department of the Store ") @RequestParam(value = "department", required = false) Long department,
        @ApiParam(value = "billing address of the Store ") @RequestParam(value = "billingAddress", required = false) String billingAddress,
        @ApiParam(value = "delivery address of the Store ") @RequestParam(value = "deliveryAddress", required = false) String deliveryAddress,
        @ApiParam(value = "contact no1 of the Store ") @RequestParam(value = "contactNo1", required = false) String contactNo1,
        @ApiParam(value = "contact no2 of the Store ") @RequestParam(value = "contactNo2", required = false) String contactNo2,
        @ApiParam(value = "email of the Store ") @RequestParam(value = "email", required = false) String email,
        @ApiParam(value = "store in charge of the Store ") @RequestParam(value = "storeInCharge", required = false) Long storeInCharge,
        @ApiParam(value = "is central store of the Store ") @RequestParam(value = "isCentralStore", required = false) Boolean isCentralStore,
        @ApiParam(value = "Whether Store is Active or not. If the value is TRUE, then Store is active,If the value is FALSE then Store is inactive,Default value is TRUE ") @RequestParam(value = "active", required = false) Boolean active,
         @Min(0) @Max(100)@ApiParam(value = "Number of records returned.", defaultValue = "20") @RequestParam(value = "pageSize", required = false, defaultValue="20") Integer pageSize,
        @ApiParam(value = "Page number", defaultValue = "1") @RequestParam(value = "pageNumber", required = false, defaultValue="1") Integer pageNumber,
        @ApiParam(value = "This takes any field from the Object seperated by comma and asc,desc keywords. example name asc,code desc or name,code or name,code desc", defaultValue = "id") @RequestParam(value = "sortBy", required = false, defaultValue="id") String sortBy) {
        // do some magic!
        return new ResponseEntity<StoreResponse>(HttpStatus.OK);
    }

    public ResponseEntity<StoreResponse> storesUpdatePost( @NotNull@ApiParam(value = "Unique id for a tenant.", required = true) @RequestParam(value = "tenantId", required = true) String tenantId,
        @ApiParam(value = "common Request info"  )  @Valid @RequestBody StoreRequest storeRequest) {
        // do some magic!
        return new ResponseEntity<StoreResponse>(HttpStatus.OK);
    }

}
