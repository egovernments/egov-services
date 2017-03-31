package org.egov.pgr.employee.enrichment.consumer;

import org.egov.pgr.employee.enrichment.model.SevaRequest;
import org.egov.pgr.employee.enrichment.repository.ComplaintMessageQueueRepository;
import org.egov.pgr.employee.enrichment.service.DepartmentService;
import org.egov.pgr.employee.enrichment.service.EscalationDateService;
import org.egov.pgr.employee.enrichment.service.WorkflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class ComplaintAssignmentListener {

    private ComplaintMessageQueueRepository complaintMessageQueueRepository;
    private WorkflowService workflowService;
    private EscalationDateService escalationDateService;
    private DepartmentService departmentService;

    @Autowired
    public ComplaintAssignmentListener(ComplaintMessageQueueRepository complaintMessageQueueRepository,
                                       WorkflowService workflowService,
                                       EscalationDateService escalationDateService,
                                       DepartmentService departmentService) {
        this.workflowService = workflowService;
        this.complaintMessageQueueRepository = complaintMessageQueueRepository;
        this.escalationDateService = escalationDateService;
        this.departmentService = departmentService;
    }

    @KafkaListener(topics = "${kafka.topics.pgr.locationpopulated.name}")
    public void process(HashMap<String, Object> sevaRequestMap) {
        final SevaRequest sevaRequest = new SevaRequest(sevaRequestMap);
        final SevaRequest enrichedSevaRequest = workflowService.enrichWorkflow(sevaRequest);
        departmentService.enrichRequestWithDesignation(enrichedSevaRequest);
        escalationDateService.enrichRequestWithEscalationDate(enrichedSevaRequest);
        complaintMessageQueueRepository.save(enrichedSevaRequest);
    }

}
