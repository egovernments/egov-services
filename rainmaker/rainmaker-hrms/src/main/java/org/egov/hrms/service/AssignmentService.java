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

package org.egov.hrms.service;

import org.egov.hrms.model.Assignment;
import org.egov.hrms.model.Employee;
import org.egov.hrms.model.HODDepartment;
import org.egov.hrms.model.enums.EntityType;
import org.egov.hrms.repository.AssignmentRepository;
import org.egov.hrms.repository.EmployeeDocumentsRepository;
import org.egov.hrms.web.contract.AssignmentGetRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class AssignmentService {

	@Autowired
	private AssignmentRepository assignmentRepository;

	@Autowired
	private EmployeeDocumentsRepository employeeDocumentsRepository;

	public List<Assignment> getAssignments(Long employeeId, AssignmentGetRequest assignmentGetRequest) {
		return assignmentRepository.findForCriteria(employeeId, assignmentGetRequest);
	}

	public void update(Employee employee) {
		List<Assignment> assignments = assignmentRepository.findByEmployeeId(employee.getId(), employee.getTenantId());
		employee.getAssignments().forEach((assignment) -> {
			if (needsInsert(assignment, assignments)) {
				assignmentRepository.insert(assignment, employee.getId());
			} else if (needsUpdate(assignment, assignments)) {
				assignment.setTenantId(employee.getTenantId());
				assignmentRepository.update(assignment);
			}
		});
		deleteAssignmentsInDBThatAreNotInInput(employee.getAssignments(), assignments, employee.getId(),
				employee.getTenantId());
	}

	private List<String> getListOfHodIdsToDelete(List<HODDepartment> hods, List<String> hodsFromDb) {
		List<String> hodsIdsToDelete = new ArrayList<>();
		for (String hodInDb : hodsFromDb) {
			boolean found = false;
			for (HODDepartment hod : hods)
				if (hod.getDepartment().equals(hodInDb)) {
					found = true;
					break;
				}
			if (!found)
				hodsIdsToDelete.add(hodInDb);
		}
		return hodsIdsToDelete;
	}

	private void deleteAssignmentsInDBThatAreNotInInput(List<Assignment> inputAssignments,
			List<Assignment> assignmentsFromDb, Long employeeId, String tenantId) {
		List<Assignment> assignmentsToDelete = getListOfAssignmentsToDelete(inputAssignments, assignmentsFromDb);
		List<Long> assignmentsIdsToDelete = assignmentsToDelete.stream().map(Assignment::getId)
				.collect(Collectors.toList());
		if (!assignmentsIdsToDelete.isEmpty()) {
			employeeDocumentsRepository.deleteForReferenceIds(employeeId, EntityType.ASSIGNMENT, assignmentsIdsToDelete, tenantId);
			assignmentRepository.delete(assignmentsIdsToDelete, employeeId, tenantId);
		}
	}

	private List<Assignment> getListOfAssignmentsToDelete(List<Assignment> inputAssignments,
			List<Assignment> assignmentsFromDb) {
		List<Assignment> assignmentsToDelete = new ArrayList<>();
		for (Assignment assignmentInDb : assignmentsFromDb) {
			boolean found = false;
			if (!isEmpty(inputAssignments)) {
				// if empty, found remains false and the record becomes eligible for deletion.
				for (Assignment inputAssignment : inputAssignments)
					if (inputAssignment.getId().equals(assignmentInDb.getId())) {
						found = true;
						break;
					}
			}
			if (!found)
				assignmentsToDelete.add(assignmentInDb);
		}
		return assignmentsToDelete;
	}

	private boolean needsInsert(Assignment assignment, List<Assignment> assignments) {
		for (Assignment oldAssignment : assignments)
			if (assignment.getId().equals(oldAssignment.getId()))
				return false;
		return true;
	}

	/**
	 * Note: needsUpdate checks if any field has changed by comparing with
	 * contents from db. If yes, then only do an update.
	 */
	private boolean needsUpdate(Assignment assignment, List<Assignment> assignments) {
		for (Assignment oldAssignment : assignments)
			if (assignment.equals(oldAssignment))
				return false;
		return true;
	}
}