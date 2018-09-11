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
import org.egov.eis.web.contract.PositionGetRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class PositionQueryBuilder {

	private static final Logger logger = LoggerFactory.getLogger(PositionQueryBuilder.class);

	@Autowired
	private ApplicationProperties applicationProperties;

	private static final String BASE_QUERY = "SELECT p.id AS p_id, p.name AS p_name,"
			+ " p.isPostOutsourced AS p_isPostOutsourced, p.active AS p_active, p.tenantId AS p_tenantId,"
			+ " depDes.id AS depDes_id, depDes.departmentCode as depDes_departmentCode, depDes.designationCode as depDes_designationCode"
			+ " FROM egeis_position p"
			+ " JOIN egeis_departmentDesignation depDes ON p.deptDesigId = depDes.id AND depDes.tenantId = p.tenantId";
	
	@SuppressWarnings("rawtypes")
	public String getQuery(PositionGetRequest positionGetRequest,  Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(BASE_QUERY);

		addWhereClause(selectQuery, preparedStatementValues, positionGetRequest);
		addOrderByClause(selectQuery, positionGetRequest);
		addPagingClause(selectQuery, preparedStatementValues, positionGetRequest);

		logger.debug("Query : " + selectQuery);
		return selectQuery.toString();
	}

	@SuppressWarnings("rawtypes")
	public String getPaginatedQuery(PositionGetRequest positionGetRequest,  Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(BASE_QUERY);

		addWhereClause(selectQuery, preparedStatementValues, positionGetRequest);
		addOrderByClause(selectQuery, positionGetRequest);

		logger.debug("Query : " + selectQuery);
		return selectQuery.toString();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addWhereClause(StringBuilder selectQuery,  Map<String, Object> preparedStatementValues,
			PositionGetRequest positionGetRequest) {

		if (positionGetRequest.getId() == null && positionGetRequest.getName() == null
				&& positionGetRequest.getActive() == null && positionGetRequest.getDepartmentId() == null
				&& positionGetRequest.getDesignationId() == null && positionGetRequest.getTenantId() == null)
			return;

		selectQuery.append(" WHERE");
		boolean isAppendAndClause = false;

		if (positionGetRequest.getTenantId() != null) {
			isAppendAndClause = true;
			selectQuery.append(" p.tenantId = :tenantId");
			preparedStatementValues.put("tenantId",positionGetRequest.getTenantId());
		}

		if (positionGetRequest.getId() != null && !positionGetRequest.getId().isEmpty()) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" p.id IN " + getIdQuery(positionGetRequest.getId()));
		}

		if (positionGetRequest.getName() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" p.name = :name");
			preparedStatementValues.put("name",positionGetRequest.getName());
		}

		if (positionGetRequest.getDepartmentId() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" depDes.departmentId = :deptId");
			preparedStatementValues.put("deptId",positionGetRequest.getDepartmentId());
		}

		if (positionGetRequest.getDesignationId() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" depDes.designationId = :desigId");
			preparedStatementValues.put("desigId",positionGetRequest.getDesignationId());
		}

		if (positionGetRequest.getActive() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" p.active = :active");
			preparedStatementValues.put("active",positionGetRequest.getActive());
		}
		
		if (positionGetRequest.getDepartmentCode() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" depDes.departmentCode = :deptCode");
			preparedStatementValues.put("deptCode",positionGetRequest.getDepartmentCode());
		}

		if (positionGetRequest.getDesignationCode() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" depDes.designationCode = :desigCode");
			preparedStatementValues.put("desigCode",positionGetRequest.getDesignationCode());
		}
	}

	private void addOrderByClause(StringBuilder selectQuery, PositionGetRequest positionGetRequest) {
		String sortBy = (positionGetRequest.getSortBy() == null ? "p.name"
				: positionGetRequest.getSortBy());
		String sortOrder = (positionGetRequest.getSortOrder() == null ? "ASC"
				: positionGetRequest.getSortOrder());
		selectQuery.append(" ORDER BY " + sortBy + " " + sortOrder);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addPagingClause(StringBuilder selectQuery, Map<String, Object> preparedStatementValues,
			PositionGetRequest positionGetRequest) {
		// handle limit(also called pageSize) here
		selectQuery.append(" LIMIT :pageSize");
		long pageSize = Integer.parseInt(applicationProperties.hrSearchPageSizeDefault());
		if (positionGetRequest.getPageSize() != null)
			pageSize = positionGetRequest.getPageSize();
		preparedStatementValues.put("pageSize", pageSize); // Set limit to pagesize

		// handle offset here
		selectQuery.append(" OFFSET :pageNumber");
		int pageNumber = 0; // Default pageNo is zero meaning first page
		if (positionGetRequest.getPageNumber() != null)
			pageNumber = positionGetRequest.getPageNumber() - 1;
		preparedStatementValues.put("pageNumber",pageNumber * pageSize); // Set offset to pageNo * pageSize
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
