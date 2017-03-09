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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.egov.eis.model.Assignment;
import org.egov.eis.model.EmployeeInfo;
import org.egov.eis.model.HODDepartment;
import org.egov.eis.model.enums.EmployeeStatus;
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
		Map<Long, EmpInfo> empInfoMap = new LinkedHashMap<>();

		while (rs.next()) {
			Long employeeId = rs.getLong("e_id");

			EmpInfo empInfo = empInfoMap.get(employeeId);

			// populate empInfo fields from result set
			if (empInfo == null) {
				empInfo = new EmpInfo();
				empInfo.setId(rs.getLong("e_id"));
				empInfo.setCode(rs.getString("e_code"));
				empInfo.setEmployeeStatus(EmployeeStatus.fromValue(rs.getString("e_employeeStatus")));
				empInfo.setEmployeeType(rs.getLong("e_employeeTypeId"));
				empInfo.setBank(rs.getLong("e_bankId"));
				empInfo.setBankBranch(rs.getLong("e_bankBranchId"));
				empInfo.setBankAccount(rs.getString("e_bankAccount"));
				empInfo.setDocuments(rs.getString("e_documents"));
				empInfo.setTenantId(rs.getString("e_tenantId"));
				empInfoMap.put(employeeId, empInfo);
			}

			Map<Long, AssignmentInfo> assignmentInfoMap = empInfo.getAssignments();

			Long assignmentId = rs.getLong("a_id");
			AssignmentInfo assignmentInfo = assignmentInfoMap.get(assignmentId);

			// populate assignmentInfo fields from result set
			if (assignmentInfo == null) {
				assignmentInfo = new AssignmentInfo();
				assignmentInfo.setId(rs.getLong("a_id"));
				assignmentInfo.setPosition(rs.getLong("a_positionId"));
				assignmentInfo.setFund(rs.getLong("a_fundId"));
				assignmentInfo.setFunctionary(rs.getLong("a_functionaryId"));
				assignmentInfo.setFunction(rs.getLong("a_functionId"));
				assignmentInfo.setDesignation(rs.getLong("a_designationId"));
				assignmentInfo.setDepartment(rs.getLong("a_departmentId"));
				assignmentInfo.setIsPrimary(rs.getBoolean("a_isPrimary"));
				assignmentInfo.setFromDate(rs.getDate("a_fromDate"));
				assignmentInfo.setToDate(rs.getDate("a_toDate"));
				assignmentInfo.setGrade(rs.getLong("a_gradeId"));
				assignmentInfo.setGovtOrderNumber(rs.getString("a_govtOrderNumber"));
				assignmentInfo.setDocuments(rs.getString("a_documents"));
				assignmentInfo.setCreatedBy(rs.getLong("a_createdBy"));
				assignmentInfo.setCreatedDate(rs.getDate("a_createdDate"));
				assignmentInfo.setLastModifiedBy(rs.getLong("a_lastModifiedBy"));
				assignmentInfo.setLastModifiedDate(rs.getDate("a_lastModifiedDate"));

				assignmentInfoMap.put(assignmentId, assignmentInfo);
			}

			Map<Long, HODDepartment> hodDeptMap = assignmentInfo.getHodDeptMap();
			Long hodId = rs.getLong("hod_id");

			if (rs.getLong("hod_id") != 0) {
				HODDepartment hodDepartment = hodDeptMap.get(hodId);
				if (hodDepartment == null) {
					hodDepartment = new HODDepartment();
					hodDepartment.setId(rs.getLong("hod_id"));
					hodDepartment.setDepartment(rs.getLong("hod_departmentId"));
					hodDeptMap.put(hodId, hodDepartment);
				}
			}

			List<Long> jurisdictionIds = empInfo.getJurisdictionIds();
			Long jurisdictionId = rs.getLong("ej_jurisdictionId");

			if (jurisdictionId != 0 && !jurisdictionIds.contains(jurisdictionId))
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
					.employeeStatus(empInfo.getEmployeeStatus()).employeeType(empInfo.getEmployeeType())
					.bank(empInfo.getBank()).bankBranch(empInfo.getBankBranch()).bankAccount(empInfo.getBankAccount())
					.documents(empInfo.getDocuments()).tenantId(empInfo.getTenantId()).build();

			List<Assignment> assignmentList = new ArrayList<>();
			for (Map.Entry<Long, AssignmentInfo> assignmentInfoEntry : empInfo.getAssignments().entrySet()) {
				AssignmentInfo assignmentInfo = assignmentInfoEntry.getValue();

				Assignment assignment = Assignment.builder().id(assignmentInfo.getId())
						.position(assignmentInfo.getPosition()).fund(assignmentInfo.getFund())
						.functionary(assignmentInfo.getFunctionary()).function(assignmentInfo.getFunction())
						.department(assignmentInfo.getDepartment()).designation(assignmentInfo.getDesignation())
						.isPrimary(assignmentInfo.getIsPrimary()).fromDate(assignmentInfo.getFromDate())
						.toDate(assignmentInfo.getToDate()).grade(assignmentInfo.getGrade())
						.govtOrderNumber(assignmentInfo.getGovtOrderNumber()).documents(assignmentInfo.getDocuments())
						.createdBy(assignmentInfo.getCreatedBy()).createdDate(assignmentInfo.getCreatedDate())
						.lastModifiedBy(assignmentInfo.getLastModifiedBy())
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
		private EmployeeStatus employeeStatus;
		private Long employeeType;
		// Key is assignmentId in the assignments map
		private Map<Long, AssignmentInfo> assignments = new LinkedHashMap<>();
		private List<Long> jurisdictionIds = new ArrayList<>();
		private Long bank;
		private Long bankBranch;
		private String bankAccount;
		private String documents;
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
		private Long department;
		private Long designation;
		private Boolean isPrimary;
		private Date fromDate;
		private Date toDate;
		private Long grade;
		private String govtOrderNumber;
		private String documents;
		private Long createdBy;
		private Date createdDate;
		private Long lastModifiedBy;
		private Date lastModifiedDate;
		// Key is id for HODDepartment in the hodDeptMap map
		private Map<Long, HODDepartment> hodDeptMap = new LinkedHashMap<>();
	}
}