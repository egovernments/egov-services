package io.swagger.api;

import io.swagger.model.ErrorRes;

import io.swagger.model.Store;
import io.swagger.model.StoreResponse;
import io.swagger.model.Supplier;
import io.swagger.model.SupplierRequest;
import io.swagger.model.SupplierResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.inv.domain.service.SupplierService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-02T13:59:35.200+05:30")

@Controller
public class SuppliersApiController implements SuppliersApi {
	
	@Autowired
	private SupplierService supplierService;

    private static final Logger log = LoggerFactory.getLogger(SuppliersApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public SuppliersApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<SupplierResponse> suppliersCreatePost(@NotNull @ApiParam(value = "Unique id for a tenant.", required = true) @Valid @RequestParam(value = "tenantId", required = true) String tenantId,@ApiParam(value = "Create  new"  )  @Valid @RequestBody SupplierRequest supplierRequest,
    		@RequestHeader(value = "Accept", required = false) String accept,BindingResult errors) throws Exception {
    	List<Supplier> suppliers = supplierService.create(supplierRequest, tenantId, errors);
    	SupplierResponse supplierResponse = buildSupplierResponse(suppliers, supplierRequest.getRequestInfo());
      
        if (accept != null && accept.contains("application/json")) {
           
                return new ResponseEntity<SupplierResponse>(objectMapper.readValue("{  \"suppliers\" : [ {    \"bankInfo\" : {      \"acctNo\" : \"acctNo\",      \"tenantId\" : \"tenantId\",      \"name\" : \"name\",      \"micr\" : \"micr\",      \"ifsc\" : \"ifsc\"    },    \"website\" : \"website\",    \"code\" : \"code\",    \"address\" : \"address\",    \"panNo\" : \"panNo\",    \"vatNo\" : \"vatNo\",    \"contactPerson\" : \"contactPerson\",    \"cstNo\" : \"cstNo\",    \"tinNo\" : \"tinNo\",    \"contactPersonNo\" : \"contactPersonNo\",    \"faxNo\" : \"faxNo\",    \"supplierContactNo\" : \"supplierContactNo\",    \"narration\" : \"narration\",    \"auditDetails\" : {      \"lastModifiedTime\" : 1,      \"createdBy\" : \"createdBy\",      \"lastModifiedBy\" : \"lastModifiedBy\",      \"createdTime\" : 6    },    \"tenantId\" : \"tenantId\",    \"name\" : \"name\",    \"inActiveDate\" : 0,    \"id\" : \"id\",    \"supplierType\" : {      \"code\" : \"code\",      \"tenantId\" : \"tenantId\",      \"name\" : \"name\"    },    \"email\" : \"email\",    \"status\" : {      \"code\" : \"code\",      \"tenantId\" : \"tenantId\",      \"name\" : \"name\"    }  }, {    \"bankInfo\" : {      \"acctNo\" : \"acctNo\",      \"tenantId\" : \"tenantId\",      \"name\" : \"name\",      \"micr\" : \"micr\",      \"ifsc\" : \"ifsc\"    },    \"website\" : \"website\",    \"code\" : \"code\",    \"address\" : \"address\",    \"panNo\" : \"panNo\",    \"vatNo\" : \"vatNo\",    \"contactPerson\" : \"contactPerson\",    \"cstNo\" : \"cstNo\",    \"tinNo\" : \"tinNo\",    \"contactPersonNo\" : \"contactPersonNo\",    \"faxNo\" : \"faxNo\",    \"supplierContactNo\" : \"supplierContactNo\",    \"narration\" : \"narration\",    \"auditDetails\" : {      \"lastModifiedTime\" : 1,      \"createdBy\" : \"createdBy\",      \"lastModifiedBy\" : \"lastModifiedBy\",      \"createdTime\" : 6    },    \"tenantId\" : \"tenantId\",    \"name\" : \"name\",    \"inActiveDate\" : 0,    \"id\" : \"id\",    \"supplierType\" : {      \"code\" : \"code\",      \"tenantId\" : \"tenantId\",      \"name\" : \"name\"    },    \"email\" : \"email\",    \"status\" : {      \"code\" : \"code\",      \"tenantId\" : \"tenantId\",      \"name\" : \"name\"    }  } ],  \"page\" : {    \"totalResults\" : 1,    \"offSet\" : 1,    \"totalPages\" : 1,    \"pageSize\" : 6,    \"currentPage\" : 7  },  \"responseInfo\" : {    \"ver\" : \"ver\",    \"resMsgId\" : \"resMsgId\",    \"msgId\" : \"msgId\",    \"apiId\" : \"apiId\",    \"ts\" : 0,    \"status\" : \"SUCCESSFUL\"  }}", SupplierResponse.class), HttpStatus.OK);
           
        }

        return new ResponseEntity(supplierResponse,HttpStatus.OK);
    }

    public ResponseEntity<SupplierResponse> suppliersSearchPost(@NotNull @ApiParam(value = "Unique id for a tenant.", required = true) @Valid @RequestParam(value = "tenantId", required = true) String tenantId,@ApiParam(value = "Parameter to carry Request metadata in the request body"  )  @Valid @RequestBody RequestInfo requestInfo,@Size(max=50) @ApiParam(value = "comma seperated list of Ids") @Valid @RequestParam(value = "ids", required = false) List<String> ids,@ApiParam(value = "code of the Supplier ") @Valid @RequestParam(value = "code", required = false) String code,@Min(0) @Max(100) @ApiParam(value = "Number of records returned.", defaultValue = "20") @Valid @RequestParam(value = "pageSize", required = false, defaultValue="20") Integer pageSize,@ApiParam(value = "offset") @Valid @RequestParam(value = "offset", required = false) Integer offset,@ApiParam(value = "Page number", defaultValue = "1") @Valid @RequestParam(value = "pageNumber", required = false, defaultValue="1") Integer pageNumber,@ApiParam(value = "This takes any field from the Object seperated by comma and asc,desc keywords. example name asc,code desc or name,code or name,code desc", defaultValue = "id") @Valid @RequestParam(value = "sortBy", required = false, defaultValue="id") String sortBy) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<SupplierResponse>(objectMapper.readValue("{  \"suppliers\" : [ {    \"bankInfo\" : {      \"acctNo\" : \"acctNo\",      \"tenantId\" : \"tenantId\",      \"name\" : \"name\",      \"micr\" : \"micr\",      \"ifsc\" : \"ifsc\"    },    \"website\" : \"website\",    \"code\" : \"code\",    \"address\" : \"address\",    \"panNo\" : \"panNo\",    \"vatNo\" : \"vatNo\",    \"contactPerson\" : \"contactPerson\",    \"cstNo\" : \"cstNo\",    \"tinNo\" : \"tinNo\",    \"contactPersonNo\" : \"contactPersonNo\",    \"faxNo\" : \"faxNo\",    \"supplierContactNo\" : \"supplierContactNo\",    \"narration\" : \"narration\",    \"auditDetails\" : {      \"lastModifiedTime\" : 1,      \"createdBy\" : \"createdBy\",      \"lastModifiedBy\" : \"lastModifiedBy\",      \"createdTime\" : 6    },    \"tenantId\" : \"tenantId\",    \"name\" : \"name\",    \"inActiveDate\" : 0,    \"id\" : \"id\",    \"supplierType\" : {      \"code\" : \"code\",      \"tenantId\" : \"tenantId\",      \"name\" : \"name\"    },    \"email\" : \"email\",    \"status\" : {      \"code\" : \"code\",      \"tenantId\" : \"tenantId\",      \"name\" : \"name\"    }  }, {    \"bankInfo\" : {      \"acctNo\" : \"acctNo\",      \"tenantId\" : \"tenantId\",      \"name\" : \"name\",      \"micr\" : \"micr\",      \"ifsc\" : \"ifsc\"    },    \"website\" : \"website\",    \"code\" : \"code\",    \"address\" : \"address\",    \"panNo\" : \"panNo\",    \"vatNo\" : \"vatNo\",    \"contactPerson\" : \"contactPerson\",    \"cstNo\" : \"cstNo\",    \"tinNo\" : \"tinNo\",    \"contactPersonNo\" : \"contactPersonNo\",    \"faxNo\" : \"faxNo\",    \"supplierContactNo\" : \"supplierContactNo\",    \"narration\" : \"narration\",    \"auditDetails\" : {      \"lastModifiedTime\" : 1,      \"createdBy\" : \"createdBy\",      \"lastModifiedBy\" : \"lastModifiedBy\",      \"createdTime\" : 6    },    \"tenantId\" : \"tenantId\",    \"name\" : \"name\",    \"inActiveDate\" : 0,    \"id\" : \"id\",    \"supplierType\" : {      \"code\" : \"code\",      \"tenantId\" : \"tenantId\",      \"name\" : \"name\"    },    \"email\" : \"email\",    \"status\" : {      \"code\" : \"code\",      \"tenantId\" : \"tenantId\",      \"name\" : \"name\"    }  } ],  \"page\" : {    \"totalResults\" : 1,    \"offSet\" : 1,    \"totalPages\" : 1,    \"pageSize\" : 6,    \"currentPage\" : 7  },  \"responseInfo\" : {    \"ver\" : \"ver\",    \"resMsgId\" : \"resMsgId\",    \"msgId\" : \"msgId\",    \"apiId\" : \"apiId\",    \"ts\" : 0,    \"status\" : \"SUCCESSFUL\"  }}", SupplierResponse.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<SupplierResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<SupplierResponse>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<SupplierResponse> suppliersUpdatePost(@NotNull @ApiParam(value = "Unique id for a tenant.", required = true) @Valid @RequestParam(value = "tenantId", required = true) String tenantId,@ApiParam(value = "common Request info"  )  @Valid @RequestBody SupplierRequest supplierRequest) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<SupplierResponse>(objectMapper.readValue("{  \"suppliers\" : [ {    \"bankInfo\" : {      \"acctNo\" : \"acctNo\",      \"tenantId\" : \"tenantId\",      \"name\" : \"name\",      \"micr\" : \"micr\",      \"ifsc\" : \"ifsc\"    },    \"website\" : \"website\",    \"code\" : \"code\",    \"address\" : \"address\",    \"panNo\" : \"panNo\",    \"vatNo\" : \"vatNo\",    \"contactPerson\" : \"contactPerson\",    \"cstNo\" : \"cstNo\",    \"tinNo\" : \"tinNo\",    \"contactPersonNo\" : \"contactPersonNo\",    \"faxNo\" : \"faxNo\",    \"supplierContactNo\" : \"supplierContactNo\",    \"narration\" : \"narration\",    \"auditDetails\" : {      \"lastModifiedTime\" : 1,      \"createdBy\" : \"createdBy\",      \"lastModifiedBy\" : \"lastModifiedBy\",      \"createdTime\" : 6    },    \"tenantId\" : \"tenantId\",    \"name\" : \"name\",    \"inActiveDate\" : 0,    \"id\" : \"id\",    \"supplierType\" : {      \"code\" : \"code\",      \"tenantId\" : \"tenantId\",      \"name\" : \"name\"    },    \"email\" : \"email\",    \"status\" : {      \"code\" : \"code\",      \"tenantId\" : \"tenantId\",      \"name\" : \"name\"    }  }, {    \"bankInfo\" : {      \"acctNo\" : \"acctNo\",      \"tenantId\" : \"tenantId\",      \"name\" : \"name\",      \"micr\" : \"micr\",      \"ifsc\" : \"ifsc\"    },    \"website\" : \"website\",    \"code\" : \"code\",    \"address\" : \"address\",    \"panNo\" : \"panNo\",    \"vatNo\" : \"vatNo\",    \"contactPerson\" : \"contactPerson\",    \"cstNo\" : \"cstNo\",    \"tinNo\" : \"tinNo\",    \"contactPersonNo\" : \"contactPersonNo\",    \"faxNo\" : \"faxNo\",    \"supplierContactNo\" : \"supplierContactNo\",    \"narration\" : \"narration\",    \"auditDetails\" : {      \"lastModifiedTime\" : 1,      \"createdBy\" : \"createdBy\",      \"lastModifiedBy\" : \"lastModifiedBy\",      \"createdTime\" : 6    },    \"tenantId\" : \"tenantId\",    \"name\" : \"name\",    \"inActiveDate\" : 0,    \"id\" : \"id\",    \"supplierType\" : {      \"code\" : \"code\",      \"tenantId\" : \"tenantId\",      \"name\" : \"name\"    },    \"email\" : \"email\",    \"status\" : {      \"code\" : \"code\",      \"tenantId\" : \"tenantId\",      \"name\" : \"name\"    }  } ],  \"page\" : {    \"totalResults\" : 1,    \"offSet\" : 1,    \"totalPages\" : 1,    \"pageSize\" : 6,    \"currentPage\" : 7  },  \"responseInfo\" : {    \"ver\" : \"ver\",    \"resMsgId\" : \"resMsgId\",    \"msgId\" : \"msgId\",    \"apiId\" : \"apiId\",    \"ts\" : 0,    \"status\" : \"SUCCESSFUL\"  }}", SupplierResponse.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<SupplierResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<SupplierResponse>(HttpStatus.NOT_IMPLEMENTED);
    }
    
    private SupplierResponse buildSupplierResponse(List<Supplier> suppliers, RequestInfo requestInfo) {
		return SupplierResponse.builder().responseInfo(getResponseInfo(requestInfo)).suppliers(suppliers).build();
	}

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer())
				.resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
	}

	
}