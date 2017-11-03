package org.egov.pgrrest.read.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class DashboardResponse {

    private Integer count;

    private Integer openComplaintsCount;

    private Integer closedComplaintsCount;

    private String month;

    private String year;

    private String day;

    private String date;

    private String status;

    public org.egov.pgrrest.read.web.contract.DashboardResponse toContract(org.egov.pgrrest.read.domain.model.DashboardResponse dashboardResponse){
        return org.egov.pgrrest.read.web.contract.DashboardResponse.builder()
            .count(dashboardResponse.getCount())
            .name(dashboardResponse.getMonth().concat("-").concat(dashboardResponse.getYear()))
            .build();
    }

    public org.egov.pgrrest.read.web.contract.DashboardResponse toWeeklyContract(DashboardResponse response){
        return  org.egov.pgrrest.read.web.contract.DashboardResponse.builder()
                .count(response.getOpenComplaintsCount())
                .closedCount(response.getClosedComplaintsCount())
                .name(response.day.trim().concat("-").concat(response.date))
                .build();
    }
}
