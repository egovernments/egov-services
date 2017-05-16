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
import org.egov.eis.repository.EmployeeLanguageRepository;
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
public class EmployeeLanguageServiceTest {

	@Mock
	private EmployeeLanguageRepository employeeLanguageRepository;

	@InjectMocks
	private EmployeeLanguageService employeeLanguageService;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testUpdate() {

		List<Long> insertedLanguages = new ArrayList<>();
		List<Long> deletedLanguages = new ArrayList<>();

		when(employeeLanguageRepository.findByEmployeeId(100L, "1")).thenReturn(getLanguagesInDBForEmployee());
		doAnswer(new Answer<Void>() {
			public Void answer(InvocationOnMock invocation) {
				Object[] args = invocation.getArguments();
				insertedLanguages.add((Long) args[0]);
				return null;
			}
		}).when(employeeLanguageRepository).insert(Matchers.anyLong(), Matchers.anyLong(), Matchers.anyString());
		doAnswer(new Answer<Void>() {
			public Void answer(InvocationOnMock invocation) {
				Object[] args = invocation.getArguments();
				deletedLanguages.addAll((List<Long>) args[0]);
				return null;
			}
		}).when(employeeLanguageRepository).delete(Matchers.anyListOf(Long.class), Matchers.anyLong(),
				Matchers.anyString());

		Employee employee = null;
		try {
			employee = getEmployee("org/egov/eis/service/ServiceHistoryService.employees1.json");
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

		employeeLanguageService.update(employee);

		List<Long> expectedInsertedLanguages = new ArrayList<>();
		List<Long> expectedDeletedLanguages = new ArrayList<>();

		expectedInsertedLanguages.add(employee.getLanguagesKnown().get(1));
		expectedDeletedLanguages.add(2L);

		assertTrue(expectedInsertedLanguages.containsAll(insertedLanguages));
		assertEquals(expectedInsertedLanguages.size(), insertedLanguages.size());

		assertTrue(expectedDeletedLanguages.containsAll(deletedLanguages));
		assertEquals(expectedDeletedLanguages.size(), deletedLanguages.size());

	}

	private Employee getEmployee(String filePath) throws IOException {
		String empJson = new FileUtils().getFileContents(filePath);
		return new ObjectMapper().readValue(empJson, Employee.class);
	}

	private List<Long> getLanguagesInDBForEmployee() {
		List<Long> languages = new ArrayList<>();
		languages.add(1L);
		languages.add(2L);
		languages.add(3L);
		return languages;
	}
}
