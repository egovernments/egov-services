package org.egov.egf.master.domain.repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.egov.common.domain.model.Pagination;
import org.egov.egf.master.domain.model.SubScheme;
import org.egov.egf.master.web.contract.SubSchemeSearchContract;
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
public class SubSchemeESRepository {

	private static final String DEFAULT_SORT_FIELD = "name";
	private TransportClient esClient;
	private ElasticSearchQueryFactory elasticSearchQueryFactory;

	public SubSchemeESRepository(TransportClient esClient, ElasticSearchQueryFactory elasticSearchQueryFactory) {
		this.esClient = esClient;
		this.elasticSearchQueryFactory = elasticSearchQueryFactory;
	}

	public Pagination<SubScheme> search(SubSchemeSearchContract subSchemeSearchContract) {
		final SearchRequestBuilder searchRequestBuilder = getSearchRequest(subSchemeSearchContract);
		final SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();
		return mapToSubSchemeList(searchResponse,subSchemeSearchContract);
	}


    @SuppressWarnings("deprecation")
	private Pagination<SubScheme> mapToSubSchemeList(SearchResponse searchResponse,SubSchemeSearchContract subSchemeSearchContract) {
		Pagination<SubScheme> page = new Pagination<>();
		if (searchResponse.getHits() == null || searchResponse.getHits().getTotalHits() == 0L) {
			return page;
		}
		List<SubScheme> subSchemes = new ArrayList<SubScheme>();
		SubScheme subScheme=null;
		for (SearchHit hit : searchResponse.getHits()) {
			
			ObjectMapper mapper = new ObjectMapper();
			//JSON from file to Object
			try {
			    subScheme = mapper.readValue(hit.sourceAsString(), SubScheme.class);
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
		
			subSchemes.add(subScheme);
		}
		
		page.setTotalResults(Long.valueOf(searchResponse.getHits().getTotalHits()).intValue());
		page.setPagedData(subSchemes);

		return page;
	}

	private SearchRequestBuilder getSearchRequest(SubSchemeSearchContract criteria) {
		final BoolQueryBuilder boolQueryBuilder = elasticSearchQueryFactory.searchSubScheme(criteria);
		final SearchRequestBuilder searchRequestBuilder = esClient.prepareSearch(SubScheme.class.getSimpleName().toLowerCase()).setTypes(SubScheme.class.getSimpleName().toLowerCase())
				.addSort(DEFAULT_SORT_FIELD, SortOrder.ASC).setQuery(boolQueryBuilder);
		return searchRequestBuilder;
	}

}
