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
import org.egov.eis.web.contract.EmployeeGetRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmployeeQueryBuilder {

	private static final Logger logger = LoggerFactory.getLogger(EmployeeQueryBuilder.class);

	@Autowired
	private ApplicationProperties applicationProperties;
	
	private static final String EMPLOYEE_IDS_QUERY = "SELECT distinct e.id as e_id "
			+ " FROM egeis_employee e"
			+ " JOIN egeis_assignment a ON e.id = a.employeeId"
			+ " LEFT JOIN egeis_hodDepartment hod ON a.id = hod.assignmentId AND hod.tenantId = ?"
			+ " LEFT JOIN egeis_employeeJurisdictions ej ON e.id = ej.employeeId AND ej.tenantId = ?";

	private static final String BASE_QUERY = "SELECT e.id as e_id, e.code as e_code, e.employeeStatus as e_employeeStatus,"
			+ " e.employeeTypeId as e_employeeTypeId, e.bankId as e_bankId, e.bankBranchId as e_bankBranchId,"
			+ " e.bankAccount as e_bankAccount, e.documents as e_documents, e.tenantId as e_tenantId,"
			+ " a.id as a_id, a.positionId as a_positionId, a.fundId as a_fundId, a.functionaryId as a_functionaryId,"
			+ " a.functionId as a_functionId, a.designationId as a_designationId, a.departmentId as a_departmentId,"
			+ " a.isPrimary as a_isPrimary, a.fromDate as a_fromDate, a.toDate as a_toDate, a.gradeId as a_gradeId,"
			+ " a.govtOrderNumber as a_govtOrderNumber, a.documents as a_documents, a.createdBy as a_createdBy,"
			+ " a.createdDate as a_createdDate, a.lastModifiedBy as a_lastModifiedBy,"
			+ " a.lastModifiedDate as a_lastModifiedDate, a.employeeId as a_employeeId,"
			+ " hod.id as hod_id, hod.departmentId as hod_departmentId,"
			+ " ej.jurisdictionId as ej_jurisdictionId"
			+ " FROM egeis_employee e"
			+ " JOIN egeis_assignment a ON e.id = a.employeeId"
			+ " LEFT JOIN egeis_hodDepartment hod ON a.id = hod.assignmentId AND hod.tenantId = ?"
			+ " LEFT JOIN egeis_employeeJurisdictions ej ON e.id = ej.employeeId AND ej.tenantId = ?";

	@SuppressWarnings("rawtypes")
	public String getQueryForListOfEmployeeIds(EmployeeGetRequest employeeGetRequest, List preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(EMPLOYEE_IDS_QUERY);

		addWhereClause(selectQuery, preparedStatementValues, employeeGetRequest, null);
		addPagingClause(selectQuery, preparedStatementValues, employeeGetRequest);

		logger.debug("Query : " + selectQuery);
		return selectQuery.toString();
	}

	@SuppressWarnings("rawtypes")
	public String getQuery(EmployeeGetRequest employeeGetRequest, List preparedStatementValues, List<Long> empIds) {
		StringBuilder selectQuery = new StringBuilder(BASE_QUERY);

		addWhereClause(selectQuery, preparedStatementValues, employeeGetRequest, empIds);
		addOrderByClause(selectQuery, employeeGetRequest);

		logger.debug("Query : " + selectQuery);
		return selectQuery.toString();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addWhereClause(StringBuilder selectQuery, List preparedStatementValues,
			EmployeeGetRequest employeeGetRequest, List<Long> empIds) {

		// Pushing 2 tenantIds for hod.tenantId & ej.tenantId
		preparedStatementValues.add(employeeGetRequest.getTenantId());
		preparedStatementValues.add(employeeGetRequest.getTenantId());

		if (employeeGetRequest.getId() == null && employeeGetRequest.getCode() == null
				&& employeeGetRequest.getDepartmentId() == null && employeeGetRequest.getIsPrimary() == null
				&& employeeGetRequest.getDesignationId() == null && employeeGetRequest.getAsOnDate() == null
				&& employeeGetRequest.getTenantId() == null)
			return;

		selectQuery.append(" WHERE");
		boolean isAppendAndClause = false;

		if (employeeGetRequest.getTenantId() != null) {
			isAppendAndClause = true;
			selectQuery.append(" e.tenantId = ?");
			preparedStatementValues.add(employeeGetRequest.getTenantId());
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" a.tenantId = ?");
			preparedStatementValues.add(employeeGetRequest.getTenantId());
		}

		if(empIds != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" e.id IN " + getIdQuery(empIds));
		}

		if (employeeGetRequest.getId() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" e.id IN " + getIdQuery(employeeGetRequest.getId()));
		}

		if (employeeGetRequest.getCode() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" e.code = ?");
			preparedStatementValues.add(employeeGetRequest.getCode());
		}

		if (employeeGetRequest.getDepartmentId() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" a.departmentId = ?");
			preparedStatementValues.add(employeeGetRequest.getDepartmentId());
		}

		if (employeeGetRequest.getDesignationId() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" a.designationId = ?");
			preparedStatementValues.add(employeeGetRequest.getDesignationId());
		}

		if (employeeGetRequest.getAsOnDate() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" ? BETWEEN a.fromDate AND a.toDate");
			preparedStatementValues.add(employeeGetRequest.getAsOnDate());
		}

		if (employeeGetRequest.getIsPrimary() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" a.isPrimary = ?");
			preparedStatementValues.add(employeeGetRequest.getIsPrimary());
		}
	}

	private void addOrderByClause(StringBuilder selectQuery, EmployeeGetRequest employeeGetRequest) {
		String sortBy = (employeeGetRequest.getSortBy() == null ? "e.id" : employeeGetRequest.getSortBy());
		String sortOrder = (employeeGetRequest.getSortOrder() == null ? "ASC" : employeeGetRequest.getSortOrder());
		selectQuery.append(" ORDER BY " + sortBy + " " + sortOrder);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addPagingClause(StringBuilder selectQuery, List preparedStatementValues,
			EmployeeGetRequest employeeGetRequest) {
		// handle limit(also called pageSize) here
		selectQuery.append(" LIMIT ?");
		long pageSize = Integer.parseInt(applicationProperties.empSearchPageSizeDefault());
		if (employeeGetRequest.getPageSize() != null)
			pageSize = employeeGetRequest.getPageSize();
		preparedStatementValues.add(pageSize); // Set limit to pageSize

		// handle offset here
		selectQuery.append(" OFFSET ?");
		int pageNumber = 0; // Default pageNo is zero meaning first page
		if (employeeGetRequest.getPageNumber() != null)
			pageNumber = employeeGetRequest.getPageNumber() - 1;
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


	// FIXME : Optimize - Add Question Marks instead of hard-coding the values
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
