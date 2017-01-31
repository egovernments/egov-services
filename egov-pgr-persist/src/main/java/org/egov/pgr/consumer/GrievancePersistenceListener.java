package org.egov.pgr.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.egov.pgr.entity.Complaint;
import org.egov.pgr.model.EmailMessage;
import org.egov.pgr.model.ServiceRequest;
import org.egov.pgr.model.SevaRequest;
import org.egov.pgr.model.SmsMessage;
import org.egov.pgr.producer.GrievanceProducer;
import org.egov.pgr.service.ComplaintService;
import org.egov.pgr.service.ComplaintStatusService;
import org.egov.pgr.service.ComplaintTypeService;
import org.egov.pgr.transform.ComplaintBuilder;
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
    private GrievanceProducer kafkaProducer;

    @KafkaListener(id = "grievancePersister", topics = "ap.public.mseva.validatedx", group = "grievances")
    public void processMessage(ConsumerRecord<String, SevaRequest> record) {
        SevaRequest sevaRequest = record.value();
        persistComplaint(sevaRequest);
        triggerEmail(sevaRequest.getServiceRequest());
        triggerSms(sevaRequest.getServiceRequest());
        triggerIndexing(sevaRequest);
        triggerWorkflow(sevaRequest);
    }

    private void triggerWorkflow(SevaRequest record) {

    }

    private void triggerIndexing(SevaRequest record) {
        kafkaProducer.sendMessage(".mseva.validated", record);
    }

    private void triggerEmail(ServiceRequest record) {
        EmailMessage emailMessage = new EmailMessage(record.getEmail(), "subject", "email body", null);
        kafkaProducer.sendMessage(".mseva.validated", emailMessage);
    }

    private void triggerSms(ServiceRequest record) {
        SmsMessage smsMessage = new SmsMessage(record.getPhone(), "sms message");
        kafkaProducer.sendMessage(".mseva.validated", smsMessage);
    }

    private void persistComplaint(SevaRequest sevaRequest) {
        Complaint complaint = new ComplaintBuilder(sevaRequest.getServiceRequest(), complaintTypeService, complaintStatusService).build();
        complaintService.createComplaint(complaint);
    }

}
