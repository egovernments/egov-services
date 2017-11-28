package org.egov.swm.persistence.queue.repository;

import org.egov.swm.domain.model.Route;
import org.egov.swm.persistence.repository.RouteCollectionPointMapJdbcRepository;
import org.egov.swm.web.requests.RouteRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class RouteQueueRepository {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private RouteCollectionPointMapJdbcRepository routeCollectionPointMapJdbcRepository;

    @Value("${egov.swm.route.save.topic}")
    private String saveTopic;

    @Value("${egov.swm.route.update.topic}")
    private String updateTopic;

    @Value("${egov.swm.route.indexer.topic}")
    private String indexerTopic;

    public RouteRequest save(final RouteRequest routeRequest) {

        kafkaTemplate.send(saveTopic, routeRequest);

        kafkaTemplate.send(indexerTopic, routeRequest);

        return routeRequest;

    }

    public RouteRequest update(final RouteRequest routeRequest) {

        for (final Route r : routeRequest.getRoutes())
            routeCollectionPointMapJdbcRepository.delete(r.getTenantId(), r.getCode());

        kafkaTemplate.send(updateTopic, routeRequest);

        kafkaTemplate.send(indexerTopic, routeRequest);

        return routeRequest;

    }

}