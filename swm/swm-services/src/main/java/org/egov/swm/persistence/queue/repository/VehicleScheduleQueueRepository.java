package org.egov.swm.persistence.queue.repository;

import org.egov.swm.web.requests.VehicleScheduleRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class VehicleScheduleQueueRepository {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${egov.swm.vehicleschedule.save.topic}")
    private String saveTopic;

    @Value("${egov.swm.vehicleschedule.update.topic}")
    private String updateTopic;

    @Value("${egov.swm.vehicleschedule.indexer.topic}")
    private String indexerTopic;

    public VehicleScheduleRequest save(final VehicleScheduleRequest vehicleScheduleRequest) {

        kafkaTemplate.send(saveTopic, vehicleScheduleRequest);

        kafkaTemplate.send(indexerTopic, vehicleScheduleRequest);

        return vehicleScheduleRequest;

    }

    public VehicleScheduleRequest update(final VehicleScheduleRequest vehicleScheduleRequest) {

        kafkaTemplate.send(updateTopic, vehicleScheduleRequest);

        kafkaTemplate.send(indexerTopic, vehicleScheduleRequest);

        return vehicleScheduleRequest;

    }

}