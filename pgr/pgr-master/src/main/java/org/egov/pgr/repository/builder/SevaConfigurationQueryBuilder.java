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

package org.egov.pgr.repository.builder;

import java.util.List;

import org.egov.pgr.config.ApplicationProperties;
import org.egov.pgr.web.contract.SevaConfigurationGetRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SevaConfigurationQueryBuilder {

	private static final Logger logger = LoggerFactory.getLogger(SevaConfigurationQueryBuilder.class);

	@Autowired
	private ApplicationProperties applicationProperties;

	private static final String BASE_QUERY = "SELECT ck.keyName as key, cv.value as value" + " FROM egseva_config ck"
			+ " JOIN egseva_configvalues cv ON ck.id = cv.keyId AND ck.tenantId = cv.tenantId";

	@SuppressWarnings("rawtypes")
	public String getQuery(SevaConfigurationGetRequest sevaConfigurationGetRequest, List preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(BASE_QUERY);

		addWhereClause(selectQuery, preparedStatementValues, sevaConfigurationGetRequest);
		addOrderByClause(selectQuery, sevaConfigurationGetRequest);
		addPagingClause(selectQuery, preparedStatementValues, sevaConfigurationGetRequest);

		logger.debug("Query : " + selectQuery);
		return selectQuery.toString();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addWhereClause(StringBuilder selectQuery, List preparedStatementValues,
			SevaConfigurationGetRequest sevaConfigurationGetRequest) {

		if (sevaConfigurationGetRequest.getId() == null && sevaConfigurationGetRequest.getEffectiveFrom() == null
				&& sevaConfigurationGetRequest.getKeyName() == null && sevaConfigurationGetRequest.getTenantId() == null)
			return;

		selectQuery.append(" WHERE");
		boolean isAppendAndClause = false;

		if (sevaConfigurationGetRequest.getTenantId() != null) {
			isAppendAndClause = true;
			selectQuery.append(" ck.tenantId = ?");
			preparedStatementValues.add(sevaConfigurationGetRequest.getTenantId());
		}

		if (sevaConfigurationGetRequest.getId() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" ck.id IN " + getIdQuery(sevaConfigurationGetRequest.getId()));
		}

		if (sevaConfigurationGetRequest.getKeyName() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" ck.keyName = ?");
			preparedStatementValues.add(sevaConfigurationGetRequest.getKeyName());
		}

		if (sevaConfigurationGetRequest.getEffectiveFrom() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" cv.effectiveFrom = ?");
			preparedStatementValues.add(sevaConfigurationGetRequest.getEffectiveFrom());
		}
	}

	private void addOrderByClause(StringBuilder selectQuery, SevaConfigurationGetRequest sevaConfigurationGetRequest) {
		String sortBy = (sevaConfigurationGetRequest.getSortBy() == null ? "keyName"
				: sevaConfigurationGetRequest.getSortBy());
		String sortOrder = (sevaConfigurationGetRequest.getSortOrder() == null ? "ASC"
				: sevaConfigurationGetRequest.getSortOrder());
		selectQuery.append(" ORDER BY " + sortBy + " " + sortOrder);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addPagingClause(StringBuilder selectQuery, List preparedStatementValues,
			SevaConfigurationGetRequest hrConfigurationGetRequest) {
		// handle limit(also called pageSize) here
		selectQuery.append(" LIMIT ?");
		long pageSize = Integer.parseInt(applicationProperties.sevaSearchPageSizeDefault());
		if (hrConfigurationGetRequest.getPageSize() != null)
			pageSize = hrConfigurationGetRequest.getPageSize();
		preparedStatementValues.add(pageSize); // Set limit to pageSize

		// handle offset here
		selectQuery.append(" OFFSET ?");
		int pageNumber = 0; // Default pageNo is zero meaning first page
		if (hrConfigurationGetRequest.getPageNumber() != null)
			pageNumber = hrConfigurationGetRequest.getPageNumber() - 1;
		preparedStatementValues.add(pageNumber * pageSize); // Set offset to
															// pageNo * pageSize
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
