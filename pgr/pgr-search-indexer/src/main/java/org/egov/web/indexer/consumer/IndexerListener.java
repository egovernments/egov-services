package org.egov.web.indexer.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.web.indexer.adaptor.ComplaintAdapter;
import org.egov.web.indexer.contract.SevaRequest;
import org.egov.web.indexer.repository.ElasticSearchRepository;
import org.egov.web.indexer.repository.contract.ComplaintIndex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class IndexerListener {

    private static final String OBJECT_TYPE_COMPLAINT = "complaint";

    private ElasticSearchRepository elasticSearchRepository;
    private ComplaintAdapter complaintAdapter;
    private ObjectMapper objectMapper;

    @Autowired
    public IndexerListener(ElasticSearchRepository elasticSearchRepository,
                           ComplaintAdapter complaintAdapter,
                           ObjectMapper objectMapper) {
        this.elasticSearchRepository = elasticSearchRepository;
        this.complaintAdapter = complaintAdapter;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(id = "${kafka.topics.egov.index.id}",
        topics = "${kafka.topics.egov.index.name}",
        group = "${kafka.topics.egov.index.group}")
    public void listen(HashMap<String, Object> sevaRequestMap) {
        SevaRequest sevaRequest = objectMapper.convertValue(sevaRequestMap, SevaRequest.class);
        if (sevaRequest.getServiceRequest() != null && !sevaRequest.getServiceRequest().getValues().isEmpty()
            && sevaRequest.getServiceRequest().getValues().get("status").equalsIgnoreCase("REGISTERED")) {
            ComplaintIndex complaintIndex = complaintAdapter.indexOnCreate(sevaRequest.getServiceRequest());
            elasticSearchRepository.index(OBJECT_TYPE_COMPLAINT, complaintIndex.getCrn(), complaintIndex);
        }

    }
}
