package org.egov.pgr.consumer;

import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.egov.pgr.config.PropertiesManager;
import org.egov.pgr.model.RequestInfo;
import org.egov.pgr.model.ServiceRequest;
import org.egov.pgr.model.SevaRequest;
import org.egov.pgr.producer.GrievanceAssignmentProducer;
import org.egov.pgr.services.BoundaryService;
import org.egov.pgr.services.CrossHierarchyService;
import org.egov.pgr.contract.BoundaryResponse;
import org.egov.pgr.contract.CrossHierarchyResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.HashMap;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.egov.pgr.model.ServiceRequest.*;


public class GrievanceLocationEnrichmentListener {

    private PropertiesManager propertiesManager;
    private GrievanceAssignmentProducer kafkaProducer;
    private BoundaryService boundaryService;
    private CrossHierarchyService crossHierarchyService;

    @Autowired
    public GrievanceLocationEnrichmentListener(BoundaryService boundaryService, CrossHierarchyService crossHierarchyService, GrievanceAssignmentProducer producer, PropertiesManager propertiesManager) {
        this.boundaryService = boundaryService;
        this.crossHierarchyService = crossHierarchyService;
        this.propertiesManager = propertiesManager;
        kafkaProducer = producer;
    }

    /*
     * Example message {"RequestInfo":{"api_id":"1","ver":"1","ts":null,"action":"create","did":
     * "","key":"","msg_id":"","requester_id":"","auth_token": "ed7fee08-f523-4b30-aa0f-d72b93ba722a"},"ServiceRequest":{
     * "service_request_id":"00029-2017-TK","status":null,"status_details":null,
     * "service_name":null,"service_code":null,"service_id":null,"description":
     * null,"agency_responsible":null,"service_notice":null,"requested_datetime"
     * :null,"updated_datetime":null,"expected_datetime":null,"address":null,
     * "address_id":null,"zipcode":null,"lat":12.9188214,"lng":77.6699807,
     * "media_url":null,"first_name":"999999999","last_name":"Jake","phone":null
     * ,"email":null,"device_id":null,"account_id":null,"approval_position":null ,"approval_comment":null,"values":[]}}
     */
    @KafkaListener(id = "${kafka.topics.pgr.validated.id}", topics = "${kafka.topics.pgr.validated.name}", group = "${kafka.topics.pgr.validated.group}")
    public void listen(ConsumerRecord<String, SevaRequest> record) {
        SevaRequest sevaRequest = record.value();
        if (locationIdIsNotProvided(sevaRequest)) populateLocation(sevaRequest);
        kafkaProducer.sendMessage(propertiesManager.getLocationEnrichedTopicName(), sevaRequest);
    }

    private void populateLocation(SevaRequest sevaRequest) {
        ServiceRequest serviceRequest = sevaRequest.getServiceRequest();
        RequestInfo requestInfo = sevaRequest.getRequestInfo();
        if (locationHasBeenProvided(serviceRequest)) {
            BoundaryResponse response = boundaryService.fetchBoundaryByLatLng(requestInfo, serviceRequest.getLat(),
                    serviceRequest.getLng());
            if (sevaRequest.getServiceRequest().getValues() == null) {
                sevaRequest.getServiceRequest().setValues(new HashMap<>());
            }

            sevaRequest.getServiceRequest().getValues().put(LOCATION_ID, String.valueOf(response.getId()));
            sevaRequest.getServiceRequest().getValues().put(LOCATION_NAME, response.getName());
        }
        if (crossHierarchyIdHasBeenProvided(serviceRequest)) {
            CrossHierarchyResponse chResponse = crossHierarchyService.fetchCrossHierarchyById(requestInfo,
                    serviceRequest.getCrossHierarchyId());
            if (sevaRequest.getServiceRequest().getValues() == null) {
                sevaRequest.getServiceRequest().setValues(new HashMap<>());
            }
            sevaRequest.getServiceRequest().getValues().put(LOCATION_ID, chResponse.getParent().getId().toString());
            sevaRequest.getServiceRequest().getValues().put(LOCATION_NAME, chResponse.getParent().getName());
            sevaRequest.getServiceRequest().getValues().put(CHILD_LOCATION_ID, chResponse.getChild().getId().toString());
        }
    }

    private boolean locationIdIsNotProvided(SevaRequest sevaRequest) {
        return sevaRequest.getServiceRequest().getValues() == null ||
                sevaRequest.getServiceRequest().getValues().get(LOCATION_ID) == null ||
                StringUtils.isEmpty(sevaRequest.getServiceRequest().getValues().get(LOCATION_ID));
    }

    private boolean crossHierarchyIdHasBeenProvided(ServiceRequest serviceRequest) {
        return isNotEmpty(serviceRequest.getCrossHierarchyId());
    }

    private boolean locationHasBeenProvided(ServiceRequest serviceRequest) {
        return serviceRequest.getLat() != null &&
                serviceRequest.getLat() > 0 &&
                serviceRequest.getLng() != null &&
                serviceRequest.getLng() > 0;
    }

}
