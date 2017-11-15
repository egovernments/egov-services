package org.egov.pgrrest.read.domain.service;

import org.egov.common.contract.request.RequestInfo;
import org.egov.pgr.common.model.Employee;
import org.egov.pgr.common.repository.EmployeeRepository;
import org.egov.pgrrest.common.contract.web.SevaRequest;
import org.egov.pgrrest.common.domain.model.AttributeEntry;
import org.egov.pgrrest.common.domain.model.AuthenticatedUser;
import org.egov.pgrrest.common.domain.model.Requester;
import org.egov.pgrrest.common.domain.model.UserType;
import org.egov.pgrrest.common.persistence.repository.UserRepository;
import org.egov.pgrrest.read.domain.model.*;
import org.egov.pgrrest.read.persistence.repository.ServiceRequestRepository;
import org.egov.pgrrest.read.persistence.repository.SubmissionRepository;
import org.egov.pgrrest.read.web.contract.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ServiceRequestServiceTest {

    private static final String CRN = "crn";
    private static final String TENANT_ID = "tenantId";
    @Mock
    private ServiceRequestRepository complaintRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SevaNumberGeneratorService sevaNumberGeneratorService;

    @Mock
    private ServiceRequestTypeService serviceRequestTypeService;

    private ServiceRequestService serviceRequestService;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private SubmissionRepository submissionRepository;

    @Mock
    private ServiceRequestValidator serviceRequestValidator;

    @Mock
    private ServiceRequestCustomFieldService customFieldsService;

    @Mock
    private DraftService draftService;

    private
    @Value("${postgres.enabled}")
    boolean postgresEnabled;

    @Before
    public void before() {
        when(serviceRequestValidator.canValidate(any())).thenReturn(true);
        final List<ServiceRequestValidator> validators = Collections.singletonList(serviceRequestValidator);
        serviceRequestService = new ServiceRequestService(complaintRepository, sevaNumberGeneratorService,
            userRepository, serviceRequestTypeService, validators, customFieldsService, draftService, postgresEnabled);
    }

    @Test
    public void test_should_validate_service_request_on_save() {
        final ServiceRequest serviceRequest = mock(ServiceRequest.class);
        when(serviceRequest.getAuthenticatedUser()).thenReturn(getCitizen());
        final SevaRequest sevaRequest = getSevaRequest();
        when(userRepository.getUserByUserName("anonymous", "tenantId")).thenReturn(populateUser());
        serviceRequestService.save(serviceRequest, sevaRequest);

        verify(serviceRequestValidator, times(1)).validate(serviceRequest);
    }

    @Test
    public void test_should_delete_draft_when_present_on_save() {
        final ServiceRequest serviceRequest = mock(ServiceRequest.class);
        when(serviceRequest.getDraftId()).thenReturn(3L);
        when(serviceRequest.getAuthenticatedUser()).thenReturn(getCitizen());
        final SevaRequest sevaRequest = getSevaRequest();
        when(userRepository.getUserByUserName("anonymous", "tenantId")).thenReturn(populateUser());

        serviceRequestService.save(serviceRequest, sevaRequest);

        verify(draftService).delete(3L);
    }

    @Test
    public void test_should_not_delete_draft_when_draft_not_present_on_save() {
        final ServiceRequest serviceRequest = mock(ServiceRequest.class);
        when(serviceRequest.getDraftId()).thenReturn(null);
        when(serviceRequest.getAuthenticatedUser()).thenReturn(getCitizen());
        final SevaRequest sevaRequest = getSevaRequest();
        when(userRepository.getUserByUserName("anonymous", "tenantId")).thenReturn(populateUser());

        serviceRequestService.save(serviceRequest, sevaRequest);

        verify(draftService, times(0)).delete(any());
    }

    @Test
    public void test_should_validate_service_request_on_update() {
        final ServiceRequest serviceRequest = mock(ServiceRequest.class);
        when(serviceRequest.getAuthenticatedUser()).thenReturn(getCitizen());
        when(serviceRequest.getTenantId()).thenReturn("tenantId");
        final SevaRequest sevaRequest = getSevaRequest();
        when(userRepository.getUserByUserName("anonymous", "tenantId")).thenReturn(populateUser());
        when(employeeRepository.getEmployeeById(1L, "tenantId")).thenReturn(getEmployee());
        when(submissionRepository.getPosition(serviceRequest.getCrn(), serviceRequest.getTenantId()))
            .thenReturn(1L);

        serviceRequestService.update(serviceRequest, sevaRequest);

        verify(serviceRequestValidator, times(1)).validate(serviceRequest);
    }

    @Test
    public void test_should_delete_draft_when_present_on_update() {
        final ServiceRequest serviceRequest = mock(ServiceRequest.class);
        when(serviceRequest.getDraftId()).thenReturn(4L);
        when(serviceRequest.getAuthenticatedUser()).thenReturn(getCitizen());
        when(serviceRequest.getTenantId()).thenReturn("tenantId");
        final SevaRequest sevaRequest = getSevaRequest();
        when(userRepository.getUserByUserName("anonymous", "tenantId")).thenReturn(populateUser());
        when(employeeRepository.getEmployeeById(1L, "tenantId")).thenReturn(getEmployee());
        when(submissionRepository.getPosition(serviceRequest.getCrn(), serviceRequest.getTenantId()))
            .thenReturn(1L);

        serviceRequestService.update(serviceRequest, sevaRequest);

        verify(draftService).delete(4L);
    }

    @Test
    public void test_should_not_delete_draft_when_draft_not_present_on_update() {
        final ServiceRequest serviceRequest = mock(ServiceRequest.class);
        when(serviceRequest.getDraftId()).thenReturn(null);
        when(serviceRequest.getAuthenticatedUser()).thenReturn(getCitizen());
        when(serviceRequest.getTenantId()).thenReturn("tenantId");
        final SevaRequest sevaRequest = getSevaRequest();
        when(userRepository.getUserByUserName("anonymous", "tenantId")).thenReturn(populateUser());
        when(employeeRepository.getEmployeeById(1L, "tenantId")).thenReturn(getEmployee());
        when(submissionRepository.getPosition(serviceRequest.getCrn(), serviceRequest.getTenantId()))
            .thenReturn(1L);

        serviceRequestService.update(serviceRequest, sevaRequest);

        verify(draftService, times(0)).delete(any());
    }

    @Test
    public void testShouldUpdateSevaRequestWithDomainComplaintOnSave() {
        final ServiceRequest complaint = getComplaint();
        final SevaRequest sevaRequest = mock(SevaRequest.class);
        when(userRepository.getUserByUserName("anonymous", "tenantId")).thenReturn(populateUser());
        serviceRequestService.save(complaint, sevaRequest);

        verify(sevaRequest).update(complaint);
    }

    @Test
    public void testShouldSetGeneratedCrnToDomainComplaintOnSave() {
        final ServiceRequest complaint = getComplaint();
        final SevaRequest sevaRequest = getSevaRequest();
        when(sevaNumberGeneratorService.generate("tenantId")).thenReturn(CRN);
        when(userRepository.getUserByUserName("anonymous", "tenantId")).thenReturn(populateUser());
        serviceRequestService.save(complaint, sevaRequest);

        assertEquals(CRN, complaint.getCrn());
    }

    @Test
    public void test_should_set_anonymous_user_when_user_is_not_authenticated() {
        final ServiceRequest complaint = getComplaint();
        final SevaRequest sevaRequest = getSevaRequest();
        sevaRequest.getRequestInfo().setUserInfo(null);
        when(sevaNumberGeneratorService.generate("default")).thenReturn(CRN);
        when(userRepository.getUserByUserName("anonymous", "tenantId")).thenReturn(populateUser());

        serviceRequestService.save(complaint, sevaRequest);

        final org.egov.common.contract.request.User userInfo = sevaRequest.getRequestInfo().getUserInfo();
        assertNotNull(userInfo);
        assertEquals(Long.valueOf(1L), userInfo.getId());
        assertEquals("SYSTEM", userInfo.getType());
    }

    @Test
    public void test_should_not_manipulate_user_info_when_when_user_is_authenticated() {
        final ServiceRequest complaint = getComplaint();
        final SevaRequest sevaRequest = getSevaRequest();
        final org.egov.common.contract.request.User user = org.egov.common.contract.request.User.builder()
            .type("CITIZEN")
            .id(2L)
            .build();
        sevaRequest.getRequestInfo().setUserInfo(user);
        when(sevaNumberGeneratorService.generate("default")).thenReturn(CRN);

        serviceRequestService.save(complaint, sevaRequest);

        final org.egov.common.contract.request.User userInfo = sevaRequest.getRequestInfo().getUserInfo();
        assertNotNull(userInfo);
        assertEquals(Long.valueOf(2L), userInfo.getId());
        assertEquals("CITIZEN", userInfo.getType());
    }


    @Test
    public void testShouldPersistSevaRequestOnSave() {
        final ServiceRequest complaint = getComplaint();
        final org.egov.pgrrest.common.contract.web.ServiceRequest serviceRequest = getServiceRequest();
        final SevaRequest sevaRequest = new SevaRequest(new RequestInfo(), serviceRequest);
        when(sevaNumberGeneratorService.generate("default")).thenReturn(CRN);
        when(userRepository.getUserByUserName("anonymous", "tenantId")).thenReturn(populateUser());
        serviceRequestService.save(complaint, sevaRequest);

        verify(complaintRepository).save(sevaRequest);
    }

    @Test
    public void testShouldPersistSevaRequestOnUpdate() {
        final ServiceRequest complaint = getComplaint();
        final org.egov.pgrrest.common.contract.web.ServiceRequest serviceRequest = getServiceRequest();
        final SevaRequest sevaRequest = new SevaRequest(new RequestInfo(), serviceRequest);
        when(userRepository.getUserByUserName("anonymous", "tenantId")).thenReturn(populateUser());
        when(employeeRepository.getEmployeeById(1L, "tenantId")).thenReturn(getEmployee());
        when(submissionRepository.getPosition(complaint.getCrn(), complaint.getTenantId()))
            .thenReturn(1L);
        serviceRequestService.update(complaint, sevaRequest);

        verify(complaintRepository).update(sevaRequest);
    }

    @Test
    public void testShouldFindAllComplaintsBySearchCriteria() {
        final ServiceRequestSearchCriteria searchCriteria = ServiceRequestSearchCriteria.builder().build();
        final ServiceRequest expectedComplaint = getComplaint();
        when(complaintRepository.find(searchCriteria)).thenReturn(Collections.singletonList(expectedComplaint));

        final List<ServiceRequest> actualComplaints = serviceRequestService.findAll(searchCriteria);

        assertEquals(1, actualComplaints.size());
        assertEquals(expectedComplaint, actualComplaints.get(0));
    }

    private ServiceRequest getComplaint() {
        final Coordinates coordinates = new Coordinates(0d, 0d);
        final ServiceRequestLocation serviceRequestLocation =
            new ServiceRequestLocation(coordinates, "id", null);
        final Requester complainant = Requester.builder()
            .userId("userId")
            .firstName("first name")
            .mobile("mobile number")
            .email("email@gmail.com")
            .build();
        final ServiceRequestType serviceRequestType =
            new ServiceRequestType(null, "complaintCode", "tenantId", null);
        final ArrayList<AttributeEntry> attributeEntries = new ArrayList<>();
        attributeEntries.add(new AttributeEntry("systemStatus", "REGISTERED"));
        return ServiceRequest.builder()
            .requester(complainant)
            .authenticatedUser(getCitizen())
            .serviceRequestLocation(serviceRequestLocation)
            .tenantId(TENANT_ID)
            .description("description")
            .crn("crn")
            .department(2L)
            .serviceRequestType(serviceRequestType)
            .attributeEntries(attributeEntries)
            .build();
    }

    private AuthenticatedUser getCitizen() {
        return AuthenticatedUser.builder().id(1L).type(UserType.CITIZEN).roleCodes(getUserRoles()).build();
    }

    private SevaRequest getSevaRequest() {
        final org.egov.pgrrest.common.contract.web.ServiceRequest serviceRequest = org.egov.pgrrest.common.contract
            .web.ServiceRequest.builder().tenantId("tenantId").build();
        return new SevaRequest(new RequestInfo(), serviceRequest);
    }

    private org.egov.pgrrest.common.contract.web.ServiceRequest getServiceRequest() {
        return org.egov.pgrrest.common.contract.web.ServiceRequest.builder()
            .tenantId("tenantId")
            .attribValues(new ArrayList<>())
            .build();
    }

    private User populateUser() {
        return User.builder().id(1L).name("user").build();
    }

    private Employee getEmployee() {
        return Employee.builder().primaryPosition(2L).build();
    }

    private List<String> getUserRoles() {
        return Arrays.asList("GRO");
    }

}