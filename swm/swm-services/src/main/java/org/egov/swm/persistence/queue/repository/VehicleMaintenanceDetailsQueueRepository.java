package org.egov.swm.persistence.queue.repository;

import org.egov.swm.web.requests.VehicleMaintenanceDetailsRequest;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class VehicleMaintenanceDetailsQueueRepository {

    private final LogAwareKafkaTemplate kafkaTemplate;

    private final String createTopic;

    private final String updateTopic;

    private final String indexTopic;

    public VehicleMaintenanceDetailsQueueRepository(final LogAwareKafkaTemplate kafkaTemplate,
            @Value("${egov.swm.vehiclemaintenancedetail.save.topic}") final String createTopic,
            @Value("${egov.swm.vehiclemaintenancedetail.update.topic}") final String updateTopic,
            @Value("${egov.swm.vehiclemaintenancedetail.indexer.topic}") final String indexTopic) {
        this.kafkaTemplate = kafkaTemplate;
        this.createTopic = createTopic;
        this.updateTopic = updateTopic;
        this.indexTopic = indexTopic;
    }

    public VehicleMaintenanceDetailsRequest save(final VehicleMaintenanceDetailsRequest vehicleMaintenanceDetailsRequest) {

        kafkaTemplate.send(createTopic, vehicleMaintenanceDetailsRequest);

        kafkaTemplate.send(indexTopic, vehicleMaintenanceDetailsRequest);

        return vehicleMaintenanceDetailsRequest;
    }

    public VehicleMaintenanceDetailsRequest update(final VehicleMaintenanceDetailsRequest vehicleMaintenanceDetailsRequest) {

        kafkaTemplate.send(updateTopic, vehicleMaintenanceDetailsRequest);

        kafkaTemplate.send(indexTopic, vehicleMaintenanceDetailsRequest);

        return vehicleMaintenanceDetailsRequest;
    }
}
