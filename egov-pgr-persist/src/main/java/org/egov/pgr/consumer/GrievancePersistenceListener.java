package org.egov.pgr.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.egov.pgr.entity.Complaint;
import org.egov.pgr.model.EmailMessage;
import org.egov.pgr.model.SevaRequest;
import org.egov.pgr.model.SmsMessage;
import org.egov.pgr.producer.GrievanceProducer;
import org.egov.pgr.service.ComplaintService;
import org.egov.pgr.service.ComplaintStatusService;
import org.egov.pgr.service.ComplaintTypeService;
import org.egov.pgr.service.EscalationService;
import org.egov.pgr.transform.ComplaintBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

import java.text.SimpleDateFormat;

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

    @KafkaListener(id = "grievancePersister", topics = "ap.public.mseva.persistreadyf", group = "grievances")
    public void processMessage(ConsumerRecord<String, SevaRequest> record) {
        SevaRequest sevaRequest = record.value();
        Complaint complaint = persistComplaint(sevaRequest);
        triggerEmail(complaint);
        triggerSms(complaint);
        triggerIndexing(sevaRequest);
        triggerWorkflow(sevaRequest);
    }

    private void triggerWorkflow(SevaRequest record) {

    }

    private void triggerIndexing(SevaRequest record) {
        kafkaProducer.sendMessage(".mseva.validated", record);
    }

    private void triggerEmail(Complaint complaint) {
        EmailMessage emailMessage = new EmailMessage(complaint.getComplainant().getEmail(), getEmailSubject(complaint), getEmailBody(complaint), null);
        kafkaProducer.sendMessage(".mseva.validated", emailMessage);
    }

    private String getEmailSubject(Complaint complaint) {
        //TODO - Get boundary name by id
        String locationName = "";
        final String formattedCreatedDate = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(complaint.getCreatedDate());
        final StringBuffer emailBody = new StringBuffer().append("Dear ")
                .append(complaint.getComplainant().getName())
                .append(",\n \n \tThank you for registering a grievance (")
                .append(complaint.getCrn())
                .append("). Your grievance is registered successfully.\n \tPlease use this number for all future references.")
                .append("\n \n Grievance Details - \n \n Complaint type - ")
                .append(complaint.getComplaintType().getName());
        if (complaint.getLocation() != null)
            emailBody.append(" \n Location details - ").append(locationName);
        emailBody.append("\n Grievance description - ")
                .append(complaint.getDetails())
                .append("\n Grievance status -")
                .append(complaint.getStatus().getName())
                .append("\n Grievance Registration Date - ")
                .append(formattedCreatedDate);
        return emailBody.toString();
    }

    private String getEmailBody(Complaint complaint) {
        return new StringBuffer()
                .append("Registered Grievance -")
                .append(complaint.getCrn())
                .append(" successfuly").toString();
    }

    private void triggerSms(Complaint complaint) {
        SmsMessage smsMessage = new SmsMessage(complaint.getComplainant().getEmail(), getSmsBody(complaint));
        kafkaProducer.sendMessage(".mseva.validated", smsMessage);
    }

    private Complaint persistComplaint(SevaRequest sevaRequest) {
        Complaint complaint = new ComplaintBuilder(sevaRequest.getServiceRequest(), complaintTypeService, complaintStatusService, escalationService).build();
        complaintService.createComplaint(complaint);
        return complaint;
    }

    private String getSmsBody(Complaint complaint) {
        return new StringBuffer().append("Your grievance for ")
                .append(complaint.getComplaintType().getName())
                .append(" has been registered successfully with tracking number (").append(complaint.getCrn())
                .append("). Please use this number for all future references.").toString();
    }


}
