package org.egov.swm.persistence.queue.repository;

import org.egov.swm.domain.model.CollectionPoint;
import org.egov.swm.persistence.repository.BinDetailsJdbcRepository;
import org.egov.swm.persistence.repository.CollectionPointDetailsJdbcRepository;
import org.egov.swm.web.requests.CollectionPointRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class CollectionPointQueueRepository {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private CollectionPointDetailsJdbcRepository collectionDetailsPointJdbcRepository;

    @Autowired
    private BinDetailsJdbcRepository binDetailsJdbcRepository;

    @Value("${egov.swm.collectionpoint.save.topic}")
    private String saveTopic;

    @Value("${egov.swm.collectionpoint.update.topic}")
    private String updateTopic;

    @Value("${egov.swm.collectionpoint.indexer.topic}")
    private String indexerTopic;

    public CollectionPointRequest save(final CollectionPointRequest collectionPointRequest) {

        kafkaTemplate.send(saveTopic, collectionPointRequest);

        kafkaTemplate.send(indexerTopic, collectionPointRequest);

        return collectionPointRequest;

    }

    public CollectionPointRequest update(final CollectionPointRequest collectionPointRequest) {

        for (final CollectionPoint cp : collectionPointRequest.getCollectionPoints()) {

            binDetailsJdbcRepository.delete(cp.getTenantId(), cp.getCode());

            collectionDetailsPointJdbcRepository.delete(cp.getTenantId(), cp.getCode());

        }

        kafkaTemplate.send(updateTopic, collectionPointRequest);

        kafkaTemplate.send(indexerTopic, collectionPointRequest);

        return collectionPointRequest;

    }

}