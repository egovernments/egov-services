package io.swagger.api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.egov.common.contract.response.ResponseInfo;
import org.egov.inv.domain.model.ReceiptNotesSearchCriteria;
import org.egov.inv.domain.service.ReceiptNoteApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.annotations.ApiParam;
import io.swagger.model.MaterialReceipt;
import io.swagger.model.MaterialReceiptRequest;
import io.swagger.model.MaterialReceiptResponse;
import io.swagger.model.Pagination;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-10T10:57:23.411Z")

@Controller
public class ReceiptnotesApiController implements ReceiptnotesApi {
	    
	    @Autowired
		private ReceiptNoteApiService receiptNoteApiService;

		
		private final HttpServletRequest request;

	    public ReceiptnotesApiController( HttpServletRequest request) {
	        this.request = request;
	    }


    public ResponseEntity<MaterialReceiptResponse> receiptnotesCreatePost(
    		@ApiParam(value = "Details for the new material receipt." ,required=true )
    		@Valid @RequestParam(value = "tenantId", required = true) String tenantId,
    		@Valid @RequestBody MaterialReceiptRequest materialReceipt) 
    	{
        	List<MaterialReceipt> material = receiptNoteApiService.create(materialReceipt, tenantId);
        	MaterialReceiptResponse materialResponse = buildRecieptResponse(material, materialReceipt.getRequestInfo());
    		
            return new ResponseEntity<MaterialReceiptResponse>(materialResponse,HttpStatus.OK);
        }
    
    public ResponseEntity<MaterialReceiptResponse> receiptnotesUpdatePost(
    		@ApiParam(value = "Details for the new material receipts." ,required=true ) 
    		@Valid @RequestParam(value = "tenantId", required = true) String tenantId,
    		@Valid @RequestBody MaterialReceiptRequest materialReceipt) {
    	List<MaterialReceipt> material = receiptNoteApiService.update(materialReceipt, tenantId);
    	MaterialReceiptResponse materialResponse = buildRecieptResponse(material, materialReceipt.getRequestInfo());
        return new ResponseEntity<MaterialReceiptResponse>(materialResponse,HttpStatus.OK);
    }
    
    public ResponseEntity<MaterialReceiptResponse> receiptnotesSearchPost(@ApiParam(value = "Request header for the service request details." ,required=true )  @Valid @RequestBody org.egov.common.contract.request.RequestInfo requestInfo,
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
    	ReceiptNotesSearchCriteria receiptSearch = ReceiptNotesSearchCriteria
    			.builder()
    			.tenantId(tenantId)
    			.mrnNumber(mrnNumber)
				.receiptType(receiptType)
				.mrnStatus(mrnStatus)
				.receivingStore(receivingStore)
				.supplierCode(supplierCode)
				.receiptDateFrom(receiptDateFrom)
				.receiptDateT0(receiptDateT0)
				.pageSize(pageSize)
				.pageNumber(pageNumber)
				.sortBy(sortBy)
				.build();
    	Pagination<MaterialReceipt> materialHeader = receiptNoteApiService.search(receiptSearch);
    	MaterialReceiptResponse response = new MaterialReceiptResponse();
		response.setMaterialReceipt(materialHeader.getPagedData());
		response.setResponseInfo(getResponseInfo(requestInfo));
		

           return new ResponseEntity<MaterialReceiptResponse>(response,HttpStatus.OK);
           }

    
    
    private MaterialReceiptResponse buildRecieptResponse(List<MaterialReceipt> material, org.egov.common.contract.request.RequestInfo requestInfo) 
    {
		return MaterialReceiptResponse.builder().resposneInfo(getResponseInfo(requestInfo)).materialReceipt(material).build();
	}

	private ResponseInfo getResponseInfo(org.egov.common.contract.request.RequestInfo requestInfo) 
	{
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer())
				.resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
	}

}
