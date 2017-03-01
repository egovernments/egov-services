package org.egov.pgr.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class GrievancePersistenceListener {

    private ComplaintTypeService complaintTypeService;
    private ComplaintStatusService complaintStatusService;
    private ComplaintService complaintService;
    private EscalationService escalationService;
    private GrievanceProducer kafkaProducer;
    private PositionRepository positionRepository;
    private TemplateService templateService;
    private PersistenceProperties persistenceProperties;

    @Autowired
    public GrievancePersistenceListener(ComplaintTypeService complaintTypeService, ComplaintStatusService complaintStatusService,
                                        ComplaintService complaintService, EscalationService escalationService,
                                        GrievanceProducer grievanceProducer, PositionRepository positionRepository,
                                        TemplateService templateService,PersistenceProperties persistenceProperties) {
        this.complaintService = complaintService;
        this.complaintTypeService = complaintTypeService;
        this.complaintStatusService = complaintStatusService;
        this.complaintService = complaintService;
        this.escalationService = escalationService;
        this.kafkaProducer = grievanceProducer;
        this.positionRepository = positionRepository;
        this.templateService = templateService;
        this.persistenceProperties = persistenceProperties;
    }

    @KafkaListener(id = "${kafka.topics.pgr.workflowupdated.id}",
            topics = "${kafka.topics.pgr.workflowupdated.name}",
            group = "${kafka.topics.pgr.workflowupdated.group}")
    public void processMessage(ConsumerRecord<String, Object> record) {
        SevaRequest sevaRequest = null;
        //TODO - Move away from concrete service request binding to a hash map.
        try {
            sevaRequest = new ObjectMapper().readValue((String) record.value(), SevaRequest.class);
            Complaint complaint = persistComplaint(sevaRequest);
            triggerSms(complaint);
            triggerEmail(complaint);
            triggerIndexing(complaint);
        } catch (IOException e) {
            //Log error around deserialization failed
            e.printStackTrace();
        }
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
        String complaintCrn = sevaRequest.getServiceRequest().getCrn();
        Complaint complaintByCrn = complaintService.findByCrn(complaintCrn);
        Complaint complaint = new ComplaintBuilder(complaintByCrn, sevaRequest.getServiceRequest(), complaintTypeService, complaintStatusService, escalationService, positionRepository).build();
        complaint = complaintService.save(complaint);
        return complaint;
    }

}
