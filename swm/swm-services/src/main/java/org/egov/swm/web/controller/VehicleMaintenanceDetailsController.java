package org.egov.swm.web.controller;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.VehicleMaintenanceDetails;
import org.egov.swm.domain.model.VehicleMaintenanceDetailsSearch;
import org.egov.swm.domain.service.VehicleMaintenanceDetailsService;
import org.egov.swm.web.requests.ScheduledMaintenanceDateResponse;
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

    @PostMapping("/_update")
    @ResponseStatus(HttpStatus.CREATED)
    public VehicleMaintenanceDetailsResponse update(@RequestBody @Valid VehicleMaintenanceDetailsRequest vehicleMaintenanceDetailsRequest) {

        VehicleMaintenanceDetailsResponse vehicleMaintenanceDetailsResponse  = new VehicleMaintenanceDetailsResponse();
        vehicleMaintenanceDetailsResponse.setResponseInfo(getResponseInfo(vehicleMaintenanceDetailsRequest.getRequestInfo()));

        vehicleMaintenanceDetailsRequest = vehicleMaintenanceDetailsService.update(vehicleMaintenanceDetailsRequest);

        vehicleMaintenanceDetailsResponse.setVehicleMaintenanceDetails(vehicleMaintenanceDetailsRequest.getVehicleMaintenanceDetails());

        return vehicleMaintenanceDetailsResponse;
    }

    @PostMapping("/_search")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public VehicleMaintenanceDetailsResponse search (@ModelAttribute VehicleMaintenanceDetailsSearch vehicleMaintenanceDetailsSearch ,
                                                @RequestBody RequestInfo requestInfo, @RequestParam String tenantId){

        Pagination<VehicleMaintenanceDetails> vehicleMaintenanceDetailsList  = vehicleMaintenanceDetailsService.search(vehicleMaintenanceDetailsSearch);

        return VehicleMaintenanceDetailsResponse.builder()
                .vehicleMaintenanceDetails(vehicleMaintenanceDetailsList.getPagedData())
                .responseInfo(getResponseInfo(requestInfo))
                .build();
    }

    @PostMapping("/_getnextscheduleddate")
    @ResponseStatus(HttpStatus.OK)
    public ScheduledMaintenanceDateResponse getScheduledMaintenanceDate(@RequestBody RequestInfo requestInfo, @RequestParam String tenantId,
                                                                       @RequestParam String vehicleCode){

        Long scheduledDate = vehicleMaintenanceDetailsService.calaculateNextSceduledMaintenanceDate(tenantId, vehicleCode);

        return ScheduledMaintenanceDateResponse.builder()
                .responseInfo(new ResponseInfo())
                .sceduledDate(scheduledDate)
                .build();
    }

    private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
        return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer())
                .resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
    }
}
