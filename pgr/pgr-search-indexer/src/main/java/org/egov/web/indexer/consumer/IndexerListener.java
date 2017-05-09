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
    public IndexerListener(ElasticSearchRepository elasticSearchRepository, ComplaintAdapter complaintAdapter,
            ObjectMapper objectMapper) {
        this.elasticSearchRepository = elasticSearchRepository;
        this.complaintAdapter = complaintAdapter;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "${kafka.topics.egov.index.name}")
    public void listen(HashMap<String, Object> sevaRequestMap) {
        SevaRequest sevaRequest = objectMapper.convertValue(sevaRequestMap, SevaRequest.class);
        if (sevaRequest.getServiceRequest() != null) {
            if (sevaRequest.getServiceRequest().isNewServiceRequest()) {
                createIndexDocument(sevaRequest);
            } else {
                updateIndexDocument(sevaRequest);
            }
        }

    }

    private void updateIndexDocument(SevaRequest sevaRequest) {
        ComplaintIndex complaintIndex = complaintAdapter.indexOnUpdate(sevaRequest.getServiceRequest());
        elasticSearchRepository.index(OBJECT_TYPE_COMPLAINT, complaintIndex.getCrn(), complaintIndex);
    }

    private void createIndexDocument(SevaRequest sevaRequest) {
        ComplaintIndex complaintIndex = complaintAdapter.indexOnCreate(sevaRequest.getServiceRequest());
        elasticSearchRepository.index(OBJECT_TYPE_COMPLAINT, complaintIndex.getCrn(), complaintIndex);
    }
}
