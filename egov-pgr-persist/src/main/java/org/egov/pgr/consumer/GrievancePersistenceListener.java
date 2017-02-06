package org.egov.pgr.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.egov.pgr.contracts.grievance.SevaRequest;
import org.egov.pgr.entity.Complaint;
import org.egov.pgr.model.EmailComposer;
import org.egov.pgr.model.SmsComposer;
import org.egov.pgr.producer.GrievanceProducer;
import org.egov.pgr.service.ComplaintService;
import org.egov.pgr.service.ComplaintStatusService;
import org.egov.pgr.service.ComplaintTypeService;
import org.egov.pgr.service.EscalationService;
import org.egov.pgr.transform.ComplaintBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.trimou.engine.MustacheEngine;
import org.trimou.engine.MustacheEngineBuilder;
import org.trimou.engine.locator.ClassPathTemplateLocator;
import org.trimou.engine.locator.FileSystemTemplateLocator;

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

    MustacheEngine templatingEngine = MustacheEngineBuilder.newBuilder().addTemplateLocator(new ClassPathTemplateLocator(1, "templates","txt")).build();

    @KafkaListener(id = "grievancePersister", topics = "ap.public.mseva.persistreadyg", group = "grievances")
    public void processMessage(ConsumerRecord<String, SevaRequest> record) {
        SevaRequest sevaRequest = record.value();
        Complaint complaint = persistComplaint(sevaRequest);
        triggerSms(complaint);
        triggerEmail(complaint);
        triggerIndexing(complaint);
        triggerWorkflow(sevaRequest);
    }

    private void triggerWorkflow(SevaRequest record) {
        //TODO - Trigger workflow
    }

    private void triggerIndexing(Complaint record) {
        kafkaProducer.sendMessage(".mseva.index", record);
    }

    private void triggerEmail(Complaint complaint) {
        kafkaProducer.sendMessage(".mseva.email", new EmailComposer(complaint, templatingEngine).compose());
    }

    private void triggerSms(Complaint complaint) {
        kafkaProducer.sendMessage(".mseva.sms", new SmsComposer(complaint, templatingEngine).compose());
    }

    private Complaint persistComplaint(SevaRequest sevaRequest) {
        Complaint complaint = new ComplaintBuilder(sevaRequest.getServiceRequest(), complaintTypeService, complaintStatusService, escalationService).build();
//        complaintService.createComplaint(complaint);
        return complaint;
    }

}
