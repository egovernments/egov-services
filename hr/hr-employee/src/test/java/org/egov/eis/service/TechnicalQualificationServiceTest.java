package org.egov.eis.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.egov.eis.model.Employee;
import org.egov.eis.model.TechnicalQualification;
import org.egov.eis.model.enums.EntityType;
import org.egov.eis.repository.EmployeeDocumentsRepository;
import org.egov.eis.repository.TechnicalQualificationRepository;
import org.egov.eis.utils.FileUtils;
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
public class TechnicalQualificationServiceTest {

	@Mock
	private TechnicalQualificationRepository technicalQualificationRepository;

	@Mock
	private EmployeeDocumentsRepository employeeDocumentsRepository;

	@InjectMocks
	private TechnicalQualificationService technicalQualificationService;

	@Test
	public void testUpdate() {
		final List<TechnicalQualification> insertedTechnicals = new ArrayList<>();
		final List<TechnicalQualification> updatedTechnicals = new ArrayList<>();
		final List<Long> deletedTechnicalIds = new ArrayList<>();
		final List<Long> deletedDocumentsReferenceIds = new ArrayList<>();

		when(technicalQualificationRepository.findByEmployeeId(100L, "1")).thenReturn(getTechnicalsInDBForEmployee());

		doAnswer(new Answer<Void>() {
			public Void answer(InvocationOnMock invocation) {
				Object[] args = invocation.getArguments();
				deletedTechnicalIds.addAll((List<Long>) args[0]);
				return null;
			}
		}).when(technicalQualificationRepository).delete(Matchers.anyListOf(Long.class), Matchers.anyLong(),
				Matchers.anyString());
		doAnswer(new Answer<Void>() {
			public Void answer(InvocationOnMock invocation) {
				Object[] args = invocation.getArguments();
				insertedTechnicals.add((TechnicalQualification) args[0]);
				return null;
			}
		}).when(technicalQualificationRepository).insert(Matchers.any(TechnicalQualification.class),
				Matchers.anyLong());
		doAnswer(new Answer<Void>() {
			public Void answer(InvocationOnMock invocation) {
				Object[] args = invocation.getArguments();
				updatedTechnicals.add((TechnicalQualification) args[0]);
				return null;
			}
		}).when(technicalQualificationRepository).update(Matchers.any(TechnicalQualification.class));
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
			employee = getEmployee("org/egov/eis/service/TechnicalQualificationService.employees1.json");
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

		technicalQualificationService.update(employee);

		List<TechnicalQualification> expectedInsertedTechnicals = new ArrayList<>();
		List<TechnicalQualification> expectedUpdatedTechnicals = new ArrayList<>();
		List<Long> expectedDeletedTechnicalIds = new ArrayList<>();
		List<Long> expectedDeletedDocumentsReferenceIds = new ArrayList<>();

		expectedInsertedTechnicals.add(employee.getTechnical().get(0));

		expectedUpdatedTechnicals.add(employee.getTechnical().get(1));

		expectedDeletedTechnicalIds.add(10L);
		expectedDeletedDocumentsReferenceIds.add(10L);

		assertTrue(expectedInsertedTechnicals.containsAll(insertedTechnicals));
		assertEquals(expectedInsertedTechnicals.size(), insertedTechnicals.size());

		assertTrue(expectedUpdatedTechnicals.containsAll(updatedTechnicals));
		assertEquals(expectedUpdatedTechnicals.size(), updatedTechnicals.size());

		assertTrue(expectedDeletedTechnicalIds.containsAll(deletedTechnicalIds));
		assertEquals(expectedDeletedTechnicalIds.size(), deletedTechnicalIds.size());

		assertTrue(expectedDeletedDocumentsReferenceIds.containsAll(deletedDocumentsReferenceIds));
		assertEquals(expectedDeletedDocumentsReferenceIds.size(), deletedDocumentsReferenceIds.size());
	}

	private List<TechnicalQualification> getTechnicalsInDBForEmployee() {
		List<TechnicalQualification> technicals = new ArrayList<>();
		technicals.add(TechnicalQualification.builder().id(10L).skill("cs").build());
		technicals.add(TechnicalQualification.builder().id(6L).skill("ec").build());
		return technicals;
	}

	private Employee getEmployee(String filePath) throws IOException {
		String empJson = new FileUtils().getFileContents(filePath);
		return new ObjectMapper().readValue(empJson, Employee.class);
	}
}
