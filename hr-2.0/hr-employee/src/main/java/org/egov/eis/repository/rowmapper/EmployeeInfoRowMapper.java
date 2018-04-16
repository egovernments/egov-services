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

package org.egov.eis.repository.rowmapper;

import static org.springframework.util.ObjectUtils.isEmpty;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.egov.eis.model.Assignment;
import org.egov.eis.model.EmployeeInfo;
import org.egov.eis.model.HODDepartment;
import org.egov.eis.model.enums.MaritalStatus;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
public class EmployeeInfoRowMapper implements ResultSetExtractor<List<EmployeeInfo>> {

	@Override
	public List<EmployeeInfo> extractData(ResultSet rs) throws SQLException, DataAccessException {

		Map<Long, EmpInfo> empInfoMap = getEmpInfoMap(rs);
		List<EmployeeInfo> employeeInfoList = getEmployeeInfoList(empInfoMap);

		return employeeInfoList;
	}

	/**
	 * Convert flat Result set data into hierarchical/map structure.
	 *
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private Map<Long, EmpInfo> getEmpInfoMap(ResultSet rs) throws SQLException {
		final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		Map<Long, EmpInfo> empInfoMap = new LinkedHashMap<>();

		while (rs.next()) {
			Long employeeId = (Long) rs.getObject("e_id");

			EmpInfo empInfo = empInfoMap.get(employeeId);

			// populate empInfo fields from result set
			if (empInfo == null) {
				empInfo = new EmpInfo();
				empInfo.setId((Long) rs.getObject("e_id"));
				empInfo.setCode(rs.getString("e_code"));
				empInfo.setEmployeeStatus((Long) rs.getObject("e_employeeStatus"));
				MaritalStatus maritalStatus = isEmpty(rs.getString("e_maritalStatus")) ? null
						: MaritalStatus.fromValue(rs.getString("e_maritalStatus"));
				empInfo.setMaritalStatus(maritalStatus);
				try {
					Date date = isEmpty(rs.getDate("e_dateOfAppointment")) ? null
							: sdf.parse(sdf.format(rs.getDate("e_dateOfAppointment")));
					empInfo.setDateOfAppointment(date);
					date = isEmpty(rs.getDate("e_dateOfRetirement")) ? null
							: sdf.parse(sdf.format(rs.getDate("e_dateOfRetirement")));
					empInfo.setDateOfRetirement(date);

				} catch (ParseException e) {
					e.printStackTrace();
					throw new SQLException("Parse exception while parsing date");
				}
				empInfo.setEmployeeType((Long) rs.getObject("e_employeeTypeId"));
				empInfo.setBank((Long) rs.getObject("e_bankId"));
				empInfo.setBankBranch((Long) rs.getObject("e_bankBranchId"));
				empInfo.setBankAccount(rs.getString("e_bankAccount"));
				empInfo.setIfscCode(rs.getString("e_ifscCode"));
				empInfo.setTenantId(rs.getString("e_tenantId"));
				empInfoMap.put(employeeId, empInfo);
			}

			Map<Long, AssignmentInfo> assignmentInfoMap = empInfo.getAssignments();

			Long assignmentId = (Long) rs.getObject("a_id");
			AssignmentInfo assignmentInfo = assignmentInfoMap.get(assignmentId);

			// populate assignmentInfo fields from result set
			if (assignmentInfo == null) {
				assignmentInfo = new AssignmentInfo();
				assignmentInfo.setId((Long) rs.getObject("a_id"));
				assignmentInfo.setPosition((Long) rs.getObject("a_positionId"));
				assignmentInfo.setFund((Long) rs.getObject("a_fundId"));
				assignmentInfo.setFunctionary((Long) rs.getObject("a_functionaryId"));
				assignmentInfo.setFunction((Long) rs.getObject("a_functionId"));
				assignmentInfo.setDesignation((Long) rs.getObject("a_designationId"));
				assignmentInfo.setDepartment(rs.getString("a_departmentId"));
				assignmentInfo.setIsPrimary((Boolean) rs.getObject("a_isPrimary"));
				assignmentInfo.setGrade((Long) rs.getObject("a_gradeId"));
				assignmentInfo.setGovtOrderNumber(rs.getString("a_govtOrderNumber"));
				assignmentInfo.setCreatedBy((Long) rs.getObject("a_createdBy"));
				assignmentInfo.setLastModifiedBy((Long) rs.getObject("a_lastModifiedBy"));
				try {
					Date date = isEmpty(rs.getDate("a_fromDate")) ? null
							: sdf.parse(sdf.format(rs.getDate("a_fromDate")));
					assignmentInfo.setFromDate(date);
					date = isEmpty(rs.getDate("a_toDate")) ? null : sdf.parse(sdf.format(rs.getDate("a_toDate")));
					assignmentInfo.setToDate(date);
					date = isEmpty(rs.getDate("a_createdDate")) ? null
							: sdf.parse(sdf.format(rs.getDate("a_createdDate")));
					assignmentInfo.setCreatedDate(date);
					date = isEmpty(rs.getDate("a_lastModifiedDate")) ? null
							: sdf.parse(sdf.format(rs.getDate("a_lastModifiedDate")));
					assignmentInfo.setLastModifiedDate(date);
				} catch (ParseException e) {
					e.printStackTrace();
					throw new SQLException("Parse exception while parsing date");
				}

				assignmentInfoMap.put(assignmentId, assignmentInfo);
			}

			Map<Long, HODDepartment> hodDeptMap = assignmentInfo.getHodDeptMap();
			Long hodId = (Long) rs.getObject("hod_id");

			if ((Long) rs.getObject("hod_id") != null) {
				HODDepartment hodDepartment = hodDeptMap.get(hodId);
				if (hodDepartment == null) {
					hodDepartment = new HODDepartment();
					hodDepartment.setId((Long) rs.getObject("hod_id"));
					hodDepartment.setDepartment(rs.getString("hod_departmentId"));
					hodDeptMap.put(hodId, hodDepartment);
				}
			}

			List<Long> jurisdictionIds = empInfo.getJurisdictionIds();
			Long jurisdictionId = (Long) rs.getObject("ej_jurisdictionId");

			if (jurisdictionId != null && jurisdictionIds != null && !jurisdictionIds.contains(jurisdictionId))
				jurisdictionIds.add(jurisdictionId);

		}
		return empInfoMap;
	}

	/**
	 * Convert intermediate Map into List of EmployeeInfo
	 *
	 * @param empInfoMap
	 * @return
	 */
	private List<EmployeeInfo> getEmployeeInfoList(Map<Long, EmpInfo> empInfoMap) {
		List<EmployeeInfo> employeeInfoList = new ArrayList<>();
		for (Map.Entry<Long, EmpInfo> empInfoEntry : empInfoMap.entrySet()) {
			EmpInfo empInfo = empInfoEntry.getValue();

			EmployeeInfo employeeInfo = EmployeeInfo.builder().id(empInfo.getId()).code(empInfo.getCode())
					.employeeStatus(empInfo.getEmployeeStatus()).maritalStatus(empInfo.getMaritalStatus())
					.employeeType(empInfo.getEmployeeType()).dateOfAppointment(empInfo.getDateOfAppointment())
					.dateOfRetirement(empInfo.getDateOfRetirement()).bank(empInfo.getBank())
					.bankBranch(empInfo.getBankBranch()).bankAccount(empInfo.getBankAccount())
					.ifscCode(empInfo.getIfscCode()).tenantId(empInfo.getTenantId()).build();

			List<Assignment> assignmentList = new ArrayList<>();
			for (Map.Entry<Long, AssignmentInfo> assignmentInfoEntry : empInfo.getAssignments().entrySet()) {
				AssignmentInfo assignmentInfo = assignmentInfoEntry.getValue();

				Assignment assignment = Assignment.builder().id(assignmentInfo.getId())
						.position(assignmentInfo.getPosition()).fund(assignmentInfo.getFund())
						.functionary(assignmentInfo.getFunctionary()).function(assignmentInfo.getFunction())
						.department(assignmentInfo.getDepartment()).designation(assignmentInfo.getDesignation())
						.isPrimary(assignmentInfo.getIsPrimary()).fromDate(assignmentInfo.getFromDate())
						.toDate(assignmentInfo.getToDate()).grade(assignmentInfo.getGrade())
						.govtOrderNumber(assignmentInfo.getGovtOrderNumber()).createdBy(assignmentInfo.getCreatedBy())
						.createdDate(assignmentInfo.getCreatedDate()).lastModifiedBy(assignmentInfo.getLastModifiedBy())
						.lastModifiedDate(assignmentInfo.getLastModifiedDate()).build();

				List<HODDepartment> hodDepartmentList = new ArrayList<>();
				for (Map.Entry<Long, HODDepartment> hodDepartmentEntry : assignmentInfo.getHodDeptMap().entrySet()) {
					HODDepartment hodDepartment = hodDepartmentEntry.getValue();
					hodDepartmentList.add(hodDepartment);
				}

				assignment.setHod(hodDepartmentList);

				assignmentList.add(assignment);
			}

			employeeInfo.setAssignments(assignmentList);
			employeeInfo.setJurisdictions(empInfo.getJurisdictionIds());

			employeeInfoList.add(employeeInfo);
		}
		return employeeInfoList;
	}

	/**
	 * Intermediate class that holds the flat resultset from database in
	 * object/hierarchical form. Note that the Map assignments makes it easy for
	 * adding unique assignments
	 */
	@Getter
	@Setter
	private class EmpInfo {
		private Long id;
		private String code;
		private Long employeeStatus;
		private Date dateOfAppointment;
		private Date dateOfRetirement;
		private Long employeeType;
		private MaritalStatus maritalStatus;
		// Key is assignmentId in the assignments map
		private Map<Long, AssignmentInfo> assignments = new LinkedHashMap<>();
		private List<Long> jurisdictionIds = new ArrayList<>();
		private Long bank;
		private Long bankBranch;
		private String bankAccount;
		private String ifscCode;
		private String tenantId;
	}

	@Getter
	@Setter
	private class AssignmentInfo {
		private Long id;
		private Long position;
		private Long fund;
		private Long functionary;
		private Long function;
		private String department;
		private Long designation;
		private Boolean isPrimary;
		private Date fromDate;
		private Date toDate;
		private Long grade;
		private String govtOrderNumber;
		private Long createdBy;
		private Date createdDate;
		private Long lastModifiedBy;
		private Date lastModifiedDate;
		// Key is id for HODDepartment in the hodDeptMap map
		private Map<Long, HODDepartment> hodDeptMap = new LinkedHashMap<>();
	}
}