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
import java.util.Date;
import java.util.List;

import org.egov.eis.model.Employee;
import org.egov.eis.model.EmployeeInfo;
import org.egov.eis.repository.builder.EmployeeQueryBuilder;
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
	public void save(EmployeeRequest employeeRequest) {
		Employee employee = employeeRequest.getEmployee();
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
				employee.getTenantId()
		};

		jdbcTemplate.update(INSERT_EMPLOYEE_QUERY, obj);
	}

	public void populateIds(EmployeeRequest employeeRequest) {
		Employee employee = employeeRequest.getEmployee();

		employee.getAssignments().forEach((assignment) -> {
			assignment.setId(jdbcTemplate.queryForObject("SELECT nextval('seq_egeis_assignment')", Long.class));
			assignment.setTenantId(employee.getTenantId());
			assignment.setCreatedBy(Long.parseLong((employeeRequest.getRequestInfo().getRequesterId())));
			assignment.setCreatedDate(new Date());
			assignment.setLastModifiedBy((Long.parseLong((employeeRequest.getRequestInfo().getRequesterId()))));
			assignment.setLastModifiedDate((new Date()));
		});
		employee.getAssignments().forEach((assignment) -> {
			assignment.getHod().forEach((hod) -> {
				hod.setId(jdbcTemplate.queryForObject("SELECT nextval('seq_egeis_hoddepartment')", Long.class));
				hod.setTenantId(employee.getTenantId());
			});
		});
		employee.getEducation().forEach((eduction) -> {
			eduction.setId(jdbcTemplate.queryForObject("SELECT nextval('seq_egeis_educationalqualification')", Long.class));
			eduction.setTenantId(employee.getTenantId());
			eduction.setCreatedBy(Long.parseLong((employeeRequest.getRequestInfo().getRequesterId())));
			eduction.setCreatedDate(new Date());
			eduction.setLastModifiedBy((Long.parseLong((employeeRequest.getRequestInfo().getRequesterId()))));
			eduction.setLastModifiedDate((new Date()));
		});
		employee.getTest().forEach((test) -> {
			test.setId(jdbcTemplate.queryForObject("SELECT nextval('seq_egeis_departmentaltest')", Long.class));
			test.setTenantId(employee.getTenantId());
			test.setCreatedBy(Long.parseLong((employeeRequest.getRequestInfo().getRequesterId())));
			test.setCreatedDate(new Date());
			test.setLastModifiedBy((Long.parseLong((employeeRequest.getRequestInfo().getRequesterId()))));
			test.setLastModifiedDate((new Date()));
		});
		employee.getProbation().forEach((probation) -> {
			probation.setId(jdbcTemplate.queryForObject("SELECT nextval('seq_egeis_probation')", Long.class));
			probation.setTenantId(employee.getTenantId());
			probation.setCreatedBy(Long.parseLong((employeeRequest.getRequestInfo().getRequesterId())));
			probation.setCreatedDate(new Date());
			probation.setLastModifiedBy((Long.parseLong((employeeRequest.getRequestInfo().getRequesterId()))));
			probation.setLastModifiedDate((new Date()));
		});
		employee.getRegularisation().forEach((regularisation) -> {
			regularisation.setId(jdbcTemplate.queryForObject("SELECT nextval('seq_egeis_regularisation')", Long.class));
			regularisation.setTenantId(employee.getTenantId());
			regularisation.setCreatedBy(Long.parseLong((employeeRequest.getRequestInfo().getRequesterId())));
			regularisation.setCreatedDate(new Date());
			regularisation.setLastModifiedBy((Long.parseLong((employeeRequest.getRequestInfo().getRequesterId()))));
			regularisation.setLastModifiedDate((new Date()));
		});
		employee.getServiceHistory().forEach((serviceHistory) -> {
			serviceHistory.setId(jdbcTemplate.queryForObject("SELECT nextval('seq_egeis_servicehistory')", Long.class));
			serviceHistory.setTenantId(employee.getTenantId());
			serviceHistory.setCreatedBy(Long.parseLong((employeeRequest.getRequestInfo().getRequesterId())));
			serviceHistory.setCreatedDate(new Date());
			serviceHistory.setLastModifiedBy((Long.parseLong((employeeRequest.getRequestInfo().getRequesterId()))));
			serviceHistory.setLastModifiedDate((new Date()));
		});
		employee.getTechnical().forEach((technical) -> {
			technical.setId(jdbcTemplate.queryForObject("SELECT nextval('seq_egeis_technicalqualification')", Long.class));
			technical.setTenantId(employee.getTenantId());
			technical.setCreatedBy(Long.parseLong((employeeRequest.getRequestInfo().getRequesterId())));
			technical.setCreatedDate(new Date());
			technical.setLastModifiedBy((Long.parseLong((employeeRequest.getRequestInfo().getRequesterId()))));
			technical.setLastModifiedDate((new Date()));
		});
	}
}