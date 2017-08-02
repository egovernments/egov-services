package org.egov.egf.master.domain.repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.egov.common.domain.model.Pagination;
import org.egov.egf.master.domain.model.ChartOfAccountDetail;
import org.egov.egf.master.web.contract.ChartOfAccountDetailSearchContract;
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
public class ChartOfAccountDetailESRepository {

	private TransportClient esClient;
	private ElasticSearchQueryFactory elasticSearchQueryFactory;

	public ChartOfAccountDetailESRepository(TransportClient esClient, ElasticSearchQueryFactory elasticSearchQueryFactory) {
		this.esClient = esClient;
		this.elasticSearchQueryFactory = elasticSearchQueryFactory;
	}

	public Pagination<ChartOfAccountDetail> search(ChartOfAccountDetailSearchContract chartOfAccountDetailSearchContract) {
		final SearchRequestBuilder searchRequestBuilder = getSearchRequest(chartOfAccountDetailSearchContract);
		final SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();
		return mapToChartOfAccountDetailList(searchResponse,chartOfAccountDetailSearchContract);
	}


    @SuppressWarnings("deprecation")
	private Pagination<ChartOfAccountDetail> mapToChartOfAccountDetailList(SearchResponse searchResponse,ChartOfAccountDetailSearchContract chartOfAccountDetailSearchContract) {
		Pagination<ChartOfAccountDetail> page = new Pagination<>();
		if (searchResponse.getHits() == null || searchResponse.getHits().getTotalHits() == 0L) {
			return page;
		}
		List<ChartOfAccountDetail> chartOfAccountDetails = new ArrayList<ChartOfAccountDetail>();
		ChartOfAccountDetail chartOfAccountDetail=null;
		for (SearchHit hit : searchResponse.getHits()) {
			
			ObjectMapper mapper = new ObjectMapper();
			//JSON from file to Object
			try {
			    chartOfAccountDetail = mapper.readValue(hit.sourceAsString(), ChartOfAccountDetail.class);
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
		
			chartOfAccountDetails.add(chartOfAccountDetail);
		}
		
		page.setTotalResults(Long.valueOf(searchResponse.getHits().getTotalHits()).intValue());
		page.setPagedData(chartOfAccountDetails);

		return page;
	}

	private SearchRequestBuilder getSearchRequest(ChartOfAccountDetailSearchContract criteria) {
		final BoolQueryBuilder boolQueryBuilder = elasticSearchQueryFactory.searchChartOfAccountDetail(criteria);
		final SearchRequestBuilder searchRequestBuilder = esClient.prepareSearch(ChartOfAccountDetail.class.getSimpleName().toLowerCase()).setTypes(ChartOfAccountDetail.class.getSimpleName().toLowerCase())
				.setQuery(boolQueryBuilder);
		return searchRequestBuilder;
	}

}
