package org.egov.egf.master.domain.repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.egov.common.domain.model.Pagination;
import org.egov.egf.master.domain.model.AccountDetailKey;
import org.egov.egf.master.web.contract.AccountDetailKeySearchContract;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AccountDetailKeyESRepository {

    private TransportClient esClient;
    private ElasticSearchQueryFactory elasticSearchQueryFactory;

    public AccountDetailKeyESRepository(TransportClient esClient, ElasticSearchQueryFactory elasticSearchQueryFactory) {
        this.esClient = esClient;
        this.elasticSearchQueryFactory = elasticSearchQueryFactory;
    }

    public Pagination<AccountDetailKey> search(AccountDetailKeySearchContract accountCodeKeySearchContract) {
        final SearchRequestBuilder searchRequestBuilder = getSearchRequest(accountCodeKeySearchContract);
        final SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();
        return mapToAccountDetailKeyList(searchResponse, accountCodeKeySearchContract);
    }

    @SuppressWarnings("deprecation")
    private Pagination<AccountDetailKey> mapToAccountDetailKeyList(SearchResponse searchResponse,
            AccountDetailKeySearchContract accountCodePurposeSearchContract) {
        Pagination<AccountDetailKey> page = new Pagination<>();
        if (searchResponse.getHits() == null || searchResponse.getHits().getTotalHits() == 0L) {
            return page;
        }
        List<AccountDetailKey> accountDetailkeys = new ArrayList<AccountDetailKey>();
        AccountDetailKey accountDetailKey = null;
        for (SearchHit hit : searchResponse.getHits()) {

            ObjectMapper mapper = new ObjectMapper();
            // JSON from file to Object
            try {
                accountDetailKey = mapper.readValue(hit.sourceAsString(), AccountDetailKey.class);
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

            accountDetailkeys.add(accountDetailKey);
        }

        page.setTotalResults(Long.valueOf(searchResponse.getHits().getTotalHits()).intValue());
        page.setPagedData(accountDetailkeys);

        return page;
    }

    private SearchRequestBuilder getSearchRequest(AccountDetailKeySearchContract criteria) {
        final BoolQueryBuilder boolQueryBuilder = elasticSearchQueryFactory.searchAccountDetailKey(criteria);
        final SearchRequestBuilder searchRequestBuilder = esClient
                .prepareSearch(AccountDetailKey.class.getSimpleName().toLowerCase())
                .setTypes(AccountDetailKey.class.getSimpleName().toLowerCase())
                .setQuery(boolQueryBuilder);
        return searchRequestBuilder;
    }

}
