package org.egov.swm.web.controller;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.PaginationContract;
import org.egov.swm.domain.model.VehicleMaintenance;
import org.egov.swm.domain.model.VehicleMaintenanceSearch;
import org.egov.swm.domain.service.VehicleMaintenanceService;
import org.egov.swm.web.requests.VehicleMaintenanceRequest;
import org.egov.swm.web.requests.VehicleMaintenanceResponse;
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
@RequestMapping("/vehiclemaintenances")
public class VehicleMaintenanceController {

    @Autowired
    private VehicleMaintenanceService vehicleMaintenanceService;

    @PostMapping("/_create")
    @ResponseStatus(HttpStatus.CREATED)
    public VehicleMaintenanceResponse create(@RequestBody @Valid VehicleMaintenanceRequest vehicleMaintenanceRequest) {

        vehicleMaintenanceRequest = vehicleMaintenanceService.create(vehicleMaintenanceRequest);

        return VehicleMaintenanceResponse.builder()
                .responseInfo(getResponseInfo(vehicleMaintenanceRequest.getRequestInfo()))
                .vehicleMaintenances(vehicleMaintenanceRequest.getVehicleMaintenances()).build();
    }

    @PostMapping("/_update")
    @ResponseStatus(HttpStatus.CREATED)
    public VehicleMaintenanceResponse update(@RequestBody @Valid VehicleMaintenanceRequest vehicleMaintenanceRequest) {

        vehicleMaintenanceRequest = vehicleMaintenanceService.update(vehicleMaintenanceRequest);

        return VehicleMaintenanceResponse.builder()
                .responseInfo(getResponseInfo(vehicleMaintenanceRequest.getRequestInfo()))
                .vehicleMaintenances(vehicleMaintenanceRequest.getVehicleMaintenances()).build();
    }

    @PostMapping("/_search")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public VehicleMaintenanceResponse search(@ModelAttribute final VehicleMaintenanceSearch vehicleMaintenanceSearch,
            @RequestBody final RequestInfo requestInfo, @RequestParam final String tenantId) {

        final Pagination<VehicleMaintenance> vehicleMaintenanceList = vehicleMaintenanceService
                .search(vehicleMaintenanceSearch);

        return VehicleMaintenanceResponse.builder().responseInfo(getResponseInfo(requestInfo))
                .vehicleMaintenances(vehicleMaintenanceList.getPagedData())
                .page(new PaginationContract(vehicleMaintenanceList)).build();

    }

    private ResponseInfo getResponseInfo(final RequestInfo requestInfo) {
        return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer())
                .resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
    }

}