/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.inv.persistence.repository;

import static org.elasticsearch.common.Strings.isEmpty;
import static org.elasticsearch.index.query.QueryBuilders.boolQuery;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.egov.inv.domain.service.ElasticSearchUtils;
import org.egov.inv.persistence.entity.StoreEntity;
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

import io.swagger.model.Pagination;
import io.swagger.model.Store;
import io.swagger.model.StoreGetRequest;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class StoreESRepository extends ESRepository {

	private static final String TRUE = "true";

	private static final String EMPTY_STRING = "";
	private static final String NEW_LINE = "\n";

	private ElasticSearchUtils elasticSearchUtils;

	private TransportClient esClient;

	private boolean isESRequestLoggingEnabled;

	public StoreESRepository(TransportClient esClient,
			ElasticSearchUtils elasticSearchUtils,
			@Value("${es.log.request}") String isESRequestLoggingEnabled) {
		this.esClient = esClient;
		this.elasticSearchUtils = elasticSearchUtils;
		this.isESRequestLoggingEnabled = TRUE
				.equalsIgnoreCase(isESRequestLoggingEnabled);
	}

	public Pagination<Store> search(StoreGetRequest storeGetRequest) {
		final SearchRequestBuilder searchRequestBuilder = getSearchRequest(
				storeGetRequest);
		final SearchResponse searchResponse = searchRequestBuilder.execute()
				.actionGet();
		logResponse(searchResponse);

		return mapToStore(searchResponse);
	}

	private Pagination<Store> mapToStore(SearchResponse searchResponse) {

		Pagination<Store> page = new Pagination<>();

		if (searchResponse.getHits() == null
				|| searchResponse.getHits().getTotalHits() == 0L) {
			return page;
		}

		List<Store> stores = new ArrayList<>();
		Store store = null;

		for (SearchHit hit : searchResponse.getHits()) {

			ObjectMapper mapper = new ObjectMapper();
			try {
				store = mapper.readValue(hit.sourceAsString(), Store.class);
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

			stores.add(store);
		}

		page.setTotalResults(Long
				.valueOf(searchResponse.getHits().getTotalHits()).intValue());
		page.setPagedData(stores);

		return page;
	}

	private SearchRequestBuilder getSearchRequest(StoreGetRequest criteria) {

		List<String> orderByList = new ArrayList<>();

		if (criteria.getSortBy() != null && !criteria.getSortBy().isEmpty()) {

			validateSortByOrder(criteria.getSortBy());
			validateEntityFieldName(criteria.getSortBy(), StoreEntity.class);
			orderByList = prepareOrderBy(criteria.getSortBy());

		}

		final BoolQueryBuilder boolQueryBuilder = boolQueryBuilderForStore(
				criteria);
		SearchRequestBuilder searchRequestBuilder = esClient
				.prepareSearch("stores");

		if (!orderByList.isEmpty()) {
			for (String orderBy : orderByList) {
				searchRequestBuilder = searchRequestBuilder.addSort(
						orderBy.split(" ")[0],
						orderBy.split(" ")[1].equalsIgnoreCase("asc")
								? SortOrder.ASC : SortOrder.DESC);
			}
		}

		searchRequestBuilder.setQuery(boolQueryBuilder);

		return searchRequestBuilder;

	}

	public BoolQueryBuilder boolQueryBuilderForStore(
			StoreGetRequest storeGetRequest) {

		BoolQueryBuilder boolQueryBuilder = boolQuery();

		if (null != storeGetRequest.getId())
			elasticSearchUtils.in(storeGetRequest.getId(), "id",
					boolQueryBuilder);
		if (null != storeGetRequest.getCode())
			elasticSearchUtils.in(storeGetRequest.getCode(), "code",
					boolQueryBuilder);
		if (null != storeGetRequest.getActive())
			elasticSearchUtils.add(storeGetRequest.getActive(), "active",
					boolQueryBuilder);
		if (null != storeGetRequest.getName())
			elasticSearchUtils.add(storeGetRequest.getName(), "name",
					boolQueryBuilder);
		if (null != storeGetRequest.getDescription())
			elasticSearchUtils.add(storeGetRequest.getDescription(),
					"description", boolQueryBuilder);
		if (null != storeGetRequest.getBillingAddress())
			elasticSearchUtils.add(storeGetRequest.getBillingAddress(),
					"billingAddress", boolQueryBuilder);
		if (null != storeGetRequest.getDeliveryAddress())
			elasticSearchUtils.add(storeGetRequest.getDeliveryAddress(),
					"deliveryAddress", boolQueryBuilder);
		if (null != storeGetRequest.getDepartment())
			elasticSearchUtils.add(storeGetRequest.getDepartment(),
					"department.code", boolQueryBuilder);
		if (null != storeGetRequest.getContactNo1())
			elasticSearchUtils.add(storeGetRequest.getContactNo1(),
					"contactNo1", boolQueryBuilder);
		if (null != storeGetRequest.getContactNo2())
			elasticSearchUtils.add(storeGetRequest.getContactNo2(),
					"contactNo2", boolQueryBuilder);
		if (null != storeGetRequest.getEmail())
			elasticSearchUtils.add(storeGetRequest.getEmail(), "email",
					boolQueryBuilder);
		if (null != storeGetRequest.getStoreInCharge())
			elasticSearchUtils.add(storeGetRequest.getStoreInCharge(),
					"storeInCharge.code", boolQueryBuilder);
		if (null != storeGetRequest.getIsCentralStore())
			elasticSearchUtils.add(storeGetRequest.getIsCentralStore(),
					"isCentralStore", boolQueryBuilder);
		if (!isEmpty(storeGetRequest.getTenantId()))
			elasticSearchUtils.add(storeGetRequest.getTenantId(),
					"auditDetails.tenantId", boolQueryBuilder);

		return boolQueryBuilder;

	}

	private void logRequest(SearchRequestBuilder searchRequestBuilder) {
		if (isESRequestLoggingEnabled) {
			log.info(removeNewLines(searchRequestBuilder.toString()));
		}
	}

	private void logResponse(SearchResponse searchResponse) {
		if (isESRequestLoggingEnabled) {
			log.info(removeNewLines(searchResponse.toString()));
		}
	}

	private String removeNewLines(String string) {
		if (string == null) {
			return EMPTY_STRING;
		}
		return string.replaceAll(NEW_LINE, EMPTY_STRING);
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

			if (s.contains(" ") && (s.toLowerCase().trim().endsWith("asc")
					|| s.toLowerCase().trim().endsWith("desc"))) {
				orderByList.add(s.trim());
			} else {

				orderByList.add(s.trim() + " asc");
			}

		}

		return orderByList;
	}

}
