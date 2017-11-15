package org.egov.inv.api;

import io.swagger.annotations.ApiParam;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.egov.inv.domain.service.MaterialService;
import org.egov.inv.model.MaterialRequest;
import org.egov.inv.model.MaterialResponse;
import org.egov.inv.model.MaterialSearchRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.ObjectMapper;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-10-28T13:21:55.964+05:30")

@Controller
public class MaterialsApiController implements MaterialsApi {
    private final ObjectMapper objectMapper;

    private MaterialService materialService;


    public MaterialsApiController(ObjectMapper objectMapper,
                                  MaterialService materialService) {
        this.objectMapper = objectMapper;
        this.materialService = materialService;
    }

    public ResponseEntity<MaterialResponse> materialsCreatePost(@NotNull @ApiParam(value = "Unique id for a tenant.", required = true) @Valid @RequestParam(value = "tenantId", required = true) String tenantId,
                                                                @ApiParam(value = "Create  new") @Valid @RequestBody MaterialRequest materialRequest,
                                                                @RequestHeader(value = "Accept", required = false) String accept) throws Exception {
        return new ResponseEntity<MaterialResponse>(materialService.save(materialRequest, tenantId), HttpStatus.OK);
    }

    public ResponseEntity<MaterialResponse> materialsSearchPost(@NotNull @ApiParam(value = "Unique id for a tenant.", required = true) @Valid @RequestParam(value = "tenantId", required = true) String tenantId,
                                                                @ApiParam(value = "Parameter to carry Request metadata in the request body") @Valid @RequestBody org.egov.common.contract.request.RequestInfo requestInfo,
                                                                @Size(max = 50) @ApiParam(value = "comma seperated list of Ids") @Valid @RequestParam(value = "ids", required = false) List<String> ids,
                                                                @ApiParam(value = "code of the Material ") @Valid @RequestParam(value = "code", required = false) String code,
                                                                @ApiParam(value = "store of the Material ") @Valid @RequestParam(value = "store", required = false) String store,
                                                                @ApiParam(value = "name of the Material ") @Valid @RequestParam(value = "name", required = false) String name,
                                                                @ApiParam(value = "description of the Material ") @Valid @RequestParam(value = "description", required = false) String description,
                                                                @ApiParam(value = "old code of the Material ") @Valid @RequestParam(value = "oldCode", required = false) String oldCode,
                                                                @ApiParam(value = "material type of the Material ") @Valid @RequestParam(value = "materialType", required = false) String materialType,
                                                                @ApiParam(value = "base uom of the Material ") @Valid @RequestParam(value = "baseUom", required = false) Long baseUom,
                                                                @ApiParam(value = "inventory type of the Material ", allowableValues = "Asset, Consumable") @Valid @RequestParam(value = "inventoryType", required = false) String inventoryType,
                                                                @ApiParam(value = "status of the Material ", allowableValues = "Active, Withdrawn, Obsolete") @Valid @RequestParam(value = "status", required = false) String status,
                                                                @ApiParam(value = "purchase uom of the Material ") @Valid @RequestParam(value = "purchaseUom", required = false) Long purchaseUom,
                                                                @ApiParam(value = "expense account of the Material ") @Valid @RequestParam(value = "expenseAccount", required = false) Long expenseAccount,
                                                                @ApiParam(value = "min quantity of the Material ") @Valid @RequestParam(value = "minQuantity", required = false) Long minQuantity,
                                                                @ApiParam(value = "max quantity of the Material ") @Valid @RequestParam(value = "maxQuantity", required = false) Long maxQuantity,
                                                                @ApiParam(value = "staocking uom of the Material ") @Valid @RequestParam(value = "staockingUom", required = false) Long staockingUom,
                                                                @ApiParam(value = "material class of the Material ", allowableValues = "HighUsage, MediumUsage, LowUsage") @Valid @RequestParam(value = "materialClass", required = false) String materialClass,
                                                                @ApiParam(value = "reorder level of the Material ") @Valid @RequestParam(value = "reorderLevel", required = false) Long reorderLevel,
                                                                @ApiParam(value = "reorder quantity of the Material ") @Valid @RequestParam(value = "reorderQuantity", required = false) Long reorderQuantity,
                                                                @ApiParam(value = "material control type of the Material ", allowableValues = "LOTControl, Shelf_life_Control") @Valid @RequestParam(value = "materialControlType", required = false) String materialControlType,
                                                                @ApiParam(value = "model of the Material ") @Valid @RequestParam(value = "model", required = false) String model,
                                                                @ApiParam(value = "manufacture part no of the Material ") @Valid @RequestParam(value = "manufacturePartNo", required = false) String manufacturePartNo,
                                                                @ApiParam(value = "techincal specs of the Material ") @Valid @RequestParam(value = "techincalSpecs", required = false) String techincalSpecs,
                                                                @ApiParam(value = "terms of delivery of the Material ") @Valid @RequestParam(value = "termsOfDelivery", required = false) String termsOfDelivery,
                                                                @ApiParam(value = "override material control type of the Material ") @Valid @RequestParam(value = "overrideMaterialControlType", required = false) Boolean overrideMaterialControlType,
                                                                @ApiParam(value = "pageSize") @Valid @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                                @ApiParam(value = "offset") @Valid @RequestParam(value = "offset", required = false) Integer offset,
                                                                @ApiParam(value = "This takes any field from the Object seperated by comma and asc,desc keywords.   example name asc,code desc or name,code or name,code desc  ") @Valid @RequestParam(value = "sortBy", required = false) String sortBy,
                                                                @RequestHeader(value = "Accept", required = false) String accept) throws Exception {


        MaterialSearchRequest materialSearchRequest = MaterialSearchRequest.builder()
                .tenantId(tenantId)
                .ids(ids)
                .code(code)
                .store(store)
                .name(name)
                .description(description)
                .oldCode(oldCode)
                .materialType(materialType)
                .inventoryType(inventoryType)
                .status(status)
                .materialClass(materialClass)
                .materialControlType(materialControlType)
                .model(model)
                .manufacturePartNo(manufacturePartNo)
                .pageSize(pageSize)
                .offSet(offset)
                .sortBy(sortBy)
                .build();

        return new ResponseEntity<MaterialResponse>(materialService.search(materialSearchRequest, requestInfo), HttpStatus.OK);
    }

    public ResponseEntity<MaterialResponse> materialsUpdatePost(@NotNull @ApiParam(value = "Unique id for a tenant.", required = true) @Valid @RequestParam(value = "tenantId", required = true) String tenantId,
                                                                @ApiParam(value = "common Request info") @Valid @RequestBody MaterialRequest materialRequest,
                                                                @RequestHeader(value = "Accept", required = false) String accept) throws Exception {

        return new ResponseEntity<MaterialResponse>(materialService.update(materialRequest, tenantId), HttpStatus.OK);
    }

}
