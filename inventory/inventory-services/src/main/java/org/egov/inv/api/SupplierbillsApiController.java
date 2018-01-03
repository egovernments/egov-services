package org.egov.inv.api;

import io.swagger.annotations.ApiParam;
import org.egov.common.contract.request.RequestInfo;
import org.egov.inv.domain.service.SupplierBillService;
import org.egov.inv.model.SupplierBillRequest;
import org.egov.inv.model.SupplierBillResponse;
import org.egov.inv.model.SupplierBillSearch;
import org.springframework.beans.factory.annotation.Autowired;
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

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-12-28T09:20:06.607Z")

@Controller
public class SupplierbillsApiController implements SupplierbillsApi {


    private SupplierBillService supplierBillService;

    @Autowired
    public SupplierbillsApiController(SupplierBillService supplierBillService) {
        this.supplierBillService = supplierBillService;
    }

    public ResponseEntity<SupplierBillResponse> supplierbillsCreatePost(@NotNull @ApiParam(value = "Unique id for a tenant.", required = true) @RequestParam(value = "tenantId", required = true) String tenantId,
                                                                        @ApiParam(value = "Details for the new Supplier Bill .", required = true) @Valid @RequestBody SupplierBillRequest supplierBillRequest) {
        return new ResponseEntity<SupplierBillResponse>(supplierBillService.create(supplierBillRequest, tenantId), HttpStatus.OK);
    }

    public ResponseEntity<SupplierBillResponse> supplierbillsSearchPost(@ApiParam(value = "Request header for the service request details.", required = true) @Valid @RequestBody org.egov.common.contract.request.RequestInfo requestInfo,
                                                                        @NotNull @ApiParam(value = "Unique id for a tenant.", required = true) @RequestParam(value = "tenantId", required = true) String tenantId,
                                                                        @Size(max = 50) @ApiParam(value = "comma seperated list of Ids") @RequestParam(value = "ids", required = false) List<String> ids,
                                                                        @ApiParam(value = "The store code of the supplier bill") @RequestParam(value = "store", required = false) String store,
                                                                        @ApiParam(value = "invoice number of the supplier bill.") @RequestParam(value = "invoiceNumber", required = false) String invoiceNumber,
                                                                        @ApiParam(value = "invoice date of the supplier bill.") @RequestParam(value = "invoiceDate", required = false) Long invoiceDate,
                                                                        @ApiParam(value = "approved date of the supplier bill") @RequestParam(value = "approvedDate", required = false) Long approvedDate,
                                                                        @Min(0) @Max(100) @ApiParam(value = "Number of records returned.", defaultValue = "20") @RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize,
                                                                        @ApiParam(value = "Page number", defaultValue = "1") @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                                                                        @ApiParam(value = "This takes any field from the Object seperated by comma and asc,desc keywords. example name asc,code desc or name,code or name,code desc") @RequestParam(value = "sortBy", required = false, defaultValue = "id") String sortBy) {
        SupplierBillSearch supplierBillSearch = SupplierBillSearch.builder()
                .ids(ids)
                .invoiceDate(invoiceDate)
                .invoiceNumber(invoiceNumber)
                .approvedDate(approvedDate)
                .pageSize(pageSize)
                .store(store)
                .tenantId(tenantId)
                .pageNumber(pageNumber)
                .build();

        return new ResponseEntity<SupplierBillResponse>(supplierBillService.search(supplierBillSearch), HttpStatus.OK);
    }

    public ResponseEntity<SupplierBillResponse> supplierbillsUpdatePost(@NotNull @ApiParam(value = "Unique id for a tenant.", required = true) @RequestParam(value = "tenantId", required = true) String tenantId,
                                                                        @ApiParam(value = "Details for the new material receipts.", required = true) @Valid @RequestBody SupplierBillRequest supplierBillRequest) {
        return new ResponseEntity<SupplierBillResponse>(supplierBillService.update(supplierBillRequest, tenantId), HttpStatus.OK);
    }

}
