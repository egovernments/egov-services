package org.egov.web.indexer.adaptor;

import org.egov.pgr.common.contract.AttributeEntry;
import org.egov.web.indexer.config.IndexerProperties;
import org.egov.web.indexer.contract.*;
import org.egov.web.indexer.repository.*;
import org.egov.web.indexer.repository.contract.ComplaintIndex;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

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

    @Mock
    private DepartmentRestRepository departmentRestRepository;

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
        final ArrayList<AttributeEntry> attribValues = new ArrayList<>();
        attribValues.add(new AttributeEntry("status", "COMPLETED"));
        serviceRequest.setAttribValues(attribValues);
        serviceRequest.setAttribValues(Collections.singletonList(new AttributeEntry("status", "COMPLETED")));
        ComplaintIndex complaintIndex = complaintAdapter.indexOnUpdate(serviceRequest);
        assertNotNull(complaintIndex);
        serviceRequest.setAttribValues(Collections.singletonList(new AttributeEntry("status", "PROCESSING")));
        complaintIndex = complaintAdapter.indexOnUpdate(serviceRequest);
        assertNotNull(complaintIndex);
        serviceRequest.setAttribValues(Collections.singletonList(new AttributeEntry("status", "REJECTED")));
        serviceRequest.setCreatedDate(toDefaultDateTimeFormat(new Date()));
        complaintIndex = complaintAdapter.indexOnUpdate(serviceRequest);
        assertNotNull(complaintIndex);
        serviceRequest.setAttribValues(Collections.singletonList(new AttributeEntry("status", "REOPENED")));
        complaintIndex = complaintAdapter.indexOnUpdate(serviceRequest);
        assertNotNull(complaintIndex);
    }
	
	private ServiceRequest setUpServiceRequest() {
		ServiceRequest serviceRequest = ServiceRequest.builder().build();
		serviceRequest.setCreatedDate("20-10-2016 10:47:21");
		serviceRequest.setEscalationDate("20-10-2016 10:47:21");
		serviceRequest.setFirstName("abc");
		serviceRequest.setTenantId("ap.public");
		serviceRequest.setLat(0.0);
		serviceRequest.setLng(0.0);
		serviceRequest.setComplaintTypeCode("AOS");
		serviceRequest.setCrn("AB-123");
        final ArrayList<AttributeEntry> attribValues = new ArrayList<>();
        attribValues.add(new AttributeEntry("receivingMode", ""));
        attribValues.add(new AttributeEntry("receivingMode", ""));
        attribValues.add(new AttributeEntry("userType", ""));
        attribValues.add(new AttributeEntry("citizenFeedback", "0"));
        attribValues.add(new AttributeEntry("escalationHours", "0"));
        attribValues.add(new AttributeEntry("locationId", "1"));
        attribValues.add(new AttributeEntry("childLocationId", "2"));
        attribValues.add(new AttributeEntry("assignmentId", "1"));
        serviceRequest.setAttribValues(attribValues);
		final ComplaintType expectedComplaintType = getComplaintType();
		final Boundary expectedBoundary = getExpectedBoundary();
		//final City expectedCityContent = getExpectedCityContent();
		final Employee expectedAssignment = getExpectedEmployee();
		when(complaintTypeRepository.fetchComplaintTypeByCode("AOS","ap.public")).thenReturn(expectedComplaintType);
		when(boundaryRepository.fetchBoundaryById(1L,"ap.public")).thenReturn(expectedBoundary);
		when(boundaryRepository.fetchBoundaryById(2L,"ap.public")).thenReturn(expectedBoundary);
		when(tenantRepository.fetchTenantByCode("ap.public")).thenReturn(getCity());
		//when(cityRepository.fetchCityById(Long.valueOf(values.get("tenantId")))).thenReturn(expectedCityContent);
		when(employeeRepository.fetchEmployeeByPositionId(1L, new LocalDate(),
				serviceRequest.getTenantId())).thenReturn(expectedAssignment);
        when(departmentRestRepository.getDepartmentById(1l,"ap.public")).thenReturn(getDepartment());
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
		assign.setDepartment(1l);
		assign.setDesignation(1L);
		assignment.add(assign);
		employee.setAssignments(assignment);
		return employee;
	}

    private DepartmentRes getDepartment(){
        final Department department=Department.builder().id(1L).name("ADMINISTRATION").code("ADM").tenantId("tenantId").build();
        final List<Department> departmentList=new ArrayList<Department>();
        departmentList.add(department);
        return DepartmentRes.builder().department(departmentList).build();
    }
}