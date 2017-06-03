package org.egov.pgrrest.read.persistence.repository;

import org.egov.pgrrest.read.domain.model.ServiceRequestSearchCriteria;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ServiceRequestESRepository {
    private TransportClient esClient;
    private String indexName;
    private String documentType;
    private QueryFactory queryFactory;

    public ServiceRequestESRepository(TransportClient esClient,
                                      @Value("${es.index.name}") String indexName,
                                      @Value("${es.document.type}") String documentType,
                                      QueryFactory queryFactory) {
        this.esClient = esClient;
        this.indexName = indexName;
        this.documentType = documentType;
        this.queryFactory = queryFactory;
    }

    public long getCount(ServiceRequestSearchCriteria serviceRequestSearchCriteria) {
        final BoolQueryBuilder boolQueryBuilder = queryFactory.create(serviceRequestSearchCriteria);
        final SearchResponse searchResponse = esClient.prepareSearch(indexName)
            .setTypes(documentType)
            .setSize(0)
            .setQuery(boolQueryBuilder)
            .execute()
            .actionGet();
        return searchResponse.getHits().getTotalHits();
    }
}

