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
import org.egov.eis.model.Regularisation;
import org.egov.eis.model.enums.EntityType;
import org.egov.eis.repository.EmployeeDocumentsRepository;
import org.egov.eis.repository.RegularisationRepository;
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
public class RegularisationServiceTest {

	@Mock
	private RegularisationRepository regularisationRepository;

	@Mock
	private EmployeeDocumentsRepository employeeDocumentsRepository;

	@InjectMocks
	private RegularisationService regularisationService;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testUpdate() {
		final List<Regularisation> insertedRegularisations = new ArrayList<>();
		final List<Regularisation> updatedRegularisations = new ArrayList<>();
		final List<Long> deletedRegularisationIds = new ArrayList<>();
		final List<Long> deletedDocumentsReferenceIds = new ArrayList<>();

		when(regularisationRepository.findByEmployeeId(100L, "1")).thenReturn(getRegularisationsInDBForEmployee());
		doAnswer(new Answer<Void>() {
			public Void answer(InvocationOnMock invocation) {
				Object[] args = invocation.getArguments();
				deletedRegularisationIds.addAll((List<Long>) args[0]);
				return null;
			}
		}).when(regularisationRepository).delete(Matchers.anyListOf(Long.class), Matchers.anyLong(),
				Matchers.anyString());
		doAnswer(new Answer<Void>() {
			public Void answer(InvocationOnMock invocation) {
				Object[] args = invocation.getArguments();
				insertedRegularisations.add((Regularisation) args[0]);
				return null;
			}
		}).when(regularisationRepository).insert(Matchers.any(Regularisation.class), Matchers.anyLong());
		doAnswer(new Answer<Void>() {
			public Void answer(InvocationOnMock invocation) {
				Object[] args = invocation.getArguments();
				updatedRegularisations.add((Regularisation) args[0]);
				return null;
			}
		}).when(regularisationRepository).update(Matchers.any(Regularisation.class));
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
			employee = getEmployee("org/egov/eis/service/RegularisationService.employees1.json");
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

		regularisationService.update(employee);

		List<Regularisation> expectedInsertedRegularisations = new ArrayList<>();
		List<Regularisation> expectedUpdatedRegularisations = new ArrayList<>();
		List<Long> expectedDeletedRegularisationIds = new ArrayList<>();
		List<Long> expectedDeletedDocumentsReferenceIds = new ArrayList<>();

		expectedInsertedRegularisations.add(employee.getRegularisation().get(0));
		expectedInsertedRegularisations.add(employee.getRegularisation().get(1));

		expectedUpdatedRegularisations.add(employee.getRegularisation().get(2));

		expectedDeletedRegularisationIds.add(105L);
		expectedDeletedDocumentsReferenceIds.add(105L);

		assertTrue(expectedInsertedRegularisations.containsAll(insertedRegularisations));
		assertEquals(expectedInsertedRegularisations.size(), insertedRegularisations.size());

		assertTrue(expectedUpdatedRegularisations.containsAll(updatedRegularisations));
		assertEquals(expectedUpdatedRegularisations.size(), updatedRegularisations.size());

		assertTrue(expectedDeletedRegularisationIds.containsAll(deletedRegularisationIds));
		assertEquals(expectedDeletedRegularisationIds.size(), deletedRegularisationIds.size());

		assertTrue(expectedDeletedDocumentsReferenceIds.containsAll(deletedDocumentsReferenceIds));
		assertEquals(expectedDeletedDocumentsReferenceIds.size(), deletedDocumentsReferenceIds.size());
	}

	private List<Regularisation> getRegularisationsInDBForEmployee() {
		List<Regularisation> regularisations = new ArrayList<>();
		regularisations.add(Regularisation.builder().id(105L).designation(2L).declaredOn(new Date()).build());
		regularisations.add(Regularisation.builder().id(7L).designation(4L).declaredOn(new Date()).build());
		return regularisations;
	}

	private Employee getEmployee(String filePath) throws IOException {
		String empJson = new FileUtils().getFileContents(filePath);
		return new ObjectMapper().readValue(empJson, Employee.class);
	}

}
