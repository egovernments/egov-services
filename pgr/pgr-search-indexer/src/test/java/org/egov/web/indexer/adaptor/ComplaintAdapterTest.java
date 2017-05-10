package org.egov.web.indexer.adaptor;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.web.indexer.config.IndexerProperties;
import org.egov.web.indexer.contract.*;
import org.egov.web.indexer.repository.*;
import org.egov.web.indexer.repository.contract.ComplaintIndex;
import org.hibernate.validator.constraints.Mod10Check;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ComplaintAdapterTest {
    

    public static final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:MM:ss");

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

	@Mock
    private TenantRepository tenantRepository;

    public static String toDefaultDateTimeFormat(final Date date) {
         return sdf.format(date);
     }

	@Test
	public void test_create_index() {
		ServiceRequest serviceRequest = setUpServiceRequest();
		ComplaintIndex complaintIndex = complaintAdapter.indexOnCreate(serviceRequest);
		assertNotNull(complaintIndex);
	}
	
	@Test
    public void test_update_index() {
        ServiceRequest serviceRequest = setUpServiceRequest();
        serviceRequest.getValues().put("status", "COMPLETED");
        ComplaintIndex complaintIndex = complaintAdapter.indexOnUpdate(serviceRequest);
        assertNotNull(complaintIndex);
        serviceRequest.getValues().put("status", "PROCESSING");
        complaintIndex = complaintAdapter.indexOnUpdate(serviceRequest);
        assertNotNull(complaintIndex);
        serviceRequest.getValues().put("status", "REJECTED");
        serviceRequest.setCreatedDate(toDefaultDateTimeFormat(new Date()));
        complaintIndex = complaintAdapter.indexOnUpdate(serviceRequest);
        assertNotNull(complaintIndex);
        serviceRequest.getValues().put("status", "REOPENED");
        complaintIndex = complaintAdapter.indexOnUpdate(serviceRequest);
        assertNotNull(complaintIndex);
    }
	
	private ServiceRequest setUpServiceRequest() {
		ServiceRequest serviceRequest = ServiceRequest.builder().values(new HashMap<>()).build();
		serviceRequest.setCreatedDate("20-10-2016 10:47:21");
		serviceRequest.setEscalationDate("20-10-2016 10:47:21");
		serviceRequest.setFirstName("abc");
		serviceRequest.setTenantId("ap.public");
		serviceRequest.setLat(0.0);
		serviceRequest.setLng(0.0);
		serviceRequest.setComplaintTypeCode("AOS");
		serviceRequest.setCrn("AB-123");
		Map<String, String> values = serviceRequest.getValues();
		values.put("receivingMode", "");
		values.put("userType", "");
		values.put("citizenFeedback", "0");
		values.put("escalationHours", "0");
		values.put("locationId", "1");
		values.put("childLocationId", "2");
		values.put("assignmentId", "1");
		final ComplaintType expectedComplaintType = getComplaintType();
		final Boundary expectedBoundary = getExpectedBoundary();
		//final City expectedCityContent = getExpectedCityContent();
		final Employee expectedAssignment = getExpectedEmployee();
		when(complaintTypeRepository.fetchComplaintTypeByCode("AOS","ap.public")).thenReturn(expectedComplaintType);
		when(boundaryRepository.fetchBoundaryById(Long.valueOf(values.get("locationId")),"ap.public")).thenReturn(expectedBoundary);
		when(boundaryRepository.fetchBoundaryById(Long.valueOf(values.get("childLocationId")),"ap.public")).thenReturn(expectedBoundary);
		when(tenantRepository.fetchTenantByCode("ap.public")).thenReturn(getCity());
		//when(cityRepository.fetchCityById(Long.valueOf(values.get("tenantId")))).thenReturn(expectedCityContent);
		when(employeeRepository.fetchEmployeeByPositionId(Long.valueOf(values.get("assignmentId")), new LocalDate(),
				serviceRequest.getTenantId())).thenReturn(expectedAssignment);
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

	/*private City getExpectedCityContent() {
		City city = new City();
		city.setName("Kurnool");
		city.setCode("KC");
		city.setDistrictCode("KC01");
		city.setDistrictName("Kurnool District");
		city.setGrade("Grade A");
		city.setDomainURL("http://localhost");
		city.setRegionName("Kurnool Region");
		return city;
	}*/

	private City getCity(){
	    City city=new City();
	    city.setCode("KC");
	    city.setDistrictCode("Kurnool");
	    city.setDistrictName("kurnool");
	    city.setName("kurnool");
	    city.setId("kurnool");
	    return  city;
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