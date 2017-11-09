package org.egov.inv.api;

import org.egov.inv.model.ErrorRes;
import org.egov.inv.model.PriceListRequest;
import org.egov.inv.model.PriceListResponse;
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
public class PricelistsApiController implements PricelistsApi {



    public ResponseEntity<PriceListResponse> pricelistsCreatePost( @NotNull@ApiParam(value = "Unique id for a tenant.", required = true) @RequestParam(value = "tenantId", required = true) String tenantId,
        @ApiParam(value = "Create new pricelist"  )  @Valid @RequestBody PriceListRequest priceListRequest) {
        // do some magic!
        return new ResponseEntity<PriceListResponse>(HttpStatus.OK);
    }

    public ResponseEntity<PriceListResponse> pricelistsSearchPost( @NotNull@ApiParam(value = "Unique id for a tenant.", required = true) @RequestParam(value = "tenantId", required = true) String tenantId,
        @ApiParam(value = "Parameter to carry Request metadata in the request body"  )  @Valid @RequestBody RequestInfo requestInfo,
        @ApiParam(value = "Name of the vendor supplying materials required ") @RequestParam(value = "supplierName", required = false) List<String> supplierName,
        @ApiParam(value = "reference no of the material contract from the supplier ") @RequestParam(value = "rateContractNumber", required = false) String rateContractNumber,
        @ApiParam(value = "Agreement no with the supplier of materials ") @RequestParam(value = "agreementNumber", required = false) List<String> agreementNumber,
        @ApiParam(value = "contract date of the rate for item with the supplier.Date in epoc format. ") @RequestParam(value = "rateContractDate", required = false) Long rateContractDate,
        @ApiParam(value = "Date on which agreement done with supplier ") @RequestParam(value = "agreementDate", required = false) Long agreementDate,
        @ApiParam(value = "Date from which the agreement is valid with supplier ") @RequestParam(value = "agreementStartDate", required = false) Long agreementStartDate,
        @ApiParam(value = "Date on which the agreement expires with the supplier ") @RequestParam(value = "agreementEndDate", required = false) Long agreementEndDate,
        @ApiParam(value = "type of the information about the material we are getting from the supplier ", allowableValues = "DGSC_RATECONTRACT, ULB_RATE_CONTRACT, ONE_TIME_TENDER, QUOTATION") @RequestParam(value = "rateType", required = false) String rateType,
         @Min(0) @Max(100)@ApiParam(value = "Number of records returned.", defaultValue = "20") @RequestParam(value = "pageSize", required = false, defaultValue="20") Integer pageSize,
        @ApiParam(value = "Page number", defaultValue = "1") @RequestParam(value = "pageNumber", required = false, defaultValue="1") Integer pageNumber,
        @ApiParam(value = "This takes any field from the Object seperated by comma and asc,desc keywords. example name asc,code desc or name,code or name,code desc", defaultValue = "id") @RequestParam(value = "sortBy", required = false, defaultValue="id") String sortBy) {
        // do some magic!
        return new ResponseEntity<PriceListResponse>(HttpStatus.OK);
    }

    public ResponseEntity<PriceListResponse> pricelistsUpdatePost( @NotNull@ApiParam(value = "Unique id for a tenant.", required = true) @RequestParam(value = "tenantId", required = true) String tenantId,
        @ApiParam(value = "common Request info"  )  @Valid @RequestBody PriceListRequest pricelistRequest) {
        // do some magic!
        return new ResponseEntity<PriceListResponse>(HttpStatus.OK);
    }

}
