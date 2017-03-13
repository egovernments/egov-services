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
import org.egov.eis.model.EmployeeInfo;
import org.egov.eis.repository.builder.EmployeeQueryBuilder;
import org.egov.eis.repository.rowmapper.EmployeeIdsRowMapper;
import org.egov.eis.repository.rowmapper.EmployeeInfoRowMapper;
import org.egov.eis.web.contract.EmployeeGetRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class EmployeeRepository {

	public static final Logger LOGGER = LoggerFactory.getLogger(EmployeeRepository.class);

	// FIXME Employee sequence
	public static final String INSERT_EMPLOYEE_QUERY = "INSERT INTO egeis_employee"
			+ " (id, code, dateOfAppointment, dateofjoining, dateofretirement, employeestatus, recruitmentmodeId,"
			+ " recruitmenttypeId, recruitmentquotaId, retirementage, dateofresignation, dateoftermination,"
			+ " employeetypeId, mothertongueId, religionId, communityId, categoryId, physicallydisabled,"
			+ " medicalreportproduced, maritalstatus, passportno, gpfno, bankId, bankbranchId, bankaccount, groupId,"
			+ " placeofbirth, tenantId)"
			+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private EmployeeIdsRowMapper employeeIdsRowMapper;

	@Autowired
	private EmployeeInfoRowMapper employeeInfoRowMapper;

	@Autowired
	private EmployeeQueryBuilder employeeQueryBuilder;

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

	// FIXME put tenantId
	public void save(Employee employee) {
		System.out.println("Received Employee: " + employee);

		Object[] obj = new Object[] {
				employee.getId(),
				employee.getCode(),
				employee.getDateOfAppointment(),
				employee.getDateOfJoining(),
				employee.getDateOfRetirement(),
				employee.getEmployeeStatus(),
				employee.getRecruitmentMode(),
				employee.getRecruitmentType(),
				employee.getRecruitmentQuota(),
				employee.getRetirementAge(),
				employee.getDateOfResignation(),
				employee.getDateOfTermination(),
				employee.getEmployeeType(),
				employee.getMotherTongue(),
				employee.getReligion(),
				employee.getCommunity(),
				employee.getCategory(),
				employee.getPhysicallyDisabled(),
				employee.getMedicalReportProduced(),
				employee.getMaritalStatus().toString(),
				employee.getPassportNo(),
				employee.getGpfNo(),
				employee.getBank(),
				employee.getBankBranch(),
				employee.getBankAccount(),
				employee.getGroup(),
				employee.getPlaceOfBirth(),
				"1"
		};

		jdbcTemplate.update(INSERT_EMPLOYEE_QUERY, obj);
	}
}