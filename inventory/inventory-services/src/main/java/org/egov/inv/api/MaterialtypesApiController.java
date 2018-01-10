package org.egov.inv.api;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.egov.common.contract.request.RequestInfo;
import org.egov.inv.domain.service.MaterialTypeService;
import org.egov.inv.model.MaterialTypeRequest;
import org.egov.inv.model.MaterialTypeResponse;
import org.egov.inv.model.MaterialTypeSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.annotations.ApiParam;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-01-10T06:55:29.202Z")

@Controller
public class MaterialtypesApiController implements MaterialtypesApi {

@Autowired
private MaterialTypeService materialTypeService;


    public ResponseEntity<MaterialTypeResponse> materialtypesCreatePost( @NotNull@ApiParam(value = "Unique id for a tenant.", required = true) @RequestParam(value = "tenantId", required = true) String tenantId,
        @ApiParam(value = "Create  new"  )  @Valid @RequestBody MaterialTypeRequest materialTypeRequest) {
       
        return new ResponseEntity<MaterialTypeResponse>(materialTypeService.save(materialTypeRequest, tenantId),HttpStatus.OK);
    }

    public ResponseEntity<MaterialTypeResponse> materialtypesSearchPost( @NotNull@ApiParam(value = "Unique id for a tenant.", required = true) @RequestParam(value = "tenantId", required = true) String tenantId,
        @ApiParam(value = "Parameter to carry Request metadata in the request body"  )  @Valid @RequestBody RequestInfo requestInfo,
         @Size(max=50)@ApiParam(value = "comma seperated list of Ids") @RequestParam(value = "ids", required = false) List<String> ids,
        @ApiParam(value = "code of the Material Type ") @RequestParam(value = "code", required = false) String code,
        @ApiParam(value = "name of the Material Type ") @RequestParam(value = "name", required = false) String name,
        @ApiParam(value = "store to which material type is mapped ") @RequestParam(value = "store", required = false) String store,
         @Min(0) @Max(100)@ApiParam(value = "Number of records returned.", defaultValue = "20") @RequestParam(value = "pageSize", required = false, defaultValue="20") Integer pageSize,
        @ApiParam(value = "Page number", defaultValue = "1") @RequestParam(value = "pageNumber", required = false, defaultValue="1") Integer pageNumber,
        @ApiParam(value = "This takes any field from the Object seperated by comma and asc,desc keywords. example name asc,code desc or name,code or name,code desc", defaultValue = "id") @RequestParam(value = "sortBy", required = false, defaultValue="id") String sortBy) {
         
    	MaterialTypeSearch materialTypeSearch = MaterialTypeSearch.builder()
    											.code(code)
    											.ids(ids)
    											.name(name)
    											.store(store)
    											.tenantId(tenantId)
    											.build();
        return new ResponseEntity<MaterialTypeResponse>(materialTypeService.search(materialTypeSearch, requestInfo), HttpStatus.OK);
    }

    public ResponseEntity<MaterialTypeResponse> materialtypesUpdatePost( @NotNull@ApiParam(value = "Unique id for a tenant.", required = true) @RequestParam(value = "tenantId", required = true) String tenantId,
        @ApiParam(value = "common Request info"  )  @Valid @RequestBody MaterialTypeRequest materialTypeRequest) {
        
        return new ResponseEntity<MaterialTypeResponse>(materialTypeService.update(materialTypeRequest, tenantId),HttpStatus.OK);
    }

}
