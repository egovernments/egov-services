package io.swagger.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiParam;
import io.swagger.model.MaterialStoreMapping;
import io.swagger.model.MaterialStoreMappingRequest;
import io.swagger.model.MaterialStoreMappingResponse;
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
import javax.validation.constraints.NotNull;
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

        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<MaterialStoreMappingResponse>(objectMapper.readValue("{  \"materialStoreMappings\" : [ {    \"material\" : \"material\",    \"chartofAccount\" : {      \"glCode\" : \"glCode\",      \"auditDetails\" : {        \"lastModifiedTime\" : 1,        \"createdBy\" : \"createdBy\",        \"lastModifiedBy\" : \"lastModifiedBy\",        \"createdTime\" : 6      },      \"tenantId\" : \"tenantId\",      \"name\" : \"name\",      \"id\" : \"id\"    },    \"auditDetails\" : {      \"lastModifiedTime\" : 1,      \"createdBy\" : \"createdBy\",      \"lastModifiedBy\" : \"lastModifiedBy\",      \"createdTime\" : 6    },    \"active\" : true,    \"id\" : \"id\",    \"store\" : \"store\"  }, {    \"material\" : \"material\",    \"chartofAccount\" : {      \"glCode\" : \"glCode\",      \"auditDetails\" : {        \"lastModifiedTime\" : 1,        \"createdBy\" : \"createdBy\",        \"lastModifiedBy\" : \"lastModifiedBy\",        \"createdTime\" : 6      },      \"tenantId\" : \"tenantId\",      \"name\" : \"name\",      \"id\" : \"id\"    },    \"auditDetails\" : {      \"lastModifiedTime\" : 1,      \"createdBy\" : \"createdBy\",      \"lastModifiedBy\" : \"lastModifiedBy\",      \"createdTime\" : 6    },    \"active\" : true,    \"id\" : \"id\",    \"store\" : \"store\"  } ],  \"page\" : {    \"totalResults\" : 1,    \"offSet\" : 1,    \"totalPages\" : 1,    \"pageSize\" : 6,    \"currentPage\" : 7  },  \"responseInfo\" : {    \"ver\" : \"ver\",    \"resMsgId\" : \"resMsgId\",    \"msgId\" : \"msgId\",    \"apiId\" : \"apiId\",    \"ts\" : 0,    \"status\" : \"SUCCESSFUL\"  }}", MaterialStoreMappingResponse.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<MaterialStoreMappingResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        List<MaterialStoreMapping> materialStoreMappings = materialStoreMappingService.create(materialStoreMappingRequest, tenantId, errors);
        return new ResponseEntity<MaterialStoreMappingResponse>(buildMaterialStoreMappingResponse(materialStoreMappings,
                materialStoreMappingRequest.getRequestInfo()), HttpStatus.OK);
    }

    public ResponseEntity<MaterialStoreMappingResponse> materialstoremappingUpdatePost(@NotNull @ApiParam(value = "Unique id for a tenant.", required = true) @Valid @RequestParam(value = "tenantId", required = true) String tenantId, @ApiParam(value = "common Request info") @Valid @RequestBody MaterialStoreMappingRequest materialStoreMappingRequest) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<MaterialStoreMappingResponse>(objectMapper.readValue("{  \"materialStoreMappings\" : [ {    \"material\" : \"material\",    \"chartofAccount\" : {      \"glCode\" : \"glCode\",      \"auditDetails\" : {        \"lastModifiedTime\" : 1,        \"createdBy\" : \"createdBy\",        \"lastModifiedBy\" : \"lastModifiedBy\",        \"createdTime\" : 6      },      \"tenantId\" : \"tenantId\",      \"name\" : \"name\",      \"id\" : \"id\"    },    \"auditDetails\" : {      \"lastModifiedTime\" : 1,      \"createdBy\" : \"createdBy\",      \"lastModifiedBy\" : \"lastModifiedBy\",      \"createdTime\" : 6    },    \"active\" : true,    \"id\" : \"id\",    \"store\" : \"store\"  }, {    \"material\" : \"material\",    \"chartofAccount\" : {      \"glCode\" : \"glCode\",      \"auditDetails\" : {        \"lastModifiedTime\" : 1,        \"createdBy\" : \"createdBy\",        \"lastModifiedBy\" : \"lastModifiedBy\",        \"createdTime\" : 6      },      \"tenantId\" : \"tenantId\",      \"name\" : \"name\",      \"id\" : \"id\"    },    \"auditDetails\" : {      \"lastModifiedTime\" : 1,      \"createdBy\" : \"createdBy\",      \"lastModifiedBy\" : \"lastModifiedBy\",      \"createdTime\" : 6    },    \"active\" : true,    \"id\" : \"id\",    \"store\" : \"store\"  } ],  \"page\" : {    \"totalResults\" : 1,    \"offSet\" : 1,    \"totalPages\" : 1,    \"pageSize\" : 6,    \"currentPage\" : 7  },  \"responseInfo\" : {    \"ver\" : \"ver\",    \"resMsgId\" : \"resMsgId\",    \"msgId\" : \"msgId\",    \"apiId\" : \"apiId\",    \"ts\" : 0,    \"status\" : \"SUCCESSFUL\"  }}", MaterialStoreMappingResponse.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<MaterialStoreMappingResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<MaterialStoreMappingResponse>(HttpStatus.NOT_IMPLEMENTED);
    }

    private MaterialStoreMappingResponse buildMaterialStoreMappingResponse(List<MaterialStoreMapping> materialStoreMappings, RequestInfo requestInfo) {
        return MaterialStoreMappingResponse.builder()
                .responseInfo(inventoryUtilityService.getResponseInfo(requestInfo))
                .materialStoreMappings(materialStoreMappings)
                .build();
    }
}
