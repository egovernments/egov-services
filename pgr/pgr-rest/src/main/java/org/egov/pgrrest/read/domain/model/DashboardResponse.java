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

    private String month;

    private String year;

    public org.egov.pgrrest.read.web.contract.DashboardResponse toContract(org.egov.pgrrest.read.domain.model.DashboardResponse dashboardResponse){
        return org.egov.pgrrest.read.web.contract.DashboardResponse.builder()
            .count(dashboardResponse.getCount())
            .name(dashboardResponse.getMonth().concat("-").concat(dashboardResponse.getYear()))
            .build();
    }
}
