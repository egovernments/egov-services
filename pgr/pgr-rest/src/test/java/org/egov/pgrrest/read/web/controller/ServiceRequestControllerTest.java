package org.egov.pgrrest.read.web.controller;

import org.egov.pgrrest.Resources;
import org.egov.pgrrest.TestConfiguration;
import org.egov.pgrrest.common.contract.SevaRequest;
import org.egov.pgrrest.common.model.AttributeEntry;
import org.egov.pgrrest.common.model.AuthenticatedUser;
import org.egov.pgrrest.common.model.Requester;
import org.egov.pgrrest.common.model.UserType;
import org.egov.pgrrest.common.repository.UserRepository;
import org.egov.pgrrest.read.domain.exception.InvalidComplaintException;
import org.egov.pgrrest.read.domain.model.*;
import org.egov.pgrrest.read.domain.service.ServiceRequestService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ServiceRequestController.class)
@Import(TestConfiguration.class)
public class ServiceRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private Resources resources = new Resources();

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private ServiceRequestService serviceRequestService;

    @Test
    public void test_should_return_error_response_when_tenant_id_is_not_present_on_creating_a_complaint()
        throws Exception {
        when(userRepository.getUser("authToken")).thenReturn(getCitizen());
        ServiceRequest invalidComplaint = getComplaintWithNoTenantId();
        doThrow(new InvalidComplaintException(invalidComplaint)).when(serviceRequestService).save(any(ServiceRequest
                .class),
            any(SevaRequest.class));

        mockMvc.perform(post("/seva/_create")
            .param("foo", "b1", "b2")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(resources.getFileContents("createComplaintRequest.json")))
            .andExpect(status().isBadRequest())
            .andExpect(content().json(resources.getFileContents("createComplaintErrorResponse.json")));
    }

    @Test
    public void test_for_creating_a_complaint()
        throws Exception {
        when(userRepository.getUser("authToken")).thenReturn(getCitizen());

        mockMvc.perform(post("/seva/_create")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(resources.getFileContents("createComplaintRequest.json")))
            .andExpect(status().isCreated())
            .andExpect(content().json(resources.getFileContents("createComplaintResponse.json")));
    }

    @Test
    public void test_for_updating_a_complaint()
        throws Exception {
        when(userRepository.getUser("authToken")).thenReturn(getCitizen());

        mockMvc.perform(post("/seva/_update")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(resources.getFileContents("updateComplaintRequest.json")))
            .andExpect(status().isOk())
            .andExpect(content().json(resources.getFileContents("updateComplaintResponse.json")));
    }

    @Test
    public void test_should_return_service_requests_for_given_search_criteria() throws Exception {
        String crn = "1234";
        String receivingMode = "MANUAL";
        String receivingCenter = "Commissioner Office";
        String stateId = "1";
        Long assigneeId = 2L;
        String address = null;
        List<String> mediaUrls = new ArrayList<>();
        mediaUrls.add(null);
        mediaUrls.add(null);
        String jurisdictionId = "1";
        String description = null;
        String mobileNumber = null;
        String emailId = null;
        String name = "kumar";
        long id = 67;
        boolean anonymousUser = false;
        AuthenticatedUser user = AuthenticatedUser.builder()
            .mobileNumber(mobileNumber)
            .email(emailId)
            .name(name)
            .id(id)
            .anonymousUser(anonymousUser)
            .type(UserType.CITIZEN)
            .tenantId("tenantId")
            .build();
        final Requester domainComplainant =
            new Requester("kumar", null, null, "mico layout", "user");
        final Coordinates coordinates = new Coordinates(0.0, 0.0);
        final ServiceRequestLocation serviceRequestLocation = new ServiceRequestLocation(coordinates,
            null, "34");
        ServiceRequest complaint = ServiceRequest.builder()
            .authenticatedUser(user)
            .crn(crn)
            .serviceRequestType(new ServiceRequestType("abc", "complaintCode", "tenantId"))
            .address(address)
            .mediaUrls(mediaUrls)
            .serviceRequestLocation(serviceRequestLocation)
            .requester(domainComplainant)
            .tenantId(jurisdictionId)
            .description(description)
            .state(stateId)
            .assignee(assigneeId)
            .receivingCenter(receivingCenter)
            .receivingMode(receivingMode)
            .serviceRequestStatus("FORWARDED")
            .childLocation("Gadu Veedhi")
            .department(3L)
            .tenantId("tenantId")
            .build();
        ServiceRequestSearchCriteria criteria = ServiceRequestSearchCriteria.builder()
            .assignmentId(10L)
            .endDate(null)
            .escalationDate(null)
            .lastModifiedDatetime(null)
            .serviceCode("serviceCode_123")
            .serviceRequestId("serid_123").startDate(null)
            .status(Arrays.asList("REGISTERED", "FORWARDED"))
            .userId(10L)
            .emailId("abc@gmail.com")
            .mobileNumber("74742487428")
            .name(name)
            .locationId(4L)
            .childLocationId(5L)
            .receivingMode(5L)
            .tenantId("tenantId")
            .pageSize(20)
            .fromIndex(2)
            .build();

        List<ServiceRequest> complaints = new ArrayList<>(Collections.singletonList(complaint));
        when(serviceRequestService.findAll(criteria)).thenReturn(complaints);

        mockMvc.perform(
            post("/seva/_search")
                .param("tenantId", "tenantId")
                .param("serviceRequestId", "serid_123")
                .param("serviceCode", "serviceCode_123")
                .param("status", "REGISTERED")
                .param("status", "FORWARDED")
                .param("assignmentId", "10")
                .param("userId", "10")
                .param("name", "kumar")
                .param("emailId", "abc@gmail.com")
                .param("mobileNumber", "74742487428")
                .param("receivingMode", "5")
                .param("locationId", "4")
                .param("childLocationId", "5")
                .param("fromIndex", "2")
                .param("sizePerPage", "20")
                .content(resources.getFileContents("requestinfobody.json"))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(content().json(resources.getFileContents("getServiceRequests.json")));
    }

    @Test
    public void test_should_return_count_of_matching_service_requests_for_given_search_criteria() throws Exception {
        ServiceRequestSearchCriteria criteria = ServiceRequestSearchCriteria.builder()
            .assignmentId(10L)
            .endDate(null)
            .escalationDate(null)
            .lastModifiedDatetime(null)
            .serviceCode("serviceCode_123")
            .serviceRequestId("serid_123").startDate(null)
            .status(Arrays.asList("REGISTERED", "FORWARDED"))
            .userId(10L)
            .emailId("abc@gmail.com")
            .mobileNumber("74742487428")
            .name("kumar")
            .locationId(4L)
            .childLocationId(5L)
            .receivingMode(5L)
            .tenantId("tenantId")
            .build();

        when(serviceRequestService.getCount(criteria)).thenReturn(30L);

        mockMvc.perform(
            post("/seva/_count")
                .param("tenantId", "tenantId")
                .param("serviceRequestId", "serid_123")
                .param("serviceCode", "serviceCode_123")
                .param("status", "REGISTERED")
                .param("status", "FORWARDED")
                .param("assignmentId", "10")
                .param("userId", "10")
                .param("name", "kumar")
                .param("emailId", "abc@gmail.com")
                .param("mobileNumber", "74742487428")
                .param("receivingMode", "5")
                .param("locationId", "4")
                .param("childLocationId", "5")
                .content(resources.getFileContents("requestinfobody.json"))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(content().json(resources.getFileContents("getServiceRequestCount.json")));
    }

    private ServiceRequest getComplaintWithNoTenantId() {
        final ServiceRequestLocation serviceRequestLocation = ServiceRequestLocation.builder()
            .coordinates(new Coordinates(11.22d, 12.22d)).build();
        final Requester complainant = Requester.builder()
            .userId("userId")
            .firstName("first name")
            .mobile("mobile number")
            .build();
        return ServiceRequest.builder()
            .requester(complainant)
            .authenticatedUser(getCitizen())
            .serviceRequestLocation(serviceRequestLocation)
            .tenantId(null)
            .description("description")
            .serviceRequestType(new ServiceRequestType(null, "complaintCode", null))
            .attributeEntries(new ArrayList<>())
            .build();
    }

    private AuthenticatedUser getCitizen() {
        return AuthenticatedUser.builder().id(1L).type(UserType.CITIZEN).build();
    }

}