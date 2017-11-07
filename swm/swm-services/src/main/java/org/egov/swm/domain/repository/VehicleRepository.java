package org.egov.swm.domain.repository;

import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.Vehicle;
import org.egov.swm.domain.model.VehicleSearch;
import org.egov.swm.persistence.queue.repository.VehicleQueueRepository;
import org.egov.swm.persistence.repository.VehicleJdbcRepository;
import org.egov.swm.web.requests.VehicleRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VehicleRepository {

	@Autowired
	private VehicleQueueRepository vehicleQueueRepository;

	@Autowired
	private VehicleJdbcRepository vehicleJdbcRepository;

	public VehicleRequest save(VehicleRequest vehicleRequest) {

		return vehicleQueueRepository.save(vehicleRequest);

	}

	public VehicleRequest update(VehicleRequest vehicleRequest) {

		return vehicleQueueRepository.update(vehicleRequest);

	}

	public Pagination<Vehicle> search(VehicleSearch vehicleSearch) {
		return vehicleJdbcRepository.search(vehicleSearch);

	}

	public Boolean uniqueCheck(String tenantId, String fieldName, String fieldValue, String uniqueFieldName,
			String uniqueFieldValue) {

		return vehicleJdbcRepository.uniqueCheck(tenantId, fieldName, fieldValue, uniqueFieldName, uniqueFieldValue);
	}

}