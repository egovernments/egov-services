package org.egov.egf.master.domain.repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.egov.common.domain.model.Pagination;
import org.egov.egf.master.domain.model.AccountDetailType;
import org.egov.egf.master.web.contract.AccountDetailTypeSearchContract;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AccountDetailTypeESRepository {

	private static final String DEFAULT_SORT_FIELD = "name";
	private TransportClient esClient;
	private ElasticSearchQueryFactory elasticSearchQueryFactory;

	public AccountDetailTypeESRepository(TransportClient esClient, ElasticSearchQueryFactory elasticSearchQueryFactory) {
		this.esClient = esClient;
		this.elasticSearchQueryFactory = elasticSearchQueryFactory;
	}

	public Pagination<AccountDetailType> search(AccountDetailTypeSearchContract accountCodeTypeSearchContract) {
		final SearchRequestBuilder searchRequestBuilder = getSearchRequest(accountCodeTypeSearchContract);
		final SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();
		return mapToAccountDetailTypeList(searchResponse,accountCodeTypeSearchContract);
	}


    @SuppressWarnings("deprecation")
	private Pagination<AccountDetailType> mapToAccountDetailTypeList(SearchResponse searchResponse,AccountDetailTypeSearchContract accountCodeTypeSearchContract) {
		Pagination<AccountDetailType> page = new Pagination<>();
		if (searchResponse.getHits() == null || searchResponse.getHits().getTotalHits() == 0L) {
			return page;
		}
		List<AccountDetailType> accountDetailkeys = new ArrayList<AccountDetailType>();
		AccountDetailType accountDetailKey=null;
		for (SearchHit hit : searchResponse.getHits()) {
			
			ObjectMapper mapper = new ObjectMapper();
			//JSON from file to Object
			try {
				accountDetailKey = mapper.readValue(hit.sourceAsString(), AccountDetailType.class);
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

	private SearchRequestBuilder getSearchRequest(AccountDetailTypeSearchContract criteria) {
		final BoolQueryBuilder boolQueryBuilder = elasticSearchQueryFactory.searchAccountDetailType(criteria);
		final SearchRequestBuilder searchRequestBuilder = esClient.prepareSearch(AccountDetailType.class.getSimpleName().toLowerCase()).setTypes(AccountDetailType.class.getSimpleName().toLowerCase())
				.addSort(DEFAULT_SORT_FIELD, SortOrder.ASC).setQuery(boolQueryBuilder);
		return searchRequestBuilder;
	}

}
