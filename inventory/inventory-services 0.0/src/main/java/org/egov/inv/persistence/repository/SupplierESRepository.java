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

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.egov.inv.domain.model.SupplierGetRequest;
import org.egov.inv.domain.service.ElasticSearchUtils;
import org.egov.inv.persistence.entity.SupplierEntity;
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
import static org.elasticsearch.common.Strings.isEmpty;

import io.swagger.model.Pagination;
import io.swagger.model.Supplier;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SupplierESRepository extends ESRepository {

	private static final String TRUE = "true";

	private static final String EMPTY_STRING = "";
	private static final String NEW_LINE = "\n";

	private ElasticSearchUtils elasticSearchUtils;

	private TransportClient esClient;

	private boolean isESRequestLoggingEnabled;

	public SupplierESRepository(TransportClient esClient,
			ElasticSearchUtils elasticSearchUtils,
			@Value("${es.log.request}") String isESRequestLoggingEnabled) {
		this.esClient = esClient;
		this.elasticSearchUtils = elasticSearchUtils;
		this.isESRequestLoggingEnabled = TRUE
				.equalsIgnoreCase(isESRequestLoggingEnabled);
	}

	public Pagination<Supplier> search(SupplierGetRequest supplierGetRequest) {
		final SearchRequestBuilder searchRequestBuilder = getSearchRequest(
				supplierGetRequest);
		final SearchResponse searchResponse = searchRequestBuilder.execute()
				.actionGet();
		logResponse(searchResponse);

		return mapToSupplier(searchResponse);
	}

	private Pagination<Supplier> mapToSupplier(SearchResponse searchResponse) {

		Pagination<Supplier> page = new Pagination<>();

		if (searchResponse.getHits() == null
				|| searchResponse.getHits().getTotalHits() == 0L) {
			return page;
		}

		List<Supplier> suppliers = new ArrayList<>();
		Supplier supplier = null;

		for (SearchHit hit : searchResponse.getHits()) {

			ObjectMapper mapper = new ObjectMapper();
			try {
				supplier = mapper.readValue(hit.sourceAsString(),
						Supplier.class);
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

			suppliers.add(supplier);
		}

		page.setTotalResults(Long
				.valueOf(searchResponse.getHits().getTotalHits()).intValue());
		page.setPagedData(suppliers);

		return page;
	}

	private SearchRequestBuilder getSearchRequest(SupplierGetRequest criteria) {

		List<String> orderByList = new ArrayList<>();

		if (criteria.getSortBy() != null && !criteria.getSortBy().isEmpty()) {

			validateSortByOrder(criteria.getSortBy());
			validateEntityFieldName(criteria.getSortBy(), SupplierEntity.class);
			orderByList = prepareOrderBy(criteria.getSortBy());

		}

		final BoolQueryBuilder boolQueryBuilder = boolQueryBuilderForSupplier(
				criteria);
		SearchRequestBuilder searchRequestBuilder = esClient
				.prepareSearch("suppliers");

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

	public BoolQueryBuilder boolQueryBuilderForSupplier(
			SupplierGetRequest supplierGetRequest) {

		BoolQueryBuilder boolQueryBuilder = boolQuery();

		if (null != supplierGetRequest.getId())
			elasticSearchUtils.in(supplierGetRequest.getId(), "id",
					boolQueryBuilder);
		if (null != supplierGetRequest.getCode())
			elasticSearchUtils.in(supplierGetRequest.getCode(), "code",
					boolQueryBuilder);
		if (null != supplierGetRequest.getActive())
			elasticSearchUtils.add(supplierGetRequest.getActive(), "active",
					boolQueryBuilder);
		if (null != supplierGetRequest.getName())
			elasticSearchUtils.add(supplierGetRequest.getName(), "name",
					boolQueryBuilder);
		if (null != supplierGetRequest.getType())
			elasticSearchUtils.add(supplierGetRequest.getType(), "type",
					boolQueryBuilder);
		if (null != supplierGetRequest.getStatus())
			elasticSearchUtils.add(supplierGetRequest.getStatus(), "status",
					boolQueryBuilder);
		if (null != supplierGetRequest.getInActiveDate())
			elasticSearchUtils.add(supplierGetRequest.getInActiveDate(),
					"inActiveDate", boolQueryBuilder);
		if (null != supplierGetRequest.getContactNo())
			elasticSearchUtils.add(supplierGetRequest.getContactNo(),
					"contactNo", boolQueryBuilder);
		if (null != supplierGetRequest.getFaxNo())
			elasticSearchUtils.add(supplierGetRequest.getFaxNo(), "faxNo",
					boolQueryBuilder);
		if (null != supplierGetRequest.getWebsite())
			elasticSearchUtils.add(supplierGetRequest.getWebsite(), "website",
					boolQueryBuilder);
		if (null != supplierGetRequest.getEmail())
			elasticSearchUtils.add(supplierGetRequest.getEmail(), "email",
					boolQueryBuilder);
		if (null != supplierGetRequest.getPanNo())
			elasticSearchUtils.add(supplierGetRequest.getPanNo(), "panNo",
					boolQueryBuilder);
		if (null != supplierGetRequest.getTinNo())
			elasticSearchUtils.add(supplierGetRequest.getTinNo(), "tinNo",
					boolQueryBuilder);
		if (null != supplierGetRequest.getCstNo())
			elasticSearchUtils.add(supplierGetRequest.getCstNo(), "cstNo",
					boolQueryBuilder);
		if (null != supplierGetRequest.getVatNo())
			elasticSearchUtils.add(supplierGetRequest.getVatNo(), "vatNo",
					boolQueryBuilder);
		if (null != supplierGetRequest.getBankCode())
			elasticSearchUtils.add(supplierGetRequest.getBankCode(), "bankCode",
					boolQueryBuilder);
		if (null != supplierGetRequest.getBankBranch())
			elasticSearchUtils.add(supplierGetRequest.getBankBranch(),
					"bankBranch", boolQueryBuilder);
		if (null != supplierGetRequest.getGstNo())
			elasticSearchUtils.add(supplierGetRequest.getGstNo(), "gstNo",
					boolQueryBuilder);
		if (null != supplierGetRequest.getContactPerson())
			elasticSearchUtils.add(supplierGetRequest.getContactPerson(),
					"contactPerson", boolQueryBuilder);
		if (null != supplierGetRequest.getContactPersonNo())
			elasticSearchUtils.add(supplierGetRequest.getContactPersonNo(),
					"contactPersonNo", boolQueryBuilder);
		if (null != supplierGetRequest.getBankAccNo())
			elasticSearchUtils.add(supplierGetRequest.getBankAccNo(), "acctNo",
					boolQueryBuilder);
		if (null != supplierGetRequest.getBankIfsc())
			elasticSearchUtils.add(supplierGetRequest.getBankIfsc(), "ifsc",
					boolQueryBuilder);
		if (!isEmpty(supplierGetRequest.getTenantId()))
			elasticSearchUtils.add(supplierGetRequest.getTenantId(),
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
