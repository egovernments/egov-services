package org.egov.pgrrest.read.web.controller;

import org.egov.pgrrest.read.domain.model.TopComplaintTypesResponse;
import org.egov.pgrrest.read.domain.model.TopFiveComplaintTypesResponse;
import org.egov.pgrrest.read.domain.service.DashboardService;
import org.egov.pgrrest.read.web.contract.AgeingResponse;
import org.egov.pgrrest.read.web.contract.ComplaintTypeLegend;
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
                                                        @RequestParam(value = "type", defaultValue = "Monthly") String type
                                                        ){
        List<org.egov.pgrrest.read.domain.model.DashboardResponse> response = dashboardService.getComplaintTypeWiseCount(tenantId,type);

        if(type.equalsIgnoreCase("weekly"))
            return getWeeklyResponseList(response);

        return getResponseList(response);
    }

    @PostMapping("/complainttype")
    public List<org.egov.pgrrest.read.web.contract.TopComplaintTypesResponse> getTopComplaintTypesCount(@RequestParam(value = "tenantId", defaultValue = "default") String tenantId,
                                                                                                        @RequestParam(value = "size", defaultValue = "5") Integer size,
                                                                                                        @RequestParam(value = "type", required = false, defaultValue = "") String type,
                                                                                                        @RequestParam(value = "servicecode", defaultValue = "") String serviceCode){

        List<TopComplaintTypesResponse> responseList;

        if(type.equalsIgnoreCase("wardwise") || type.equalsIgnoreCase("wardwiseregistered") ||
            type.equalsIgnoreCase("wardwiseresolved") )
            responseList = dashboardService.getWardWiseCount(tenantId,serviceCode,type);
        else
            responseList = dashboardService.getTopComplaintTypes(tenantId, size,type);

        return getTopComplaintTypesList(responseList, type);
    }

    @PostMapping("/complainttype/topfive")
    public org.egov.pgrrest.read.web.contract.TopFiveComplaintTypesResponse getTopFiveComplaintTypes(@RequestParam(value = "tenantId", defaultValue = "default") String tenantId){

        TopFiveComplaintTypesResponse topFiveComplaintTypesList = dashboardService.getTopFiveComplaintTypes(tenantId);

        return getTopFiveComplaintTypesList(topFiveComplaintTypesList);
    }

    @PostMapping("/ageing")
    public List<AgeingResponse> getAgeingData(@RequestParam(value = "tenantId", defaultValue = "default") String tenantId,
                                              @RequestParam(value = "range") List<Integer> range){

        List<org.egov.pgrrest.read.domain.model.AgeingResponse> responseList = dashboardService.getComplaintsAgeingData(tenantId, range);

        return getAgeingResponseList(responseList);
    }

    private org.egov.pgrrest.read.web.contract.TopFiveComplaintTypesResponse getTopFiveComplaintTypesList(TopFiveComplaintTypesResponse responseList){

        List<ComplaintTypeLegend> legendsList = responseList.getLegends().stream()
            .map(org.egov.pgrrest.read.domain.model.ComplaintTypeLegend::toContract)
            .collect(Collectors.toList());

        List<org.egov.pgrrest.read.web.contract.TopComplaintTypesResponse> topComplaintsList = responseList
            .getComplaintTypes().stream()
            .map(TopComplaintTypesResponse::toTopFiveComplaintTypesContract)
            .collect(Collectors.toList());

        return org.egov.pgrrest.read.web.contract.TopFiveComplaintTypesResponse.builder()
            .legends(legendsList)
            .complaintTypes(topComplaintsList)
            .build();
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
        List<TopComplaintTypesResponse> responseList, String type){

        if(type.equalsIgnoreCase("wardwise") || type.equalsIgnoreCase("wardwiseregistered") ||
            type.equalsIgnoreCase("wardwiseresolved"))
            return responseList.stream()
            .map(TopComplaintTypesResponse::toWardWiseContract)
            .collect(Collectors.toList());

        return responseList.stream()
            .map(record -> type.equalsIgnoreCase("topfive") ? record.toTopFiveComplaintTypesContract() : record.toContract())
            .collect(Collectors.toList());
    }

    private List<AgeingResponse> getAgeingResponseList(
        List<org.egov.pgrrest.read.domain.model.AgeingResponse> responseList){

        return responseList.stream()
            .map(org.egov.pgrrest.read.domain.model.AgeingResponse::toContract)
            .collect(Collectors.toList());
    }

}
