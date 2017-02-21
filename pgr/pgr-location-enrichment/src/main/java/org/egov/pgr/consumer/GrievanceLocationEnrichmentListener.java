package org.egov.pgr.consumer;

import org.egov.pgr.model.RequestContext;
import org.egov.pgr.model.SevaRequest;
import org.egov.pgr.repository.ComplaintRepository;
import org.egov.pgr.services.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;


@Service
public class GrievanceLocationEnrichmentListener {

    private LocationService locationService;
    private ComplaintRepository complaintRepository;

    @Autowired
    public GrievanceLocationEnrichmentListener(LocationService locationService,
                                               ComplaintRepository complaintRepository) {
        this.locationService = locationService;
        this.complaintRepository = complaintRepository;
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
    @KafkaListener(id = "${kafka.topics.pgr.validated.id}",
            topics = "${kafka.topics.pgr.validated.name}",
            group = "${kafka.topics.pgr.validated.group}")
    public void process(HashMap<String, Object> sevaRequestMap) {
        final SevaRequest sevaRequest = new SevaRequest(sevaRequestMap);
        RequestContext.setId(sevaRequest.getCorrelationId());
        final SevaRequest enrichedSevaRequest = locationService.enrich(sevaRequest);
        complaintRepository.save(enrichedSevaRequest);
    }

}
