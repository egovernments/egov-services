/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.eis.repository.builder;

import org.egov.eis.config.ApplicationProperties;
import org.egov.eis.web.contract.RecruitmentModeGetRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RecruitmentModeQueryBuilder {

	private static final Logger logger = LoggerFactory.getLogger(RecruitmentModeQueryBuilder.class);

	@Autowired
	private ApplicationProperties applicationProperties;

	private static final String BASE_QUERY = "SELECT id, name, description, tenantId"
			+ " FROM egeis_recruitmentMode";

	@SuppressWarnings("rawtypes")
	public String getQuery(RecruitmentModeGetRequest recruitmentModeGetRequest,
			List preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(BASE_QUERY);

		addWhereClause(selectQuery, preparedStatementValues, recruitmentModeGetRequest);
		addOrderByClause(selectQuery, recruitmentModeGetRequest);
		addPagingClause(selectQuery, preparedStatementValues, recruitmentModeGetRequest);

		logger.debug("Query : " + selectQuery);
		return selectQuery.toString();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addWhereClause(StringBuilder selectQuery, List preparedStatementValues,
			RecruitmentModeGetRequest recruitmentModeGetRequest) {

		if (recruitmentModeGetRequest.getId() == null && recruitmentModeGetRequest.getName() == null
				&& recruitmentModeGetRequest.getTenantId() == null)
			return;

		selectQuery.append(" WHERE");
		boolean isAppendAndClause = false;

		if (recruitmentModeGetRequest.getTenantId() != null) {
			isAppendAndClause = true;
			selectQuery.append(" tenantId = ?");
			preparedStatementValues.add(recruitmentModeGetRequest.getTenantId());
		}

		if (recruitmentModeGetRequest.getId() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" id IN " + getIdQuery(recruitmentModeGetRequest.getId()));
		}

		if (recruitmentModeGetRequest.getName() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" name = ?");
			preparedStatementValues.add(recruitmentModeGetRequest.getName());
		}
	}

	private void addOrderByClause(StringBuilder selectQuery,
			RecruitmentModeGetRequest recruitmentModeGetRequest) {
		String sortBy = (recruitmentModeGetRequest.getSortBy() == null ? "name"
				: recruitmentModeGetRequest.getSortBy());
		String sortOrder = (recruitmentModeGetRequest.getSortOrder() == null ? "ASC"
				: recruitmentModeGetRequest.getSortOrder());
		selectQuery.append(" ORDER BY " + sortBy + " " + sortOrder);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addPagingClause(StringBuilder selectQuery, List preparedStatementValues,
			RecruitmentModeGetRequest recruitmentModeGetRequest) {
		// handle limit(also called pageSize) here
		selectQuery.append(" LIMIT ?");
		long pageSize = Integer.parseInt(applicationProperties.hrSearchPageSizeDefault());
		if (recruitmentModeGetRequest.getPageSize() != null)
			pageSize = recruitmentModeGetRequest.getPageSize();
		preparedStatementValues.add(pageSize); // Set limit to pageSize

		// handle offset here
		selectQuery.append(" OFFSET ?");
		int pageNumber = 0; // Default pageNo is zero meaning first page
		if (recruitmentModeGetRequest.getPageNumber() != null)
			pageNumber = recruitmentModeGetRequest.getPageNumber() - 1;
		preparedStatementValues.add(pageNumber * pageSize); // Set offset to pageNo * pageSize
	}

	/**
	 * This method is always called at the beginning of the method so that and
	 * is prepended before the field's predicate is handled.
	 * 
	 * @param appendAndClauseFlag
	 * @param queryString
	 * @return boolean indicates if the next predicate should append an "AND"
	 */
	private boolean addAndClauseIfRequired(boolean appendAndClauseFlag, StringBuilder queryString) {
		if (appendAndClauseFlag)
			queryString.append(" AND");

		return true;
	}

	private static String getIdQuery(List<Long> idList) {
		StringBuilder query = new StringBuilder("(");
		if (idList.size() >= 1) {
			query.append(idList.get(0).toString());
			for (int i = 1; i < idList.size(); i++) {
				query.append(", " + idList.get(i));
			}
		}
		return query.append(")").toString();
	}
}
