package org.egov.swm.persistence.queue.repository;

import org.egov.swm.web.requests.VehicleMaintenanceRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class VehicleMaintenanceQueueRepository {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${egov.swm.vehiclemaintenance.save.topic}")
    private String saveTopic;

    @Value("${egov.swm.vehiclemaintenance.update.topic}")
    private String updateTopic;

    @Value("${egov.swm.vehiclemaintenance.indexer.topic}")
    private String indexerTopic;

    public VehicleMaintenanceRequest save(final VehicleMaintenanceRequest vehicleMaintenanceRequest) {

        kafkaTemplate.send(saveTopic, vehicleMaintenanceRequest);

        kafkaTemplate.send(indexerTopic, vehicleMaintenanceRequest.getVehicleMaintenances());

        return vehicleMaintenanceRequest;

    }

    public VehicleMaintenanceRequest update(final VehicleMaintenanceRequest vehicleMaintenanceRequest) {

        kafkaTemplate.send(updateTopic, vehicleMaintenanceRequest);

        kafkaTemplate.send(indexerTopic, vehicleMaintenanceRequest.getVehicleMaintenances());

        return vehicleMaintenanceRequest;

    }

}