package org.egov.pgrrest.read.web.controller;

import org.egov.pgrrest.read.domain.service.DashboardService;
import org.egov.pgrrest.read.web.contract.DashboardResponse;
import org.springframework.stereotype.Controller;
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
    public List<DashboardResponse> getDashboardResponse(@RequestParam(value = "tenantId", defaultValue = "default") String tenantId){
        List<org.egov.pgrrest.read.domain.model.DashboardResponse> response = dashboardService.getComplaintTypeWiseCount(tenantId);

        return getResponseList(response);
    }

    private List<DashboardResponse> getResponseList(List<org.egov.pgrrest.read.domain.model.DashboardResponse> responseList){
        return responseList.stream()
            .map(record -> record.toContract(record))
            .collect(Collectors.toList());
    }

}
