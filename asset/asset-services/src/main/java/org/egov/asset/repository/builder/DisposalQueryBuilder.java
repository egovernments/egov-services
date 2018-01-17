/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
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
 *
 */

package org.egov.asset.repository.builder;

import java.util.List;

import org.egov.asset.config.ApplicationProperties;
import org.egov.asset.model.DisposalCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class DisposalQueryBuilder {
	
	@Autowired
	private ApplicationProperties applicationProperties;

	public static final String INSERT_QUERY = "INSERT into egasset_disposal "
			+ "(id,tenantid,assetid,buyername,buyeraddress,disposalreason,disposaldate,pancardnumber,"
			+ "aadharcardnumber,assetcurrentvalue,salevalue,transactiontype,assetsaleaccount,"
			+ "createdby,createddate,lastmodifiedby,lastmodifieddate,profitlossvoucherreference)"
			+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	public String BASE_QUERY = "SELECT "
			+ "id,tenantid,assetid,buyername,buyeraddress,disposalreason,disposaldate,pancardnumber,"
			+ "aadharcardnumber,assetcurrentvalue,salevalue,transactiontype,assetsaleaccount,"
			+ "createdby,createddate,lastmodifiedby,lastmodifieddate,profitlossvoucherreference FROM"
			+ " egasset_disposal as disposal";

	@SuppressWarnings("rawtypes")
	public String getQuery(final DisposalCriteria disposalCriteria, final List preparedStatementValues) {
		final StringBuilder selectQuery = new StringBuilder(BASE_QUERY);
		log.info("get query");
		addWhereClause(selectQuery, preparedStatementValues, disposalCriteria);
		addPagingClause(selectQuery, preparedStatementValues, disposalCriteria);
		log.info("Query from asset querybuilde for search : " + selectQuery);
		return selectQuery.toString();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addWhereClause(final StringBuilder selectQuery, final List preparedStatementValues,
			final DisposalCriteria disposalCriteria) {

		System.out.println("revaluationCriteria>>" + disposalCriteria);
		boolean isAppendAndClause = false;

		selectQuery.append(" WHERE");

		if (disposalCriteria.getTenantId() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" disposal.tenantId = ?");
			preparedStatementValues.add(disposalCriteria.getTenantId());
		}

		if (disposalCriteria.getId() == null && disposalCriteria.getAssetId() == null)
			return;

		if (disposalCriteria.getId() != null && !disposalCriteria.getId().isEmpty()) {
			
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" disposal.id IN (" + getIdQuery(disposalCriteria.getId()));
		}
		if (disposalCriteria.getAssetId() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" disposal.assetid IN (" + getIdQuery(disposalCriteria.getAssetId()));
		}
		if(disposalCriteria.getFromDate() != null && disposalCriteria.getToDate() != null){
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" disposal.disposalDate BETWEEN "+disposalCriteria.getFromDate() +" AND "+disposalCriteria.getToDate());
		}

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addPagingClause(final StringBuilder selectQuery, final List preparedStatementValues,
			final DisposalCriteria disposalCriteria) {
		// handle limit(also called pageSize) here

		selectQuery.append(" LIMIT ?");
		long pageSize = Integer.parseInt(applicationProperties.getSearchPageSizeDefault());

		if (disposalCriteria.getSize() != null)
			pageSize = disposalCriteria.getSize();
		preparedStatementValues.add(pageSize); // Set limit to pageSize

		// handle offset here
		selectQuery.append(" OFFSET ?");
		long pageNumber = 0; // Default pageNo is zero meaning first page
		if (disposalCriteria.getOffset() != null)
			pageNumber = disposalCriteria.getOffset() - 1;
		preparedStatementValues.add(pageNumber * pageSize); // Set offset to
															// pageNo * pageSize
	}

	/**
	 *
	 *
	 * This method is always called at the beginning of the method so that and
	 * is prepended before the field's predicate is handled.
	 *
	 * @param appendAndClauseFlag
	 * @param queryString
	 * @return boolean indicates if the next predicate should append an "AND"
	 */
	private boolean addAndClauseIfRequired(final boolean appendAndClauseFlag, final StringBuilder queryString) {
		if (appendAndClauseFlag)
			queryString.append(" AND");
		return true;
	}

	private static String getIdQuery(final List<Long> idList) {
		StringBuilder query = null;
		if (!idList.isEmpty()) {
			query = new StringBuilder(idList.get(0).toString());
			for (int i = 1; i < idList.size(); i++)
				query.append("," + idList.get(i));
		}
		return query.append(")").toString();
	}

}
