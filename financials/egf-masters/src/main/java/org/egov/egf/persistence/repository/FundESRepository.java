package org.egov.egf.persistence.repository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.egov.egf.persistence.entity.Fund;
import org.egov.egf.persistence.entity.FundSearchCriteria;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FundESRepository {

	private static final String DEFAULT_SORT_FIELD = "lastModifiedDate";
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

	public long getCount(FundSearchCriteria fundSearchCriteria) {
		final BoolQueryBuilder boolQueryBuilder = fundQueryFactory.create(fundSearchCriteria);
		final SearchResponse searchResponse = esClient.prepareSearch(indexName).setTypes(documentType).setSize(0)
				.setQuery(boolQueryBuilder).execute().actionGet();
		return searchResponse.getHits().getTotalHits();
	}

	public List<Fund> getMatchingFunds(FundSearchCriteria criteria) {
		final SearchRequestBuilder searchRequestBuilder = getSearchRequest(criteria);
		final SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();
		return mapToFundList(searchResponse);
	}

	private List<Fund> mapToFundList(SearchResponse searchResponse) {
		if (searchResponse.getHits() == null || searchResponse.getHits().getTotalHits() == 0L) {
			return Collections.emptyList();
		}
		List<Fund> funds = new ArrayList<Fund>();
		Fund fund;
		for (SearchHit hit : searchResponse.getHits()) {
			fund = new Fund();
			fund.setId(Long.valueOf(hit.getSource().get("id") != null ? hit.getSource().get("id").toString() : ""));
			fund.setName(hit.getSource().get("name") != null ? hit.getSource().get("name").toString() : "");
			fund.setCode(hit.getSource().get("code") != null ? hit.getSource().get("code").toString() : "");
			fund.setIdentifier(
					hit.getSource().get("id") != null ? hit.getSource().get("identifier").toString().charAt(0) : null);
			fund.setLevel(hit.getSource().get("level") != null ? Long.valueOf(hit.getSource().get("level").toString())
					: null);
			fund.setParentId(hit.getSource().get("parentId") != null
					? Long.valueOf(hit.getSource().get("parentId").toString()) : null);
			fund.setIsParent(hit.getSource().get("isParent") != null
					? Boolean.valueOf(hit.getSource().get("isParent").toString()) : null);
			fund.setActive(hit.getSource().get("active") != null
					? Boolean.valueOf(hit.getSource().get("active").toString()) : null);
			fund.setCreatedBy(hit.getSource().get("createdBy") != null
					? Long.valueOf(hit.getSource().get("createdBy").toString()) : null);
			fund.setLastModifiedBy(hit.getSource().get("lastModifiedBy") != null
					? Long.valueOf(hit.getSource().get("lastModifiedBy").toString()) : null);
			try {
				if (hit.getSource().get("createdDate") != null)
					fund.setCreatedDate(
							new SimpleDateFormat("dd/MM/yyyy").parse(hit.getSource().get("createdDate").toString()));
				if (hit.getSource().get("lastModifiedDate") != null)
					fund.setLastModifiedDate(new SimpleDateFormat("dd/MM/yyyy")
							.parse(hit.getSource().get("lastModifiedDate").toString()));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			fund.setTenantId(hit.getSource().get("tenantId") != null ? hit.getSource().get("tenantId").toString() : "");
			funds.add(fund);
		}
		return funds;
	}

	private SearchRequestBuilder getSearchRequest(FundSearchCriteria criteria) {
		final BoolQueryBuilder boolQueryBuilder = fundQueryFactory.create(criteria);
		final SearchRequestBuilder searchRequestBuilder = esClient.prepareSearch(indexName).setTypes(documentType)
				.addSort(DEFAULT_SORT_FIELD, SortOrder.DESC).setQuery(boolQueryBuilder);
		setResponseCount(criteria, searchRequestBuilder);
		return searchRequestBuilder;
	}

	private void setResponseCount(FundSearchCriteria criteria, SearchRequestBuilder searchRequestBuilder) {
		if (criteria.isPaginationCriteriaPresent()) {
			searchRequestBuilder.setFrom(criteria.getFromIndex()).setSize(criteria.getPageSize());
		} else {
			searchRequestBuilder.setSize(Long.valueOf(getCount(criteria)).intValue());
		}
	}
}
