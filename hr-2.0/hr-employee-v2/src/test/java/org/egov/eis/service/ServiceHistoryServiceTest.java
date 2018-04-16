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
import org.egov.eis.model.ServiceHistory;
import org.egov.eis.model.enums.EntityType;
import org.egov.eis.repository.EmployeeDocumentsRepository;
import org.egov.eis.repository.ServiceHistoryRepository;
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
public class ServiceHistoryServiceTest {

	@Mock
	private ServiceHistoryRepository serviceHistoryRepository;

	@Mock
	private EmployeeDocumentsRepository employeeDocumentsRepository;

	@InjectMocks
	private ServiceHistoryService serviceHistoryService;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testUpdate() {
		final List<ServiceHistory> insertedServiceHistories = new ArrayList<>();
		final List<ServiceHistory> updatedServiceHistories = new ArrayList<>();
		final List<Long> deletedServiceHistoryIds = new ArrayList<>();
		final List<Long> deletedDocumentsReferenceIds = new ArrayList<>();

		when(serviceHistoryRepository.findByEmployeeId(100L, "1")).thenReturn(getServiceHistoriesInDBForEmployee());
		doAnswer(new Answer<Void>() {
			public Void answer(InvocationOnMock invocation) {
				Object[] args = invocation.getArguments();
				deletedServiceHistoryIds.addAll((List<Long>) args[0]);
				return null;
			}
		}).when(serviceHistoryRepository).delete(Matchers.anyListOf(Long.class), Matchers.anyLong(),
				Matchers.anyString());
		doAnswer(new Answer<Void>() {
			public Void answer(InvocationOnMock invocation) {
				Object[] args = invocation.getArguments();
				insertedServiceHistories.add((ServiceHistory) args[0]);
				return null;
			}
		}).when(serviceHistoryRepository).insert(Matchers.any(ServiceHistory.class), Matchers.anyLong());
		doAnswer(new Answer<Void>() {
			public Void answer(InvocationOnMock invocation) {
				Object[] args = invocation.getArguments();
				updatedServiceHistories.add((ServiceHistory) args[0]);
				return null;
			}
		}).when(serviceHistoryRepository).update(Matchers.any(ServiceHistory.class));
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
			employee = getEmployee("org/egov/eis/service/ServiceHistoryService.employees1.json");
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

		serviceHistoryService.update(employee);

		List<ServiceHistory> expectedInsertedServiceHistories = new ArrayList<>();
		List<ServiceHistory> expectedUpdatedServiceHistories = new ArrayList<>();
		List<Long> expectedDeletedServiceHistoryIds = new ArrayList<>();
		List<Long> expectedDeletedDocumentsReferenceIds = new ArrayList<>();

		expectedInsertedServiceHistories.add(employee.getServiceHistory().get(1));
		expectedUpdatedServiceHistories.add(employee.getServiceHistory().get(0));
		expectedDeletedServiceHistoryIds.add(10L);
		expectedDeletedDocumentsReferenceIds.add(10L);

		assertTrue(expectedInsertedServiceHistories.containsAll(insertedServiceHistories));
		assertEquals(expectedInsertedServiceHistories.size(), insertedServiceHistories.size());

		assertTrue(expectedUpdatedServiceHistories.containsAll(updatedServiceHistories));
		assertEquals(expectedUpdatedServiceHistories.size(), updatedServiceHistories.size());

		assertTrue(expectedDeletedServiceHistoryIds.containsAll(deletedServiceHistoryIds));
		assertEquals(expectedDeletedServiceHistoryIds.size(), deletedServiceHistoryIds.size());

		assertTrue(expectedDeletedDocumentsReferenceIds.containsAll(deletedDocumentsReferenceIds));
		assertEquals(expectedDeletedDocumentsReferenceIds.size(), deletedDocumentsReferenceIds.size());
	}

	private Employee getEmployee(String filePath) throws IOException {
		String empJson = new FileUtils().getFileContents(filePath);
		return new ObjectMapper().readValue(empJson, Employee.class);
	}

	private List<ServiceHistory> getServiceHistoriesInDBForEmployee() {
		List<ServiceHistory> serviceHistories = new ArrayList<>();
		serviceHistories.add(ServiceHistory.builder().id(10L).serviceInfo("Employed").serviceFrom(new Date()).build());
		serviceHistories.add(ServiceHistory.builder().id(5L).serviceInfo("Employed").serviceFrom(new Date()).build());
		return serviceHistories;
	}
}
