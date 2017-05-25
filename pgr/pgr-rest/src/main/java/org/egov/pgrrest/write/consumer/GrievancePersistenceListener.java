package org.egov.pgrrest.write.consumer;

import org.egov.pgrrest.write.contracts.grievance.SevaRequest;
import org.egov.pgrrest.write.service.ServiceRequestWriteService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class GrievancePersistenceListener {

    private ServiceRequestWriteService complaintWriteService;

    public GrievancePersistenceListener(ServiceRequestWriteService complaintWriteService) {
        this.complaintWriteService = complaintWriteService;
    }

    @KafkaListener(topics = "${kafka.topics.pgr.workflowupdated.name}")
    public void processMessage(HashMap<String, Object> sevaRequestMap) {
        SevaRequest sevaRequest = new SevaRequest(sevaRequestMap);
        complaintWriteService.updateOrInsert(sevaRequest.toDomain());
    }

}
