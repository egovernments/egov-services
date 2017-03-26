package org.egov.pgr.employee.enrichment.consumer;

import org.egov.pgr.employee.enrichment.model.SevaRequest;
import org.egov.pgr.employee.enrichment.repository.ComplaintRepository;
import org.egov.pgr.employee.enrichment.service.WorkflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class ComplaintAssignmentListener {

    private ComplaintRepository complaintRepository;
    private WorkflowService workflowService;

    @Autowired
    public ComplaintAssignmentListener(ComplaintRepository complaintRepository, WorkflowService workflowService) {
        this.workflowService = workflowService;
        this.complaintRepository = complaintRepository;
    }

    @KafkaListener(id = "${kafka.topics.pgr.locationpopulated.id}",
        topics = "${kafka.topics.pgr.locationpopulated.name}",
        group = "${kafka.topics.pgr.locationpopulated.group}")
    public void process(HashMap<String, Object> sevaRequestMap) {
        final SevaRequest sevaRequest = new SevaRequest(sevaRequestMap);
        final SevaRequest enrichedSevaRequest = workflowService.enrichWorkflow(sevaRequest);
        complaintRepository.save(enrichedSevaRequest);
    }

}
