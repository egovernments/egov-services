package org.egov.eis.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.eis.model.Assignment;
import org.egov.eis.model.Employee;
import org.egov.eis.model.HODDepartment;
import org.egov.eis.model.enums.EntityType;
import org.egov.eis.repository.AssignmentRepository;
import org.egov.eis.repository.EmployeeDocumentsRepository;
import org.egov.eis.repository.HODDepartmentRepository;
import org.egov.eis.utils.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(MockitoJUnitRunner.class)
public class AssignmentServiceTest {

	@Mock
	private AssignmentRepository assignmentRepository;

	@Mock
	private HODDepartmentRepository hodDepartmentRepository;

	@Mock
	private EmployeeDocumentsRepository employeeDocumentsRepository;

	@InjectMocks
	private AssignmentService assignmentService;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testUpdate() {
		final List<Assignment> insertedAssignments = new ArrayList<>();
		final List<Assignment> updatedAssignments = new ArrayList<>();
		final List<Long> deletedAssignmentIds = new ArrayList<>();

		final List<Assignment> insertedHodDepartmentAlongWithAssignment = new ArrayList<>();
		final List<String> insertedHodDepartmentIdsDuringUpdate = new ArrayList<>();
		final List<String> deletedHodDepartmentIds = new ArrayList<>();

		final List<Long> deletedDocumentsReferenceIds = new ArrayList<>();

		when(assignmentRepository.findByEmployeeId(100L, "1")).thenReturn(getAssignmentsInDBForEmployee());
		doAnswer(new Answer<Void>() {
			public Void answer(InvocationOnMock invocation) {
				Object[] args = invocation.getArguments();
				deletedAssignmentIds.addAll((List<Long>) args[0]);
				return null;
			}
		}).when(assignmentRepository).delete(Matchers.anyListOf(Long.class), Matchers.anyLong(), Matchers.anyString());
		doAnswer(new Answer<Void>() {
			public Void answer(InvocationOnMock invocation) {
				Object[] args = invocation.getArguments();
				insertedAssignments.add((Assignment) args[0]);
				return null;
			}
		}).when(assignmentRepository).insert(Matchers.any(Assignment.class), Matchers.anyLong());
		doAnswer(new Answer<Void>() {
			public Void answer(InvocationOnMock invocation) {
				Object[] args = invocation.getArguments();
				updatedAssignments.add((Assignment) args[0]);
				return null;
			}
		}).when(assignmentRepository).update(Matchers.any(Assignment.class));

		when(hodDepartmentRepository.findByAssignmentId(10L, "1")).thenReturn(getHodsInDbForAssignment());
		doAnswer(new Answer<Void>() {
			public Void answer(InvocationOnMock invocation) {
				Object[] args = invocation.getArguments();
				deletedHodDepartmentIds.addAll((List<String>) args[0]);
				return null;
			}
		}).when(hodDepartmentRepository).delete(Matchers.anyListOf(Long.class), Matchers.anyString());
		doAnswer(new Answer<Void>() {
			public Void answer(InvocationOnMock invocation) {
				Object[] args = invocation.getArguments();
				insertedHodDepartmentIdsDuringUpdate.add(args[0].toString());
				return null;
			}
		}).when(hodDepartmentRepository).insert(Matchers.anyLong(), Matchers.anyString(), Matchers.anyString());
		doAnswer(new Answer<Void>() {
			public Void answer(InvocationOnMock invocation) {
				Object[] args = invocation.getArguments();
				insertedHodDepartmentAlongWithAssignment.add((Assignment) args[0]);
				return null;
			}
		}).when(hodDepartmentRepository).save(Matchers.any(Assignment.class), Matchers.anyString());
		doAnswer(new Answer<Void>() {
			public Void answer(InvocationOnMock invocation) {
				Object[] args = invocation.getArguments();
				deletedDocumentsReferenceIds.addAll((List<Long>) args[2]);
				return null;
			}
		}).when(employeeDocumentsRepository).deleteForReferenceIds(Matchers.anyLong(), Matchers.any(EntityType.class),
				Matchers.anyListOf(Long.class), Matchers.anyString());

		Employee employee = null;
		try {
			employee = getEmployee("org/egov/eis/service/AssignmentService.employees1.json");
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

		assignmentService.update(employee);

		List<Assignment> expectedInsertedAssignments = new ArrayList<>();
		List<Assignment> expectedUpdatedAssignments = new ArrayList<>();
		List<Long> expectedDeletedAssignmentIds = new ArrayList<>();

		List<Assignment> expectedInsertedHodAlongWithAssignment = new ArrayList<>();
		List<String> expectedInsertedHodDepartmentIdsDuringUpdate = new ArrayList<>();
		List<String> expectedDeletedHodDepartmentIds = new ArrayList<>();
		List<String> expectedDeletedDocumentsReferenceIds = new ArrayList<>();

		expectedInsertedAssignments.add(employee.getAssignments().get(2));
		expectedInsertedHodAlongWithAssignment.add(employee.getAssignments().get(2));
		expectedInsertedHodDepartmentIdsDuringUpdate
				.add(employee.getAssignments().get(0).getHod().get(0).getDepartment());

		expectedUpdatedAssignments.add(employee.getAssignments().get(0));
		expectedUpdatedAssignments.add(employee.getAssignments().get(1));

		expectedDeletedAssignmentIds.add(10L);
		expectedDeletedHodDepartmentIds.add("10");
		expectedDeletedDocumentsReferenceIds.add("10");

		assertTrue(expectedInsertedAssignments.containsAll(insertedAssignments));
		assertEquals(expectedInsertedAssignments.size(), insertedAssignments.size());

		assertTrue(expectedUpdatedAssignments.containsAll(updatedAssignments));
		assertEquals(expectedUpdatedAssignments.size(), updatedAssignments.size());

		assertTrue(expectedDeletedAssignmentIds.containsAll(deletedAssignmentIds));
		assertEquals(expectedDeletedAssignmentIds.size(), deletedAssignmentIds.size());

		assertTrue(expectedInsertedHodAlongWithAssignment.containsAll(insertedHodDepartmentAlongWithAssignment));
		assertEquals(expectedInsertedHodAlongWithAssignment.size(), insertedHodDepartmentAlongWithAssignment.size());

		assertTrue(expectedInsertedHodDepartmentIdsDuringUpdate.containsAll(insertedHodDepartmentIdsDuringUpdate));
		assertEquals(expectedInsertedHodDepartmentIdsDuringUpdate.size(), insertedHodDepartmentIdsDuringUpdate.size());

		assertEquals(expectedDeletedHodDepartmentIds.size(), deletedHodDepartmentIds.size());
		
		assertEquals(expectedDeletedDocumentsReferenceIds.size(), deletedDocumentsReferenceIds.size());
	}

	private List<HODDepartment> getHodsInDbForAssignment() {
		List<HODDepartment> hodDepartments = new ArrayList<>();
		HODDepartment hodDepartment = HODDepartment.builder().id(1L).department("5").tenantId("").build();
		hodDepartments.add(hodDepartment);
		return hodDepartments;
	}

	private Employee getEmployee(String filePath) throws IOException {
		String empJson = new FileUtils().getFileContents(filePath);
		return new ObjectMapper().readValue(empJson, Employee.class);
	}

	private List<Assignment> getAssignmentsInDBForEmployee() {
		List<Assignment> assignments = new ArrayList<>();
		assignments.add(Assignment.builder().id(10L).position(5L).department("5").designation(5L).isPrimary(false)
				.fromDate(new Date()).toDate(new Date()).build());
		assignments.add(Assignment.builder().id(5L).position(5L).department("5").designation(5L).isPrimary(false)
				.fromDate(new Date()).toDate(new Date()).build());
		assignments.add(Assignment.builder().id(6L).position(6L).department("6").designation(6L).isPrimary(false)
				.fromDate(new Date()).toDate(new Date()).build());
		return assignments;
	}
}
