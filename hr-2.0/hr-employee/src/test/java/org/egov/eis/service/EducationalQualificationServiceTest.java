package org.egov.eis.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.egov.eis.model.EducationalQualification;
import org.egov.eis.model.Employee;
import org.egov.eis.model.enums.EntityType;
import org.egov.eis.repository.EducationalQualificationRepository;
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
public class EducationalQualificationServiceTest {

	@Mock
	private EducationalQualificationRepository educationalQualificationRepository;

	@Mock
	private EmployeeDocumentsRepository employeeDocumentsRepository;

	@InjectMocks
	private EducationalQualificationService educationalQualificationService;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testUpdate() {
		final List<EducationalQualification> insertedEducationalQualifications = new ArrayList<>();
		final List<EducationalQualification> updatedEducationalQualifications = new ArrayList<>();
		final List<Long> deletedEducationalQualificationIds = new ArrayList<>();
		final List<Long> deletedDocumentsReferenceIds = new ArrayList<>();

		when(educationalQualificationRepository.findByEmployeeId(100L, "1"))
				.thenReturn(getEducationalQualificationsInDBForEmployee());
		doAnswer(new Answer<Void>() {
			public Void answer(InvocationOnMock invocation) {
				Object[] args = invocation.getArguments();
				deletedEducationalQualificationIds.addAll((List<Long>) args[0]);
				return null;
			}
		}).when(educationalQualificationRepository).delete(Matchers.anyListOf(Long.class), Matchers.anyLong(),
				Matchers.anyString());
		doAnswer(new Answer<Void>() {
			public Void answer(InvocationOnMock invocation) {
				Object[] args = invocation.getArguments();
				insertedEducationalQualifications.add((EducationalQualification) args[0]);
				return null;
			}
		}).when(educationalQualificationRepository).insert(Matchers.any(EducationalQualification.class),
				Matchers.anyLong());
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
				updatedEducationalQualifications.add((EducationalQualification) args[0]);
				return null;
			}
		}).when(educationalQualificationRepository).update(Matchers.any(EducationalQualification.class));

		Employee employee = null;
		try {
			employee = getEmployee("org/egov/eis/service/EducationalQualificationService.employees1.json");
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

		educationalQualificationService.update(employee);

		List<EducationalQualification> expectedInsertedEducationalQualifications = new ArrayList<>();
		List<EducationalQualification> expectedUpdatedEducationalQualifications = new ArrayList<>();
		List<Long> expecteddeletedEducationalQualificationIds = new ArrayList<>();
		List<Long> expectedDeletedEducationalQualificationReferenceIds = new ArrayList<>();

		expectedUpdatedEducationalQualifications.add(employee.getEducation().get(0));
		expectedInsertedEducationalQualifications.add(employee.getEducation().get(1));
		expectedInsertedEducationalQualifications.add(employee.getEducation().get(2));
		expectedInsertedEducationalQualifications.add(employee.getEducation().get(3));

		expecteddeletedEducationalQualificationIds.add(26L);
		expectedDeletedEducationalQualificationReferenceIds.add(26L);

		assertTrue(expectedInsertedEducationalQualifications.containsAll(insertedEducationalQualifications));
		assertEquals(expectedInsertedEducationalQualifications.size(), insertedEducationalQualifications.size());

		assertTrue(expectedUpdatedEducationalQualifications.containsAll(updatedEducationalQualifications));
		assertEquals(expectedUpdatedEducationalQualifications.size(), updatedEducationalQualifications.size());

		assertTrue(expecteddeletedEducationalQualificationIds.containsAll(deletedEducationalQualificationIds));
		assertEquals(expecteddeletedEducationalQualificationIds.size(), deletedEducationalQualificationIds.size());

		assertTrue(expectedDeletedEducationalQualificationReferenceIds.containsAll(deletedDocumentsReferenceIds));
		assertEquals(expectedDeletedEducationalQualificationReferenceIds.size(), deletedDocumentsReferenceIds.size());

	}

	private List<EducationalQualification> getEducationalQualificationsInDBForEmployee() {

		List<EducationalQualification> educationalQualifications = new ArrayList<>();
		educationalQualifications
				.add(EducationalQualification.builder().id(501L).qualification("as").yearOfPassing(2013).build());
		educationalQualifications
				.add(EducationalQualification.builder().id(26L).qualification("BE").yearOfPassing(2010).build());
		return educationalQualifications;
	}

	private Employee getEmployee(String filePath) throws IOException {
		String empJson = new FileUtils().getFileContents(filePath);
		return new ObjectMapper().readValue(empJson, Employee.class);
	}

}
