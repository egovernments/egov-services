package io.swagger.api;

import io.swagger.model.ErrorRes;
import io.swagger.model.RequestInfo;
import io.swagger.model.StoreRequest;
import io.swagger.model.StoreResponse;

import io.swagger.annotations.*;

import org.egov.inv.domain.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.validation.constraints.*;
import javax.validation.Valid;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-10-28T13:21:55.964+05:30")

@Controller
public class StoresApiController implements StoresApi {
	
	@Autowired
	private StoreService storesService;
	
    private final ObjectMapper objectMapper;

    public StoresApiController(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public ResponseEntity<StoreResponse> storesCreatePost( @NotNull@ApiParam(value = "Unique id for a tenant.", required = true) @Valid @RequestParam(value = "tenantId", required = true) String tenantId,
        @ApiParam(value = "Create  new"  )  @Valid @RequestBody StoreRequest storeRequest,
        @RequestHeader(value = "Accept", required = false) String accept) throws Exception {
    	storesService.create(storeRequest , tenantId);

        if (accept != null && accept.contains("application/json")) {
            return new ResponseEntity<StoreResponse>(objectMapper.readValue("{  \"stores\" : [ {    \"code\" : \"code\",    \"isCentralStore\" : true,    \"contactNo1\" : \"contactNo1\",    \"contactNo2\" : \"contactNo2\",    \"description\" : \"description\",    \"active\" : true,    \"storeInCharge\" : {      \"code\" : \"code\",      \"auditDetails\" : {        \"lastModifiedTime\" : 1,        \"createdBy\" : \"createdBy\",        \"lastModifiedBy\" : \"lastModifiedBy\",        \"createdTime\" : 6      },      \"tenantId\" : \"tenantId\",      \"name\" : \"name\",      \"id\" : \"id\"    },    \"deliveryAddress\" : \"deliveryAddress\",    \"auditDetails\" : {      \"lastModifiedTime\" : 1,      \"createdBy\" : \"createdBy\",      \"lastModifiedBy\" : \"lastModifiedBy\",      \"createdTime\" : 6    },    \"tenantId\" : \"tenantId\",    \"name\" : \"name\",    \"id\" : \"id\",    \"billingAddress\" : \"billingAddress\",    \"department\" : {      \"code\" : \"code\",      \"auditDetails\" : {        \"lastModifiedTime\" : 1,        \"createdBy\" : \"createdBy\",        \"lastModifiedBy\" : \"lastModifiedBy\",        \"createdTime\" : 6      },      \"tenantId\" : \"tenantId\",      \"name\" : \"name\",      \"active\" : true,      \"id\" : \"id\"    },    \"email\" : \"email\"  }, {    \"code\" : \"code\",    \"isCentralStore\" : true,    \"contactNo1\" : \"contactNo1\",    \"contactNo2\" : \"contactNo2\",    \"description\" : \"description\",    \"active\" : true,    \"storeInCharge\" : {      \"code\" : \"code\",      \"auditDetails\" : {        \"lastModifiedTime\" : 1,        \"createdBy\" : \"createdBy\",        \"lastModifiedBy\" : \"lastModifiedBy\",        \"createdTime\" : 6      },      \"tenantId\" : \"tenantId\",      \"name\" : \"name\",      \"id\" : \"id\"    },    \"deliveryAddress\" : \"deliveryAddress\",    \"auditDetails\" : {      \"lastModifiedTime\" : 1,      \"createdBy\" : \"createdBy\",      \"lastModifiedBy\" : \"lastModifiedBy\",      \"createdTime\" : 6    },    \"tenantId\" : \"tenantId\",    \"name\" : \"name\",    \"id\" : \"id\",    \"billingAddress\" : \"billingAddress\",    \"department\" : {      \"code\" : \"code\",      \"auditDetails\" : {        \"lastModifiedTime\" : 1,        \"createdBy\" : \"createdBy\",        \"lastModifiedBy\" : \"lastModifiedBy\",        \"createdTime\" : 6      },      \"tenantId\" : \"tenantId\",      \"name\" : \"name\",      \"active\" : true,      \"id\" : \"id\"    },    \"email\" : \"email\"  } ],  \"page\" : {    \"totalResults\" : 1,    \"offSet\" : 1,    \"totalPages\" : 1,    \"pageSize\" : 6,    \"currentPage\" : 7  },  \"responseInfo\" : {    \"ver\" : \"ver\",    \"resMsgId\" : \"resMsgId\",    \"msgId\" : \"msgId\",    \"apiId\" : \"apiId\",    \"ts\" : 0,    \"status\" : \"SUCCESSFUL\"  }}", StoreResponse.class), HttpStatus.OK);
        }

        return new ResponseEntity<StoreResponse>(HttpStatus.OK);
    }

    public ResponseEntity<StoreResponse> storesSearchPost( @NotNull@ApiParam(value = "Unique id for a tenant.", required = true) @Valid @RequestParam(value = "tenantId", required = true) String tenantId,
        @ApiParam(value = "Parameter to carry Request metadata in the request body"  )  @Valid @RequestBody RequestInfo requestInfo,
         @Size(max=50)@ApiParam(value = "comma seperated list of Ids") @Valid @RequestParam(value = "ids", required = false) List<String> ids,
        @ApiParam(value = "code of the Store ") @Valid @RequestParam(value = "code", required = false) String code,
        @ApiParam(value = "name of the Store ") @Valid @RequestParam(value = "name", required = false) String name,
        @ApiParam(value = "description of the Store ") @Valid @RequestParam(value = "description", required = false) String description,
        @ApiParam(value = "department of the Store ") @Valid @RequestParam(value = "department", required = false) Long department,
        @ApiParam(value = "billing address of the Store ") @Valid @RequestParam(value = "billingAddress", required = false) String billingAddress,
        @ApiParam(value = "delivery address of the Store ") @Valid @RequestParam(value = "deliveryAddress", required = false) String deliveryAddress,
        @ApiParam(value = "contact no1 of the Store ") @Valid @RequestParam(value = "contactNo1", required = false) String contactNo1,
        @ApiParam(value = "contact no2 of the Store ") @Valid @RequestParam(value = "contactNo2", required = false) String contactNo2,
        @ApiParam(value = "email of the Store ") @Valid @RequestParam(value = "email", required = false) String email,
        @ApiParam(value = "store in charge of the Store ") @Valid @RequestParam(value = "storeInCharge", required = false) Long storeInCharge,
        @ApiParam(value = "is central store of the Store ") @Valid @RequestParam(value = "isCentralStore", required = false) Boolean isCentralStore,
        @ApiParam(value = "Whether Store is Active or not. If the value is TRUE, then Store is active,If the value is FALSE then Store is inactive,Default value is TRUE ") @Valid @RequestParam(value = "active", required = false) Boolean active,
        @ApiParam(value = "pageSize") @Valid @RequestParam(value = "pageSize", required = false) Integer pageSize,
        @ApiParam(value = "offset") @Valid @RequestParam(value = "offset", required = false) Integer offset,
        @ApiParam(value = "This takes any field from the Object seperated by comma and asc,desc keywords.   example name asc,code desc or name,code or name,code desc  ") @Valid @RequestParam(value = "sortBy", required = false) String sortBy,
        @RequestHeader(value = "Accept", required = false) String accept) throws Exception {
        // do some magic!

        if (accept != null && accept.contains("application/json")) {
            return new ResponseEntity<StoreResponse>(objectMapper.readValue("{  \"stores\" : [ {    \"code\" : \"code\",    \"isCentralStore\" : true,    \"contactNo1\" : \"contactNo1\",    \"contactNo2\" : \"contactNo2\",    \"description\" : \"description\",    \"active\" : true,    \"storeInCharge\" : {      \"code\" : \"code\",      \"auditDetails\" : {        \"lastModifiedTime\" : 1,        \"createdBy\" : \"createdBy\",        \"lastModifiedBy\" : \"lastModifiedBy\",        \"createdTime\" : 6      },      \"tenantId\" : \"tenantId\",      \"name\" : \"name\",      \"id\" : \"id\"    },    \"deliveryAddress\" : \"deliveryAddress\",    \"auditDetails\" : {      \"lastModifiedTime\" : 1,      \"createdBy\" : \"createdBy\",      \"lastModifiedBy\" : \"lastModifiedBy\",      \"createdTime\" : 6    },    \"tenantId\" : \"tenantId\",    \"name\" : \"name\",    \"id\" : \"id\",    \"billingAddress\" : \"billingAddress\",    \"department\" : {      \"code\" : \"code\",      \"auditDetails\" : {        \"lastModifiedTime\" : 1,        \"createdBy\" : \"createdBy\",        \"lastModifiedBy\" : \"lastModifiedBy\",        \"createdTime\" : 6      },      \"tenantId\" : \"tenantId\",      \"name\" : \"name\",      \"active\" : true,      \"id\" : \"id\"    },    \"email\" : \"email\"  }, {    \"code\" : \"code\",    \"isCentralStore\" : true,    \"contactNo1\" : \"contactNo1\",    \"contactNo2\" : \"contactNo2\",    \"description\" : \"description\",    \"active\" : true,    \"storeInCharge\" : {      \"code\" : \"code\",      \"auditDetails\" : {        \"lastModifiedTime\" : 1,        \"createdBy\" : \"createdBy\",        \"lastModifiedBy\" : \"lastModifiedBy\",        \"createdTime\" : 6      },      \"tenantId\" : \"tenantId\",      \"name\" : \"name\",      \"id\" : \"id\"    },    \"deliveryAddress\" : \"deliveryAddress\",    \"auditDetails\" : {      \"lastModifiedTime\" : 1,      \"createdBy\" : \"createdBy\",      \"lastModifiedBy\" : \"lastModifiedBy\",      \"createdTime\" : 6    },    \"tenantId\" : \"tenantId\",    \"name\" : \"name\",    \"id\" : \"id\",    \"billingAddress\" : \"billingAddress\",    \"department\" : {      \"code\" : \"code\",      \"auditDetails\" : {        \"lastModifiedTime\" : 1,        \"createdBy\" : \"createdBy\",        \"lastModifiedBy\" : \"lastModifiedBy\",        \"createdTime\" : 6      },      \"tenantId\" : \"tenantId\",      \"name\" : \"name\",      \"active\" : true,      \"id\" : \"id\"    },    \"email\" : \"email\"  } ],  \"page\" : {    \"totalResults\" : 1,    \"offSet\" : 1,    \"totalPages\" : 1,    \"pageSize\" : 6,    \"currentPage\" : 7  },  \"responseInfo\" : {    \"ver\" : \"ver\",    \"resMsgId\" : \"resMsgId\",    \"msgId\" : \"msgId\",    \"apiId\" : \"apiId\",    \"ts\" : 0,    \"status\" : \"SUCCESSFUL\"  }}", StoreResponse.class), HttpStatus.OK);
        }

        return new ResponseEntity<StoreResponse>(HttpStatus.OK);
    }

    public ResponseEntity<StoreResponse> storesUpdatePost( @NotNull@ApiParam(value = "Unique id for a tenant.", required = true) @Valid @RequestParam(value = "tenantId", required = true) String tenantId,
        @ApiParam(value = "common Request info"  )  @Valid @RequestBody StoreRequest storeRequest,
        @RequestHeader(value = "Accept", required = false) String accept) throws Exception {
        // do some magic!

        if (accept != null && accept.contains("application/json")) {
            return new ResponseEntity<StoreResponse>(objectMapper.readValue("{  \"stores\" : [ {    \"code\" : \"code\",    \"isCentralStore\" : true,    \"contactNo1\" : \"contactNo1\",    \"contactNo2\" : \"contactNo2\",    \"description\" : \"description\",    \"active\" : true,    \"storeInCharge\" : {      \"code\" : \"code\",      \"auditDetails\" : {        \"lastModifiedTime\" : 1,        \"createdBy\" : \"createdBy\",        \"lastModifiedBy\" : \"lastModifiedBy\",        \"createdTime\" : 6      },      \"tenantId\" : \"tenantId\",      \"name\" : \"name\",      \"id\" : \"id\"    },    \"deliveryAddress\" : \"deliveryAddress\",    \"auditDetails\" : {      \"lastModifiedTime\" : 1,      \"createdBy\" : \"createdBy\",      \"lastModifiedBy\" : \"lastModifiedBy\",      \"createdTime\" : 6    },    \"tenantId\" : \"tenantId\",    \"name\" : \"name\",    \"id\" : \"id\",    \"billingAddress\" : \"billingAddress\",    \"department\" : {      \"code\" : \"code\",      \"auditDetails\" : {        \"lastModifiedTime\" : 1,        \"createdBy\" : \"createdBy\",        \"lastModifiedBy\" : \"lastModifiedBy\",        \"createdTime\" : 6      },      \"tenantId\" : \"tenantId\",      \"name\" : \"name\",      \"active\" : true,      \"id\" : \"id\"    },    \"email\" : \"email\"  }, {    \"code\" : \"code\",    \"isCentralStore\" : true,    \"contactNo1\" : \"contactNo1\",    \"contactNo2\" : \"contactNo2\",    \"description\" : \"description\",    \"active\" : true,    \"storeInCharge\" : {      \"code\" : \"code\",      \"auditDetails\" : {        \"lastModifiedTime\" : 1,        \"createdBy\" : \"createdBy\",        \"lastModifiedBy\" : \"lastModifiedBy\",        \"createdTime\" : 6      },      \"tenantId\" : \"tenantId\",      \"name\" : \"name\",      \"id\" : \"id\"    },    \"deliveryAddress\" : \"deliveryAddress\",    \"auditDetails\" : {      \"lastModifiedTime\" : 1,      \"createdBy\" : \"createdBy\",      \"lastModifiedBy\" : \"lastModifiedBy\",      \"createdTime\" : 6    },    \"tenantId\" : \"tenantId\",    \"name\" : \"name\",    \"id\" : \"id\",    \"billingAddress\" : \"billingAddress\",    \"department\" : {      \"code\" : \"code\",      \"auditDetails\" : {        \"lastModifiedTime\" : 1,        \"createdBy\" : \"createdBy\",        \"lastModifiedBy\" : \"lastModifiedBy\",        \"createdTime\" : 6      },      \"tenantId\" : \"tenantId\",      \"name\" : \"name\",      \"active\" : true,      \"id\" : \"id\"    },    \"email\" : \"email\"  } ],  \"page\" : {    \"totalResults\" : 1,    \"offSet\" : 1,    \"totalPages\" : 1,    \"pageSize\" : 6,    \"currentPage\" : 7  },  \"responseInfo\" : {    \"ver\" : \"ver\",    \"resMsgId\" : \"resMsgId\",    \"msgId\" : \"msgId\",    \"apiId\" : \"apiId\",    \"ts\" : 0,    \"status\" : \"SUCCESSFUL\"  }}", StoreResponse.class), HttpStatus.OK);
        }

        return new ResponseEntity<StoreResponse>(HttpStatus.OK);
    }

}
