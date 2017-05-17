package org.egov.pgrrest.read.domain.service;

import org.egov.common.contract.request.RequestInfo;
import org.egov.pgrrest.common.contract.SevaRequest;
import org.egov.pgrrest.common.model.AuthenticatedUser;
import org.egov.pgrrest.common.model.Requester;
import org.egov.pgrrest.common.model.UserType;
import org.egov.pgrrest.common.repository.ComplaintJpaRepository;
import org.egov.pgrrest.common.repository.UserRepository;
import org.egov.pgrrest.read.domain.model.*;
import org.egov.pgrrest.read.persistence.repository.ServiceRequestRepository;
import org.egov.pgrrest.read.web.contract.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
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
    private ComplaintJpaRepository complaintJpaRepository;

    @InjectMocks
    private ServiceRequestService serviceRequestService;

    @Test
    public void testShouldValidateComplaintOnSave() {
        final ServiceRequest complaint = mock(ServiceRequest.class);
        when(complaint.getAuthenticatedUser()).thenReturn(getCitizen());
        final SevaRequest sevaRequest = getSevaRequest();
        when(userRepository.getUserByUserName("anonymous","tenantId")).thenReturn(populateUser());
        serviceRequestService.save(complaint, sevaRequest);

        verify(complaint, times(1)).validate();
    }

    @Test
    public void testShouldValidateComplaintOnUpdate() {
        final ServiceRequest complaint = mock(ServiceRequest.class);
        when(complaint.getAuthenticatedUser()).thenReturn(getCitizen());
        final SevaRequest sevaRequest = getSevaRequest();
        when(userRepository.getUserByUserName("anonymous","tenantId")).thenReturn(populateUser());
        serviceRequestService.update(complaint, sevaRequest);

        verify(complaint, times(1)).validate();
    }

    @Test
    public void testShouldUpdateSevaRequestWithDomainComplaintOnSave() {
        final ServiceRequest complaint = getComplaint();
        final SevaRequest sevaRequest = mock(SevaRequest.class);
        when(userRepository.getUserByUserName("anonymous","tenantId")).thenReturn(populateUser());
        serviceRequestService.save(complaint, sevaRequest);

        verify(sevaRequest).update(complaint);
    }

    @Test
    public void testShouldUpdateSevaRequestWithDomainComplaintOnUpdate() {
        final ServiceRequest complaint = getComplaint();
        final SevaRequest sevaRequest = mock(SevaRequest.class);
        when(userRepository.getUserByUserName("anonymous","tenantId")).thenReturn(populateUser());
        serviceRequestService.update(complaint, sevaRequest);

        verify(sevaRequest).update(complaint);
    }

    @Test
    public void testShouldSetGeneratedCrnToDomainComplaintOnSave() {
        final ServiceRequest complaint = getComplaint();
        final SevaRequest sevaRequest = getSevaRequest();
        when(sevaNumberGeneratorService.generate()).thenReturn(CRN);
        when(userRepository.getUserByUserName("anonymous","tenantId")).thenReturn(populateUser());
        serviceRequestService.save(complaint, sevaRequest);

        assertEquals(CRN, complaint.getCrn());
    }

    @Test
    public void test_should_set_anonymous_user_when_user_is_not_authenticated() {
        final ServiceRequest complaint = getComplaint();
        final SevaRequest sevaRequest = getSevaRequest();
        sevaRequest.getRequestInfo().setUserInfo(null);
        when(sevaNumberGeneratorService.generate()).thenReturn(CRN);
        when(userRepository.getUserByUserName("anonymous","tenantId")).thenReturn(populateUser());

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
        when(sevaNumberGeneratorService.generate()).thenReturn(CRN);

        serviceRequestService.save(complaint, sevaRequest);

        final org.egov.common.contract.request.User userInfo = sevaRequest.getRequestInfo().getUserInfo();
        assertNotNull(userInfo);
        assertEquals(Long.valueOf(2L), userInfo.getId());
        assertEquals("CITIZEN", userInfo.getType());
    }


    @Test
    public void testShouldPersistSevaRequestOnSave() {
        final ServiceRequest complaint = getComplaint();
        final org.egov.pgrrest.common.contract.ServiceRequest serviceRequest = getServiceRequest();
        final SevaRequest sevaRequest = new SevaRequest(new RequestInfo(), serviceRequest);
        when(sevaNumberGeneratorService.generate()).thenReturn(CRN);
        when(userRepository.getUserByUserName("anonymous","tenantId")).thenReturn(populateUser());
        serviceRequestService.save(complaint, sevaRequest);

        verify(complaintRepository).save(sevaRequest);
    }

    @Test
    public void testShouldPersistSevaRequestOnUpdate() {
        final ServiceRequest complaint = getComplaint();
        final org.egov.pgrrest.common.contract.ServiceRequest serviceRequest = getServiceRequest();
        final SevaRequest sevaRequest = new SevaRequest(new RequestInfo(), serviceRequest);
        when(userRepository.getUserByUserName("anonymous","tenantId")).thenReturn(populateUser());
        serviceRequestService.update(complaint, sevaRequest);

        verify(complaintRepository).update(sevaRequest);
    }

    @Test
    public void testShouldFindAllComplaintsBySearchCriteria() {
        final ServiceRequestSearchCriteria searchCriteria = ServiceRequestSearchCriteria.builder().build();
        final ServiceRequest expectedComplaint = getComplaint();
        when(complaintRepository.findAll(searchCriteria)).thenReturn(Collections.singletonList(expectedComplaint));

        final List<ServiceRequest> actualComplaints = serviceRequestService.findAll(searchCriteria);

        assertEquals(1, actualComplaints.size());
        assertEquals(expectedComplaint, actualComplaints.get(0));
    }

    @Test
    public void testShouldUpdateLastAccessedTime() {
       serviceRequestService.updateLastAccessedTime("crn", "tenantId");
        verify(complaintJpaRepository).updateLastAccessedTime(any(Date.class), eq("crn"), eq("tenantId"));
        }
    
    public void test_should_fetch_all_modified_citizen_complaints_by_user_id() {
        final ServiceRequest expectedComplaint = getComplaint();
        when(complaintRepository.getAllModifiedServiceRequestsForCitizen(any(Long.class),any(String.class)))
            .thenReturn(Collections.singletonList(expectedComplaint));
        final List<ServiceRequest> actualComplaints = serviceRequestService.getAllModifiedCitizenComplaints(1L, "tenantId");
        assertEquals(1, actualComplaints.size());
        assertEquals(expectedComplaint, actualComplaints.get(0));
    }

    @Test
    public void test_should_fetch_empty_list_for_invalid_userid() {
        when(complaintRepository.getAllModifiedServiceRequestsForCitizen(any(Long.class),any(String.class)))
            .thenReturn(new ArrayList<>());
        final List<ServiceRequest> actualComplaints = serviceRequestService.getAllModifiedCitizenComplaints(1L, "tenantId");
        assertEquals(0, actualComplaints.size());
    }

    private ServiceRequest getComplaint() {
        final Coordinates coordinates = new Coordinates(0d, 0d, "tenantId");
        final ServiceRequestLocation serviceRequestLocation = new ServiceRequestLocation(coordinates, "id", null, "tenantId");
        final Requester complainant = Requester.builder()
            .userId("userId")
            .firstName("first name")
            .mobile("mobile number")
            .build();
        return ServiceRequest.builder()
            .requester(complainant)
            .authenticatedUser(getCitizen())
            .serviceRequestLocation(serviceRequestLocation)
            .tenantId(TENANT_ID)
            .description("description")
            .crn("crn")
            .lastAccessedTime(new Date())
            .department(2L)
            .complaintType(new ComplaintType(null, "complaintCode", "tenantId"))
            .build();
    }

    private AuthenticatedUser getCitizen() {
        return AuthenticatedUser.builder().id(1L).type(UserType.CITIZEN).build();
    }

    private SevaRequest getSevaRequest() {
        final org.egov.pgrrest.common.contract.ServiceRequest serviceRequest = org.egov.pgrrest.common.contract
            .ServiceRequest.builder().tenantId("tenantId").build();
        return new SevaRequest(new RequestInfo(), serviceRequest);
    }

    private org.egov.pgrrest.common.contract.ServiceRequest getServiceRequest() {
        return org.egov.pgrrest.common.contract.ServiceRequest.builder().tenantId("tenantId").build();
    }

    private User populateUser() {
        return User.builder().id(1L).name("user").build();
    }

}