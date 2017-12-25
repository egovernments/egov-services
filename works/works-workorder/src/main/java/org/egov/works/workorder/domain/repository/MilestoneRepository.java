package org.egov.works.workorder.domain.repository;

import org.egov.works.workorder.web.contract.Milestone;
import org.egov.works.workorder.web.contract.MilestoneSearchContract;
import org.egov.works.workorder.web.contract.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by ritesh on 27/11/17.
 */
@Repository
public class MilestoneRepository {

    @Autowired
    private MilestoneJdbcRepository milestoneJdbcRepository;

    public List<Milestone> search(final MilestoneSearchContract milestoneSearchContract, final RequestInfo requestInfo) {
        return milestoneJdbcRepository.search(milestoneSearchContract, requestInfo);
    }


}
