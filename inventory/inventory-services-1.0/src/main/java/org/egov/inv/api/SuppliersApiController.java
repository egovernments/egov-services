package org.egov.inv.api;

import org.egov.inv.model.ErrorRes;
import org.egov.inv.model.RequestInfo;
import org.egov.inv.model.SupplierRequest;
import org.egov.inv.model.SupplierResponse;

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
public class SuppliersApiController implements SuppliersApi {



    public ResponseEntity<SupplierResponse> suppliersCreatePost( @NotNull@ApiParam(value = "Unique id for a tenant.", required = true) @RequestParam(value = "tenantId", required = true) String tenantId,
        @ApiParam(value = "Create  new"  )  @Valid @RequestBody SupplierRequest supplierRequest) {
        // do some magic!
        return new ResponseEntity<SupplierResponse>(HttpStatus.OK);
    }

    public ResponseEntity<SupplierResponse> suppliersSearchPost( @NotNull@ApiParam(value = "Unique id for a tenant.", required = true) @RequestParam(value = "tenantId", required = true) String tenantId,
        @ApiParam(value = "Parameter to carry Request metadata in the request body"  )  @Valid @RequestBody RequestInfo requestInfo,
         @Size(max=50)@ApiParam(value = "comma seperated list of Ids") @RequestParam(value = "ids", required = false) List<String> ids,
        @ApiParam(value = "list of codes  of the Supplier ") @RequestParam(value = "codes", required = false) List<String> codes,
        @ApiParam(value = "name of the Supplier ") @RequestParam(value = "name", required = false) String name,
        @ApiParam(value = "type of the Supplier ") @RequestParam(value = "type", required = false) String type,
        @ApiParam(value = "status     ") @RequestParam(value = "status", required = false) String status,
        @ApiParam(value = "inActiveDate ") @RequestParam(value = "inActiveDate", required = false) Long inActiveDate,
        @ApiParam(value = "active or inactive status of the supplier ") @RequestParam(value = "active", required = false) Boolean active,
        @ApiParam(value = "contactNo of the Supplier ") @RequestParam(value = "contactNo", required = false) String contactNo,
        @ApiParam(value = "faxNo of the Supplier ") @RequestParam(value = "faxNo", required = false) String faxNo,
        @ApiParam(value = "website of the Supplier ") @RequestParam(value = "website", required = false) String website,
        @ApiParam(value = "email of the Supplier ") @RequestParam(value = "email", required = false) String email,
        @ApiParam(value = "panNo of the Supplier ") @RequestParam(value = "panNo", required = false) String panNo,
        @ApiParam(value = "tinNo of the Supplier ") @RequestParam(value = "tinNo", required = false) String tinNo,
        @ApiParam(value = "cstNo of the Supplier ") @RequestParam(value = "cstNo", required = false) String cstNo,
        @ApiParam(value = "vatNo  ") @RequestParam(value = "vatNo", required = false) String vatNo,
        @ApiParam(value = "gstNo     ") @RequestParam(value = "gstNo", required = false) String gstNo,
        @ApiParam(value = "contactPerson      ") @RequestParam(value = "contactPerson", required = false) String contactPerson,
        @ApiParam(value = "contactPersonNo      ") @RequestParam(value = "contactPersonNo", required = false) String contactPersonNo,
        @ApiParam(value = "code of the bank ") @RequestParam(value = "bankCode", required = false) String bankCode,
        @ApiParam(value = "name of the bankBranch ") @RequestParam(value = "bankBranch", required = false) String bankBranch,
        @ApiParam(value = "account number of the bank account ") @RequestParam(value = "bankAccNo", required = false) String bankAccNo,
        @ApiParam(value = "ifsc code of the bank account ") @RequestParam(value = "bankIfsc", required = false) String bankIfsc,
         @Min(0) @Max(100)@ApiParam(value = "Number of records returned.", defaultValue = "20") @RequestParam(value = "pageSize", required = false, defaultValue="20") Integer pageSize,
        @ApiParam(value = "Page number", defaultValue = "1") @RequestParam(value = "pageNumber", required = false, defaultValue="1") Integer pageNumber,
        @ApiParam(value = "This takes any field from the Object seperated by comma and asc,desc keywords. example name asc,code desc or name,code or name,code desc", defaultValue = "id") @RequestParam(value = "sortBy", required = false, defaultValue="id") String sortBy) {
        // do some magic!
        return new ResponseEntity<SupplierResponse>(HttpStatus.OK);
    }

    public ResponseEntity<SupplierResponse> suppliersUpdatePost( @NotNull@ApiParam(value = "Unique id for a tenant.", required = true) @RequestParam(value = "tenantId", required = true) String tenantId,
        @ApiParam(value = "common Request info"  )  @Valid @RequestBody SupplierRequest supplierRequest) {
        // do some magic!
        return new ResponseEntity<SupplierResponse>(HttpStatus.OK);
    }

}
