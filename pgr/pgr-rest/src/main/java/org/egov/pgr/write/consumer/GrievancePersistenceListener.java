package org.egov.pgr.write.consumer;

import org.egov.pgr.write.contracts.grievance.SevaRequest;
import org.egov.pgr.write.service.ComplaintWriteService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class GrievancePersistenceListener {

    private ComplaintWriteService complaintWriteService;

    public GrievancePersistenceListener(ComplaintWriteService complaintWriteService) {
        this.complaintWriteService = complaintWriteService;
    }

    @KafkaListener(topics = "${kafka.topics.pgr.workflowupdated.name}")
    public void processMessage(HashMap<String, Object> sevaRequestMap) {
        SevaRequest sevaRequest = new SevaRequest(sevaRequestMap);
        complaintWriteService.updateOrInsert(sevaRequest.toDomain());
    }

}
