/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
 *
 *  The updated version of eGov suite of products AS by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License AS published by
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
 *         reasonable ways AS different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.eis.repository.builder;

import org.egov.eis.config.ApplicationProperties;
import org.egov.eis.web.contract.EmployeeCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EmployeeQueryBuilder {

	private static final Logger logger = LoggerFactory.getLogger(EmployeeQueryBuilder.class);

	@Autowired
	private ApplicationProperties applicationProperties;
	
	private static final String EMPLOYEE_IDS_QUERY = "SELECT distinct e.id AS id "
			+ " FROM egeis_employee e"
			+ " JOIN egeis_assignment a ON e.id = a.employeeId AND a.tenantId = e.tenantId"
			+ " LEFT JOIN egeis_hodDepartment hod ON a.id = hod.assignmentId AND hod.tenantId = e.tenantId"
			+ " LEFT JOIN egeis_employeeJurisdictions ej ON e.id = ej.employeeId AND ej.tenantId = e.tenantId";

	private static final String BASE_QUERY = "SELECT e.id AS e_id, e.code AS e_code,"
			+ " e.employeeStatus AS e_employeeStatus, e.employeeTypeId AS e_employeeTypeId, e.bankId AS e_bankId,"
			+ " e.bankBranchId AS e_bankBranchId, e.bankAccount AS e_bankAccount, e.tenantId AS e_tenantId,"
			+ " a.id AS a_id, a.positionId AS a_positionId, a.fundId AS a_fundId, a.functionaryId AS a_functionaryId,"
			+ " a.functionId AS a_functionId, a.designationId AS a_designationId, a.departmentId AS a_departmentId,"
			+ " a.isPrimary AS a_isPrimary, a.fromDate AS a_fromDate, a.toDate AS a_toDate, a.gradeId AS a_gradeId,"
			+ " a.govtOrderNumber AS a_govtOrderNumber, a.createdBy AS a_createdBy, a.createdDate AS a_createdDate,"
			+ " a.lastModifiedBy AS a_lastModifiedBy, a.lastModifiedDate AS a_lastModifiedDate,"
			+ " a.employeeId AS a_employeeId,"
			+ " hod.id AS hod_id, hod.departmentId AS hod_departmentId,"
			+ " ej.jurisdictionId AS ej_jurisdictionId"
			+ " FROM egeis_employee e"
			+ " JOIN egeis_assignment a ON e.id = a.employeeId AND a.tenantId = e.tenantId"
			+ " LEFT JOIN egeis_hodDepartment hod ON a.id = hod.assignmentId AND hod.tenantId = e.tenantId"
			+ " LEFT JOIN egeis_employeeJurisdictions ej ON e.id = ej.employeeId AND ej.tenantId = e.tenantId";

	@SuppressWarnings("rawtypes")
	public String getQueryForListOfEmployeeIds(EmployeeCriteria employeeCriteria, List preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(EMPLOYEE_IDS_QUERY);

		addWhereClause(selectQuery, preparedStatementValues, employeeCriteria, null);
		addPagingClause(selectQuery, preparedStatementValues, employeeCriteria);

		logger.debug("Query : " + selectQuery);
		return selectQuery.toString();
	}

	@SuppressWarnings("rawtypes")
	public String getQuery(EmployeeCriteria employeeCriteria, List preparedStatementValues, List<Long> empIds) {
		StringBuilder selectQuery = new StringBuilder(BASE_QUERY);

		addWhereClause(selectQuery, preparedStatementValues, employeeCriteria, empIds);
		addOrderByClause(selectQuery, employeeCriteria);

		logger.debug("Query : " + selectQuery);
		return selectQuery.toString();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addWhereClause(StringBuilder selectQuery, List preparedStatementValues,
			EmployeeCriteria employeeCriteria, List<Long> empIds) {

		if (employeeCriteria.getId() == null && employeeCriteria.getCode() == null
				&& employeeCriteria.getDepartmentId() == null && employeeCriteria.getIsPrimary() == null
				&& employeeCriteria.getDesignationId() == null && employeeCriteria.getPositionId() == null
				&& employeeCriteria.getAsOnDate() == null && employeeCriteria.getEmployeeStatus() == null
				&& employeeCriteria.getTenantId() == null)
			return;

		selectQuery.append(" WHERE");
		boolean isAppendAndClause = false;

		if (employeeCriteria.getTenantId() != null) {
			isAppendAndClause = true;
			selectQuery.append(" e.tenantId = ?");
			preparedStatementValues.add(employeeCriteria.getTenantId());
		}

		if(empIds != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" e.id IN " + getIdQuery(empIds));
		} else if (employeeCriteria.getId() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" e.id IN " + getIdQuery(employeeCriteria.getId()));
		}

		if (employeeCriteria.getCode() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" e.code = ?");
			preparedStatementValues.add(employeeCriteria.getCode());
		}

		if (employeeCriteria.getDepartmentId() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" a.departmentId = ?");
			preparedStatementValues.add(employeeCriteria.getDepartmentId());
		}

		if (employeeCriteria.getDesignationId() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" a.designationId = ?");
			preparedStatementValues.add(employeeCriteria.getDesignationId());
		}

		if (employeeCriteria.getPositionId() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" a.positionId = ?");
			preparedStatementValues.add(employeeCriteria.getPositionId());
		}

		if (employeeCriteria.getAsOnDate() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" ? BETWEEN a.fromDate AND a.toDate");
			preparedStatementValues.add(employeeCriteria.getAsOnDate());
		}

		if (employeeCriteria.getIsPrimary() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" a.isPrimary = ?");
			preparedStatementValues.add(employeeCriteria.getIsPrimary());
		}

		if (employeeCriteria.getEmployeeStatus() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" e.employeeStatus IN " + getIdQuery(employeeCriteria.getEmployeeStatus()));
		}
	}

	private void addOrderByClause(StringBuilder selectQuery, EmployeeCriteria employeeCriteria) {
		String sortBy = (employeeCriteria.getSortBy() == null ? "e.id" : employeeCriteria.getSortBy());
		String sortOrder = (employeeCriteria.getSortOrder() == null ? "ASC" : employeeCriteria.getSortOrder());
		selectQuery.append(" ORDER BY " + sortBy + " " + sortOrder);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addPagingClause(StringBuilder selectQuery, List preparedStatementValues,
			EmployeeCriteria employeeCriteria) {
		// handle limit(also called pageSize) here
		selectQuery.append(" LIMIT ?");
		long pageSize = Integer.parseInt(applicationProperties.empSearchPageSizeDefault());
		if (employeeCriteria.getPageSize() != null)
			pageSize = employeeCriteria.getPageSize();
		preparedStatementValues.add(pageSize); // Set limit to pageSize

		// handle offset here
		selectQuery.append(" OFFSET ?");
		int pageNumber = 0; // Default pageNo is zero meaning first page
		if (employeeCriteria.getPageNumber() != null)
			pageNumber = employeeCriteria.getPageNumber() - 1;
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