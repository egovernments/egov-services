package org.egov.swm.persistence.queue.repository;

import org.egov.swm.web.requests.VehicleRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class VehicleQueueRepository {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${egov.swm.vehicle.save.topic}")
    private String saveTopic;

    @Value("${egov.swm.vehicle.update.topic}")
    private String updateTopic;

    @Value("${egov.swm.vehicle.indexer.topic}")
    private String indexerTopic;

    public VehicleRequest save(final VehicleRequest vehicleRequest) {

        kafkaTemplate.send(saveTopic, vehicleRequest);

        kafkaTemplate.send(indexerTopic, vehicleRequest);

        return vehicleRequest;

    }

    public VehicleRequest update(final VehicleRequest vehicleRequest) {

        kafkaTemplate.send(updateTopic, vehicleRequest);

        kafkaTemplate.send(indexerTopic, vehicleRequest);

        return vehicleRequest;

    }

}