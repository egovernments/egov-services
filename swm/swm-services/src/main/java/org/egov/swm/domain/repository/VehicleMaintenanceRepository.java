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

	public VehicleMaintenanceRequest save(VehicleMaintenanceRequest vehicleMaintenanceRequest) {

		return vehicleMaintenanceQueueRepository.save(vehicleMaintenanceRequest);

	}

	public VehicleMaintenanceRequest update(VehicleMaintenanceRequest vehicleMaintenanceRequest) {

		return vehicleMaintenanceQueueRepository.update(vehicleMaintenanceRequest);

	}

	public Pagination<VehicleMaintenance> search(VehicleMaintenanceSearch vehicleMaintenanceSearch) {
		return vehicleMaintenanceJdbcRepository.search(vehicleMaintenanceSearch);

	}

}