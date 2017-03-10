package org.egov.eis.domain.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.egov.eis.persistence.entity.Assignment;
import org.egov.eis.persistence.entity.Department;
import org.egov.eis.persistence.entity.Designation;
import org.egov.eis.persistence.repository.AssignmentRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AssignmentServiceTest {

	@Mock
	private AssignmentRepository assignmentRepository;

	@InjectMocks
	private AssignmentService assignmentService;

	@Mock
	private Department department;

	@Mock
	private Designation designation;

	@Test
	public void testShouldReturnAssignmentWhenAssignmentIdIsSpecified() {
		final Assignment expectedAssignment = new Assignment();
		expectedAssignment.setId(1L);
		when(assignmentRepository.findOne(1L)).thenReturn(expectedAssignment);

		final Assignment actualAssignment = assignmentService.getAssignmentById(1L);

		assertEquals(1, actualAssignment.getId().intValue());

	}

	@Test
	public void testShouldReturnAllAssignmentsWhenIdIsNotSpecified() {
		when(assignmentRepository.findAll()).thenReturn(Arrays.asList(new Assignment(), new Assignment()));

		final List<Assignment> actualAssignments = assignmentService.getAll();

		assertEquals(2, actualAssignments.size());
	}

	@Test
	public void test_should_return_assignment_for_employeeId(){
		when(assignmentRepository.getPrimaryAssignmentForEmployee(18L))
				.thenReturn(Assignment.builder().id(2L).build());

		final Assignment actualAssignment = assignmentService.getPrimaryAssignmentForEmployee(18L);

		assertEquals(2,actualAssignment.getId().longValue());
	}

	@Test
	public void test_should_fetch_all_assignments_for_given_deptid_or_desgnid() {
		List<Assignment> actualAssignment = new ArrayList<>();
		final Assignment expectedAssignment = Assignment.builder().id(1L).department(department)
				.designation(designation).build();
		// given both department and designation id
		when(assignmentRepository.getPrimaryAssignmentForDepartmentAndDesignation(any(Long.class), any(Long.class),
				any(Date.class))).thenReturn(Collections.singletonList(expectedAssignment));
		actualAssignment = assignmentService.getPositionsByDepartmentAndDesignationForGivenRange(any(Long.class),
				any(Long.class), any(Date.class));
		assertEquals(1, actualAssignment.size());
		// given only department id
		when(assignmentRepository.getPrimaryAssignmentForDepartment(any(Long.class), any(Date.class)))
				.thenReturn(Collections.singletonList(expectedAssignment));
		actualAssignment = assignmentService.getPositionsByDepartmentAndDesignationForGivenRange(any(Long.class), null,
				any(Date.class));
		assertEquals(1, actualAssignment.size());
		// given only designation id
		when(assignmentRepository.getPrimaryAssignmentForDesignation(any(Long.class), any(Date.class)))
				.thenReturn(Collections.singletonList(expectedAssignment));
		actualAssignment = assignmentService.getPositionsByDepartmentAndDesignationForGivenRange(null, any(Long.class),
				any(Date.class));
		assertEquals(1, actualAssignment.size());
		// when all params are null
		actualAssignment = assignmentService.getPositionsByDepartmentAndDesignationForGivenRange(null, null, null);
		assertEquals(true, actualAssignment.isEmpty());
	}

}