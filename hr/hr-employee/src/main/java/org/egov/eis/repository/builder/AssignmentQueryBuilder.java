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

import java.util.List;

import org.egov.eis.config.ApplicationProperties;
import org.egov.eis.web.contract.AssignmentGetRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AssignmentQueryBuilder {

	private static final Logger logger = LoggerFactory.getLogger(AssignmentQueryBuilder.class);

	@Autowired
	private ApplicationProperties applicationProperties;

	private static final String BASE_QUERY = "SELECT a.id as a_id, a.positionId as a_positionId, a.fundId as a_fundId,"
			+ " a.functionaryId as a_functionaryId, a.functionId as a_functionId, a.designationId as a_designationId,"
			+ " a.departmentId as a_departmentId, a.isPrimary as a_isPrimary, a.fromDate as a_fromDate,"
			+ " a.toDate as a_toDate, a.gradeId as a_gradeId, a.govtOrderNumber as a_govtOrderNumber,"
			+ " a.documents as a_documents, a.createdBy as a_createdBy, a.createdDate as a_createdDate,"
			+ " a.lastModifiedBy as a_lastModifiedBy, a.lastModifiedDate as a_lastModifiedDate,"
			+ " a.tenantId as a_tenantId,"
			+ " hod.id as hod_id, hod.departmentId as hod_departmentId"
			+ " FROM egeis_employee e"
			+ " JOIN egeis_assignment a ON a.employeeId = e.id"
			+ " LEFT JOIN egeis_hodDepartment hod ON a.id = hod.assignmentId AND hod.tenantId = ?";

	@SuppressWarnings("rawtypes")
	public String getQuery(Long employeeId, AssignmentGetRequest assignmentGetRequest, List preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(BASE_QUERY);

		addWhereClause(employeeId, selectQuery, preparedStatementValues, assignmentGetRequest);
		addOrderByClause(selectQuery, assignmentGetRequest);
		addPagingClause(selectQuery, preparedStatementValues, assignmentGetRequest);

		logger.debug("Query : " + selectQuery);
		return selectQuery.toString();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addWhereClause(Long employeeId, StringBuilder selectQuery, List preparedStatementValues,
			AssignmentGetRequest assignmentGetRequest) {

		// Pushing tenantId for hod.tenantId
		preparedStatementValues.add(assignmentGetRequest.getTenantId());
		System.out.println("");

		selectQuery.append(" WHERE e.id = ?");
		preparedStatementValues.add(employeeId);

		if (assignmentGetRequest.getAssignmentId() == null && assignmentGetRequest.getIsPrimary() == null
				&& assignmentGetRequest.getAsOnDate() == null && assignmentGetRequest.getTenantId() == null)
			return;

		boolean isAppendAndClause = true;

		if (assignmentGetRequest.getTenantId() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" a.tenantId = ?");
			preparedStatementValues.add(assignmentGetRequest.getTenantId());
		}

		if (assignmentGetRequest.getAssignmentId() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" a.id IN " + getIdQuery(assignmentGetRequest.getAssignmentId()));
		}

		if (assignmentGetRequest.getIsPrimary() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" a.isPrimary = ?");
			preparedStatementValues.add(assignmentGetRequest.getIsPrimary());
		}

		if (assignmentGetRequest.getAsOnDate() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" ? BETWEEN a.fromDate AND a.toDate");
			preparedStatementValues.add(assignmentGetRequest.getAsOnDate());
		}
	}

	private void addOrderByClause(StringBuilder selectQuery, AssignmentGetRequest assignmentGetRequest) {
		String sortBy = (assignmentGetRequest.getSortBy() == null ? "a.fromDate" : assignmentGetRequest.getSortBy());
		String sortOrder = (assignmentGetRequest.getSortOrder() == null ? "DESC" : assignmentGetRequest.getSortOrder());
		selectQuery.append(" ORDER BY " + sortBy + " " + sortOrder);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addPagingClause(StringBuilder selectQuery, List preparedStatementValues,
			AssignmentGetRequest assignmentGetRequest) {
		// handle limit(also called pageSize) here
		selectQuery.append(" LIMIT ?");
		long pageSize = Integer.parseInt(applicationProperties.empSearchPageSizeDefault());
		if (assignmentGetRequest.getPageSize() != null)
			pageSize = assignmentGetRequest.getPageSize();
		preparedStatementValues.add(pageSize); // Set limit to pageSize

		// handle offset here
		selectQuery.append(" OFFSET ?");
		int pageNumber = 0; // Default pageNo is zero meaning first page
		if (assignmentGetRequest.getPageNumber() != null)
			pageNumber = assignmentGetRequest.getPageNumber() - 1;
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
