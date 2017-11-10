package io.swagger.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiParam;
import io.swagger.model.*;
import org.egov.common.contract.request.RequestInfo;
import org.egov.inv.domain.service.InventoryUtilityService;
import org.egov.inv.domain.service.MaterialStoreMappingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.IOException;
import java.util.List;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-02T16:27:56.269+05:30")

@Controller
public class MaterialStoreMappingApiController implements MaterialStoreMappingApi {

    private static final Logger log = LoggerFactory.getLogger(MaterialStoreMappingApiController.class);

    private MaterialStoreMappingService materialStoreMappingService;

    private InventoryUtilityService inventoryUtilityService;

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Autowired
    public MaterialStoreMappingApiController(ObjectMapper objectMapper, HttpServletRequest request,
                                             MaterialStoreMappingService materialStoreMappingService,
                                             InventoryUtilityService inventoryUtilityService) {
        this.objectMapper = objectMapper;
        this.request = request;
        this.materialStoreMappingService = materialStoreMappingService;
        this.inventoryUtilityService = inventoryUtilityService;
    }

    public ResponseEntity<MaterialStoreMappingResponse> materialstoremappingCreatePost(@NotNull @ApiParam(value = "Unique id for a tenant.", required = true) @Valid @RequestParam(value = "tenantId", required = true) String tenantId,
                                                                                       @ApiParam(value = "Create  new") @Valid @RequestBody MaterialStoreMappingRequest materialStoreMappingRequest,
                                                                                       @RequestHeader(value = "Accept", required = false) String accept, BindingResult errors) throws Exception {

        List<MaterialStoreMapping> materialStoreMappings = materialStoreMappingService.create(materialStoreMappingRequest, tenantId);
        return new ResponseEntity<MaterialStoreMappingResponse>(buildMaterialStoreMappingResponse(materialStoreMappings,
                materialStoreMappingRequest.getRequestInfo()), HttpStatus.OK);
    }

    public ResponseEntity<MaterialStoreMappingResponse> materialstoremappingSearchPost(@NotNull @ApiParam(value = "Unique id for a tenant.", required = true) @RequestParam(value = "tenantId", required = true) String tenantId,
                                                                                       @ApiParam(value = "Parameter to carry Request metadata in the request body") @Valid @RequestBody RequestInfo requestInfo,
                                                                                       @Size(max = 50) @ApiParam(value = "comma seperated list of Ids") @RequestParam(value = "ids", required = false) List<String> ids,
                                                                                       @ApiParam(value = "material code of material store mapping ") @RequestParam(value = "material", required = false) String material,
                                                                                       @ApiParam(value = "store code of material store mapping ") @RequestParam(value = "store", required = false) String store,
                                                                                       @ApiParam(value = "active flag of material store mapping ") @RequestParam(value = "active", required = false) Boolean active,
                                                                                       @Min(0) @Max(100) @ApiParam(value = "Number of records returned.", defaultValue = "20") @RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize,
                                                                                       @ApiParam(value = "offset") @RequestParam(value = "offset", required = false) Integer offset,
                                                                                       @ApiParam(value = "Page number", defaultValue = "1") @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                                                                                       @ApiParam(value = "This takes any field from the Object seperated by comma and asc,desc keywords. example name asc,code desc or name,code or name,code desc", defaultValue = "id") @RequestParam(value = "sortBy", required = false) String sortBy,
                                                                                       @RequestHeader(value = "Accept", required = false) String accept, BindingResult errors) throws Exception {
        MaterialStoreMappingSearchRequest materialStoreMappingSearchRequest = MaterialStoreMappingSearchRequest.builder()
                .ids(ids)
                .store(store)
                .material(material)
                .active(active)
                .pageSize(pageSize)
                .offset(offset)
                .sortBy(sortBy)
                .tenantId(tenantId)
                .build();
        Pagination<MaterialStoreMapping> materialStoreMappingPagination = materialStoreMappingService.search(materialStoreMappingSearchRequest);
        MaterialStoreMappingResponse buildMaterialStoreMappingResponse = MaterialStoreMappingResponse.builder()
                .responseInfo(inventoryUtilityService.getResponseInfo(requestInfo))
                .materialStoreMappings(materialStoreMappingPagination.getPagedData())
                .build();

        return new ResponseEntity<MaterialStoreMappingResponse>(buildMaterialStoreMappingResponse, HttpStatus.OK);
    }

    public ResponseEntity<MaterialStoreMappingResponse> materialstoremappingUpdatePost(@NotNull @ApiParam(value = "Unique id for a tenant.", required = true) @Valid @RequestParam(value = "tenantId", required = true) String tenantId,
                                                                                       @ApiParam(value = "common Request info") @Valid @RequestBody MaterialStoreMappingRequest materialStoreMappingRequest,
                                                                                       @RequestHeader(value = "Accept", required = false) String accept, BindingResult errors) throws Exception {


        List<MaterialStoreMapping> materialStoreMappings = materialStoreMappingService.update(materialStoreMappingRequest, tenantId);
        return new ResponseEntity<MaterialStoreMappingResponse>(buildMaterialStoreMappingResponse(materialStoreMappings,
                materialStoreMappingRequest.getRequestInfo()), HttpStatus.OK);
    }

    private MaterialStoreMappingResponse buildMaterialStoreMappingResponse(List<MaterialStoreMapping> materialStoreMappings, RequestInfo requestInfo) {
        return MaterialStoreMappingResponse.builder()
                .responseInfo(inventoryUtilityService.getResponseInfo(requestInfo))
                .materialStoreMappings(materialStoreMappings)
                .build();
    }


}
