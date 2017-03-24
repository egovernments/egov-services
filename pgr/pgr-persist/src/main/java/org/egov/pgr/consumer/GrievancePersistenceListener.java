package org.egov.pgr.consumer;

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import org.egov.pgr.config.PersistenceProperties;
import org.egov.pgr.contracts.grievance.RequestInfo;
import org.egov.pgr.contracts.grievance.SevaRequest;
import org.egov.pgr.entity.Complaint;
import org.egov.pgr.model.ComplaintBuilder;
import org.egov.pgr.model.EmailComposer;
import org.egov.pgr.model.SmsComposer;
import org.egov.pgr.producer.GrievanceProducer;
import org.egov.pgr.repository.PositionRepository;
import org.egov.pgr.repository.UserRepository;
import org.egov.pgr.service.ComplaintService;
import org.egov.pgr.service.ComplaintStatusService;
import org.egov.pgr.service.ComplaintTypeService;
import org.egov.pgr.service.EscalationService;
import org.egov.pgr.service.ReceivingCenterService;
import org.egov.pgr.service.ReceivingModeService;
import org.egov.pgr.service.TemplateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class GrievancePersistenceListener {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private ComplaintTypeService complaintTypeService;
	private ComplaintStatusService complaintStatusService;
	private ComplaintService complaintService;
	private EscalationService escalationService;
	private GrievanceProducer kafkaProducer;
	private PositionRepository positionRepository;
	private UserRepository userRepository;
	private TemplateService templateService;
	private PersistenceProperties persistenceProperties;
	private ReceivingCenterService receivingCenterService;
	private RequestInfo requestInfo;
	private ReceivingModeService receivingModeService;

	@Autowired
	public GrievancePersistenceListener(ComplaintTypeService complaintTypeService,
			ComplaintStatusService complaintStatusService, ComplaintService complaintService,
			EscalationService escalationService, GrievanceProducer grievanceProducer,
			PositionRepository positionRepository, UserRepository userRepository, TemplateService templateService,
			PersistenceProperties persistenceProperties, ReceivingCenterService receivingCenterService,
			ReceivingModeService receivingModeService) {
		this.complaintService = complaintService;
		this.complaintTypeService = complaintTypeService;
		this.complaintStatusService = complaintStatusService;
		this.complaintService = complaintService;
		this.escalationService = escalationService;
		this.kafkaProducer = grievanceProducer;
		this.positionRepository = positionRepository;
		this.templateService = templateService;
		this.persistenceProperties = persistenceProperties;
		this.userRepository = userRepository;
		this.receivingCenterService = receivingCenterService;
		this.receivingModeService = receivingModeService;

	}

	@KafkaListener(id = "${kafka.topics.pgr.workflowupdated.id}", topics = "${kafka.topics.pgr.workflowupdated.name}", group = "${kafka.topics.pgr.workflowupdated.group}")
	public void processMessage(HashMap<String, Object> sevaRequestMap) {
		logger.debug("Received seva request");
		SevaRequest sevaRequest = new SevaRequest(sevaRequestMap);
		Complaint complaint = persistComplaint(sevaRequest);
//		triggerSms(complaint);
//		triggerEmail(complaint);
		triggerIndexing(sevaRequest);
	}

	private void triggerIndexing(SevaRequest record) {
		logger.debug("Triggering Indexing for" + record.getServiceRequest().getCrn());
		kafkaProducer.sendMessage(persistenceProperties.getIndexingTopic(), record.getSevaRequestMap());
	}

	private void triggerEmail(Complaint complaint) {
		if (StringUtils.isNotEmpty(complaint.getComplainant().getEmail())) {
			logger.debug("Triggering email for" + complaint.getCrn());
			kafkaProducer.sendMessage(persistenceProperties.getEmailTopic(),
					new EmailComposer(complaint, templateService).compose());
		}
	}

	private void triggerSms(Complaint complaint) {
		logger.debug("Triggering sms for" + complaint.getCrn());
		kafkaProducer.sendMessage(persistenceProperties.getSmsTopic(),
				new SmsComposer(complaint, templateService).compose());
	}

	private Complaint persistComplaint(SevaRequest sevaRequest) {
		logger.debug("Saving record in database for" + sevaRequest.getServiceRequest().getCrn());
		String complaintCrn = sevaRequest.getServiceRequest().getCrn();
		Complaint complaintByCrn = complaintService.findByCrn(complaintCrn);
		Complaint complaint = new ComplaintBuilder(complaintByCrn, sevaRequest, complaintTypeService,
				complaintStatusService, escalationService, positionRepository, userRepository, receivingCenterService,
				requestInfo, receivingModeService).build();
		complaint = complaintService.save(complaint);
		return complaint;
	}
}
