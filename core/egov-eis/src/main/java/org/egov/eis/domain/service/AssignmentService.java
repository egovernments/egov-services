/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.eis.domain.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.eis.persistence.entity.Assignment;
import org.egov.eis.persistence.entity.Employee;
import org.egov.eis.persistence.entity.Position;
import org.egov.eis.persistence.entity.Role;
import org.egov.eis.persistence.entity.User;
import org.egov.eis.web.contract.PositionRequest;
import org.egov.eis.persistence.repository.AssignmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AssignmentService {

	private final AssignmentRepository assignmentRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	public AssignmentService(final AssignmentRepository assignmentRepository) {
		this.assignmentRepository = assignmentRepository;
	}

	/**
	 * Gets assignment by id
	 *
	 * @param Id
	 * @return Assignment object
	 */
	public Assignment getAssignmentById(final Long id,final String tenantId) {
		return assignmentRepository.findByIdAndTenantId(id,tenantId);
	}

	/**
	 * Get all assignments for an employee irrespective assignment dates
	 *
	 * @param empId
	 * @return List of assignment objects
	 */
	public List<Assignment> getAllAssignmentsByEmpId(final Long empId) {
		return assignmentRepository.getAllAssignmentsByEmpId(empId);
	}

	/**
	 * Get all active assignments for an employee as of today
	 *
	 * @param empId
	 * @return List of assignment objects
	 */
	public List<Assignment> getAllActiveEmployeeAssignmentsByEmpId(final Long empId) {
		return assignmentRepository.getAllActiveAssignmentsByEmpId(empId);
	}

	/**
	 * Get all assignments for position and given date as given date which is
	 * passed as parameter. Includes both primary and secondary assignments.
	 *
	 * @param posId
	 * @param givenDate
	 * @return List of assignment objects
	 */
	public List<Assignment> getAssignmentsForPosition(final Long posId, final Date givenDate) {
		return assignmentRepository.getAssignmentsForPosition(posId, givenDate);
	}

	/**
	 * Get employee primary assignment as of today
	 *
	 * @param posId
	 * @return Assignment object
	 */
	public Assignment getPrimaryAssignmentForPositon(final Long posId) {
		return assignmentRepository.getPrimaryAssignmentForPosition(posId);
	}

	@Transactional
	public void createAssignment(final Assignment assignment) {
		assignmentRepository.save(assignment);
	}

	@Transactional
	public void updateAssignment(final Assignment assignment) {
		assignmentRepository.save(assignment);
	}

	/**
	 * Get employee primary assignment for a given user
	 *
	 * @param userId
	 * @return Assignment object
	 */
	public Assignment getPrimaryAssignmentForUser(final Long userId) {
		return assignmentRepository.getPrimaryAssignmentForUser(userId);
	}

	/**
	 * Get employee primary assignment by employee id
	 *
	 * @param empId
	 * @return Assignment object
	 */
	public Assignment getPrimaryAssignmentForEmployee(final Long empId, final String tenantId) {
		return assignmentRepository.getPrimaryAssignmentForEmployee(empId, tenantId);
	}

	/**
	 * Get employee primary assignment for a given date
	 *
	 * @param empId
	 * @param toDate
	 * @return Assignment object
	 */
	public Assignment getPrimaryAssignmentForEmployeeByToDate(final Long empId, final Date toDate) {
		return assignmentRepository.getAssignmentByEmpAndDate(empId, toDate);
	}

	/**
	 * Get employee primary assignment for given position id
	 *
	 * @param posId
	 * @return List of assignment objects
	 */
	public List<Assignment> getAssignmentsForPosition(final Long posId) {
		return assignmentRepository.getAssignmentsForPosition(posId);
	}

	/**
	 * Get employee primary assignment for position and given date
	 *
	 * @param posId
	 * @param givenDate
	 * @return Assignment object
	 */
	public Assignment getPrimaryAssignmentForPositionAndDate(final Long posId, final Date givenDate) {
		return assignmentRepository.getPrimaryAssignmentForPositionAndDate(posId, givenDate);
	}

	/**
	 * Get employee primary assignment for a given date range
	 *
	 * @param empId
	 * @param fromDate
	 * @param toDate
	 * @return Assignment object
	 */
	public Assignment getPrimaryAssignmentForGivenRange(final Long empId, final Date fromDate, final Date toDate) {
		return assignmentRepository.getPrimaryAssignmentForGivenRange(empId, fromDate, toDate);
	}

	/**
	 * Get employee primary assignment for given department and designation
	 *
	 * @param departmentId
	 * @param designationId
	 * @param givenDate
	 * @return List of assignment objects if present, else return empty list.
	 */
	public List<Assignment> getPositionsByDepartmentAndDesignationForGivenRange(final Long departmentId,
			final Long designationId, final Date givenDate, final String tenantId) {

		if (departmentId != null && designationId != null)
			return assignmentRepository.getPrimaryAssignmentForDepartmentAndDesignation(departmentId, designationId,
					givenDate, tenantId);
		else if (designationId != null && departmentId == null)
			return assignmentRepository.getPrimaryAssignmentForDesignation(designationId, givenDate, tenantId);
		else if (designationId == null && departmentId != null)
			return assignmentRepository.getPrimaryAssignmentForDepartment(departmentId, givenDate, tenantId);

		return new ArrayList<Assignment>();

	}

	/**
	 * Get employee primary/temporary assignment for given department and
	 * designation
	 *
	 * @param departmentId
	 * @param designationId
	 * @param givenDate
	 * @return List of assignment objects if present, else return empty list.
	 */
	public List<Assignment> getAllPositionsByDepartmentAndDesignationForGivenRange(final Long departmentId,
			final Long designationId, final Date givenDate) {

		if (departmentId != null && designationId != null)
			return assignmentRepository.getAllAssignmentForDepartmentAndDesignation(departmentId, designationId,
					givenDate);
		else if (designationId != null && departmentId == null)
			return assignmentRepository.getAllAssignmentForDesignation(designationId, givenDate);
		else if (designationId == null && departmentId != null)
			return assignmentRepository.getAllAssignmentForDepartment(departmentId, givenDate);
		return new ArrayList<Assignment>();

	}

	public List<Assignment> getAllPositionsByDepartmentAndPositionNameForGivenRange(final Long departmentId,
			final String positionName) {
		return assignmentRepository.getAllAssignmentForDepartmentAndPositionNameLike(departmentId, new Date(),
				positionName);
	}

	/**
	 * Get list of primary assignments for deparment,designation,fromdate and
	 * todate
	 *
	 * @param deptId
	 * @param desigId
	 * @param fromDate
	 * @param toDate
	 * @return List of assignment objects if present
	 */
	public List<Assignment> getAssignmentsByDeptDesigAndDates(final Long deptId, final Long desigId,
			final Date fromDate, final Date toDate) {
		return assignmentRepository.findByDeptDesigAndDates(deptId, desigId, fromDate, toDate);
	}

	/**
	 * Get all assignments for department,designation and givendate
	 *
	 * @param deptId
	 * @param desigId
	 * @param givenDate
	 * @return List of assignment objects if present
	 */
	public List<Assignment> findAllAssignmentsByDeptDesigAndDates(final Long deptId, final Long desigId,
			final Date givenDate) {
		return assignmentRepository.findAllAssignmentsByDeptDesigAndGivenDate(deptId, desigId, givenDate);
	}

	public List<Assignment> findByEmployeeAndGivenDate(final Long empId, final Date givenDate) {
		return assignmentRepository.findByEmployeeAndGivenDate(empId, givenDate);
	}

	public List<Assignment> findPrimaryAssignmentForDesignationName(final String name) {
		return assignmentRepository.findPrimaryAssignmentForDesignationName(name);
	}

	public List<Assignment> getAllActiveAssignments(final Long designationId) {
		return assignmentRepository.getAllActiveAssignments(designationId);
	}

	@Transactional
	public Employee removeDeletedAssignments(final Employee employee, final String removedAssignIds) {
		if (null != removedAssignIds)
			for (final String id : removedAssignIds.split(","))
				employee.getAssignments().remove(assignmentRepository.findOne(Long.valueOf(id)));
		return employee;
	}

	public Set<User> getUsersByDesignations(final String[] designationNames) {
		return assignmentRepository.getUsersByDesignations(designationNames);
	}

	public Set<Role> getRolesForExpiredAssignmentsByEmpId(final Long empId) {
		return assignmentRepository.getRolesForExpiredAssignmentsByEmpId(empId);
	}

	public Set<Role> getRolesForActiveAssignmentsByEmpId(final Long empId) {
		return assignmentRepository.getRolesForActiveAssignmentsByEmpId(empId);
	}

	public List<Assignment> findByDepartmentDesignationsAndGivenDate(final Long deptId, final List<Long> desigIds,
			final Date givenDate) {
		return assignmentRepository.findByDepartmentDesignationsAndGivenDate(deptId, desigIds, givenDate);
	}

	public List<Assignment> getAllAssignmentsByPositionNameForGivenRange(final String positionName) {
		return assignmentRepository.getAllAssignmentForPositionNameLike(new Date(), positionName);
	}

	public List<Position> findPositionsForEmployees(final String name) {
		return assignmentRepository.findEmployeePositions("%" + name + "%");
	}

	public List<Assignment> findAllAssignmentsByHODDeptAndDates(final Long deptId, final Date givenDate) {
		return assignmentRepository.findAllAssignmentsByHODDeptAndGivenDate(deptId, givenDate);
	}

	public List<Assignment> getAllAssignmentForEmployeeNameLike(final String empName) {
		return assignmentRepository.getAllAssignmentForEmployeeNameLike(new Date(), empName);
	}

	public List<Assignment> findAssignmentForDepartmentId(final Long deptId) {
		return assignmentRepository.findByDepartmentId(deptId);
	}

	public List<Assignment> findAssignmentForDesignationId(final Long desigId) {
		return assignmentRepository.findByDesignationId(desigId);
	}

	public List<Assignment> findByDepartmentAndDesignation(final Long deptId, final Long desigId) {
		return assignmentRepository.findByDepartmentIdAndDesignationId(deptId, desigId);
	}

	public List<Assignment> getPrimaryAssignmentForPositionAndDateRange(final Long posId, final Date fromDate,
			final Date toDate) {
		return assignmentRepository.getPrimaryAssignmentForPositionAndDateRange(posId, fromDate, toDate);
	}

	public List<Assignment> getAssignmentByPositionAndUserAsOnDate(final Long posId, final Long userId,
			final Date currDate) {
		return assignmentRepository.findByPositionAndEmployee(posId, userId, currDate);
	}

	/**
	 * Get all active assignments for an employee as of today
	 *
	 * @param empCode
	 * @param asonDate
	 * @return List of assignment objects
	 */
	public List<Assignment> getAllActiveEmployeeAssignmentsByEmpCode(final String code, final Date asOnDate,
			final String tenantId) {
		return assignmentRepository.getAllActiveAssignmentsByEmpCode(code, asOnDate, tenantId);
	}

	public List<Assignment> getPositionsForUser(final PositionRequest positionRequest) {
		List<Assignment> assignments = new ArrayList<Assignment>();
		if (positionRequest.getUserId() != null)
			assignments.add(assignmentRepository.getPrimaryAssignmentForUser(positionRequest.getUserId()));
		return assignments;
	}

	public List<Assignment> getAll(final String tenantId) {
		return assignmentRepository.findAllByTenantId(tenantId);
	}

}