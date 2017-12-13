package org.egov.inv.api;

import io.swagger.annotations.ApiParam;
import org.egov.inv.domain.MiscellaneousReceiptNoteService;
import org.egov.inv.model.MaterialReceiptRequest;
import org.egov.inv.model.MaterialReceiptResponse;
import org.egov.inv.model.MaterialReceiptSearch;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

import static java.util.Arrays.asList;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-12-07T12:10:19.906Z")

@Controller
public class MiscellaneousreceiptnotesApiController implements MiscellaneousreceiptnotesApi {

    private MiscellaneousReceiptNoteService miscellaneousReceiptNoteService;

    public MiscellaneousreceiptnotesApiController(MiscellaneousReceiptNoteService miscellaneousReceiptNoteService) {
        this.miscellaneousReceiptNoteService = miscellaneousReceiptNoteService;
    }

    public ResponseEntity<MaterialReceiptResponse> miscellaneousreceiptnotesCreatePost(@NotNull @ApiParam(value = "Unique id for a tenant.", required = true) @RequestParam(value = "tenantId", required = true) String tenantId,
                                                                                       @ApiParam(value = "Details for the new material receipt.", required = true) @Valid @RequestBody MaterialReceiptRequest materialReceipt) {

        MaterialReceiptResponse materialReceiptResponse = miscellaneousReceiptNoteService.create(materialReceipt, tenantId);
        return new ResponseEntity<MaterialReceiptResponse>(materialReceiptResponse, HttpStatus.OK);
    }

    public ResponseEntity<MaterialReceiptResponse> miscellaneousreceiptnotesSearchPost(@ApiParam(value = "Request header for the service request details.", required = true) @Valid @RequestBody org.egov.common.contract.request.RequestInfo requestInfo,
                                                                                       @NotNull @ApiParam(value = "Unique id for a tenant.", required = true) @RequestParam(value = "tenantId", required = true) String tenantId,
                                                                                       @Size(max = 100) @ApiParam(value = "Pass List of unique mrn number(s) then the API will returns list of receipts.") @RequestParam(value = "mrnNumber", required = false) List<String> mrnNumber,
                                                                                       @Size(max = 3) @ApiParam(value = "Mention the type of the receipt.") @RequestParam(value = "receiptType", required = false) List<String> receiptType,
                                                                                       @ApiParam(value = "Unique status code of the receipt.") @RequestParam(value = "mrnStatus", required = false) String mrnStatus,
                                                                                       @ApiParam(value = "The store code from which the receipt was received.") @RequestParam(value = "receivingStore", required = false) String receivingStore,
                                                                                       @ApiParam(value = "Unique Supplier code from which the receipt was made.") @RequestParam(value = "supplierCode", required = false) String supplierCode,
                                                                                       @ApiParam(value = "From which receipt date onwards the data needs to be fetched.") @RequestParam(value = "receiptDateFrom", required = false) Long receiptDateFrom,
                                                                                       @ApiParam(value = "Till which receipt date the data needs to be fetched.") @RequestParam(value = "receiptDateT0", required = false) Long receiptDateT0,
                                                                                       @Min(0) @Max(100) @ApiParam(value = "Number of records returned.", defaultValue = "20") @RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize,
                                                                                       @ApiParam(value = "Page number", defaultValue = "1") @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                                                                                       @ApiParam(value = "This takes any field from the Object seperated by comma and asc,desc keywords. example name asc,code desc or name,code or name,code desc", defaultValue = "id") @RequestParam(value = "sortBy", required = false, defaultValue = "id") String sortBy) {
        MaterialReceiptSearch materialReceiptSearch = MaterialReceiptSearch.builder()
                .tenantId(tenantId)
                .mrnNumber(mrnNumber)
                .receiptType(receiptType)
                .mrnStatus(null != mrnStatus ? asList(mrnStatus) : null)
                .receivingStore(receivingStore)
                .supplierCode(supplierCode)
                .receiptDate(receiptDateFrom)
                .receiptDate(receiptDateT0)
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .build();
        MaterialReceiptResponse materialReceiptResponse = miscellaneousReceiptNoteService.search(materialReceiptSearch);
        return new ResponseEntity<MaterialReceiptResponse>(materialReceiptResponse,HttpStatus.OK);
    }

    public ResponseEntity<MaterialReceiptResponse> miscellaneousreceiptnotesUpdatePost(@NotNull @ApiParam(value = "Unique id for a tenant.", required = true) @RequestParam(value = "tenantId", required = true) String tenantId,
                                                                                       @ApiParam(value = "Details for the new material receipts.", required = true) @Valid @RequestBody MaterialReceiptRequest materialReceipt) {
        MaterialReceiptResponse materialReceiptResponse = miscellaneousReceiptNoteService.update(materialReceipt, tenantId);
        return new ResponseEntity<MaterialReceiptResponse>(HttpStatus.OK);
    }

}
