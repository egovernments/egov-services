package org.egov.pgrrest.read.domain.service;

import org.egov.pgrrest.read.domain.model.AgeingResponse;
import org.egov.pgrrest.read.domain.model.DashboardResponse;
import org.egov.pgrrest.read.domain.model.TopComplaintTypesResponse;
import org.egov.pgrrest.read.persistence.repository.DashBoardRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardService {

    private DashBoardRepository dashboardRepository;

    public DashboardService(DashBoardRepository dashboardRepository) {
        this.dashboardRepository = dashboardRepository;
    }

    public List<DashboardResponse> getComplaintTypeWiseCount(String tenantId, String type){

        List<DashboardResponse> response;

        if(type.equalsIgnoreCase("weekly"))
            response = dashboardRepository.getWeeklyRegisteredAndClosedComplaintsCount(tenantId);
        else
            response = dashboardRepository.getCountByComplaintType(tenantId);

        return response;
    }

    public List<TopComplaintTypesResponse> getWardWiseCount(String tenantId, String serviceCode){

        return dashboardRepository.getWardWiseCountForComplainttype(tenantId, serviceCode);
    }

    public List<TopComplaintTypesResponse> getTopComplaintTypes(String tenantId, int size, String type){
        if(type.equalsIgnoreCase("topfive"))
            return dashboardRepository.getTopFiveComplaintTypesMonthly(tenantId);

        return dashboardRepository.getTopComplaintTypeWithCount(tenantId, size);
    }

    public List<AgeingResponse> getComplaintsAgeingData(String tenantId){
        return dashboardRepository.getAgeingOfComplaints(tenantId);
    }
}
