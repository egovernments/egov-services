package org.egov.pgr.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.egov.pgr.builder.ComplaintBuilder;
import org.egov.pgr.entity.Complaint;
import org.egov.pgr.model.ServiceRequest;
import org.egov.pgr.service.ComplaintService;
import org.egov.pgr.service.ComplaintStatusService;
import org.egov.pgr.service.ComplaintTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

public class GrievancePersistenceListener {

    @Autowired
    private ComplaintTypeService complaintTypeService;

    @Autowired
    private ComplaintStatusService complaintStatusService;

    @Autowired
    private ComplaintService complaintService;

    @KafkaListener(id = "grievancePersister", topics = "ap.public.mseva.validatedx", group = "grievances")
    public void listen(ConsumerRecord<String, ServiceRequest> record) {
        Complaint complaint = new ComplaintBuilder(record.value(), complaintTypeService, complaintStatusService).build();
        Complaint savedComplaint = complaintService.createComplaint(complaint);
        //TODO - Push the message to the workflow topic, indexing topic,
    }

}
