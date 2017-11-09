package org.egov.swm.web.controller;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.swm.domain.service.VehicleMaintenanceDetailsService;
import org.egov.swm.web.requests.VehicleMaintenanceDetailsRequest;
import org.egov.swm.web.requests.VehicleMaintenanceDetailsResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/vehiclemaintenancedetails")
public class VehicleMaintenanceDetailsController {

    VehicleMaintenanceDetailsService vehicleMaintenanceDetailsService;

    public VehicleMaintenanceDetailsController(VehicleMaintenanceDetailsService vehicleMaintenanceDetailsService) {
        this.vehicleMaintenanceDetailsService = vehicleMaintenanceDetailsService;
    }

    @PostMapping("/_create")
    @ResponseStatus(HttpStatus.CREATED)
    public VehicleMaintenanceDetailsResponse create(@RequestBody @Valid VehicleMaintenanceDetailsRequest vehicleMaintenanceDetailsRequest) {

        VehicleMaintenanceDetailsResponse vehicleMaintenanceDetailsResponse  = new VehicleMaintenanceDetailsResponse();
        vehicleMaintenanceDetailsResponse.setResponseInfo(getResponseInfo(vehicleMaintenanceDetailsRequest.getRequestInfo()));

        vehicleMaintenanceDetailsRequest = vehicleMaintenanceDetailsService.create(vehicleMaintenanceDetailsRequest);

        vehicleMaintenanceDetailsResponse.setVehicleMaintenanceDetails(vehicleMaintenanceDetailsRequest.getVehicleMaintenanceDetails());

        return vehicleMaintenanceDetailsResponse;
    }

    private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
        return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer())
                .resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
    }
}
