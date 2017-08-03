package org.egov.egf.master.domain.repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.egov.common.domain.model.Pagination;
import org.egov.egf.master.domain.model.BudgetGroup;
import org.egov.egf.master.web.contract.BudgetGroupSearchContract;
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
public class BudgetGroupESRepository {

	private static final String DEFAULT_SORT_FIELD = "name";
	private TransportClient esClient;
	private ElasticSearchQueryFactory elasticSearchQueryFactory;

	public BudgetGroupESRepository(TransportClient esClient, ElasticSearchQueryFactory elasticSearchQueryFactory) {
		this.esClient = esClient;
		this.elasticSearchQueryFactory = elasticSearchQueryFactory;
	}

	public Pagination<BudgetGroup> search(BudgetGroupSearchContract budgetGroupSearchContract) {
		final SearchRequestBuilder searchRequestBuilder = getSearchRequest(budgetGroupSearchContract);
		final SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();
		return mapToBudgetGroupList(searchResponse,budgetGroupSearchContract);
	}


    @SuppressWarnings("deprecation")
	private Pagination<BudgetGroup> mapToBudgetGroupList(SearchResponse searchResponse,BudgetGroupSearchContract budgetGroupSearchContract) {
		Pagination<BudgetGroup> page = new Pagination<>();
		if (searchResponse.getHits() == null || searchResponse.getHits().getTotalHits() == 0L) {
			return page;
		}
		List<BudgetGroup> budgetGroups = new ArrayList<BudgetGroup>();
		BudgetGroup budgetGroup=null;
		for (SearchHit hit : searchResponse.getHits()) {
			
			ObjectMapper mapper = new ObjectMapper();
			//JSON from file to Object
			try {
			    budgetGroup = mapper.readValue(hit.sourceAsString(), BudgetGroup.class);
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
		
			budgetGroups.add(budgetGroup);
		}
		
		page.setTotalResults(Long.valueOf(searchResponse.getHits().getTotalHits()).intValue());
		page.setPagedData(budgetGroups);

		return page;
	}

	private SearchRequestBuilder getSearchRequest(BudgetGroupSearchContract criteria) {
		final BoolQueryBuilder boolQueryBuilder = elasticSearchQueryFactory.searchBudgetGroup(criteria);
		final SearchRequestBuilder searchRequestBuilder = esClient.prepareSearch(BudgetGroup.class.getSimpleName().toLowerCase()).setTypes(BudgetGroup.class.getSimpleName().toLowerCase())
				.addSort(DEFAULT_SORT_FIELD, SortOrder.ASC).setQuery(boolQueryBuilder);
		return searchRequestBuilder;
	}

}
