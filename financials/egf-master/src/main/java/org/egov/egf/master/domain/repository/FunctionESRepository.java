package org.egov.egf.master.domain.repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.egov.common.domain.model.Pagination;
import org.egov.egf.master.domain.model.Function;
import org.egov.egf.master.web.contract.FunctionSearchContract;
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
public class FunctionESRepository {

	private static final String DEFAULT_SORT_FIELD = "name";
	private TransportClient esClient;
	private ElasticSearchQueryFactory elasticSearchQueryFactory;

	public FunctionESRepository(TransportClient esClient, ElasticSearchQueryFactory elasticSearchQueryFactory) {
		this.esClient = esClient;
		this.elasticSearchQueryFactory = elasticSearchQueryFactory;
	}

	public Pagination<Function> search(FunctionSearchContract functionSearchContract) {
		final SearchRequestBuilder searchRequestBuilder = getSearchRequest(functionSearchContract);
		final SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();
		return mapToFunctionList(searchResponse,functionSearchContract);
	}


    @SuppressWarnings("deprecation")
	private Pagination<Function> mapToFunctionList(SearchResponse searchResponse,FunctionSearchContract functionSearchContract) {
		Pagination<Function> page = new Pagination<>();
		if (searchResponse.getHits() == null || searchResponse.getHits().getTotalHits() == 0L) {
			return page;
		}
		List<Function> functions = new ArrayList<Function>();
		Function function=null;
		for (SearchHit hit : searchResponse.getHits()) {
			
			ObjectMapper mapper = new ObjectMapper();
			//JSON from file to Object
			try {
			    function = mapper.readValue(hit.sourceAsString(), Function.class);
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
		
			functions.add(function);
		}
		
		page.setTotalResults(Long.valueOf(searchResponse.getHits().getTotalHits()).intValue());
		page.setPagedData(functions);

		return page;
	}

	private SearchRequestBuilder getSearchRequest(FunctionSearchContract criteria) {
		final BoolQueryBuilder boolQueryBuilder = elasticSearchQueryFactory.searchFunction(criteria);
		final SearchRequestBuilder searchRequestBuilder = esClient.prepareSearch(Function.class.getSimpleName().toLowerCase()).setTypes(Function.class.getSimpleName().toLowerCase())
				.addSort(DEFAULT_SORT_FIELD, SortOrder.ASC).setQuery(boolQueryBuilder);
		return searchRequestBuilder;
	}

}
