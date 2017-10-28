package io.swagger.api;

import io.swagger.model.ErrorRes;
import io.swagger.model.RequestInfo;
import io.swagger.model.SupplierRequest;
import io.swagger.model.SupplierResponse;

import io.swagger.annotations.*;

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
public class SuppliersApiController implements SuppliersApi {
    private final ObjectMapper objectMapper;

    public SuppliersApiController(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public ResponseEntity<SupplierResponse> suppliersCreatePost( @NotNull@ApiParam(value = "Unique id for a tenant.", required = true) @Valid @RequestParam(value = "tenantId", required = true) String tenantId,
        @ApiParam(value = "Create  new"  )  @Valid @RequestBody SupplierRequest supplierRequest,
        @RequestHeader(value = "Accept", required = false) String accept) throws Exception {
        // do some magic!

        if (accept != null && accept.contains("application/json")) {
            return new ResponseEntity<SupplierResponse>(objectMapper.readValue("{  \"suppliers\" : [ {    \"code\" : \"code\",    \"auditDetails\" : {      \"lastModifiedTime\" : 1,      \"createdBy\" : \"createdBy\",      \"lastModifiedBy\" : \"lastModifiedBy\",      \"createdTime\" : 6    },    \"tenantId\" : \"tenantId\",    \"id\" : \"id\"  }, {    \"code\" : \"code\",    \"auditDetails\" : {      \"lastModifiedTime\" : 1,      \"createdBy\" : \"createdBy\",      \"lastModifiedBy\" : \"lastModifiedBy\",      \"createdTime\" : 6    },    \"tenantId\" : \"tenantId\",    \"id\" : \"id\"  } ],  \"page\" : {    \"totalResults\" : 1,    \"offSet\" : 1,    \"totalPages\" : 1,    \"pageSize\" : 6,    \"currentPage\" : 7  },  \"responseInfo\" : {    \"ver\" : \"ver\",    \"resMsgId\" : \"resMsgId\",    \"msgId\" : \"msgId\",    \"apiId\" : \"apiId\",    \"ts\" : 0,    \"status\" : \"SUCCESSFUL\"  }}", SupplierResponse.class), HttpStatus.OK);
        }

        return new ResponseEntity<SupplierResponse>(HttpStatus.OK);
    }

    public ResponseEntity<SupplierResponse> suppliersSearchPost( @NotNull@ApiParam(value = "Unique id for a tenant.", required = true) @Valid @RequestParam(value = "tenantId", required = true) String tenantId,
        @ApiParam(value = "Parameter to carry Request metadata in the request body"  )  @Valid @RequestBody RequestInfo requestInfo,
         @Size(max=50)@ApiParam(value = "comma seperated list of Ids") @Valid @RequestParam(value = "ids", required = false) List<String> ids,
        @ApiParam(value = "code of the Supplier ") @Valid @RequestParam(value = "code", required = false) String code,
        @ApiParam(value = "pageSize") @Valid @RequestParam(value = "pageSize", required = false) Integer pageSize,
        @ApiParam(value = "offset") @Valid @RequestParam(value = "offset", required = false) Integer offset,
        @ApiParam(value = "This takes any field from the Object seperated by comma and asc,desc keywords.   example name asc,code desc or name,code or name,code desc  ") @Valid @RequestParam(value = "sortBy", required = false) String sortBy,
        @RequestHeader(value = "Accept", required = false) String accept) throws Exception {
        // do some magic!

        if (accept != null && accept.contains("application/json")) {
            return new ResponseEntity<SupplierResponse>(objectMapper.readValue("{  \"suppliers\" : [ {    \"code\" : \"code\",    \"auditDetails\" : {      \"lastModifiedTime\" : 1,      \"createdBy\" : \"createdBy\",      \"lastModifiedBy\" : \"lastModifiedBy\",      \"createdTime\" : 6    },    \"tenantId\" : \"tenantId\",    \"id\" : \"id\"  }, {    \"code\" : \"code\",    \"auditDetails\" : {      \"lastModifiedTime\" : 1,      \"createdBy\" : \"createdBy\",      \"lastModifiedBy\" : \"lastModifiedBy\",      \"createdTime\" : 6    },    \"tenantId\" : \"tenantId\",    \"id\" : \"id\"  } ],  \"page\" : {    \"totalResults\" : 1,    \"offSet\" : 1,    \"totalPages\" : 1,    \"pageSize\" : 6,    \"currentPage\" : 7  },  \"responseInfo\" : {    \"ver\" : \"ver\",    \"resMsgId\" : \"resMsgId\",    \"msgId\" : \"msgId\",    \"apiId\" : \"apiId\",    \"ts\" : 0,    \"status\" : \"SUCCESSFUL\"  }}", SupplierResponse.class), HttpStatus.OK);
        }

        return new ResponseEntity<SupplierResponse>(HttpStatus.OK);
    }

    public ResponseEntity<SupplierResponse> suppliersUpdatePost( @NotNull@ApiParam(value = "Unique id for a tenant.", required = true) @Valid @RequestParam(value = "tenantId", required = true) String tenantId,
        @ApiParam(value = "common Request info"  )  @Valid @RequestBody SupplierRequest supplierRequest,
        @RequestHeader(value = "Accept", required = false) String accept) throws Exception {
        // do some magic!

        if (accept != null && accept.contains("application/json")) {
            return new ResponseEntity<SupplierResponse>(objectMapper.readValue("{  \"suppliers\" : [ {    \"code\" : \"code\",    \"auditDetails\" : {      \"lastModifiedTime\" : 1,      \"createdBy\" : \"createdBy\",      \"lastModifiedBy\" : \"lastModifiedBy\",      \"createdTime\" : 6    },    \"tenantId\" : \"tenantId\",    \"id\" : \"id\"  }, {    \"code\" : \"code\",    \"auditDetails\" : {      \"lastModifiedTime\" : 1,      \"createdBy\" : \"createdBy\",      \"lastModifiedBy\" : \"lastModifiedBy\",      \"createdTime\" : 6    },    \"tenantId\" : \"tenantId\",    \"id\" : \"id\"  } ],  \"page\" : {    \"totalResults\" : 1,    \"offSet\" : 1,    \"totalPages\" : 1,    \"pageSize\" : 6,    \"currentPage\" : 7  },  \"responseInfo\" : {    \"ver\" : \"ver\",    \"resMsgId\" : \"resMsgId\",    \"msgId\" : \"msgId\",    \"apiId\" : \"apiId\",    \"ts\" : 0,    \"status\" : \"SUCCESSFUL\"  }}", SupplierResponse.class), HttpStatus.OK);
        }

        return new ResponseEntity<SupplierResponse>(HttpStatus.OK);
    }

}
