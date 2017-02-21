package org.egov.pgr.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.egov.pgr.config.PersistenceProperties;
import org.egov.pgr.contracts.grievance.SevaRequest;
import org.egov.pgr.entity.Complaint;
import org.egov.pgr.model.ComplaintBuilder;
import org.egov.pgr.model.EmailComposer;
import org.egov.pgr.model.SmsComposer;
import org.egov.pgr.producer.GrievanceProducer;
import org.egov.pgr.repository.PositionRepository;
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
    private GrievanceProducer kafkaProducer;

    @Autowired
    private PositionRepository positionRepository;

    @Autowired
    private TemplateService templateService;

    @Autowired
    private PersistenceProperties persistenceProperties;

    @KafkaListener(id = "${kafka.topics.pgr.workflowupdated.id}", topics = "${kafka.topics.pgr.workflowupdated.name}", group = "${kafka.topics.pgr.workflowupdated.group}")
    public void processMessage(ConsumerRecord<String, SevaRequest> record) {
        SevaRequest sevaRequest = record.value();
        Complaint complaint = persistComplaint(sevaRequest);
        triggerSms(complaint);
        triggerEmail(complaint);
        triggerIndexing(complaint);
    }

    private void triggerIndexing(Complaint record) {
        kafkaProducer.sendMessage(persistenceProperties.getIndexingTopic(), record);
    }

    private void triggerEmail(Complaint complaint) {
        kafkaProducer.sendMessage(persistenceProperties.getEmailTopic(), new EmailComposer(complaint, templateService).compose());
    }

    private void triggerSms(Complaint complaint) {
        kafkaProducer.sendMessage(persistenceProperties.getSmsTopic(), new SmsComposer(complaint, templateService).compose());
    }

    private Complaint persistComplaint(SevaRequest sevaRequest) {
        Complaint complaint = new ComplaintBuilder(sevaRequest.getServiceRequest(), complaintTypeService, complaintStatusService, escalationService, positionRepository).build();
        complaint = complaintService.createComplaint(complaint);
        return complaint;
    }

}
