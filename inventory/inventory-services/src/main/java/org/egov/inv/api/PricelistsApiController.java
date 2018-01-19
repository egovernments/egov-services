package org.egov.inv.api;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.egov.inv.domain.service.PriceListService;
import org.egov.inv.model.PriceListRequest;
import org.egov.inv.model.PriceListResponse;
import org.egov.inv.model.PriceListSearchRequest;
import org.egov.inv.model.RequestInfo;
import org.egov.inv.persistence.repository.PurchaseOrderJdbcRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.ApiParam;
@javax.annotation.Generated(value = "org.egov.inv.codegen.languages.SpringCodegen", date = "2017-11-08T13:51:07.770Z")

 
@Controller
public class PricelistsApiController implements PricelistsApi {

	 private final ObjectMapper objectMapper;

	    private PriceListService priceListService;
	    
	    private PurchaseOrderJdbcRepository purchaseOrderJdbcRepository;
	    
	    @Autowired
	    public PricelistsApiController(ObjectMapper objectMapper, PriceListService priceListService, PurchaseOrderJdbcRepository purchaseOrderJdbcRepository) {
			this.objectMapper = objectMapper;
			this.priceListService = priceListService;
			this.purchaseOrderJdbcRepository = purchaseOrderJdbcRepository;
	    }

    public ResponseEntity<PriceListResponse> pricelistsCreatePost( @NotNull@ApiParam(value = "Unique id for a tenant.", required = true) @RequestParam(value = "tenantId", required = true) String tenantId,
        @ApiParam(value = "Create new pricelist"  )  @Valid @RequestBody PriceListRequest priceListRequest) {
        // do some magic!
    	return new ResponseEntity<PriceListResponse>(priceListService.save(priceListRequest, tenantId), HttpStatus.OK);
    }

    public ResponseEntity<PriceListResponse> pricelistsGettenderusedquantityPost(@ApiParam(value = "Name of the material whose usedQuantity we want to find. ") @RequestParam(value = "material", required = false) String material,
            @ApiParam(value = "reference no of the priceList in which we want to find the usedQuantity ") @RequestParam(value = "priceListId", required = false) String priceListId,
            @ApiParam(value = "UOM in which usedQuantity response need to be sent. ") @RequestParam(value = "uom", required = false) String uom,
            @ApiParam(value = "tenantId for which the details apply ") @RequestParam(value = "tenantId", required = false) String tenantId) {
            // do some magic!
    	return new ResponseEntity<PriceListResponse>(priceListService.getTenderUsedQty(material, priceListId, uom, tenantId), HttpStatus.OK);
        }


    public ResponseEntity<PriceListResponse> pricelistsSearchPost( @NotNull@ApiParam(value = "Unique id for a tenant.", required = true) @RequestParam(value = "tenantId", required = true) String tenantId,
        @ApiParam(value = "Parameter to carry Request metadata in the request body"  )   @RequestBody RequestInfo requestInfo,
        @ApiParam(value = "Name of the vendor supplying materials required ") @RequestParam(value = "suppliers", required = false) List<String> suppliers,
        @ApiParam(value = "Name of the vendor supplying materials required ") @RequestParam(value = "supplierName", required = false) String supplierName,
        @ApiParam(value = "search on basis of ids ") @RequestParam(value = "ids", required = false) String ids,
        @ApiParam(value = "reference no of the material contract from the supplier ") @RequestParam(value = "rateContractNumber", required = false) String rateContractNumber,
        @ApiParam(value = "Agreement no with the supplier of materials ") @RequestParam(value = "agreementNumber", required = false) List<String> agreementNumber,
        @ApiParam(value = "select only given materil codes ") @RequestParam(value = "materialCode", required = false) String materialCode,
        @ApiParam(value = "contract date of the rate for item with the supplier.Date in epoc format. ") @RequestParam(value = "rateContractDate", required = false) Long rateContractDate,
        @ApiParam(value = "Date on which agreement done with supplier ") @RequestParam(value = "agreementDate", required = false) Long agreementDate,
        @ApiParam(value = "Date from which the agreement is valid with supplier ") @RequestParam(value = "agreementStartDate", required = false) Long agreementStartDate,
        @ApiParam(value = "Date on which the agreement expires with the supplier ") @RequestParam(value = "agreementEndDate", required = false) Long agreementEndDate,
        @ApiParam(value = "Date on which valid pricelist exists. This will be used to fetch valid pricelist for the given date. this date should be         greater than agreementStartDate and less than agreementEndDate ") @RequestParam(value = "asOnDate", required = false) Long asOnDate,
        @ApiParam(value = "type of the information about the material we are getting from the supplier ", allowableValues = "DGSC Rate Contract, ULB Rate Contract, One Time Tender, Quotation") @RequestParam(value = "rateType", required = false) String rateType,
         @Min(0) @Max(100)@ApiParam(value = "Number of records returned.", defaultValue = "20") @RequestParam(value = "pageSize", required = false, defaultValue="20") Integer pageSize,
        @ApiParam(value = "Page number", defaultValue = "1") @RequestParam(value = "pageNumber", required = false, defaultValue="1") Integer pageNumber,
        @ApiParam(value = "This takes any field from the Object seperated by comma and asc,desc keywords. example name asc,code desc or name,code or name,code desc", defaultValue = "id") @RequestParam(value = "sortBy", required = false, defaultValue="id") String sortBy) {	
    	PriceListSearchRequest priceListSearchRequest = PriceListSearchRequest.builder()
                 .tenantId(tenantId)
                // .ids(ids)
                 .suppliers(suppliers)
                 .supplierName(supplierName)
                 .rateContractNumber(rateContractNumber)
                 .rateContractDate(rateContractDate)
                 .agreementNumbers(agreementNumber)
                 .agreementDate(agreementDate)
                 .agreementStartDate(agreementStartDate)
                 .agreementEndDate(agreementEndDate)
                 .rateType(rateType)
                 .asOnDate(asOnDate)
                 .materialCode(materialCode)
                 .pageSize(pageSize)
                 .offSet(pageNumber-1)
                 .sortBy(sortBy)
                 .build();
    	 return new ResponseEntity<PriceListResponse>(priceListService.search(priceListSearchRequest, requestInfo), HttpStatus.OK);
    }

    public ResponseEntity<PriceListResponse> pricelistsUpdatePost( @NotNull@ApiParam(value = "Unique id for a tenant.", required = true) @RequestParam(value = "tenantId", required = true) String tenantId,
        @ApiParam(value = "common Request info"  )  @Valid @RequestBody PriceListRequest pricelistRequest) {
        // do some magic!
    	return new ResponseEntity<PriceListResponse>(priceListService.update(pricelistRequest, tenantId), HttpStatus.OK);
    }

}
