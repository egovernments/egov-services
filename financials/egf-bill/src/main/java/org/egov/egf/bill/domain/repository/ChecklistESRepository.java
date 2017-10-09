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
import org.egov.egf.bill.domain.model.Checklist;
import org.egov.egf.bill.persistence.entity.ChecklistEntity;
import org.egov.egf.bill.web.contract.ChecklistSearchContract;
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
public class ChecklistESRepository extends ESRepository {

	private TransportClient esClient;

	private ElasticSearchUtils elasticSearchUtils;

	public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	@Autowired
	public ChecklistESRepository(TransportClient esClient, ElasticSearchUtils elasticSearchUtils) {

		this.esClient = esClient;
		this.elasticSearchUtils = elasticSearchUtils;

	}

	public Pagination<Checklist> search(ChecklistSearchContract checklistSearchContract) {
		final SearchRequestBuilder searchRequestBuilder = getSearchRequest(checklistSearchContract);
		final SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();
		return mapToChecklistList(searchResponse);
	}

	@SuppressWarnings("deprecation")
	private Pagination<Checklist> mapToChecklistList(SearchResponse searchResponse) {

		Pagination<Checklist> page = new Pagination<>();

		if (searchResponse.getHits() == null || searchResponse.getHits().getTotalHits() == 0L) {
			return page;
		}

		List<Checklist> checklists = new ArrayList<Checklist>();
		Checklist checklist = null;

		for (SearchHit hit : searchResponse.getHits()) {

			ObjectMapper mapper = new ObjectMapper();
			// JSON from file to Object
			try {
				checklist = mapper.readValue(hit.sourceAsString(), Checklist.class);
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

			checklists.add(checklist);
		}

		page.setTotalResults(Long.valueOf(searchResponse.getHits().getTotalHits()).intValue());
		page.setPagedData(checklists);

		return page;
	}

	private SearchRequestBuilder getSearchRequest(ChecklistSearchContract criteria) {

		List<String> orderByList = new ArrayList<>();

		if (criteria.getSortBy() != null && !criteria.getSortBy().isEmpty()) {

			validateSortByOrder(criteria.getSortBy());
			validateEntityFieldName(criteria.getSortBy(), ChecklistEntity.class);
			orderByList = prepareOrderBy(criteria.getSortBy());

		}

		final BoolQueryBuilder boolQueryBuilder = boolQueryBuilderForChecklists(criteria);
		SearchRequestBuilder searchRequestBuilder = esClient.prepareSearch(Checklist.class.getSimpleName().toLowerCase())
				.setTypes(Checklist.class.getSimpleName().toLowerCase());

		if (!orderByList.isEmpty()) {
			for (String orderBy : orderByList) {
				searchRequestBuilder = searchRequestBuilder.addSort(orderBy.split(" ")[0],
						orderBy.split(" ")[1].equalsIgnoreCase("asc") ? SortOrder.ASC : SortOrder.DESC);
			}
		}

		searchRequestBuilder.setQuery(boolQueryBuilder);

		return searchRequestBuilder;

	}

	public BoolQueryBuilder boolQueryBuilderForChecklists(ChecklistSearchContract checklistSearchContract) {

		BoolQueryBuilder boolQueryBuilder = boolQuery();

		if (checklistSearchContract.getIds() != null && !checklistSearchContract.getIds().isEmpty())
			elasticSearchUtils.add(checklistSearchContract.getIds(), "id", boolQueryBuilder);

		elasticSearchUtils.add(checklistSearchContract.getId(), "id", boolQueryBuilder);
		
		elasticSearchUtils.add(checklistSearchContract.getType(), "type", boolQueryBuilder);
		
		elasticSearchUtils.add(checklistSearchContract.getSubType(), "subType", boolQueryBuilder);
		
		elasticSearchUtils.add(checklistSearchContract.getKey(), "key", boolQueryBuilder);

		elasticSearchUtils.add(checklistSearchContract.getDescription(), "description", boolQueryBuilder);

		elasticSearchUtils.add(checklistSearchContract.getTenantId(), "tenantId", boolQueryBuilder);

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
