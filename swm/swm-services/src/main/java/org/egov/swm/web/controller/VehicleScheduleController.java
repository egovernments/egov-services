package org.egov.swm.web.controller;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.PaginationContract;
import org.egov.swm.domain.model.VehicleSchedule;
import org.egov.swm.domain.model.VehicleScheduleSearch;
import org.egov.swm.domain.service.VehicleScheduleService;
import org.egov.swm.web.requests.VehicleScheduleRequest;
import org.egov.swm.web.requests.VehicleScheduleResponse;
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
@RequestMapping("/vehicleschedules")
public class VehicleScheduleController {

    @Autowired
    private VehicleScheduleService vehicleScheduleService;

    @PostMapping("/_create")
    @ResponseStatus(HttpStatus.CREATED)
    public VehicleScheduleResponse create(@RequestBody @Valid VehicleScheduleRequest vehicleScheduleRequest) {

        vehicleScheduleRequest = vehicleScheduleService.create(vehicleScheduleRequest);

        return VehicleScheduleResponse.builder().responseInfo(getResponseInfo(vehicleScheduleRequest.getRequestInfo()))
                .vehicleSchedules(vehicleScheduleRequest.getVehicleSchedules()).build();
    }

    @PostMapping("/_update")
    @ResponseStatus(HttpStatus.CREATED)
    public VehicleScheduleResponse update(@RequestBody @Valid VehicleScheduleRequest vehicleScheduleRequest) {

        vehicleScheduleRequest = vehicleScheduleService.update(vehicleScheduleRequest);

        return VehicleScheduleResponse.builder().responseInfo(getResponseInfo(vehicleScheduleRequest.getRequestInfo()))
                .vehicleSchedules(vehicleScheduleRequest.getVehicleSchedules()).build();
    }

    @PostMapping("/_search")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public VehicleScheduleResponse search(@ModelAttribute final VehicleScheduleSearch vehicleScheduleSearch,
            @RequestBody final RequestInfo requestInfo, @RequestParam final String tenantId) {

        final Pagination<VehicleSchedule> vehicleScheduleList = vehicleScheduleService.search(vehicleScheduleSearch);

        return VehicleScheduleResponse.builder().responseInfo(getResponseInfo(requestInfo))
                .vehicleSchedules(vehicleScheduleList.getPagedData()).page(new PaginationContract(vehicleScheduleList))
                .build();

    }

    private ResponseInfo getResponseInfo(final RequestInfo requestInfo) {
        return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer())
                .resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
    }

}