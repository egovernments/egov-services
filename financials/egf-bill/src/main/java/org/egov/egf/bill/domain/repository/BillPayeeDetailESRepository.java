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
import org.egov.egf.bill.domain.model.BillPayeeDetail;
import org.egov.egf.bill.persistence.entity.BillPayeeDetailEntity;
import org.egov.egf.bill.web.contract.BillPayeeDetailSearchContract;
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
public class BillPayeeDetailESRepository extends ESRepository {

	private TransportClient esClient;

	private ElasticSearchUtils elasticSearchUtils;

	public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	@Autowired
	public BillPayeeDetailESRepository(TransportClient esClient, ElasticSearchUtils elasticSearchUtils) {

		this.esClient = esClient;
		this.elasticSearchUtils = elasticSearchUtils;

	}

	public Pagination<BillPayeeDetail> search(BillPayeeDetailSearchContract billPayeeDetailSearchContract) {
		final SearchRequestBuilder searchRequestBuilder = getSearchRequest(billPayeeDetailSearchContract);
		final SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();
		return mapToBillPayeeDetailList(searchResponse);
	}

	@SuppressWarnings("deprecation")
	private Pagination<BillPayeeDetail> mapToBillPayeeDetailList(SearchResponse searchResponse) {

		Pagination<BillPayeeDetail> page = new Pagination<>();

		if (searchResponse.getHits() == null || searchResponse.getHits().getTotalHits() == 0L) {
			return page;
		}

		List<BillPayeeDetail> billPayeeDetails = new ArrayList<BillPayeeDetail>();
		BillPayeeDetail billPayeeDetail = null;

		for (SearchHit hit : searchResponse.getHits()) {

			ObjectMapper mapper = new ObjectMapper();
			// JSON from file to Object
			try {
				billPayeeDetail = mapper.readValue(hit.sourceAsString(), BillPayeeDetail.class);
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

			billPayeeDetails.add(billPayeeDetail);
		}

		page.setTotalResults(Long.valueOf(searchResponse.getHits().getTotalHits()).intValue());
		page.setPagedData(billPayeeDetails);

		return page;
	}

	private SearchRequestBuilder getSearchRequest(BillPayeeDetailSearchContract criteria) {

		List<String> orderByList = new ArrayList<>();

		if (criteria.getSortBy() != null && !criteria.getSortBy().isEmpty()) {

			validateSortByOrder(criteria.getSortBy());
			validateEntityFieldName(criteria.getSortBy(), BillPayeeDetailEntity.class);
			orderByList = prepareOrderBy(criteria.getSortBy());

		}

		final BoolQueryBuilder boolQueryBuilder = boolQueryBuilderForBillPayeeDetails(criteria);
		SearchRequestBuilder searchRequestBuilder = esClient.prepareSearch(BillPayeeDetail.class.getSimpleName().toLowerCase())
				.setTypes(BillPayeeDetail.class.getSimpleName().toLowerCase());

		if (!orderByList.isEmpty()) {
			for (String orderBy : orderByList) {
				searchRequestBuilder = searchRequestBuilder.addSort(orderBy.split(" ")[0],
						orderBy.split(" ")[1].equalsIgnoreCase("asc") ? SortOrder.ASC : SortOrder.DESC);
			}
		}

		searchRequestBuilder.setQuery(boolQueryBuilder);

		return searchRequestBuilder;

	}

	public BoolQueryBuilder boolQueryBuilderForBillPayeeDetails(BillPayeeDetailSearchContract billPayeeDetailSearchContract) {

		BoolQueryBuilder boolQueryBuilder = boolQuery();

		if (billPayeeDetailSearchContract.getIds() != null && !billPayeeDetailSearchContract.getIds().isEmpty())
			elasticSearchUtils.add(billPayeeDetailSearchContract.getIds(), "id", boolQueryBuilder);

		elasticSearchUtils.add(billPayeeDetailSearchContract.getId(), "id", boolQueryBuilder);
		
		elasticSearchUtils.add(billPayeeDetailSearchContract.getAmount(), "amount", boolQueryBuilder);

		elasticSearchUtils.add(billPayeeDetailSearchContract.getTenantId(), "tenantId", boolQueryBuilder);

		if (billPayeeDetailSearchContract.getAccountDetailKey() != null && billPayeeDetailSearchContract.getAccountDetailKey().getId() != null)
			elasticSearchUtils.add(billPayeeDetailSearchContract.getAccountDetailKey().getId(), "accountDetailKey.id", boolQueryBuilder);

		if (billPayeeDetailSearchContract.getAccountDetailType() != null && billPayeeDetailSearchContract.getAccountDetailType().getId() != null)
			elasticSearchUtils.add(billPayeeDetailSearchContract.getAccountDetailType().getId(), "accountDetailType.id", boolQueryBuilder);

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
