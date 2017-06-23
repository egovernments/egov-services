package org.egov.pgr.location.consumer;

import org.egov.pgr.location.model.SevaRequest;
import org.egov.pgr.location.repository.ComplaintRepository;
import org.egov.pgr.location.services.LocationService;
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

    @KafkaListener(id = "${kafka.topics.pgr.validated.id}",
            topics = "${kafka.topics.pgr.validated.name}",
            group = "${kafka.topics.pgr.validated.group}")
    public void process(HashMap<String, Object> sevaRequestMap) {
        final SevaRequest sevaRequest = new SevaRequest(sevaRequestMap);
        final SevaRequest enrichedSevaRequest = locationService.enrich(sevaRequest);
        complaintRepository.save(enrichedSevaRequest);
    }

}
