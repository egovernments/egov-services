package org.egov.pgrrest.read.domain.service;

import org.egov.pgrrest.read.domain.model.DashboardResponse;
import org.egov.pgrrest.read.persistence.repository.DashBoardRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardService {

    private DashBoardRepository dashboardRepository;

    public DashboardService(DashBoardRepository dashboardRepository) {
        this.dashboardRepository = dashboardRepository;
    }

    public List<DashboardResponse> getComplaintTypeWiseCount(String tenantId){
        List<DashboardResponse> response = dashboardRepository.getCountByComplaintType(tenantId);

        return response;
    }
}
