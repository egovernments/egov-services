/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.inv.api;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiParam;
import org.egov.inv.domain.service.StoreService;
import org.egov.inv.model.StoreGetRequest;
import org.egov.inv.model.StoreRequest;
import org.egov.inv.model.StoreResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-10-28T13:21:55.964+05:30")

@Controller
public class StoresApiController implements StoresApi {

    @Autowired
    private StoreService storesService;

    private final ObjectMapper objectMapper;

    public StoresApiController(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public ResponseEntity<StoreResponse> storesCreatePost(
            @NotNull @ApiParam(value = "Unique id for a tenant.", required = true) @Valid @RequestParam(value = "tenantId", required = true) String tenantId,
            @ApiParam(value = "Create  new") @Valid @RequestBody StoreRequest storeRequest,
            @RequestHeader(value = "Accept", required = false) String accept
    ) throws Exception {
        StoreResponse storeResponse = storesService.create(storeRequest, tenantId);
        return new ResponseEntity(storeResponse, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<StoreResponse> storesSearchPost(
            @NotNull @ApiParam(value = "Unique id for a tenant.", required = true) @Valid @RequestParam(value = "tenantId", required = true) String tenantId,
            @ApiParam(value = "Parameter to carry Request metadata in the request body") @Valid @RequestBody org.egov.common.contract.request.RequestInfo RequestInfo,
            @Size(max = 50) @ApiParam(value = "comma seperated list of Ids") @Valid @RequestParam(value = "ids", required = false) List<String> ids,
            @ApiParam(value = "list of code of the Store ") @Valid @RequestParam(value = "codes", required = false) List<String> codes,
            @ApiParam(value = "name of the Store ") @Valid @RequestParam(value = "name", required = false) String name,
            @ApiParam(value = "description of the Store ") @Valid @RequestParam(value = "description", required = false) String description,
            @ApiParam(value = "department of the Store ") @Valid @RequestParam(value = "department", required = false) String department,
            @ApiParam(value = "billing address of the Store ") @Valid @RequestParam(value = "billingAddress", required = false) String billingAddress,
            @ApiParam(value = "delivery address of the Store ") @Valid @RequestParam(value = "deliveryAddress", required = false) String deliveryAddress,
            @ApiParam(value = "contact no1 of the Store ") @Valid @RequestParam(value = "contactNo1", required = false) String contactNo1,
            @ApiParam(value = "contact no2 of the Store ") @Valid @RequestParam(value = "contactNo2", required = false) String contactNo2,
            @ApiParam(value = "email of the Store ") @Valid @RequestParam(value = "email", required = false) String email,
            @ApiParam(value = "store in charge of the Store ") @Valid @RequestParam(value = "storeInCharge", required = false) String storeInCharge,
            @ApiParam(value = "is central store of the Store ") @Valid @RequestParam(value = "isCentralStore", required = false) Boolean isCentralStore,
            @ApiParam(value = "Whether Store is Active or not. If the value is TRUE, then Store is active,If the value is FALSE then Store is inactive,Default value is TRUE ") @Valid @RequestParam(value = "active", required = false) Boolean active,
            @ApiParam(value = "pageSize") @Valid @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @ApiParam(value = "offset") @Valid @RequestParam(value = "offset", required = false) Integer offset,
            @ApiParam(value = "This takes any field from the Object seperated by comma and asc,desc keywords.   example name asc,code desc or name,code or name,code desc  ") @Valid @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestHeader(value = "Accept", required = false) String accept)
            throws Exception {

        StoreGetRequest storeGetRequest = new StoreGetRequest(ids,
                codes, name, description, department, contactNo1, billingAddress, deliveryAddress, contactNo2, email, isCentralStore,
                storeInCharge, active, sortBy, pageSize, offset, tenantId);
        StoreResponse response = storesService.search(storeGetRequest);

        return new ResponseEntity(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<StoreResponse> storesUpdatePost(
            @NotNull @ApiParam(value = "Unique id for a tenant.", required = true) @Valid @RequestParam(value = "tenantId", required = true) String tenantId,
            @ApiParam(value = "common Request info") @Valid @RequestBody StoreRequest storeRequest,
            @RequestHeader(value = "Accept", required = false) String accept
    ) throws Exception {
        StoreResponse storeResponse = storesService.update(storeRequest, tenantId);
        return new ResponseEntity(storeResponse, HttpStatus.OK);
    }


}
