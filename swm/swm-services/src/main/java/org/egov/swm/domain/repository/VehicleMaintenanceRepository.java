package org.egov.swm.domain.repository;

import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.VehicleMaintenance;
import org.egov.swm.domain.model.VehicleMaintenanceSearch;
import org.egov.swm.persistence.repository.VehicleMaintenanceJdbcRepository;
import org.egov.swm.web.requests.VehicleMaintenanceRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class VehicleMaintenanceRepository {

	@Autowired
	private KafkaTemplate<String, Object> kafkaTemplate;

	@Autowired
	private VehicleMaintenanceJdbcRepository vehicleMaintenanceJdbcRepository;

	@Value("${egov.swm.vehiclemaintenance.save.topic}")
	private String saveTopic;

	@Value("${egov.swm.vehiclemaintenance.update.topic}")
	private String updateTopic;

	@Value("${egov.swm.vehiclemaintenance.indexer.topic}")
	private String indexerTopic;

	public VehicleMaintenanceRequest save(VehicleMaintenanceRequest vehicleMaintenanceRequest) {

		kafkaTemplate.send(saveTopic, vehicleMaintenanceRequest);

		kafkaTemplate.send(indexerTopic, vehicleMaintenanceRequest.getVehicleMaintenances());

		return vehicleMaintenanceRequest;

	}

	public VehicleMaintenanceRequest update(VehicleMaintenanceRequest vehicleMaintenanceRequest) {

		kafkaTemplate.send(updateTopic, vehicleMaintenanceRequest);

		kafkaTemplate.send(indexerTopic, vehicleMaintenanceRequest.getVehicleMaintenances());

		return vehicleMaintenanceRequest;

	}

	public Pagination<VehicleMaintenance> search(VehicleMaintenanceSearch vehicleMaintenanceSearch) {
		return vehicleMaintenanceJdbcRepository.search(vehicleMaintenanceSearch);

	}

}