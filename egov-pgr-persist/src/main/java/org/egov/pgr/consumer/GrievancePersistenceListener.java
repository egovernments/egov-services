package org.egov.pgr.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.egov.pgr.contracts.grievance.SevaRequest;
import org.egov.pgr.contracts.workflow.CreatWorkflowRequest;
import org.egov.pgr.entity.Complaint;
import org.egov.pgr.model.ComplaintBuilder;
import org.egov.pgr.model.EmailComposer;
import org.egov.pgr.model.SmsComposer;
import org.egov.pgr.producer.GrievanceProducer;
import org.egov.pgr.repository.PositionRepository;
import org.egov.pgr.repository.WorkflowRepository;
import org.egov.pgr.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

public class GrievancePersistenceListener {

    @Autowired
    private ComplaintTypeService complaintTypeService;

    @Autowired
    private ComplaintStatusService complaintStatusService;

    @Autowired
    private ComplaintService complaintService;

    @Autowired
    private EscalationService escalationService;

    @Autowired
    private WorkflowRepository workflowRepository;

    @Autowired
    private GrievanceProducer kafkaProducer;

    @Autowired
    private PositionRepository positionRepository;

    @Autowired
    private TemplateService templateService;

    @KafkaListener(id = "grievancePersister", topics = "ap.public.mseva.persistreadyh", group = "grievances")
    public void processMessage(ConsumerRecord<String, SevaRequest> record) {
        SevaRequest sevaRequest = record.value();
        Complaint complaint = persistComplaint(sevaRequest);
        triggerSms(complaint);
        triggerEmail(complaint);
        triggerIndexing(complaint);
        triggerWorkflow(complaint);
    }

    private void triggerWorkflow(Complaint complaint) {
        workflowRepository.startWorkFlow(new CreatWorkflowRequest().fromDomain(complaint));
    }

    private void triggerIndexing(Complaint record) {
        kafkaProducer.sendMessage(".mseva.index", record);
    }

    private void triggerEmail(Complaint complaint) {
        kafkaProducer.sendMessage(".mseva.email", new EmailComposer(complaint, templateService).compose());
    }

    private void triggerSms(Complaint complaint) {
        kafkaProducer.sendMessage(".mseva.sms", new SmsComposer(complaint, templateService).compose());
    }

    private Complaint persistComplaint(SevaRequest sevaRequest) {
        Complaint complaint = new ComplaintBuilder(sevaRequest.getServiceRequest(), complaintTypeService, complaintStatusService, escalationService, positionRepository).build();
        complaint = complaintService.createComplaint(complaint);
        return complaint;
    }

}
