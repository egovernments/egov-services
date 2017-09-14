package org.egov.pgrrest.read.persistence.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.pgrrest.read.domain.exception.ReadException;
import org.egov.pgrrest.read.domain.model.ServiceRequest;
import org.egov.pgrrest.read.domain.model.ServiceRequestESResponse;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ServiceRequestESRepository {
    private static final String DEFAULT_SORT_FIELD = "lastModifiedDate";
    private static final String EMPTY_STRING = "";
    private static final String NEW_LINE = "\n";
    private static final String TRUE = "true";
    private TransportClient esClient;
    private String indexName;
    private String documentType;
    private QueryFactory queryFactory;
    private boolean isESRequestLoggingEnabled;
    private ObjectMapper objectMapper;
    private String timeZone;

    public ServiceRequestESRepository(TransportClient esClient,
                                      @Value("${es.index.name}") String indexName,
                                      @Value("${es.document.type}") String documentType,
                                      QueryFactory queryFactory,
                                      ObjectMapper objectMapper,
                                      @Value("${es.log.request}") String isESRequestLoggingEnabled,
                                      @Value("${app.timezone}") String timeZone) {
        this.esClient = esClient;
        this.indexName = indexName;
        this.documentType = documentType;
        this.queryFactory = queryFactory;
        this.objectMapper = objectMapper;
        this.timeZone = timeZone;
        this.isESRequestLoggingEnabled = TRUE.equalsIgnoreCase(isESRequestLoggingEnabled);
    }

    public long getCount(ServiceRequestSearchCriteria serviceRequestSearchCriteria) {
        final BoolQueryBuilder boolQueryBuilder = queryFactory.create(serviceRequestSearchCriteria);
        final SearchRequestBuilder searchRequestBuilder = esClient.prepareSearch(indexName)
            .setTypes(documentType)
            .setSize(0)
            .setQuery(boolQueryBuilder);
        logRequest(searchRequestBuilder);
        final SearchResponse searchResponse = searchRequestBuilder
            .execute()
            .actionGet();
        logResponse(searchResponse);
        return searchResponse.getHits().getTotalHits();
    }

    public List<ServiceRequest> getMatchingServiceRequests(ServiceRequestSearchCriteria criteria) {
        final SearchRequestBuilder searchRequestBuilder = getSearchRequest(criteria);
        logRequest(searchRequestBuilder);
        final SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();
        logResponse(searchResponse);
        return mapToServiceRequest(searchResponse);
    }

    private void logRequest(SearchRequestBuilder searchRequestBuilder) {
        if (isESRequestLoggingEnabled) {
            log.info(removeNewLines(searchRequestBuilder.toString()));
        }
    }

    private void logResponse(SearchResponse searchResponse) {
        if (isESRequestLoggingEnabled) {
            log.info(removeNewLines(searchResponse.toString()));
        }
    }

    private String removeNewLines(String string) {
        if (string == null) {
            return EMPTY_STRING;
        }
        return string.replaceAll(NEW_LINE, EMPTY_STRING);
    }

    private List<ServiceRequest> mapToServiceRequest(SearchResponse searchResponse) {
        if (searchResponse.getHits() == null || searchResponse.getHits().getTotalHits() == 0L) {
            return Collections.emptyList();
        }
        List<ServiceRequestESResponse> esConvertedObject = getEsConvertedObject(searchResponse);
        return esConvertedObject.stream()
            .map(serviceRequestESResponse -> serviceRequestESResponse.toModel(timeZone))
            .collect(Collectors.toList());
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
            .fetchSource(true);
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

    private List<ServiceRequestESResponse> getEsConvertedObject(SearchResponse searchResponse) {
        List<ServiceRequestESResponse> responseList = new ArrayList<>();
        SearchHit[] hits = searchResponse.getHits().getHits();

        Arrays.asList(hits).forEach(hit -> {
            String json = hit.getSourceAsString();
            try {
                ServiceRequestESResponse record = objectMapper.readValue(json, ServiceRequestESResponse.class);
                responseList.add(record);
            } catch (IOException e) {
                throw new ReadException(e);
            }
        });

        return responseList;

    }

}

