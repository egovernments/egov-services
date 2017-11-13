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

    private VehicleMaintenanceDetailsQueueRepository vehicleMaintenanceDetailsQueueRepository;

    private VehicleMaintenanceDetailsJdbcRepository vehicleMaintenanceDetailsJdbcRepository;

    public VehicleMaintenanceDetailsRepository(VehicleMaintenanceDetailsQueueRepository vehicleMaintenanceDetailsQueueRepository,
                                               VehicleMaintenanceDetailsJdbcRepository vehicleMaintenanceDetailsJdbcRepository) {
        this.vehicleMaintenanceDetailsQueueRepository = vehicleMaintenanceDetailsQueueRepository;
        this.vehicleMaintenanceDetailsJdbcRepository = vehicleMaintenanceDetailsJdbcRepository;
    }

    public VehicleMaintenanceDetailsRequest create(VehicleMaintenanceDetailsRequest vehicleMaintenanceDetailsRequest){

        return vehicleMaintenanceDetailsQueueRepository.save(vehicleMaintenanceDetailsRequest);
    }

    public VehicleMaintenanceDetailsRequest update(VehicleMaintenanceDetailsRequest vehicleMaintenanceDetailsRequest){

        return vehicleMaintenanceDetailsQueueRepository.update(vehicleMaintenanceDetailsRequest);
    }

    public Pagination<VehicleMaintenanceDetails> search(VehicleMaintenanceDetailsSearch vehicleMaintenanceDetailsSearch){

        return vehicleMaintenanceDetailsJdbcRepository.search(vehicleMaintenanceDetailsSearch);
    }
}
