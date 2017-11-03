package org.egov.swm.domain.repository;

import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.Route;
import org.egov.swm.domain.model.RouteSearch;
import org.egov.swm.persistence.repository.RouteCollectionPointMapJdbcRepository;
import org.egov.swm.persistence.repository.RouteJdbcRepository;
import org.egov.swm.web.requests.RouteRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class RouteRepository {

	@Autowired
	private KafkaTemplate<String, Object> kafkaTemplate;

	@Autowired
	private RouteJdbcRepository routeJdbcRepository;

	@Autowired
	private RouteCollectionPointMapJdbcRepository routeCollectionPointMapJdbcRepository;

	@Value("${egov.swm.route.save.topic}")
	private String saveTopic;

	@Value("${egov.swm.route.update.topic}")
	private String updateTopic;

	@Value("${egov.swm.route.indexer.topic}")
	private String indexerTopic;

	public RouteRequest save(RouteRequest routeRequest) {

		kafkaTemplate.send(saveTopic, routeRequest);

		kafkaTemplate.send(indexerTopic, routeRequest.getRoutes());

		return routeRequest;

	}

	public RouteRequest update(RouteRequest routeRequest) {

		for (Route r : routeRequest.getRoutes()) {

			routeCollectionPointMapJdbcRepository.delete(r.getCode());
			
		}

		kafkaTemplate.send(updateTopic, routeRequest);

		kafkaTemplate.send(indexerTopic, routeRequest.getRoutes());

		return routeRequest;

	}

	public Pagination<Route> search(RouteSearch routeSearch) {
		return routeJdbcRepository.search(routeSearch);

	}

}