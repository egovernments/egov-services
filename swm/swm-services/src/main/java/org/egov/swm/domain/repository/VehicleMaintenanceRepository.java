package org.egov.swm.domain.repository;

import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.VehicleMaintenance;
import org.egov.swm.domain.model.VehicleMaintenanceSearch;
import org.egov.swm.persistence.queue.repository.VehicleMaintenanceQueueRepository;
import org.egov.swm.persistence.repository.VehicleMaintenanceJdbcRepository;
import org.egov.swm.web.requests.VehicleMaintenanceRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VehicleMaintenanceRepository {

    @Autowired
    private VehicleMaintenanceQueueRepository vehicleMaintenanceQueueRepository;

    @Autowired
    private VehicleMaintenanceJdbcRepository vehicleMaintenanceJdbcRepository;

    public VehicleMaintenanceRequest save(final VehicleMaintenanceRequest vehicleMaintenanceRequest) {

        return vehicleMaintenanceQueueRepository.save(vehicleMaintenanceRequest);

    }

    public VehicleMaintenanceRequest update(final VehicleMaintenanceRequest vehicleMaintenanceRequest) {

        return vehicleMaintenanceQueueRepository.update(vehicleMaintenanceRequest);

    }

    public Pagination<VehicleMaintenance> search(final VehicleMaintenanceSearch vehicleMaintenanceSearch) {
        return vehicleMaintenanceJdbcRepository.search(vehicleMaintenanceSearch);

    }

    public Boolean uniqueCheck(final String tenantId, final String fieldName, final String fieldValue,
            final String uniqueFieldName,
            final String uniqueFieldValue) {

        return vehicleMaintenanceJdbcRepository.uniqueCheck(tenantId, fieldName, fieldValue, uniqueFieldName,
                uniqueFieldValue);
    }

}