package org.egov.inv.api;

import org.egov.inv.model.ErrorRes;
import org.egov.inv.model.MaterialReceiptRequest;
import org.egov.inv.model.MaterialReceiptResponse;
import org.egov.inv.model.RequestInfo;

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
public class ReceiptnotesApiController implements ReceiptnotesApi {



    public ResponseEntity<MaterialReceiptResponse> receiptnotesCreatePost(@ApiParam(value = "Details for the new material receipt." ,required=true )  @Valid @RequestBody MaterialReceiptRequest materialReceipt) {
        // do some magic!
        return new ResponseEntity<MaterialReceiptResponse>(HttpStatus.OK);
    }

    public ResponseEntity<MaterialReceiptResponse> receiptnotesSearchPost(@ApiParam(value = "Request header for the service request details." ,required=true )  @Valid @RequestBody RequestInfo requestInfo,
         @NotNull@ApiParam(value = "Unique id for a tenant.", required = true) @RequestParam(value = "tenantId", required = true) String tenantId,
         @Size(max=100)@ApiParam(value = "Pass List of unique mrn number(s) then the API will returns list of receipts.") @RequestParam(value = "mrnNumber", required = false) List<String> mrnNumber,
         @Size(max=3)@ApiParam(value = "Mention the type of the receipt.") @RequestParam(value = "receiptType", required = false) List<String> receiptType,
        @ApiParam(value = "Unique status code of the receipt.") @RequestParam(value = "mrnStatus", required = false) String mrnStatus,
        @ApiParam(value = "The store code from which the receipt was received.") @RequestParam(value = "receivingStore", required = false) String receivingStore,
        @ApiParam(value = "Unique Supplier code from which the receipt was made.") @RequestParam(value = "supplierCode", required = false) String supplierCode,
        @ApiParam(value = "From which receipt date onwards the data needs to be fetched.") @RequestParam(value = "receiptDateFrom", required = false) Long receiptDateFrom,
        @ApiParam(value = "Till which receipt date the data needs to be fetched.") @RequestParam(value = "receiptDateT0", required = false) Long receiptDateT0,
         @Min(0) @Max(100)@ApiParam(value = "Number of records returned.", defaultValue = "20") @RequestParam(value = "pageSize", required = false, defaultValue="20") Integer pageSize,
        @ApiParam(value = "Page number", defaultValue = "1") @RequestParam(value = "pageNumber", required = false, defaultValue="1") Integer pageNumber,
        @ApiParam(value = "This takes any field from the Object seperated by comma and asc,desc keywords. example name asc,code desc or name,code or name,code desc", defaultValue = "id") @RequestParam(value = "sortBy", required = false, defaultValue="id") String sortBy) {
        // do some magic!
        return new ResponseEntity<MaterialReceiptResponse>(HttpStatus.OK);
    }

    public ResponseEntity<MaterialReceiptResponse> receiptnotesUpdatePost(@ApiParam(value = "Details for the new material receipts." ,required=true )  @Valid @RequestBody MaterialReceiptRequest materialReceipt) {
        // do some magic!
        return new ResponseEntity<MaterialReceiptResponse>(HttpStatus.OK);
    }

}
