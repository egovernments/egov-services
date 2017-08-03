package org.egov.egf.master.domain.repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.egov.common.domain.model.Pagination;
import org.egov.egf.master.domain.model.Functionary;
import org.egov.egf.master.web.contract.FunctionarySearchContract;
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
public class FunctionaryESRepository {

	private static final String DEFAULT_SORT_FIELD = "name";
	private TransportClient esClient;
	private ElasticSearchQueryFactory elasticSearchQueryFactory;

	public FunctionaryESRepository(TransportClient esClient, ElasticSearchQueryFactory elasticSearchQueryFactory) {
		this.esClient = esClient;
		this.elasticSearchQueryFactory = elasticSearchQueryFactory;
	}

	public Pagination<Functionary> search(FunctionarySearchContract functionarySearchContract) {
		final SearchRequestBuilder searchRequestBuilder = getSearchRequest(functionarySearchContract);
		final SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();
		return mapToFunctionaryList(searchResponse,functionarySearchContract);
	}


    @SuppressWarnings("deprecation")
	private Pagination<Functionary> mapToFunctionaryList(SearchResponse searchResponse,FunctionarySearchContract functionarySearchContract) {
		Pagination<Functionary> page = new Pagination<>();
		if (searchResponse.getHits() == null || searchResponse.getHits().getTotalHits() == 0L) {
			return page;
		}
		List<Functionary> functionarys = new ArrayList<Functionary>();
		Functionary functionary=null;
		for (SearchHit hit : searchResponse.getHits()) {
			
			ObjectMapper mapper = new ObjectMapper();
			//JSON from file to Object
			try {
			    functionary = mapper.readValue(hit.sourceAsString(), Functionary.class);
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
		
			functionarys.add(functionary);
		}
		
		page.setTotalResults(Long.valueOf(searchResponse.getHits().getTotalHits()).intValue());
		page.setPagedData(functionarys);

		return page;
	}

	private SearchRequestBuilder getSearchRequest(FunctionarySearchContract criteria) {
		final BoolQueryBuilder boolQueryBuilder = elasticSearchQueryFactory.searchFunctionary(criteria);
		final SearchRequestBuilder searchRequestBuilder = esClient.prepareSearch(Functionary.class.getSimpleName().toLowerCase()).setTypes(Functionary.class.getSimpleName().toLowerCase())
				.addSort(DEFAULT_SORT_FIELD, SortOrder.ASC).setQuery(boolQueryBuilder);
		return searchRequestBuilder;
	}

}
