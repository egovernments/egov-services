package org.egov.pgrrest.read.persistence.repository;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.egov.pgrrest.read.domain.model.ServiceRequestSearchCriteria;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class ServiceRequestESRepository {
    private static final String SERVICE_REQUEST_ID_FIELD_NAME = "crn";
    private static final String DEFAULT_SORT_FIELD = "lastModifiedDate";
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

    public List<String> getMatchingServiceRequestIds(ServiceRequestSearchCriteria criteria) {
        final SearchRequestBuilder searchRequestBuilder = getSearchRequest(criteria);
        final SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();
        return mapToServiceRequestIdList(searchResponse);
    }

    private List<String> mapToServiceRequestIdList(SearchResponse searchResponse) {
        if (searchResponse.getHits() == null || searchResponse.getHits().getTotalHits() == 0L) {
            return Collections.emptyList();
        }
        return Stream.of(searchResponse.getHits().getHits())
            .map(this::getFieldValue)
            .filter(StringUtils::isNotEmpty)
            .collect(Collectors.toList());
    }

    private String getFieldValue(SearchHit hit) {
        return (String) hit.getSource().get(SERVICE_REQUEST_ID_FIELD_NAME);
    }

    private SearchRequestBuilder getSearchRequest(ServiceRequestSearchCriteria criteria) {
        final BoolQueryBuilder boolQueryBuilder = queryFactory.create(criteria);
        final SearchSourceBuilder sourceBuilder = getSearchSourceBuilder(criteria);
        final SearchRequestBuilder searchRequestBuilder = esClient.prepareSearch(indexName)
            .setTypes(documentType)
            .setSource(sourceBuilder)
            .addSort(DEFAULT_SORT_FIELD, SortOrder.DESC)
            .setQuery(boolQueryBuilder);
        setResponseCount(criteria, searchRequestBuilder);
        return searchRequestBuilder;
    }

    private SearchSourceBuilder getSearchSourceBuilder(ServiceRequestSearchCriteria criteria) {
        final SearchSourceBuilder sourceBuilder = new SearchSourceBuilder()
            .fetchSource(true)
            .fetchSource(SERVICE_REQUEST_ID_FIELD_NAME, null);
        if (criteria.isPaginationCriteriaPresent()) {
            sourceBuilder.from(criteria.getFromIndex()).size(criteria.getPageSize());
        }
        return sourceBuilder;
    }

    private void setResponseCount(ServiceRequestSearchCriteria criteria, SearchRequestBuilder searchRequestBuilder) {
        if (criteria.isPaginationCriteriaPresent()) {
            searchRequestBuilder.setFrom(criteria.getFromIndex()).setSize(criteria.getPageSize());
        } else {
            searchRequestBuilder.setSize(Long.valueOf(getCount(criteria)).intValue());
        }
    }
}

