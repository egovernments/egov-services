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
import org.egov.egf.bill.domain.model.BillDetail;
import org.egov.egf.bill.persistence.entity.BillDetailEntity;
import org.egov.egf.bill.web.contract.BillDetailSearchContract;
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
public class BillDetailESRepository extends ESRepository {

	private TransportClient esClient;

	private ElasticSearchUtils elasticSearchUtils;

	public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	@Autowired
	public BillDetailESRepository(TransportClient esClient, ElasticSearchUtils elasticSearchUtils) {

		this.esClient = esClient;
		this.elasticSearchUtils = elasticSearchUtils;

	}

	public Pagination<BillDetail> search(BillDetailSearchContract billDetailSearchContract) {
		final SearchRequestBuilder searchRequestBuilder = getSearchRequest(billDetailSearchContract);
		final SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();
		return mapToBillDetailList(searchResponse);
	}

	@SuppressWarnings("deprecation")
	private Pagination<BillDetail> mapToBillDetailList(SearchResponse searchResponse) {

		Pagination<BillDetail> page = new Pagination<>();

		if (searchResponse.getHits() == null || searchResponse.getHits().getTotalHits() == 0L) {
			return page;
		}

		List<BillDetail> billDetails = new ArrayList<BillDetail>();
		BillDetail billDetail = null;

		for (SearchHit hit : searchResponse.getHits()) {

			ObjectMapper mapper = new ObjectMapper();

			try {
				billDetail = mapper.readValue(hit.sourceAsString(), BillDetail.class);
			} catch (JsonParseException e1) {
				
				e1.printStackTrace();
				
			} catch (JsonMappingException e1) {
				
				e1.printStackTrace();
				
			} catch (IOException e1) {
			
				e1.printStackTrace();
			
			}

			billDetails.add(billDetail);
		}

		page.setTotalResults(Long.valueOf(searchResponse.getHits().getTotalHits()).intValue());
		page.setPagedData(billDetails);

		return page;
	}

	private SearchRequestBuilder getSearchRequest(BillDetailSearchContract criteria) {

		List<String> orderByList = new ArrayList<>();

		if (criteria.getSortBy() != null && !criteria.getSortBy().isEmpty()) {

			validateSortByOrder(criteria.getSortBy());
			validateEntityFieldName(criteria.getSortBy(), BillDetailEntity.class);
			orderByList = prepareOrderBy(criteria.getSortBy());

		}

		final BoolQueryBuilder boolQueryBuilder = boolQueryBuilderForBillDetails(criteria);
		SearchRequestBuilder searchRequestBuilder = esClient.prepareSearch(BillDetail.class.getSimpleName().toLowerCase())
				.setTypes(BillDetail.class.getSimpleName().toLowerCase());

		if (!orderByList.isEmpty()) {
			for (String orderBy : orderByList) {
				searchRequestBuilder = searchRequestBuilder.addSort(orderBy.split(" ")[0],
						orderBy.split(" ")[1].equalsIgnoreCase("asc") ? SortOrder.ASC : SortOrder.DESC);
			}
		}

		searchRequestBuilder.setQuery(boolQueryBuilder);

		return searchRequestBuilder;

	}

	public BoolQueryBuilder boolQueryBuilderForBillDetails(BillDetailSearchContract billDetailSearchContract) {

		BoolQueryBuilder boolQueryBuilder = boolQuery();

		if (billDetailSearchContract.getIds() != null && !billDetailSearchContract.getIds().isEmpty())
			elasticSearchUtils.add(billDetailSearchContract.getIds(), "id", boolQueryBuilder);

		elasticSearchUtils.add(billDetailSearchContract.getId(), "id", boolQueryBuilder);

		elasticSearchUtils.add(billDetailSearchContract.getOrderId(), "orderId", boolQueryBuilder);
		
		elasticSearchUtils.add(billDetailSearchContract.getGlcode(), "ledgers.glcode", boolQueryBuilder);

		elasticSearchUtils.add(billDetailSearchContract.getDebitAmount(), "ledgers.debitAmount", boolQueryBuilder);

		elasticSearchUtils.add(billDetailSearchContract.getCreditAmount(), "ledgers.creditAmount", boolQueryBuilder);

		elasticSearchUtils.add(billDetailSearchContract.getTenantId(), "tenantId", boolQueryBuilder);

		if (billDetailSearchContract.getChartOfAccount() != null && billDetailSearchContract.getChartOfAccount().getId() != null)
			elasticSearchUtils.add(billDetailSearchContract.getChartOfAccount().getId(), "department.id", boolQueryBuilder);

		if (billDetailSearchContract.getFunction() != null && billDetailSearchContract.getFunction().getId() != null)
			elasticSearchUtils.add(billDetailSearchContract.getFunction().getId(), "function.id", boolQueryBuilder);

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
