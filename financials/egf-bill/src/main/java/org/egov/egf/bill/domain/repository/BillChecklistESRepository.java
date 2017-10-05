package org.egov.egf.bill.domain.repository;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.egov.common.domain.model.Pagination;
import org.egov.common.persistence.repository.ESRepository;
import org.egov.common.util.ElasticSearchUtils;
import org.egov.egf.bill.domain.model.BillChecklist;
import org.egov.egf.bill.persistence.entity.BillChecklistEntity;
import org.egov.egf.bill.web.contract.BillChecklistSearchContract;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class BillChecklistESRepository extends ESRepository {

	private TransportClient esClient;

	private ElasticSearchUtils elasticSearchUtils;

	public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	@Autowired
	public BillChecklistESRepository(TransportClient esClient, ElasticSearchUtils elasticSearchUtils) {

		this.esClient = esClient;
		this.elasticSearchUtils = elasticSearchUtils;

	}

	public Pagination<BillChecklist> search(BillChecklistSearchContract billChecklistSearchContract) {
		final SearchRequestBuilder searchRequestBuilder = getSearchRequest(billChecklistSearchContract);
		final SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();
		return mapToBillChecklistList(searchResponse);
	}

	@SuppressWarnings("deprecation")
	private Pagination<BillChecklist> mapToBillChecklistList(SearchResponse searchResponse) {

		Pagination<BillChecklist> page = new Pagination<>();

		if (searchResponse.getHits() == null || searchResponse.getHits().getTotalHits() == 0L) {
			return page;
		}

		List<BillChecklist> billChecklists = new ArrayList<BillChecklist>();
		BillChecklist billChecklist = null;

		for (SearchHit hit : searchResponse.getHits()) {

			ObjectMapper mapper = new ObjectMapper();

			try {
				billChecklist = mapper.readValue(hit.sourceAsString(), BillChecklist.class);
			} catch (JsonParseException e1) {

				e1.printStackTrace();
			
			} catch (JsonMappingException e1) {

				e1.printStackTrace();
			
			} catch (IOException e1) {

				e1.printStackTrace();
			
			}

			billChecklists.add(billChecklist);
		}

		page.setTotalResults(Long.valueOf(searchResponse.getHits().getTotalHits()).intValue());
		page.setPagedData(billChecklists);

		return page;
	}

	private SearchRequestBuilder getSearchRequest(BillChecklistSearchContract criteria) {

		List<String> orderByList = new ArrayList<>();

		if (criteria.getSortBy() != null && !criteria.getSortBy().isEmpty()) {

			validateSortByOrder(criteria.getSortBy());
			validateEntityFieldName(criteria.getSortBy(), BillChecklistEntity.class);
			orderByList = prepareOrderBy(criteria.getSortBy());

		}

		final BoolQueryBuilder boolQueryBuilder = boolQueryBuilderForBillChecklists(criteria);
		SearchRequestBuilder searchRequestBuilder = esClient.prepareSearch(BillChecklist.class.getSimpleName().toLowerCase())
				.setTypes(BillChecklist.class.getSimpleName().toLowerCase());

		if (!orderByList.isEmpty()) {
			for (String orderBy : orderByList) {
				searchRequestBuilder = searchRequestBuilder.addSort(orderBy.split(" ")[0],
						orderBy.split(" ")[1].equalsIgnoreCase("asc") ? SortOrder.ASC : SortOrder.DESC);
			}
		}

		searchRequestBuilder.setQuery(boolQueryBuilder);

		return searchRequestBuilder;

	}

	public BoolQueryBuilder boolQueryBuilderForBillChecklists(BillChecklistSearchContract billChecklistSearchContract) {

		BoolQueryBuilder boolQueryBuilder = boolQuery();

		if (billChecklistSearchContract.getIds() != null && !billChecklistSearchContract.getIds().isEmpty())
			elasticSearchUtils.add(billChecklistSearchContract.getIds(), "id", boolQueryBuilder);

		elasticSearchUtils.add(billChecklistSearchContract.getId(), "id", boolQueryBuilder);
		
		elasticSearchUtils.add(billChecklistSearchContract.getBill(), "bill", boolQueryBuilder);
		
		elasticSearchUtils.add(billChecklistSearchContract.getChecklist(), "checklist", boolQueryBuilder);
		
		elasticSearchUtils.add(billChecklistSearchContract.getChecklistValue(), "checklistValue", boolQueryBuilder);

		elasticSearchUtils.add(billChecklistSearchContract.getTenantId(), "tenantId", boolQueryBuilder);

		return boolQueryBuilder;

	}

	public List<String> prepareOrderBy(String sortBy) {

		List<String> orderByList = new ArrayList<String>();
		List<String> sortByList = new ArrayList<String>();

		if (sortBy.contains(",")) {
			sortByList = Arrays.asList(sortBy.split(","));
		} else {
			sortByList = Arrays.asList(sortBy);
		}

		for (String s : sortByList) {

			if (s.contains(" ")
					&& (s.toLowerCase().trim().endsWith("asc") || s.toLowerCase().trim().endsWith("desc"))) {
				orderByList.add(s.trim());
			} else {

				orderByList.add(s.trim() + " asc");
			}

		}

		return orderByList;
	}

}
