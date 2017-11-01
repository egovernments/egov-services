package org.egov.swm.domain.repository;

import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.VehicleFuellingDetails;
import org.egov.swm.domain.model.VehicleFuellingDetailsSearch;
import org.egov.swm.persistence.repository.DocumentJdbcRepository;
import org.egov.swm.persistence.repository.VehicleFuellingDetailsJdbcRepository;
import org.egov.swm.web.requests.VehicleFuellingDetailsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class VehicleFuellingDetailsRepository {

	@Autowired
	private KafkaTemplate<String, Object> kafkaTemplate;

	@Autowired
	private VehicleFuellingDetailsJdbcRepository vehicleFuellingDetailsJdbcRepository;

	@Autowired
	private DocumentJdbcRepository documentJdbcRepository;

	@Value("${egov.swm.vehiclefuellingdetails.save.topic}")
	private String saveTopic;

	@Value("${egov.swm.vehiclefuellingdetails.update.topic}")
	private String updateTopic;

	@Value("${egov.swm.vehiclefuellingdetails.indexer.topic}")
	private String indexerTopic;

	public VehicleFuellingDetailsRequest save(VehicleFuellingDetailsRequest vehicleFuellingDetailsRequest) {

		kafkaTemplate.send(saveTopic, vehicleFuellingDetailsRequest);

		kafkaTemplate.send(indexerTopic, vehicleFuellingDetailsRequest.getVehicleFuellingDetails());

		return vehicleFuellingDetailsRequest;

	}

	public VehicleFuellingDetailsRequest update(VehicleFuellingDetailsRequest vehicleFuellingDetailsRequest) {

		for (VehicleFuellingDetails vfd : vehicleFuellingDetailsRequest.getVehicleFuellingDetails()) {
			documentJdbcRepository.delete(vfd.getTransactionNo());
		}
		
		kafkaTemplate.send(updateTopic, vehicleFuellingDetailsRequest);

		kafkaTemplate.send(indexerTopic, vehicleFuellingDetailsRequest.getVehicleFuellingDetails());

		return vehicleFuellingDetailsRequest;

	}

	public Pagination<VehicleFuellingDetails> search(VehicleFuellingDetailsSearch vehicleFuellingDetailsSearch) {
		return vehicleFuellingDetailsJdbcRepository.search(vehicleFuellingDetailsSearch);

	}

}