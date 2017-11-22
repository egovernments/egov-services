package org.egov.swm.domain.repository;

import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.VehicleSchedule;
import org.egov.swm.domain.model.VehicleScheduleSearch;
import org.egov.swm.persistence.queue.repository.VehicleScheduleQueueRepository;
import org.egov.swm.persistence.repository.VehicleScheduleJdbcRepository;
import org.egov.swm.web.requests.VehicleScheduleRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VehicleScheduleRepository {

    @Autowired
    private VehicleScheduleJdbcRepository vehicleScheduleJdbcRepository;

    @Autowired
    private VehicleScheduleQueueRepository vehicleScheduleQueueRepository;

    public VehicleScheduleRequest save(final VehicleScheduleRequest vehicleScheduleRequest) {

        return vehicleScheduleQueueRepository.save(vehicleScheduleRequest);

    }

    public VehicleScheduleRequest update(final VehicleScheduleRequest vehicleScheduleRequest) {

        return vehicleScheduleQueueRepository.update(vehicleScheduleRequest);

    }

    public Pagination<VehicleSchedule> search(final VehicleScheduleSearch vehicleScheduleSearch) {
        return vehicleScheduleJdbcRepository.search(vehicleScheduleSearch);

    }

    public Boolean uniqueCheck(final String tenantId, final String fieldName, final String fieldValue,
            final String uniqueFieldName,
            final String uniqueFieldValue) {

        return vehicleScheduleJdbcRepository.uniqueCheck(tenantId, fieldName, fieldValue, uniqueFieldName,
                uniqueFieldValue);
    }

}