package org.egov.swm.domain.repository;

import org.egov.swm.domain.model.CollectionPointDetails;
import org.egov.swm.domain.model.CollectionPointDetailsSearch;
import org.egov.swm.domain.model.Pagination;
import org.egov.swm.persistence.repository.BinIdDetailsJdbcRepository;
import org.egov.swm.persistence.repository.CollectionPointDetailsJdbcRepository;
import org.egov.swm.web.requests.CollectionPointDetailsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class CollectionPointDetailsRepository {

	@Autowired
	private KafkaTemplate<String, Object> kafkaTemplate;

	@Autowired
	private CollectionPointDetailsJdbcRepository collectionPointDetailsJdbcRepository;

	@Value("${egov.swm.collectionpointdetails.save.topic}")
	private String saveTopic;

	@Value("${egov.swm.collectionpointdetails.update.topic}")
	private String updateTopic;

	@Value("${egov.swm.collectionpointdetails.indexer.topic}")
	private String indexerTopic;

	public CollectionPointDetailsRequest save(CollectionPointDetailsRequest collectionPointDetailsRequest) {

		kafkaTemplate.send(saveTopic, collectionPointDetailsRequest);

		kafkaTemplate.send(indexerTopic, collectionPointDetailsRequest.getCollectionPointDetails());

		return collectionPointDetailsRequest;

	}

	public CollectionPointDetailsRequest update(CollectionPointDetailsRequest collectionPointDetailsRequest) {

		kafkaTemplate.send(updateTopic, collectionPointDetailsRequest);

		kafkaTemplate.send(indexerTopic, collectionPointDetailsRequest.getCollectionPointDetails());

		return collectionPointDetailsRequest;

	}

	public Pagination<CollectionPointDetails> search(CollectionPointDetailsSearch collectionPointDetailsSearch) {
		return collectionPointDetailsJdbcRepository.search(collectionPointDetailsSearch);

	}

}