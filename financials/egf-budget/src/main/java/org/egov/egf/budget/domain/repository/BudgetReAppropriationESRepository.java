package org.egov.egf.budget.domain.repository;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.common.domain.model.Pagination;
import org.egov.common.persistence.repository.ESRepository;
import org.egov.egf.budget.domain.model.BudgetReAppropriation;
import org.egov.egf.budget.persistence.entity.BudgetReAppropriationEntity;
import org.egov.egf.budget.web.contract.BudgetReAppropriationSearchContract;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class BudgetReAppropriationESRepository extends ESRepository {

	private TransportClient esClient;
	private ElasticSearchQueryFactory elasticSearchQueryFactory;

	public BudgetReAppropriationESRepository(TransportClient esClient, ElasticSearchQueryFactory elasticSearchQueryFactory) {
		this.esClient = esClient;
		this.elasticSearchQueryFactory = elasticSearchQueryFactory;
	}

	public Pagination<BudgetReAppropriation> search(BudgetReAppropriationSearchContract budgetReAppropriationSearchContract) {
		final SearchRequestBuilder searchRequestBuilder = getSearchRequest(budgetReAppropriationSearchContract);
		final SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();
		return mapToBudgetReAppropriationList(searchResponse);
	}

	@SuppressWarnings("deprecation")
	private Pagination<BudgetReAppropriation> mapToBudgetReAppropriationList(SearchResponse searchResponse) {
		Pagination<BudgetReAppropriation> page = new Pagination<>();
		if (searchResponse.getHits() == null || searchResponse.getHits().getTotalHits() == 0L) {
			return page;
		}
		List<BudgetReAppropriation> budgetReAppropriations = new ArrayList<BudgetReAppropriation>();
		BudgetReAppropriation budgetReAppropriation = null;
		for (SearchHit hit : searchResponse.getHits()) {

			ObjectMapper mapper = new ObjectMapper();
			// JSON from file to Object
			try {
				budgetReAppropriation = mapper.readValue(hit.sourceAsString(), BudgetReAppropriation.class);
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

			budgetReAppropriations.add(budgetReAppropriation);
		}

		page.setTotalResults(Long.valueOf(searchResponse.getHits().getTotalHits()).intValue());
		page.setPagedData(budgetReAppropriations);

		return page;
	}

	private SearchRequestBuilder getSearchRequest(BudgetReAppropriationSearchContract criteria) {
		List<String> orderByList = new ArrayList<>();
		if (criteria.getSortBy() != null && !criteria.getSortBy().isEmpty()) {
			validateSortByOrder(criteria.getSortBy());
			validateEntityFieldName(criteria.getSortBy(), BudgetReAppropriationEntity.class);
			orderByList = elasticSearchQueryFactory.prepareOrderBys(criteria.getSortBy());
		}

		final BoolQueryBuilder boolQueryBuilder = elasticSearchQueryFactory.searchBudgetReAppropriation(criteria);
		SearchRequestBuilder searchRequestBuilder = esClient.prepareSearch(BudgetReAppropriation.class.getSimpleName().toLowerCase())
				.setTypes(BudgetReAppropriation.class.getSimpleName().toLowerCase());
		if (!orderByList.isEmpty()) {
			for (String orderBy : orderByList) {
				searchRequestBuilder = searchRequestBuilder.addSort(orderBy.split(" ")[0],
						orderBy.split(" ")[1].equalsIgnoreCase("asc") ? SortOrder.ASC : SortOrder.DESC);
			}
		}

		searchRequestBuilder.setQuery(boolQueryBuilder);
		return searchRequestBuilder;
	}

}