package org.egov.egf.master.domain.repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.egov.common.domain.model.Pagination;
import org.egov.egf.master.domain.model.Scheme;
import org.egov.egf.master.web.contract.SchemeSearchContract;
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
public class SchemeESRepository {

	private static final String DEFAULT_SORT_FIELD = "name";
	private TransportClient esClient;
	private ElasticSearchQueryFactory elasticSearchQueryFactory;

	public SchemeESRepository(TransportClient esClient, ElasticSearchQueryFactory elasticSearchQueryFactory) {
		this.esClient = esClient;
		this.elasticSearchQueryFactory = elasticSearchQueryFactory;
	}

	public Pagination<Scheme> search(SchemeSearchContract schemeSearchContract) {
		final SearchRequestBuilder searchRequestBuilder = getSearchRequest(schemeSearchContract);
		final SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();
		return mapToSchemeList(searchResponse,schemeSearchContract);
	}


    @SuppressWarnings("deprecation")
	private Pagination<Scheme> mapToSchemeList(SearchResponse searchResponse,SchemeSearchContract schemeSearchContract) {
		Pagination<Scheme> page = new Pagination<>();
		if (searchResponse.getHits() == null || searchResponse.getHits().getTotalHits() == 0L) {
			return page;
		}
		List<Scheme> schemes = new ArrayList<Scheme>();
		Scheme scheme=null;
		for (SearchHit hit : searchResponse.getHits()) {
			
			ObjectMapper mapper = new ObjectMapper();
			//JSON from file to Object
			try {
			    scheme = mapper.readValue(hit.sourceAsString(), Scheme.class);
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
		
			schemes.add(scheme);
		}
		
		page.setTotalResults(Long.valueOf(searchResponse.getHits().getTotalHits()).intValue());
		page.setPagedData(schemes);

		return page;
	}

	private SearchRequestBuilder getSearchRequest(SchemeSearchContract criteria) {
		final BoolQueryBuilder boolQueryBuilder = elasticSearchQueryFactory.searchScheme(criteria);
		final SearchRequestBuilder searchRequestBuilder = esClient.prepareSearch(Scheme.class.getSimpleName().toLowerCase()).setTypes(Scheme.class.getSimpleName().toLowerCase())
				.addSort(DEFAULT_SORT_FIELD, SortOrder.ASC).setQuery(boolQueryBuilder);
		return searchRequestBuilder;
	}

}
