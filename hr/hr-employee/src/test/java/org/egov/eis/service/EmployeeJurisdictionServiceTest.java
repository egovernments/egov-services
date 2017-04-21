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
import org.egov.eis.repository.EmployeeJurisdictionRepository;
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
public class EmployeeJurisdictionServiceTest {

	@Mock
	private EmployeeJurisdictionRepository employeeJurisdictionRepository;

	@InjectMocks
	private EmployeeJurisdictionService employeeJurisdictionService;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testUpdate() {

		List<Long> insertedJurisdictions = new ArrayList<>();
		List<Long> deletedJurisdictions = new ArrayList<>();

		when(employeeJurisdictionRepository.findByEmployeeId(100L, "1")).thenReturn(getJurisdictionsInDBForEmployee());
		doAnswer(new Answer<Void>() {
			public Void answer(InvocationOnMock invocation) {
				Object[] args = invocation.getArguments();
				insertedJurisdictions.add((Long) args[0]);
				return null;
			}
		}).when(employeeJurisdictionRepository).insert(Matchers.anyLong(), Matchers.anyLong(), Matchers.anyString());
		doAnswer(new Answer<Void>() {
			public Void answer(InvocationOnMock invocation) {
				Object[] args = invocation.getArguments();
				deletedJurisdictions.addAll((List<Long>) args[0]);
				return null;
			}
		}).when(employeeJurisdictionRepository).delete(Matchers.anyListOf(Long.class), Matchers.anyLong(),
				Matchers.anyString());

		Employee employee = null;
		try {
			employee = getEmployee("org/egov/eis/service/EmployeeJurisdictionService.employees1.json");
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

		employeeJurisdictionService.update(employee);

		List<Long> expectedInsertedJurisdictions = new ArrayList<>();
		List<Long> expectedDeletedJurisdictions = new ArrayList<>();

		expectedInsertedJurisdictions.add(employee.getJurisdictions().get(0));
		expectedDeletedJurisdictions.add(101L);
		expectedDeletedJurisdictions.add(102L);
		
		assertTrue(expectedInsertedJurisdictions.containsAll(insertedJurisdictions));
		assertEquals(expectedInsertedJurisdictions.size(), insertedJurisdictions.size());

		assertTrue(expectedDeletedJurisdictions.containsAll(deletedJurisdictions));
		assertEquals(expectedDeletedJurisdictions.size(), deletedJurisdictions.size());
	}

	private List<Long> getJurisdictionsInDBForEmployee() {
		List<Long> jurisdictions = new ArrayList<>();
		jurisdictions.add(100L);
		jurisdictions.add(101L);
		jurisdictions.add(102L);
		return jurisdictions;
	}

	private Employee getEmployee(String filePath) throws IOException {
		String empJson = new FileUtils().getFileContents(filePath);
		return new ObjectMapper().readValue(empJson, Employee.class);
	}

}
