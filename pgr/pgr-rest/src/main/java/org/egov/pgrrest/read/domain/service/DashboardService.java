package org.egov.pgrrest.read.domain.service;

import org.egov.pgrrest.read.domain.model.*;
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

    public List<TopComplaintTypesResponse> getWardWiseCount(String tenantId, String serviceCode, String type){

        return dashboardRepository.getWardWiseCountForComplainttype(tenantId, serviceCode, type);
    }

    public List<TopComplaintTypesResponse> getTopComplaintTypes(String tenantId, int size, String type){
        if(type.equalsIgnoreCase("topfive"))
            return dashboardRepository.getTopFiveComplaintTypesMonthly(tenantId);

        return dashboardRepository.getTopComplaintTypeWithCount(tenantId, size);
    }

    public TopFiveComplaintTypesResponse getTopFiveComplaintTypes(String tenantId){

        List<ComplaintTypeLegend> legends = dashboardRepository.getTopFiveComplaintTypesLegendData(tenantId);

        List<TopComplaintTypesResponse> complaintTypes = dashboardRepository.getTopFiveComplaintTypesMonthly(tenantId);

        return TopFiveComplaintTypesResponse.builder()
            .legends(legends)
            .complaintTypes(complaintTypes)
            .build();
    }

    public List<AgeingResponse> getComplaintsAgeingData(String tenantId, List<Integer> range){
        return dashboardRepository.getAgeingOfComplaints(tenantId, range);
    }
}
