package org.egov.swm.domain.repository;

import org.egov.swm.domain.model.CollectionPoint;
import org.egov.swm.domain.model.CollectionPointSearch;
import org.egov.swm.domain.model.Pagination;
import org.egov.swm.persistence.repository.BinIdDetailsJdbcRepository;
import org.egov.swm.persistence.repository.CollectionPointJdbcRepository;
import org.egov.swm.web.requests.CollectionPointRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class CollectionPointRepository {

	@Autowired
	private KafkaTemplate<String, Object> kafkaTemplate;

	@Autowired
	private CollectionPointJdbcRepository collectionPointJdbcRepository;

	@Autowired
	private BinIdDetailsJdbcRepository binIdDetailsJdbcRepository;

	@Value("${egov.swm.collectionpoint.save.topic}")
	private String saveTopic;

	@Value("${egov.swm.collectionpoint.update.topic}")
	private String updateTopic;

	@Value("${egov.swm.biniddetails.create.topic}")
	private String saveBinIdDetailsTopic;

	@Value("${egov.swm.collectionpoint.indexer.topic}")
	private String indexerTopic;

	public CollectionPointRequest save(CollectionPointRequest collectionPointRequest) {

		kafkaTemplate.send(saveTopic, collectionPointRequest);

		for (CollectionPoint cp : collectionPointRequest.getCollectionPoints()) {

			kafkaTemplate.send(saveBinIdDetailsTopic, cp);
		}

		kafkaTemplate.send(indexerTopic, collectionPointRequest.getCollectionPoints());

		return collectionPointRequest;

	}

	public CollectionPointRequest update(CollectionPointRequest collectionPointRequest) {

		kafkaTemplate.send(updateTopic, collectionPointRequest);

		for (CollectionPoint cp : collectionPointRequest.getCollectionPoints()) {
			
			binIdDetailsJdbcRepository.delete(cp.getId());

			kafkaTemplate.send(saveBinIdDetailsTopic, cp);
		}

		kafkaTemplate.send(indexerTopic, collectionPointRequest.getCollectionPoints());

		return collectionPointRequest;

	}

	public Pagination<CollectionPoint> search(CollectionPointSearch collectionPointSearch) {
		return collectionPointJdbcRepository.search(collectionPointSearch);

	}

}