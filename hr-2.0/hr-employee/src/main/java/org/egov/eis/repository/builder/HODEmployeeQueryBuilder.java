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

import static org.springframework.util.ObjectUtils.isEmpty;

import java.util.List;
import java.util.Map;

import org.egov.eis.config.ApplicationProperties;
import org.egov.eis.web.contract.HODEmployeeCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class HODEmployeeQueryBuilder {

	@Autowired
	private ApplicationProperties applicationProperties;

	private static final String EMPLOYEE_IDS_QUERY = "SELECT DISTINCT a.employeeId AS id"
			+ " FROM egeis_assignment a"
			+ " JOIN egeis_hodDepartment hod ON a.id = hod.assignmentId AND hod.tenantId = a.tenantId"
			+ " WHERE a.tenantId = :tenantId";

	/**
	 * FIXME : Added sorting logic for this query as user will most probably sort based on employee criteria.
	 * Find a better solution for this.
	 */
	private static final String BASE_QUERY = "SELECT e.id AS e_id, e.code AS e_code,"
			+ " e.employeeStatus AS e_employeeStatus, e.maritalStatus AS e_maritalStatus, e.dateOfAppointment as e_dateOfAppointment, e.employeeTypeId AS e_employeeTypeId, e.bankId AS e_bankId,"
			+ " e.bankBranchId AS e_bankBranchId, e.dateOfRetirement as e_dateOfRetirement, e.bankAccount AS e_bankAccount, e.ifscCode AS e_ifscCode, e.tenantId AS e_tenantId,"
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
			+ " JOIN egeis_hodDepartment hod ON a.id = hod.assignmentId AND hod.tenantId = e.tenantId"
			+ " LEFT JOIN egeis_employeeJurisdictions ej ON e.id = ej.employeeId AND ej.tenantId = e.tenantId"
			+ " WHERE e.tenantId = :tenantId";

	public String getQueryForListOfHODEmployeeIds(HODEmployeeCriteria hodEmployeeCriteria,
												  Map<String, Object> namedParameters) {
		StringBuilder selectQuery = new StringBuilder(EMPLOYEE_IDS_QUERY);

		addWhereClause(selectQuery, namedParameters, hodEmployeeCriteria, null);
		addPagingClause(selectQuery, namedParameters, hodEmployeeCriteria);

		log.debug("Get List Of HOD Employee Ids Query : " + selectQuery);
		return selectQuery.toString();
	}

	public String getQuery(HODEmployeeCriteria hodEmployeeCriteria, Map<String, Object> namedParameters, List<Long> empIds) {
		StringBuilder selectQuery = new StringBuilder(BASE_QUERY);

		addWhereClause(selectQuery, namedParameters, hodEmployeeCriteria, empIds);
		addOrderByClause(selectQuery, hodEmployeeCriteria);

		log.debug("Get HOD Employees Query : " + selectQuery);
		return selectQuery.toString();
	}

	private void addWhereClause(StringBuilder selectQuery, Map<String, Object> namedParameters,
								HODEmployeeCriteria hodEmployeeCriteria, List<Long> empIds) {

		namedParameters.put("tenantId", hodEmployeeCriteria.getTenantId());

		if (isEmpty(empIds) && isEmpty(hodEmployeeCriteria.getDepartmentId()) && isEmpty(hodEmployeeCriteria.getAsOnDate()))
			return;

		if(!isEmpty(empIds)) {
			selectQuery.append(" AND e.id IN (:ids)");
			namedParameters.put("ids", empIds);
		}

		if (!isEmpty(hodEmployeeCriteria.getDepartmentId())) {
			selectQuery.append(" AND hod.departmentId = :departmentId");
			namedParameters.put("departmentId", hodEmployeeCriteria.getDepartmentId());
		}

		if (!isEmpty(hodEmployeeCriteria.getAsOnDate())) {
			selectQuery.append(" AND :asOnDate BETWEEN a.fromDate AND a.toDate");
			namedParameters.put("asOnDate", hodEmployeeCriteria.getAsOnDate());
		}
	}

	private void addOrderByClause(StringBuilder selectQuery, HODEmployeeCriteria hodEmployeeCriteria) {
		String sortBy = (isEmpty(hodEmployeeCriteria.getSortBy()) ? "e.id" : hodEmployeeCriteria.getSortBy());
		String sortOrder = (isEmpty(hodEmployeeCriteria.getSortOrder()) ? "ASC" : hodEmployeeCriteria.getSortOrder());
		selectQuery.append(" ORDER BY " + sortBy + " " + sortOrder);
	}

	private void addPagingClause(StringBuilder selectQuery, Map<String, Object> preparedStatementValues,
								 HODEmployeeCriteria hodEmployeeCriteria) {
		// handle limit(also called pageSize) here
		selectQuery.append(" LIMIT :pageSize");
		long pageSize = Integer.parseInt(applicationProperties.empSearchPageSizeDefault());
		if (!isEmpty(hodEmployeeCriteria.getPageSize()))
			pageSize = hodEmployeeCriteria.getPageSize();
		preparedStatementValues.put("pageSize", pageSize); // Set limit to pageSize

		// handle offset here
		selectQuery.append(" OFFSET :pageNumber");
		int pageNumber = 0; // Default pageNo is zero meaning first page
		if (!isEmpty(hodEmployeeCriteria.getPageNumber()))
			pageNumber = hodEmployeeCriteria.getPageNumber() - 1;
		preparedStatementValues.put("pageNumber", pageNumber * pageSize); // Set offset to pageNo * pageSize
	}
}