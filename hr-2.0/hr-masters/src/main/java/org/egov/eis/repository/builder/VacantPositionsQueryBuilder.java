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
import org.egov.eis.web.contract.VacantPositionsGetRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VacantPositionsQueryBuilder {

	private static final Logger logger = LoggerFactory.getLogger(VacantPositionsQueryBuilder.class);

	@Autowired
	private ApplicationProperties applicationProperties;

	private static final String BASE_QUERY = "SELECT p.id AS p_id, p.name AS p_name,"
			+ " p.isPostOutsourced AS p_isPostOutsourced, p.active AS p_active, p.tenantId AS p_tenantId,"
			+ " depDes.id AS depDes_id, depDes.departmentId as depDes_departmentId,"
			+ " des.id AS des_id, des.name AS des_name, des.code AS des_code,"
			+ " des.description AS des_description, des.chartOfaccounts AS des_chartOfAccounts,"
			+ " des.active AS des_active"
			+ " FROM egeis_position p"
			+ " JOIN egeis_departmentDesignation depDes ON p.deptDesigId = depDes.id AND depDes.tenantId = p.tenantId"
			+ " JOIN egeis_designation des ON depDes.designationid = des.id AND des.tenantId = p.tenantId";

	@SuppressWarnings("rawtypes")
	public String getQuery(VacantPositionsGetRequest vacantPositionsGetRequest, List preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(BASE_QUERY);

		addWhereClause(selectQuery, preparedStatementValues, vacantPositionsGetRequest);
		addOrderByClause(selectQuery, vacantPositionsGetRequest);
		addPagingClause(selectQuery, preparedStatementValues, vacantPositionsGetRequest);

		logger.debug("Query : " + selectQuery);
		return selectQuery.toString();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addWhereClause(StringBuilder selectQuery, List preparedStatementValues,
			VacantPositionsGetRequest vacantPositionsGetRequest) {

		if (vacantPositionsGetRequest.getId() == null && vacantPositionsGetRequest.getName() == null
				&& vacantPositionsGetRequest.getTenantId() == null)
			return;

		selectQuery.append(" WHERE");
		boolean isAppendAndClause = false;

		if (vacantPositionsGetRequest.getTenantId() != null) {
			isAppendAndClause = true;
			selectQuery.append(" p.tenantId = ?");
			preparedStatementValues.add(vacantPositionsGetRequest.getTenantId());
		}

		if (vacantPositionsGetRequest.getId() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" p.id NOT IN " + getIdQuery(vacantPositionsGetRequest.getId()));
		}

		if (vacantPositionsGetRequest.getName() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" p.name = ?");
			preparedStatementValues.add(vacantPositionsGetRequest.getName());
		}

		if (vacantPositionsGetRequest.getDepartmentId() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" depDes.departmentId = ?");
			preparedStatementValues.add(vacantPositionsGetRequest.getDepartmentId());
		}

		if (vacantPositionsGetRequest.getDesignationId() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" depDes.designationId = ?");
			preparedStatementValues.add(vacantPositionsGetRequest.getDesignationId());
		}
	}

	private void addOrderByClause(StringBuilder selectQuery, VacantPositionsGetRequest vacantPositionsGetRequest) {
		String sortBy = (vacantPositionsGetRequest.getSortBy() == null ? "p.name"
				: vacantPositionsGetRequest.getSortBy());
		String sortOrder = (vacantPositionsGetRequest.getSortOrder() == null ? "ASC"
				: vacantPositionsGetRequest.getSortOrder());
		selectQuery.append(" ORDER BY " + sortBy + " " + sortOrder);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addPagingClause(StringBuilder selectQuery, List preparedStatementValues,
			VacantPositionsGetRequest vacantPositionsGetRequest) {
		// handle limit(also called pageSize) here
		selectQuery.append(" LIMIT ?");
		long pageSize = Integer.parseInt(applicationProperties.hrSearchPageSizeDefault());
		if (vacantPositionsGetRequest.getPageSize() != null)
			pageSize = vacantPositionsGetRequest.getPageSize();
		preparedStatementValues.add(pageSize); // Set limit to pageSize

		// handle offset here
		selectQuery.append(" OFFSET ?");
		int pageNumber = 0; // Default pageNo is zero meaning first page
		if (vacantPositionsGetRequest.getPageNumber() != null)
			pageNumber = vacantPositionsGetRequest.getPageNumber() - 1;
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
