package org.egov.eis.domain.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.egov.eis.persistence.entity.Assignment;
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

}