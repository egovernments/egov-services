package org.egov.egf.master.domain.repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.egov.common.domain.model.Pagination;
import org.egov.egf.master.domain.model.Fund;
import org.egov.egf.master.web.contract.FundSearchContract;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class FundESRepository {

	private static final String DEFAULT_SORT_FIELD = "name";
	private TransportClient esClient;
	private String indexName;
	private String documentType;
	private FundQueryFactory fundQueryFactory;

	public FundESRepository(TransportClient esClient, @Value("${es.fund.index.name}") String indexName,
			@Value("${es.fund.document.type}") String documentType, FundQueryFactory fundQueryFactory) {
		this.esClient = esClient;
		this.indexName = indexName;
		this.documentType = documentType;
		this.fundQueryFactory = fundQueryFactory;
	}

	public Pagination<Fund> search(FundSearchContract fundSearchContract) {
		final SearchRequestBuilder searchRequestBuilder = getSearchRequest(fundSearchContract);
		final SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();
		return mapToFundList(searchResponse,fundSearchContract);
	}

	@SuppressWarnings("deprecation")
	private Pagination<Fund>mapToFundList(SearchResponse searchResponse,FundSearchContract fundSearchContract) {
		Pagination<Fund> page = new Pagination<>();
		if (searchResponse.getHits() == null || searchResponse.getHits().getTotalHits() == 0L) {
			return page;
		}
		List<Fund> funds = new ArrayList<Fund>();
		Fund fund=null;
		for (SearchHit hit : searchResponse.getHits()) {
			
			ObjectMapper mapper = new ObjectMapper();
			//JSON from file to Object
			try {
				fund = mapper.readValue(hit.sourceAsString(), Fund.class);
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
		
			funds.add(fund);
		}
		
		page.setTotalResults(Long.valueOf(searchResponse.getHits().getTotalHits()).intValue());
		page.setPagedData(funds);

		return page;
	}

	private SearchRequestBuilder getSearchRequest(FundSearchContract criteria) {
		final BoolQueryBuilder boolQueryBuilder = fundQueryFactory.create(criteria);
		final SearchRequestBuilder searchRequestBuilder = esClient.prepareSearch(indexName).setTypes(documentType)
				.addSort(DEFAULT_SORT_FIELD, SortOrder.ASC).setQuery(boolQueryBuilder);
		return searchRequestBuilder;
	}

}
