package org.egov.pgrrest.write.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.pgrrest.write.consumer.contracts.request.SevaRequest;
import org.egov.pgrrest.write.domain.service.ServiceRequestWriteService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class GrievancePersistenceListener {

    private ServiceRequestWriteService complaintWriteService;
    private ObjectMapper objectMapper;

    public GrievancePersistenceListener(ServiceRequestWriteService complaintWriteService,
                                        ObjectMapper objectMapper) {
        this.complaintWriteService = complaintWriteService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "${kafka.topics.pgr.workflowupdated.name}")
    public void processMessage(HashMap<String, Object> sevaRequestMap) {
        SevaRequest sevaRequest = new SevaRequest(sevaRequestMap, objectMapper);
        complaintWriteService.updateOrInsert(sevaRequest.toDomain());
    }

}
