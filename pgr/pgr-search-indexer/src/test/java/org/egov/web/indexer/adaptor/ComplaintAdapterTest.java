package org.egov.web.indexer.adaptor;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.web.indexer.config.IndexerProperties;
import org.egov.web.indexer.contract.Assignment;
import org.egov.web.indexer.contract.Boundary;
import org.egov.web.indexer.contract.City;
import org.egov.web.indexer.contract.ComplaintType;
import org.egov.web.indexer.contract.Department;
import org.egov.web.indexer.contract.Designation;
import org.egov.web.indexer.contract.Employee;
import org.egov.web.indexer.contract.ServiceRequest;
import org.egov.web.indexer.repository.BoundaryRepository;
import org.egov.web.indexer.repository.CityRepository;
import org.egov.web.indexer.repository.ComplaintTypeRepository;
import org.egov.web.indexer.repository.EmployeeRepository;
import org.egov.web.indexer.repository.contract.ComplaintIndex;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ComplaintAdapterTest {

	@Mock
	private IndexerProperties propertiesManager;

	@Mock
	private BoundaryRepository boundaryRepository;

	@Mock
	private ComplaintTypeRepository complaintTypeRepository;

	@Mock
	private CityRepository cityRepository;

	@Mock
	private EmployeeRepository employeeRepository;

	@InjectMocks
	private ComplaintAdapter complaintAdapter;

	@Test
	public void test_create_index() {
		ServiceRequest serviceRequest = setUpServiceRequest();
		ComplaintIndex complaintIndex = complaintAdapter.indexOnCreate(serviceRequest);
		assertNotNull(complaintIndex);
	}

	private ServiceRequest setUpServiceRequest() {
		ServiceRequest serviceRequest = ServiceRequest.builder().values(new HashMap<>()).build();
		serviceRequest.setCreatedDate("2016-10-20");
		serviceRequest.setEscalationDate(1491374461909L);
		serviceRequest.setFirstName("abc");
		serviceRequest.setLastName("xyz");
		serviceRequest.setComplaintTypeCode("AOS");
		Map<String, String> values = serviceRequest.getValues();
		values.put("receivingMode", "");
		values.put("userType", "");
		values.put("citizenFeedback", "0");
		values.put("escalationHours", "0");
		values.put("locationId", "1");
		values.put("child_location_id", "2");
		values.put("tenantId", "1");
		values.put("assignment_id", "1");
		final ComplaintType expectedComplaintType = getComplaintType();
		final Boundary expectedBoundary = getExpectedBoundary();
		final City expectedCityContent = getExpectedCityContent();
		final Employee expectedAssignment = getExpectedEmployee();
		when(complaintTypeRepository.fetchComplaintTypeByCode("AOS")).thenReturn(expectedComplaintType);
		when(boundaryRepository.fetchBoundaryById(Long.valueOf(values.get("locationId")))).thenReturn(expectedBoundary);
		when(cityRepository.fetchCityById(Long.valueOf(values.get("tenantId")))).thenReturn(expectedCityContent);
		when(employeeRepository.fetchEmployeeByPositionId(Long.valueOf(values.get("assignment_id")), new LocalDate(),
				values.get("tenantId"))).thenReturn(expectedAssignment);
		return serviceRequest;
	}

	private ComplaintType getComplaintType() {
		ComplaintType complaintType = new ComplaintType();
		complaintType.setSlaHours(10);
		return complaintType;
	}

	private Boundary getExpectedBoundary() {
		Boundary boundary = new Boundary();
		boundary.setName("Kurnool");
		boundary.setLatitude(14.46F);
		boundary.setLongitude(78.82F);
		boundary.setBoundaryNum(3L);
		return boundary;
	}

	private City getExpectedCityContent() {
		City city = new City();
		city.setName("Kurnool");
		city.setCode("KC");
		city.setDistrictCode("KC01");
		city.setDistrictName("Kurnool District");
		city.setGrade("Grade A");
		city.setDomainURL("http://localhost");
		city.setRegionName("Kurnool Region");
		return city;
	}

	private Employee getExpectedEmployee() {
		Employee employee = new Employee();
		employee.setId(1L);
		employee.setName("Raman");
		employee.setMobileNumber("1234567890");
		Department department = new Department();
		department.setName("Revenu Dept");
		department.setCode("RD");
		Designation designation = new Designation();
		designation.setName("Asst.Eng.");
		List<Assignment> assignment = new ArrayList<Assignment>();
		Assignment assign = new Assignment();
		assign.setDepartment(department);
		assign.setDesignation(designation);
		assignment.add(assign);
		employee.setAssignments(assignment);
		return employee;
	}

}