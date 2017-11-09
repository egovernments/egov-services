package org.egov.swm.domain.repository;

import org.egov.swm.persistence.queue.repository.VehicleMaintenanceDetailsQueueRepository;
import org.egov.swm.web.requests.VehicleMaintenanceDetailsRequest;
import org.springframework.stereotype.Service;

@Service
public class VehicleMaintenanceDetailsRepository {

    private VehicleMaintenanceDetailsQueueRepository vehicleMaintenanceDetailsQueueRepository;

    public VehicleMaintenanceDetailsRepository(VehicleMaintenanceDetailsQueueRepository vehicleMaintenanceDetailsQueueRepository) {
        this.vehicleMaintenanceDetailsQueueRepository = vehicleMaintenanceDetailsQueueRepository;
    }

    public VehicleMaintenanceDetailsRequest create(VehicleMaintenanceDetailsRequest vehicleMaintenanceDetailsRequest){

        return vehicleMaintenanceDetailsQueueRepository.save(vehicleMaintenanceDetailsRequest);
    }
}
