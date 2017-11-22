package org.egov.swm.persistence.queue.repository;

import org.egov.swm.web.requests.VehicleFuellingDetailsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class VehicleFuellingDetailsQueueRepository {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${egov.swm.vehiclefuellingdetails.save.topic}")
    private String saveTopic;

    @Value("${egov.swm.vehiclefuellingdetails.update.topic}")
    private String updateTopic;

    @Value("${egov.swm.vehiclefuellingdetails.indexer.topic}")
    private String indexerTopic;

    public VehicleFuellingDetailsRequest save(final VehicleFuellingDetailsRequest vehicleFuellingDetailsRequest) {

        kafkaTemplate.send(saveTopic, vehicleFuellingDetailsRequest);

        kafkaTemplate.send(indexerTopic, vehicleFuellingDetailsRequest);

        return vehicleFuellingDetailsRequest;

    }

    public VehicleFuellingDetailsRequest update(final VehicleFuellingDetailsRequest vehicleFuellingDetailsRequest) {

        kafkaTemplate.send(updateTopic, vehicleFuellingDetailsRequest);

        kafkaTemplate.send(indexerTopic, vehicleFuellingDetailsRequest);

        return vehicleFuellingDetailsRequest;

    }

}