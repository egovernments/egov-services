package org.egov.pgr.consumer;

import static org.apache.commons.lang.StringUtils.isNotEmpty;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.egov.pgr.model.RequestInfo;
import org.egov.pgr.model.ServiceRequest;
import org.egov.pgr.model.SevaRequest;
import org.egov.pgr.producer.GrievanceAssignmentProducer;
import org.egov.pgr.services.BoundaryService;
import org.egov.pgr.services.CrossHierarchyService;
import org.egov.pgr.transform.BoundaryResponse;
import org.egov.pgr.transform.CrossHierarchyResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;

public class GrievanceLocationEnrichmentListener {

	@Value("${kafka.topics.pgr.boundary_enriched.name}")
	private String locationEnrichedTopicName;

	@Autowired
	private GrievanceAssignmentProducer kafkaProducer;

	@Autowired
	private BoundaryService boundaryService;

	@Autowired
	private CrossHierarchyService crossHierarchyService;

	/*
	 * Example message
	 * {"RequestInfo":{"api_id":"1","ver":"1","ts":null,"action":"create","did":
	 * "","key":"","msg_id":"","requester_id":"","auth_token":
	 * "ed7fee08-f523-4b30-aa0f-d72b93ba722a"},"ServiceRequest":{
	 * "service_request_id":"00029-2017-TK","status":null,"status_details":null,
	 * "service_name":null,"service_code":null,"service_id":null,"description":
	 * null,"agency_responsible":null,"service_notice":null,"requested_datetime"
	 * :null,"updated_datetime":null,"expected_datetime":null,"address":null,
	 * "address_id":null,"zipcode":null,"lat":12.9188214,"lng":77.6699807,
	 * "media_url":null,"first_name":"999999999","last_name":"Jake","phone":null
	 * ,"email":null,"device_id":null,"account_id":null,"approval_position":null
	 * ,"approval_comment":null,"values":[]}}
	 */
	@KafkaListener(id = "${kafka.topics.pgr.validated.id}", topics = "${kafka.topics.pgr.validated.name}", group = "${kafka.topics.pgr.validated.group}")
	public void listen(ConsumerRecord<String, SevaRequest> record) {
		SevaRequest sevaRequest = record.value();
		populateLocation(sevaRequest);
		kafkaProducer.sendMessage(locationEnrichedTopicName, sevaRequest);
	}

	private void populateLocation(SevaRequest sevaRequest) {
		ServiceRequest serviceRequest = sevaRequest.getServiceRequest();
		RequestInfo requestInfo = sevaRequest.getRequestInfo();
		if (locationHasBeenProvided(serviceRequest)) {
			BoundaryResponse response = boundaryService.fetchBoundaryByLatLng(requestInfo, serviceRequest.getLat(),
					serviceRequest.getLng());
			sevaRequest.getServiceRequest().getValues().put("location_id", String.valueOf(response.getId()));
			sevaRequest.getServiceRequest().getValues().put("location_name", response.getName());
		}
		;
		if (crossHierarchyIdHasBeenProvided(serviceRequest)) {
			CrossHierarchyResponse chResponse = crossHierarchyService.fetchCrossHierarchyById(requestInfo,
					serviceRequest.getCrossHierarchyId());
			sevaRequest.getServiceRequest().getValues().put("location_id", chResponse.getParent().getId().toString());
			sevaRequest.getServiceRequest().getValues().put("location_name", chResponse.getParent().getName());
			sevaRequest.getServiceRequest().getValues().put("child_location_id",
					chResponse.getChild().getId().toString());
		}
	}

	private boolean crossHierarchyIdHasBeenProvided(ServiceRequest serviceRequest) {
		return isNotEmpty(serviceRequest.getCrossHierarchyId());
	}

	private boolean locationHasBeenProvided(ServiceRequest serviceRequest) {
		return (serviceRequest.getLat() != null && serviceRequest.getLat() > 0 && serviceRequest.getLng() != null
				&& serviceRequest.getLng() > 0);
	}

}
