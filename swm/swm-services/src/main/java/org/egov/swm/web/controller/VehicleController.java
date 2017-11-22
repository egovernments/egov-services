package org.egov.swm.web.controller;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.PaginationContract;
import org.egov.swm.domain.model.Vehicle;
import org.egov.swm.domain.model.VehicleSearch;
import org.egov.swm.domain.service.VehicleService;
import org.egov.swm.web.requests.VehicleRequest;
import org.egov.swm.web.requests.VehicleResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/vehicles")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @PostMapping("/_create")
    @ResponseStatus(HttpStatus.CREATED)
    public VehicleResponse create(@RequestBody @Valid VehicleRequest vehicleRequest) {

        vehicleRequest = vehicleService.create(vehicleRequest);

        return VehicleResponse.builder().responseInfo(getResponseInfo(vehicleRequest.getRequestInfo()))
                .vehicles(vehicleRequest.getVehicles()).build();
    }

    @PostMapping("/_update")
    @ResponseStatus(HttpStatus.CREATED)
    public VehicleResponse update(@RequestBody @Valid VehicleRequest vehicleRequest) {

        vehicleRequest = vehicleService.update(vehicleRequest);

        return VehicleResponse.builder().responseInfo(getResponseInfo(vehicleRequest.getRequestInfo()))
                .vehicles(vehicleRequest.getVehicles()).build();
    }

    @PostMapping("/_search")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public VehicleResponse search(@ModelAttribute final VehicleSearch vehicleSearch, @RequestBody final RequestInfo requestInfo,
            @RequestParam final String tenantId) {

        final Pagination<Vehicle> vehicleList = vehicleService.search(vehicleSearch);

        return VehicleResponse.builder().responseInfo(getResponseInfo(requestInfo)).vehicles(vehicleList.getPagedData())
                .page(new PaginationContract(vehicleList)).build();

    }

    private ResponseInfo getResponseInfo(final RequestInfo requestInfo) {
        return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer())
                .resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
    }

}