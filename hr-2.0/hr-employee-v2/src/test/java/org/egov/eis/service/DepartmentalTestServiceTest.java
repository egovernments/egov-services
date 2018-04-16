package org.egov.eis.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.egov.eis.model.DepartmentalTest;
import org.egov.eis.model.Employee;
import org.egov.eis.model.enums.EntityType;
import org.egov.eis.repository.DepartmentalTestRepository;
import org.egov.eis.repository.EmployeeDocumentsRepository;
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
public class DepartmentalTestServiceTest {

	@Mock
	private DepartmentalTestRepository departmentalTestRepository;

	@Mock
	private EmployeeDocumentsRepository employeeDocumentsRepository;

	@InjectMocks
	private DepartmentalTestService departmentalTestService;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testUpdate() {
		final List<DepartmentalTest> insertedDepartmentalTests = new ArrayList<>();
		final List<DepartmentalTest> updatedDepartmentalTests = new ArrayList<>();
		final List<Long> deletedDepartmentalTestIds = new ArrayList<>();
		final List<Long> deletedDocumentsReferenceIds = new ArrayList<>();

		when(departmentalTestRepository.findByEmployeeId(100L, "1")).thenReturn(getdepartmentalTestInDBForEmployee());
		doAnswer(new Answer<Void>() {
			public Void answer(InvocationOnMock invocation) {
				Object[] args = invocation.getArguments();
				deletedDepartmentalTestIds.addAll((List<Long>) args[0]);
				return null;
			}
		}).when(departmentalTestRepository).delete(Matchers.anyListOf(Long.class), Matchers.anyLong(),
				Matchers.anyString());
		doAnswer(new Answer<Void>() {
			public Void answer(InvocationOnMock invocation) {
				Object[] args = invocation.getArguments();
				insertedDepartmentalTests.add((DepartmentalTest) args[0]);
				return null;
			}
		}).when(departmentalTestRepository).insert(Matchers.any(DepartmentalTest.class), Matchers.anyLong());
		doAnswer(new Answer<Void>() {
			public Void answer(InvocationOnMock invocation) {
				Object[] args = invocation.getArguments();
				deletedDocumentsReferenceIds.addAll((List<Long>) args[2]);
				return null;
			}
		}).when(employeeDocumentsRepository).deleteForReferenceIds(Matchers.anyLong(), Matchers.any(EntityType.class),
				Matchers.anyListOf(Long.class), Matchers.anyString());
		doAnswer(new Answer<Void>() {
			public Void answer(InvocationOnMock invocation) {
				Object[] args = invocation.getArguments();
				updatedDepartmentalTests.add((DepartmentalTest) args[0]);
				return null;
			}
		}).when(departmentalTestRepository).update(Matchers.any(DepartmentalTest.class));

		Employee employee = null;
		try {
			employee = getEmployee("org/egov/eis/service/DepartmentalTestService.employees1.json");
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

		departmentalTestService.update(employee);

		List<DepartmentalTest> expectedInsertedDepartmentalTests = new ArrayList<>();
		List<DepartmentalTest> expectedUpdatedDepartmentalTests = new ArrayList<>();
		List<Long> expectedDeletedDepartmentalTestIds = new ArrayList<>();
		List<Long> expectedDeletedDocumentsReferenceIds = new ArrayList<>();

		expectedUpdatedDepartmentalTests.add(employee.getTest().get(0));
		expectedUpdatedDepartmentalTests.add(employee.getTest().get(1));
		expectedInsertedDepartmentalTests.add(employee.getTest().get(2));

		expectedDeletedDepartmentalTestIds.add(35L);
		expectedDeletedDocumentsReferenceIds.add(35L);

		assertTrue(expectedInsertedDepartmentalTests.containsAll(insertedDepartmentalTests));
		assertEquals(expectedInsertedDepartmentalTests.size(), insertedDepartmentalTests.size());

		assertTrue(expectedUpdatedDepartmentalTests.containsAll(updatedDepartmentalTests));
		assertEquals(expectedUpdatedDepartmentalTests.size(), updatedDepartmentalTests.size());

		assertTrue(expectedDeletedDepartmentalTestIds.containsAll(deletedDepartmentalTestIds));
		assertEquals(expectedDeletedDepartmentalTestIds.size(), deletedDepartmentalTestIds.size());

		assertTrue(expectedDeletedDocumentsReferenceIds.containsAll(deletedDocumentsReferenceIds));
		assertEquals(expectedDeletedDocumentsReferenceIds.size(), deletedDocumentsReferenceIds.size());
	}

	private List<DepartmentalTest> getdepartmentalTestInDBForEmployee() {

		List<DepartmentalTest> departmentalTest = new ArrayList<>();
		departmentalTest.add(DepartmentalTest.builder().id(1L).test("aksdj").yearOfPassing(2013).build());
		departmentalTest.add(DepartmentalTest.builder().id(2L).test("aksdj").yearOfPassing(2013).build());
		departmentalTest.add(DepartmentalTest.builder().id(35L).test("aksdj").yearOfPassing(2013).build());
		return departmentalTest;
	}

	private Employee getEmployee(String filePath) throws IOException {
		String empJson = new FileUtils().getFileContents(filePath);
		return new ObjectMapper().readValue(empJson, Employee.class);
	}

}
