package org.egov.pgr.employee.enrichment.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.egov.pgr.employee.enrichment.producer.ComplaintPersister;
import org.egov.pgr.employee.enrichment.service.WorkflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.HashMap;
import java.util.Map;

public class ComplaintAssignment {

    @Value("${kafka.topics.pgr.employee_enriched.name}")
    private String employeeEnrichedTopicName;

    @Autowired
    private ComplaintPersister complaintPersister;

    @Autowired
    private WorkflowService workflowService;

    @Autowired
    public ComplaintAssignment(ComplaintPersister complaintPersister, WorkflowService workflowService) {
        this.workflowService = workflowService;
        this.complaintPersister = complaintPersister;
    }

    public ComplaintAssignment() {
    }

    /*
     * Example message
     * {"RequestInfo":{"api_id":"1","ver":"1","ts":null,"action":"update","did":"","key":"","msg_id":"","requester_id":"",
     * "auth_token":null},"ServiceRequest":{"service_request_id":"03329-2018-UX","status":null,"status_details":null,
     * "service_name":null,"service_code":"PHDMG","service_id":null,"description":"There is a huge problem"
     * ,"agency_responsible":null,"service_notice":null,"requested_datetime":null,"updated_datetime":null,"expected_datetime":
     * "20-02-2017 20:20:20"
     * ,"address":null,"address_id":null,"zipcode":null,"lat":12.9188214,"lng":77.6699807,"media_url":null,"first_name":"Bake",
     * "last_name":"Jake","phone":"9999999990","email":"jake@maildrop.cc","device_id":null,"account_id":null,"approval_position":
     * null,"approval_comment":null,"values":{"location_id":"1","child_location_id":"60","assignee_id":"2","location_name":
     * "testing"}}}
     */
    @KafkaListener(id = "${kafka.topics.pgr.locationpopulated.id}", topics = "${kafka.topics.pgr.locationpopulated.name}", group = "${kafka.topics.pgr.locationpopulated.group}")
    public void processMessage(ConsumerRecord record) {
        HashMap sevaRequestHash = (HashMap) record.value();
        Map enrichedSevaRequest = workflowService.enrichWorkflowDetails(sevaRequestHash);
        complaintPersister.sendMessage(employeeEnrichedTopicName, enrichedSevaRequest);
    }

}
