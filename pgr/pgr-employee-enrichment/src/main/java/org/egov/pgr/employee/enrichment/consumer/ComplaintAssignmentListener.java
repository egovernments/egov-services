package org.egov.pgr.employee.enrichment.consumer;

import java.util.HashMap;

import org.egov.pgr.employee.enrichment.model.SevaRequest;
import org.egov.pgr.employee.enrichment.repository.ComplaintMessageQueueRepository;
import org.egov.pgr.employee.enrichment.service.CommonWorkflowService;
import org.egov.pgr.employee.enrichment.service.EscalationDateService;
import org.egov.pgr.employee.enrichment.service.PositionService;
import org.egov.pgr.employee.enrichment.service.WorkflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ComplaintAssignmentListener {

	private ComplaintMessageQueueRepository complaintMessageQueueRepository;
	private WorkflowService workflowService;
	private CommonWorkflowService commonworkflowService;
	private EscalationDateService escalationDateService;
	private PositionService positionService;
	public static final String DELIVERABLE_SERVICE = "Deliverable_Service";
	public static final String COMPLAINT = "Complaint";

	@Autowired
	public ComplaintAssignmentListener(ComplaintMessageQueueRepository complaintMessageQueueRepository,
			WorkflowService workflowService, CommonWorkflowService commonworkflowService,
			EscalationDateService escalationDateService, PositionService positionService) {
		this.workflowService = workflowService;
		this.commonworkflowService = commonworkflowService;
		this.complaintMessageQueueRepository = complaintMessageQueueRepository;
		this.escalationDateService = escalationDateService;
		this.positionService = positionService;
	}

	@KafkaListener(topics = "${kafka.topics.pgr.locationpopulated.name}")
	public void process(HashMap<String, Object> sevaRequestMap) {
		final SevaRequest sevaRequest = new SevaRequest(sevaRequestMap);
		SevaRequest enrichedSevaRequest = null;

		if (sevaRequest.getKeyword() != null && sevaRequest.getKeyword().equalsIgnoreCase(DELIVERABLE_SERVICE)
				&& !sevaRequest.getKeyword().equalsIgnoreCase(COMPLAINT))
			enrichedSevaRequest = commonworkflowService.enrichWorkflow(sevaRequest);
		else
			enrichedSevaRequest = workflowService.enrichWorkflow(sevaRequest);

		positionService.enrichRequestWithPosition(enrichedSevaRequest);
		escalationDateService.enrichRequestWithEscalationDate(enrichedSevaRequest);
		complaintMessageQueueRepository.save(enrichedSevaRequest);
	}

}
