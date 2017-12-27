package org.egov.works.workorder.domain.repository;

import org.egov.works.workorder.web.contract.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Ramki on 26/12/17.
 */
@Repository
public class TrackMilestoneRepository {

    @Autowired
    private TrackMilestoneJdbcRepository trackMilestoneJdbcRepository;

    public List<TrackMilestone> search(final TrackMilestoneSearchContract trackMilestoneSearchContract, final RequestInfo requestInfo) {
        return trackMilestoneJdbcRepository.search(trackMilestoneSearchContract, requestInfo);
    }
}
