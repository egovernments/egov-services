package org.egov.swm.persistence.queue.repository;

import org.egov.swm.web.requests.VehicleMaintenanceDetailsRequest;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class VehicleMaintenanceDetailsQueueRepository {

    private LogAwareKafkaTemplate kafkaTemplate;

    private String createTopic;

    private String updateTopic;

    private String indexTopic;


    public VehicleMaintenanceDetailsQueueRepository(LogAwareKafkaTemplate kafkaTemplate,
                                                    @Value("${egov.swm.vehiclemaintenancedetail.save.topic}") final String createTopic,
                                                    @Value("${egov.swm.vehiclemaintenancedetail.update.topic}") final String updateTopic,
                                                    @Value("${egov.swm.vehiclemaintenancedetail.indexer.topic}") final String indexTopic) {
        this.kafkaTemplate = kafkaTemplate;
        this.createTopic = createTopic;
        this.updateTopic = updateTopic;
        this.indexTopic = indexTopic;
    }

    public VehicleMaintenanceDetailsRequest save(VehicleMaintenanceDetailsRequest vehicleMaintenanceDetailsRequest){

        kafkaTemplate.send(createTopic, vehicleMaintenanceDetailsRequest);

        kafkaTemplate.send(indexTopic, vehicleMaintenanceDetailsRequest.getVehicleMaintenanceDetails());

        return vehicleMaintenanceDetailsRequest;
    }

    public VehicleMaintenanceDetailsRequest update(VehicleMaintenanceDetailsRequest vehicleMaintenanceDetailsRequest){

        kafkaTemplate.send(updateTopic, vehicleMaintenanceDetailsRequest);

        kafkaTemplate.send(indexTopic, vehicleMaintenanceDetailsRequest.getVehicleMaintenanceDetails());

        return vehicleMaintenanceDetailsRequest;
    }
}
