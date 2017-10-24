package org.egov.pgrrest.read.web.controller;

import org.egov.pgrrest.read.domain.model.TopComplaintTypesResponse;
import org.egov.pgrrest.read.domain.service.DashboardService;
import org.egov.pgrrest.read.web.contract.AgeingResponse;
import org.egov.pgrrest.read.web.contract.DashboardResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = {"/dashboard"})
public class DashBoardController {

    private DashboardService dashboardService;

    public DashBoardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @PostMapping
    public List<DashboardResponse> getDashboardResponse(@RequestParam(value = "tenantId", defaultValue = "default") String tenantId,
                                                        @RequestParam(value = "type", defaultValue = "Monthly") String type){
        List<org.egov.pgrrest.read.domain.model.DashboardResponse> response = dashboardService.getComplaintTypeWiseCount(tenantId,type);

        if(type.equalsIgnoreCase("weekly"))
            return getWeeklyResponseList(response);

        return getResponseList(response);
    }

    @PostMapping("/complainttype")
    public List<org.egov.pgrrest.read.web.contract.TopComplaintTypesResponse> getTopComplaintTypesCount(@RequestParam(value = "tenantId", defaultValue = "default") String tenantId,
                                                                                                        @RequestParam(value = "size", defaultValue = "5") Integer size){

        List<TopComplaintTypesResponse> responseList = dashboardService.getTopComplaintTypes(tenantId, size);

        return getTopComplaintTypesList(responseList);
    }

    @PostMapping("/ageing")
    public List<AgeingResponse> getAgeingData(@RequestParam(value = "tenantId", defaultValue = "default") String tenantId){

        List<org.egov.pgrrest.read.domain.model.AgeingResponse> responseList = dashboardService.getComplaintsAgeingData(tenantId);

        return getAgeingResponseList(responseList);
    }

    private List<DashboardResponse> getResponseList(List<org.egov.pgrrest.read.domain.model.DashboardResponse> responseList){
        return responseList.stream()
            .map(record -> record.toContract(record))
            .collect(Collectors.toList());
    }

    private List<DashboardResponse> getWeeklyResponseList(List<org.egov.pgrrest.read.domain.model.DashboardResponse> responseList){
        return responseList.stream()
            .map(record -> record.toWeeklyContract(record))
            .collect(Collectors.toList());
    }

    private List<org.egov.pgrrest.read.web.contract.TopComplaintTypesResponse> getTopComplaintTypesList(
        List<TopComplaintTypesResponse> responseList){

        return responseList.stream()
            .map(record -> record.toContract())
            .collect(Collectors.toList());
    }

    private List<AgeingResponse> getAgeingResponseList(
        List<org.egov.pgrrest.read.domain.model.AgeingResponse> responseList){

        return responseList.stream()
            .map(record -> record.toContract())
            .collect(Collectors.toList());
    }

}
