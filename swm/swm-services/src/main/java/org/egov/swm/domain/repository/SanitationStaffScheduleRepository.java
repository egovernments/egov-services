package org.egov.swm.domain.repository;

import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.SanitationStaffSchedule;
import org.egov.swm.domain.model.SanitationStaffScheduleSearch;
import org.egov.swm.persistence.queue.repository.SanitationStaffScheduleQueueRepository;
import org.egov.swm.persistence.repository.SanitationStaffScheduleJdbcRepository;
import org.egov.swm.web.requests.SanitationStaffScheduleRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SanitationStaffScheduleRepository {

    @Autowired
    private SanitationStaffScheduleJdbcRepository sanitationStaffScheduleJdbcRepository;

    @Autowired
    private SanitationStaffScheduleQueueRepository sanitationStaffScheduleQueueRepository;

    public SanitationStaffScheduleRequest save(final SanitationStaffScheduleRequest sanitationStaffScheduleRequest) {

        return sanitationStaffScheduleQueueRepository.save(sanitationStaffScheduleRequest);

    }

    public SanitationStaffScheduleRequest update(final SanitationStaffScheduleRequest sanitationStaffScheduleRequest) {

        return sanitationStaffScheduleQueueRepository.update(sanitationStaffScheduleRequest);

    }

    public Pagination<SanitationStaffSchedule> search(final SanitationStaffScheduleSearch sanitationStaffScheduleSearch) {
        return sanitationStaffScheduleJdbcRepository.search(sanitationStaffScheduleSearch);

    }

}