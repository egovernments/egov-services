package org.egov.swm.persistence.queue.repository;

import org.egov.swm.domain.model.SourceSegregation;
import org.egov.swm.persistence.repository.CollectionDetailsJdbcRepository;
import org.egov.swm.web.requests.SourceSegregationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class SourceSegregationQueueRepository {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private CollectionDetailsJdbcRepository collectionDetailsJdbcRepository;

    @Value("${egov.swm.sourcesegregation.save.topic}")
    private String saveTopic;

    @Value("${egov.swm.sourcesegregation.update.topic}")
    private String updateTopic;

    @Value("${egov.swm.sourcesegregation.indexer.topic}")
    private String indexerTopic;

    public SourceSegregationRequest save(final SourceSegregationRequest sourceSegregationRequest) {

        kafkaTemplate.send(saveTopic, sourceSegregationRequest);

        kafkaTemplate.send(indexerTopic, sourceSegregationRequest);

        return sourceSegregationRequest;

    }

    public SourceSegregationRequest update(final SourceSegregationRequest sourceSegregationRequest) {

        for (final SourceSegregation ss : sourceSegregationRequest.getSourceSegregations())
            collectionDetailsJdbcRepository.delete(ss.getTenantId(), ss.getCode());

        kafkaTemplate.send(updateTopic, sourceSegregationRequest);

        kafkaTemplate.send(indexerTopic, sourceSegregationRequest);

        return sourceSegregationRequest;

    }

}