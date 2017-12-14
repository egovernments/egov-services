package org.egov.inv.api;

import io.swagger.annotations.ApiParam;
import org.egov.inv.domain.service.MaterialTypeStoreMappingService;
import org.egov.inv.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-12-14T09:31:14.044Z")

@Controller
public class MaterialtypestoremappingsApiController implements MaterialtypestoremappingsApi {


    @Autowired
    private MaterialTypeStoreMappingService materialTypeStoreMappingService;

    public ResponseEntity<MaterialTypeStoreResponse> materialtypestoremappingsCreatePost(@NotNull @ApiParam(value = "Unique id for a tenant.", required = true) @RequestParam(value = "tenantId", required = true) String tenantId,
                                                                                         @ApiParam(value = "This object holds the Material Type Store Mapping information.") @Valid @RequestBody MaterialTypeStoreRequest materialtypestoreRequest) {
        return new ResponseEntity<MaterialTypeStoreResponse>(materialTypeStoreMappingService.create(materialtypestoreRequest, tenantId), HttpStatus.OK);
    }

    public ResponseEntity<MaterialTypeStoreResponse> materialstoremappingSearchPost(@NotNull @ApiParam(value = "Unique id for a tenant.", required = true) @RequestParam(value = "tenantId", required = true) String tenantId,
                                                                                       @ApiParam(value = "Parameter to carry Request metadata in the request body") @Valid @RequestBody org.egov.common.contract.request.RequestInfo requestInfo,
                                                                                       @Size(max = 50) @ApiParam(value = "comma seperated list of Ids") @RequestParam(value = "ids", required = false) List<String> ids,
                                                                                       @ApiParam(value = "material type code of material type store mapping ") @RequestParam(value = "materialType", required = false) String materialType,
                                                                                       @ApiParam(value = "store code of material store mapping ") @RequestParam(value = "store", required = false) String store,
                                                                                       @ApiParam(value = "active flag of material store mapping ") @RequestParam(value = "active", required = false) Boolean active,
                                                                                       @Min(0) @Max(100) @ApiParam(value = "Number of records returned.", defaultValue = "20") @RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize,
                                                                                       @ApiParam(value = "offset") @RequestParam(value = "offset", required = false) Integer offset,
                                                                                       @ApiParam(value = "Page number", defaultValue = "1") @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                                                                                       @ApiParam(value = "This takes any field from the Object seperated by comma and asc,desc keywords. example name asc,code desc or name,code or name,code desc", defaultValue = "id") @RequestParam(value = "sortBy", required = false) String sortBy,
                                                                                       @RequestHeader(value = "Accept", required = false) String accept, BindingResult errors) throws Exception {
        MaterialTypeStoreMappingSearch materialTypeStoreMappingSearch = MaterialTypeStoreMappingSearch.builder()
                .ids(ids)
                .store(store)
                .materialType(materialType)
                .active(active)
                .pageSize(pageSize)
                .sortBy(sortBy)
                .tenantId(tenantId)
                .build();

        return new ResponseEntity<MaterialTypeStoreResponse>(materialTypeStoreMappingService.search(materialTypeStoreMappingSearch), HttpStatus.OK);
    }

    public ResponseEntity<MaterialTypeStoreResponse> materialtypestoremappingsUpdatePost(@NotNull @ApiParam(value = "Unique id for a tenant.", required = true) @RequestParam(value = "tenantId", required = true) String tenantId,
                                                                                         @ApiParam(value = "common Request info") @Valid @RequestBody MaterialTypeStoreRequest materialtypestoreRequest) {
        return new ResponseEntity<MaterialTypeStoreResponse>(materialTypeStoreMappingService.update(materialtypestoreRequest, tenantId), HttpStatus.OK);
    }

}
