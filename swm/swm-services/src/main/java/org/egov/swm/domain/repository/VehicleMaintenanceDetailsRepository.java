package org.egov.swm.domain.repository;

import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.VehicleMaintenanceDetails;
import org.egov.swm.domain.model.VehicleMaintenanceDetailsSearch;
import org.egov.swm.persistence.queue.repository.VehicleMaintenanceDetailsQueueRepository;
import org.egov.swm.persistence.repository.VehicleMaintenanceDetailsJdbcRepository;
import org.egov.swm.web.requests.VehicleMaintenanceDetailsRequest;
import org.springframework.stereotype.Service;

@Service
public class VehicleMaintenanceDetailsRepository {

    private final VehicleMaintenanceDetailsQueueRepository vehicleMaintenanceDetailsQueueRepository;

    private final VehicleMaintenanceDetailsJdbcRepository vehicleMaintenanceDetailsJdbcRepository;

    public VehicleMaintenanceDetailsRepository(
            final VehicleMaintenanceDetailsQueueRepository vehicleMaintenanceDetailsQueueRepository,
            final VehicleMaintenanceDetailsJdbcRepository vehicleMaintenanceDetailsJdbcRepository) {
        this.vehicleMaintenanceDetailsQueueRepository = vehicleMaintenanceDetailsQueueRepository;
        this.vehicleMaintenanceDetailsJdbcRepository = vehicleMaintenanceDetailsJdbcRepository;
    }

    public VehicleMaintenanceDetailsRequest create(final VehicleMaintenanceDetailsRequest vehicleMaintenanceDetailsRequest) {

        return vehicleMaintenanceDetailsQueueRepository.save(vehicleMaintenanceDetailsRequest);
    }

    public VehicleMaintenanceDetailsRequest update(final VehicleMaintenanceDetailsRequest vehicleMaintenanceDetailsRequest) {

        return vehicleMaintenanceDetailsQueueRepository.update(vehicleMaintenanceDetailsRequest);
    }

    public Pagination<VehicleMaintenanceDetails> search(final VehicleMaintenanceDetailsSearch vehicleMaintenanceDetailsSearch) {

        return vehicleMaintenanceDetailsJdbcRepository.search(vehicleMaintenanceDetailsSearch);
    }
}
