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

package org.egov.eis.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.egov.eis.model.Employee;
import org.egov.eis.model.EmployeeDocument;
import org.egov.eis.model.EmployeeInfo;
import org.egov.eis.model.enums.DocumentReferenceType;
import org.egov.eis.repository.builder.EmployeeQueryBuilder;
import org.egov.eis.repository.rowmapper.EmployeeDocumentsRowMapper;
import org.egov.eis.repository.rowmapper.EmployeeIdsRowMapper;
import org.egov.eis.repository.rowmapper.EmployeeInfoRowMapper;
import org.egov.eis.web.contract.EmployeeGetRequest;
import org.egov.eis.web.contract.EmployeeRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class EmployeeRepository {

	public static final Logger LOGGER = LoggerFactory.getLogger(EmployeeRepository.class);

	public static final String INSERT_EMPLOYEE_QUERY = "INSERT INTO egeis_employee"
			+ " (id, code, dateOfAppointment, dateofjoining, dateofretirement, employeestatus, recruitmentmodeId,"
			+ " recruitmenttypeId, recruitmentquotaId, retirementage, dateofresignation, dateoftermination,"
			+ " employeetypeId, mothertongueId, religionId, communityId, categoryId, physicallydisabled,"
			+ " medicalreportproduced, maritalstatus, passportno, gpfno, bankId, bankbranchId, bankaccount, groupId,"
			+ " placeofbirth, tenantId)" + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private EmployeeIdsRowMapper employeeIdsRowMapper;

	@Autowired
	private EmployeeInfoRowMapper employeeInfoRowMapper;

	@Autowired
	private EmployeeDocumentsRowMapper employeeDocumentsRowMapper;

	@Autowired
	private EmployeeQueryBuilder employeeQueryBuilder;

	@Autowired
	private EmployeeDocumentsRepository documentsRepository;

	@SuppressWarnings("unchecked")
	public List<EmployeeInfo> findForCriteria(EmployeeGetRequest employeeGetRequest) {
		List<Object> preparedStatementValuesForListOfEmployeeIds = new ArrayList<Object>();
		String queryStrForListOfEmployeeIds = employeeQueryBuilder.getQueryForListOfEmployeeIds(employeeGetRequest,
				preparedStatementValuesForListOfEmployeeIds);

		List<Long> listOfIds = jdbcTemplate.query(queryStrForListOfEmployeeIds,
				preparedStatementValuesForListOfEmployeeIds.toArray(), employeeIdsRowMapper);

		if (listOfIds.isEmpty()) {
			return Collections.EMPTY_LIST;
		}

		List<Object> preparedStatementValues = new ArrayList<Object>();
		String queryStr = employeeQueryBuilder.getQuery(employeeGetRequest, preparedStatementValues, listOfIds);

		List<EmployeeInfo> employeesInfo = jdbcTemplate.query(queryStr, preparedStatementValues.toArray(),
				employeeInfoRowMapper);

		return employeesInfo;
	}

	// FIXME : Figure out a better way to do this
	public List<EmployeeDocument> getDocumentsForListOfEmployeeIds(List<Long> employeeIds) {
		String SELECT_EMPLOYEE_DOCUMENTS_QUERY = "SELECT employeeId, document"
				+ " FROM egeis_employeeDocuments where employeeId IN (" + getProcessedIdsString(employeeIds) + ")";

		List<EmployeeDocument> documents = jdbcTemplate.query(SELECT_EMPLOYEE_DOCUMENTS_QUERY,
				employeeDocumentsRowMapper);
		return documents;
	}

	public void save(EmployeeRequest employeeRequest) {
		Employee employee = employeeRequest.getEmployee();
		Object[] obj = new Object[] { employee.getId(), employee.getCode(), employee.getDateOfAppointment(),
				employee.getDateOfJoining(), employee.getDateOfRetirement(), employee.getEmployeeStatus(),
				employee.getRecruitmentMode(), employee.getRecruitmentType(), employee.getRecruitmentQuota(),
				employee.getRetirementAge(), employee.getDateOfResignation(), employee.getDateOfTermination(),
				employee.getEmployeeType(), employee.getMotherTongue(), employee.getReligion(), employee.getCommunity(),
				employee.getCategory(), employee.getPhysicallyDisabled(), employee.getMedicalReportProduced(),
				employee.getMaritalStatus().toString(), employee.getPassportNo(), employee.getGpfNo(),
				employee.getBank(), employee.getBankBranch(), employee.getBankAccount(), employee.getGroup(),
				employee.getPlaceOfBirth(), employee.getTenantId() };

		if (employee.getDocuments() != null && !employee.getDocuments().isEmpty()) {
			documentsRepository.save(employee.getId(), employee.getDocuments(),
					DocumentReferenceType.EMPLOYEE_HEADER.toString(), employee.getId(), employee.getTenantId());
		}

		jdbcTemplate.update(INSERT_EMPLOYEE_QUERY, obj);
	}

	public Long generateSequence(String sequenceName) {
		return jdbcTemplate.queryForObject("SELECT nextval('" + sequenceName + "')", Long.class);
	}

	public Boolean getBooleanForDataIntegrityChecks(String table, String field, Object value) {
		String query = "SELECT exists(SELECT 1 FROM " + table + " WHERE " + field + " IN (" + value + "))";
		return jdbcTemplate.queryForObject(query, Boolean.class);
	}

	private String getProcessedIdsString(List<Long> ids) {
		return ids.toString().replace("[", "").replace("]", "");
	}
}