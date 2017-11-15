//package org.egov.inv.persistence.repository;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import io.swagger.model.MaterialStoreMapping;
//import io.swagger.model.MaterialStoreMappingSearchRequest;
//import io.swagger.model.Pagination;
//import lombok.extern.slf4j.Slf4j;
//import org.egov.inv.domain.service.ElasticSearchUtils;
//import org.egov.inv.persistence.entity.MaterialStoreMappingEntity;
//import org.egov.inv.persistence.repository.ESRepository;
//import org.elasticsearch.action.search.SearchRequestBuilder;
//import org.elasticsearch.action.search.SearchResponse;
//import org.elasticsearch.client.transport.TransportClient;
//import org.elasticsearch.index.query.BoolQueryBuilder;
//import org.elasticsearch.search.SearchHit;
//import org.elasticsearch.search.sort.SortOrder;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//import static org.elasticsearch.common.Strings.isEmpty;
//import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
//
//@Service
//@Slf4j
//public class MaterialStoreESRepository extends ESRepository {
//
//    private static final String TRUE = "true";
//
//    private static final String EMPTY_STRING = "";
//    private static final String NEW_LINE = "\n";
//
//    private ElasticSearchUtils elasticSearchUtils;
//
//    private TransportClient esClient;
//
//    private boolean isESRequestLoggingEnabled;
//
//    private ObjectMapper mapper;
//
//    public MaterialStoreESRepository(TransportClient esClient, ElasticSearchUtils elasticSearchUtils,
//                                     @Value("${es.log.request}") String isESRequestLoggingEnabled,
//                                     ObjectMapper mapper) {
//        this.esClient = esClient;
//        this.elasticSearchUtils = elasticSearchUtils;
//        this.isESRequestLoggingEnabled = TRUE.equalsIgnoreCase(isESRequestLoggingEnabled);
//        this.mapper = mapper;
//    }
//
//    public Pagination<MaterialStoreMapping> search(MaterialStoreMappingSearchRequest materialStoreMappingSearchRequest) {
//        final SearchRequestBuilder searchRequestBuilder = getSearchRequest(materialStoreMappingSearchRequest);
//        final SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();
//        logResponse(searchResponse);
//
//        return mapToMaterialStore(searchResponse);
//    }
//
//    private Pagination<MaterialStoreMapping> mapToMaterialStore(SearchResponse searchResponse) {
//
//        Pagination<MaterialStoreMapping> page = new Pagination<>();
//
//        if (null == searchResponse.getHits() || 0L == searchResponse.getHits().getTotalHits()) {
//            return page;
//        }
//
//        List<MaterialStoreMapping> responseList = new ArrayList<>();
//        SearchHit[] hits = searchResponse.getHits().getHits();
//
//        Arrays.asList(hits).forEach(hit -> {
//            String json = hit.getSourceAsString();
//            try {
//                MaterialStoreMapping record = mapper.readValue(json, MaterialStoreMapping.class);
//                responseList.add(record);
//            } catch (IOException e) {
//                throw new RuntimeException("material store mapping failed");
//            }
//        });
//
//
//        page.setTotalResults(Long.valueOf(searchResponse.getHits().getTotalHits()).intValue());
//        page.setPagedData(responseList);
//
//        return page;
//    }
//
//    private SearchRequestBuilder getSearchRequest(MaterialStoreMappingSearchRequest criteria) {
//
//        List<String> orderByList = new ArrayList<>();
//
//        if (criteria.getSortBy() != null && !criteria.getSortBy().isEmpty()) {
//
//            validateSortByOrder(criteria.getSortBy());
//            validateEntityFieldName(criteria.getSortBy(), MaterialStoreMappingEntity.class);
//            orderByList = prepareOrderBy(criteria.getSortBy());
//
//        }
//
//        final BoolQueryBuilder boolQueryBuilder = boolQueryBuilderForMaterialStore(criteria);
//        SearchRequestBuilder searchRequestBuilder = esClient.prepareSearch("materialstoremappings")
//                .setQuery(boolQueryBuilder);
//        logRequest(searchRequestBuilder);
//
//        if (!orderByList.isEmpty()) {
//            for (String orderBy : orderByList) {
//                searchRequestBuilder = searchRequestBuilder.addSort(orderBy.split(" ")[0],
//                        orderBy.split(" ")[1].equalsIgnoreCase("asc") ? SortOrder.ASC : SortOrder.DESC);
//            }
//        }
//
//        searchRequestBuilder.setQuery(boolQueryBuilder);
//
//        return searchRequestBuilder;
//
//    }
//
//    public BoolQueryBuilder boolQueryBuilderForMaterialStore(MaterialStoreMappingSearchRequest materialStoreMappingSearchRequest) {
//
//        BoolQueryBuilder boolQueryBuilder = boolQuery();
//
//        if (materialStoreMappingSearchRequest.getIds() != null && !materialStoreMappingSearchRequest.getIds().isEmpty())
//            elasticSearchUtils.in(Arrays.asList(materialStoreMappingSearchRequest.getIds()), "id", boolQueryBuilder);
//
//        if (null != materialStoreMappingSearchRequest.getActive())
//            elasticSearchUtils.add(materialStoreMappingSearchRequest.getActive(), "active", boolQueryBuilder);
//
//        if (!isEmpty(materialStoreMappingSearchRequest.getMaterial()))
//            elasticSearchUtils.add(materialStoreMappingSearchRequest.getMaterial(), "material.code", boolQueryBuilder);
//
//        if (!isEmpty(materialStoreMappingSearchRequest.getStore()))
//            elasticSearchUtils.add(materialStoreMappingSearchRequest.getStore(), "store.code", boolQueryBuilder);
//
//        if (!isEmpty(materialStoreMappingSearchRequest.getTenantId()))
//            elasticSearchUtils.add(materialStoreMappingSearchRequest.getTenantId(), "auditDetails.tenantId", boolQueryBuilder);
//
//        return boolQueryBuilder;
//
//    }
//
//    private void logRequest(SearchRequestBuilder searchRequestBuilder) {
//        if (isESRequestLoggingEnabled) {
//            log.info(removeNewLines(searchRequestBuilder.toString()));
//        }
//    }
//
//    private void logResponse(SearchResponse searchResponse) {
//        if (isESRequestLoggingEnabled) {
//            log.info(removeNewLines(searchResponse.toString()));
//        }
//    }
//
//    private String removeNewLines(String string) {
//        if (string == null) {
//            return EMPTY_STRING;
//        }
//        return string.replaceAll(NEW_LINE, EMPTY_STRING);
//    }
//
//
//    public List<String> prepareOrderBy(String sortBy) {
//
//        List<String> orderByList = new ArrayList<>();
//        List<String> sortByList;
//
//        if (sortBy.contains(",")) {
//            sortByList = Arrays.asList(sortBy.split(","));
//        } else {
//            sortByList = Arrays.asList(sortBy);
//        }
//
//        for (String s : sortByList) {
//
//            if (s.contains(" ")
//                    && (s.toLowerCase().trim().endsWith("asc") || s.toLowerCase().trim().endsWith("desc"))) {
//                orderByList.add(s.trim());
//            } else {
//
//                orderByList.add(s.trim() + " asc");
//            }
//
//        }
//
//        return orderByList;
//    }
//
//}
