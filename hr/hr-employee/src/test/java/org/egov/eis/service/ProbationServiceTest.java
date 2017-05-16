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

import org.egov.eis.model.Employee;
import org.egov.eis.model.Probation;
import org.egov.eis.model.enums.EntityType;
import org.egov.eis.repository.EmployeeDocumentsRepository;
import org.egov.eis.repository.ProbationRepository;
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
public class ProbationServiceTest {

	@Mock
	private ProbationRepository probationRepository;

	@Mock
	private EmployeeDocumentsRepository employeeDocumentsRepository;

	@InjectMocks
	private ProbationService probationService;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testUpdate() {
		final List<Probation> insertedProbations = new ArrayList<>();
		final List<Probation> updatedProbations = new ArrayList<>();
		final List<Long> deletedProbationIds = new ArrayList<>();
		final List<Long> deletedDocumentsReferenceIds = new ArrayList<>();

		when(probationRepository.findByEmployeeId(100L, "1")).thenReturn(getProbationsInDBForEmployee());
		doAnswer(new Answer<Void>() {
			public Void answer(InvocationOnMock invocation) {
				Object[] args = invocation.getArguments();
				deletedProbationIds.addAll((List<Long>) args[0]);
				return null;
			}
		}).when(probationRepository).delete(Matchers.anyListOf(Long.class), Matchers.anyLong(), Matchers.anyString());
		doAnswer(new Answer<Void>() {
			public Void answer(InvocationOnMock invocation) {
				Object[] args = invocation.getArguments();
				insertedProbations.add((Probation) args[0]);
				return null;
			}
		}).when(probationRepository).insert(Matchers.any(Probation.class), Matchers.anyLong());
		doAnswer(new Answer<Void>() {
			public Void answer(InvocationOnMock invocation) {
				Object[] args = invocation.getArguments();
				updatedProbations.add((Probation) args[0]);
				return null;
			}
		}).when(probationRepository).update(Matchers.any(Probation.class));
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
			employee = getEmployee("org/egov/eis/service/ProbationService.employees1.json");
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

		probationService.update(employee);

		List<Probation> expectedInsertedProbations = new ArrayList<>();
		List<Probation> expectedUpdatedProbations = new ArrayList<>();
		List<Long> expectedDeletedProbationIds = new ArrayList<>();
		List<Long> expectedDeletedDocumentsReferenceIds = new ArrayList<>();

		expectedInsertedProbations.add(employee.getProbation().get(1));
		expectedUpdatedProbations.add(employee.getProbation().get(0));
		expectedDeletedProbationIds.add(10L);
		expectedDeletedDocumentsReferenceIds.add(10L);

		assertTrue(expectedInsertedProbations.containsAll(insertedProbations));
		assertEquals(expectedInsertedProbations.size(), insertedProbations.size());

		assertTrue(expectedUpdatedProbations.containsAll(updatedProbations));
		assertEquals(expectedUpdatedProbations.size(), updatedProbations.size());

		assertTrue(expectedDeletedProbationIds.containsAll(deletedProbationIds));
		assertEquals(expectedDeletedProbationIds.size(), deletedProbationIds.size());

		assertTrue(expectedDeletedDocumentsReferenceIds.containsAll(deletedDocumentsReferenceIds));
		assertEquals(expectedDeletedDocumentsReferenceIds.size(), deletedDocumentsReferenceIds.size());
	}

	private Employee getEmployee(String filePath) throws IOException {
		String empJson = new FileUtils().getFileContents(filePath);
		return new ObjectMapper().readValue(empJson, Employee.class);
	}

	private List<Probation> getProbationsInDBForEmployee() {
		List<Probation> probations = new ArrayList<>();
		probations.add(Probation.builder().id(10L).designation(10L).declaredOn(new Date()).build());
		probations.add(Probation.builder().id(5L).designation(5L).declaredOn(new Date()).build());
		return probations;
	}
}
