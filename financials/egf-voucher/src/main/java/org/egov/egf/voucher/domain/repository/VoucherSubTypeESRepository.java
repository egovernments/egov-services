package org.egov.egf.voucher.domain.repository;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.egov.common.domain.model.Pagination;
import org.egov.common.persistence.repository.ESRepository;
import org.egov.common.util.ElasticSearchUtils;
import org.egov.egf.voucher.domain.model.VoucherSubType;
import org.egov.egf.voucher.persistence.entity.VoucherEntity;
import org.egov.egf.voucher.web.contract.VoucherSubTypeSearchContract;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class VoucherSubTypeESRepository extends ESRepository {

    private TransportClient esClient;

    private ElasticSearchUtils elasticSearchUtils;

    public static final SimpleDateFormat sdf = new SimpleDateFormat(
            "yyyy-MM-dd");

    @Autowired
    public VoucherSubTypeESRepository(TransportClient esClient,
            ElasticSearchUtils elasticSearchUtils) {

        this.esClient = esClient;
        this.elasticSearchUtils = elasticSearchUtils;

    }

    public Pagination<VoucherSubType> search(
            VoucherSubTypeSearchContract voucherSubTypeSearchContract) {
        final SearchRequestBuilder searchRequestBuilder = getSearchRequest(voucherSubTypeSearchContract);
        final SearchResponse searchResponse = searchRequestBuilder.execute()
                .actionGet();
        return mapToVoucherSubTypeList(searchResponse);
    }

    @SuppressWarnings("deprecation")
    private Pagination<VoucherSubType> mapToVoucherSubTypeList(
            SearchResponse searchResponse) {

        Pagination<VoucherSubType> page = new Pagination<>();

        if (searchResponse.getHits() == null
                || searchResponse.getHits().getTotalHits() == 0L) {
            return page;
        }

        List<VoucherSubType> voucherSubTypes = new ArrayList<VoucherSubType>();
        VoucherSubType voucherSubType = null;

        for (SearchHit hit : searchResponse.getHits()) {

            ObjectMapper mapper = new ObjectMapper();
            // JSON from file to Object
            try {
                voucherSubType = mapper.readValue(hit.sourceAsString(),
                        VoucherSubType.class);
            } catch (JsonParseException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (JsonMappingException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

            voucherSubTypes.add(voucherSubType);
        }

        page.setTotalResults(Long.valueOf(
                searchResponse.getHits().getTotalHits()).intValue());
        page.setPagedData(voucherSubTypes);

        return page;
    }

    private SearchRequestBuilder getSearchRequest(
            VoucherSubTypeSearchContract criteria) {

        List<String> orderByList = new ArrayList<>();

        if (criteria.getSortBy() != null && !criteria.getSortBy().isEmpty()) {

            validateSortByOrder(criteria.getSortBy());
            validateEntityFieldName(criteria.getSortBy(), VoucherEntity.class);
            orderByList = prepareOrderBy(criteria.getSortBy());

        }

        final BoolQueryBuilder boolQueryBuilder = boolQueryBuilderForVoucherSubTypes(criteria);
        SearchRequestBuilder searchRequestBuilder = esClient.prepareSearch(VoucherSubType.class.getSimpleName().toLowerCase())
                .setTypes(VoucherSubType.class.getSimpleName().toLowerCase())
                .setSize(9999);// To retrieve all records from the elastic,
        // .setScroll(new TimeValue(6000000)), this will cache the records for the set no of milli seconds,
        // any changes to these records will not be reflected in the search during this period.
        // Please take decision on implementing the caching.

        if (!orderByList.isEmpty()) {
            for (String orderBy : orderByList) {
                searchRequestBuilder = searchRequestBuilder.addSort(orderBy
                        .split(" ")[0], orderBy.split(" ")[1]
                                .equalsIgnoreCase("asc") ? SortOrder.ASC
                                        : SortOrder.DESC);
            }
        }

        searchRequestBuilder.setQuery(boolQueryBuilder);

        return searchRequestBuilder;

    }

    public BoolQueryBuilder boolQueryBuilderForVoucherSubTypes(
            VoucherSubTypeSearchContract voucherSubTypeSearchContract) {

        BoolQueryBuilder boolQueryBuilder = boolQuery();

        if (voucherSubTypeSearchContract.getIds() != null
                && !voucherSubTypeSearchContract.getIds().isEmpty())
            elasticSearchUtils.add(voucherSubTypeSearchContract.getIds(), "id",
                    boolQueryBuilder);

        elasticSearchUtils.add(voucherSubTypeSearchContract.getId(), "id",
                boolQueryBuilder);

        elasticSearchUtils.add(voucherSubTypeSearchContract.getVoucherName(),
                "voucherName", boolQueryBuilder);
        
        elasticSearchUtils.add(voucherSubTypeSearchContract.getVoucherNamePrefix(),
                "voucherNamePrefix", boolQueryBuilder);

        if (voucherSubTypeSearchContract.getVoucherType() != null)
            elasticSearchUtils.add(voucherSubTypeSearchContract.getVoucherType().toString(),
                    "voucherType", boolQueryBuilder);

        elasticSearchUtils.add(voucherSubTypeSearchContract.getExclude(),
                "exclude", boolQueryBuilder);

        if (voucherSubTypeSearchContract.getCutOffDate() != null)
            elasticSearchUtils.add(
                    sdf.format(voucherSubTypeSearchContract.getCutOffDate()),
                    "cutOffDate", boolQueryBuilder);

        return boolQueryBuilder;

    }

    public List<String> prepareOrderBy(String sortBy) {

        List<String> orderByList = new ArrayList<String>();
        List<String> sortByList = new ArrayList<String>();

        if (sortBy.contains(",")) {
            sortByList = Arrays.asList(sortBy.split(","));
        } else {
            sortByList = Arrays.asList(sortBy);
        }

        for (String s : sortByList) {

            if (s.contains(" ")
                    && (s.toLowerCase().trim().endsWith("asc") || s
                            .toLowerCase().trim().endsWith("desc"))) {
                orderByList.add(s.trim());
            } else {

                orderByList.add(s.trim() + " asc");
            }

        }

        return orderByList;
    }
}
