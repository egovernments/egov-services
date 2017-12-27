package org.egov.works.workorder.domain.repository;

import org.egov.works.workorder.web.contract.Milestone;
import org.egov.works.workorder.web.contract.MilestoneActivity;
import org.egov.works.workorder.web.contract.MilestoneSearchContract;
import org.egov.works.workorder.web.contract.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Ramki on 22/12/17.
 */
@Repository
public class MilestoneRepository {

    @Autowired
    private MilestoneJdbcRepository milestoneJdbcRepository;

    public List<Milestone> search(final MilestoneSearchContract milestoneSearchContract, final RequestInfo requestInfo) {
        return milestoneJdbcRepository.search(milestoneSearchContract, requestInfo);
    }

    public MilestoneActivity getActivityById(String milestoneActivityId, String tenantId) {
        return milestoneJdbcRepository.getActivityById(milestoneActivityId, tenantId);
    }
}
