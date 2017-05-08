package org.egov.pgrrest.read.web.controller;

import org.egov.pgrrest.Resources;
import org.egov.pgrrest.TestConfiguration;
import org.egov.pgrrest.TestResourceReader;
import org.egov.pgrrest.common.contract.SevaRequest;
import org.egov.pgrrest.common.model.AuthenticatedUser;
import org.egov.pgrrest.common.model.Complainant;
import org.egov.pgrrest.common.model.UserType;
import org.egov.pgrrest.common.repository.UserRepository;
import org.egov.pgrrest.read.domain.exception.InvalidComplaintException;
import org.egov.pgrrest.read.domain.model.*;
import org.egov.pgrrest.read.domain.service.ComplaintService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ComplaintController.class)
@Import(TestConfiguration.class)
public class ComplaintControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private Resources resources = new Resources();

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private ComplaintService complaintService;

    @Test
    public void test_should_return_error_response_when_tenant_id_is_not_present_on_creating_a_complaint()
        throws Exception {
        when(userRepository.getUser("authToken")).thenReturn(getCitizen());
        Complaint invalidComplaint = getComplaintWithNoTenantId();
        doThrow(new InvalidComplaintException(invalidComplaint)).when(complaintService).save(any(Complaint.class),
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

    public Complaint getComplaintWithNoTenantId() {
        final ComplaintLocation complaintLocation = ComplaintLocation.builder()
            .coordinates(new Coordinates(11.22d, 12.22d, null)).build();
        final Complainant complainant = Complainant.builder()
            .userId("userId")
            .firstName("first name")
            .mobile("mobile number")
            .build();
        return Complaint.builder()
            .complainant(complainant)
            .authenticatedUser(getCitizen())
            .complaintLocation(complaintLocation)
            .tenantId(null)
            .description("description")
            .complaintType(new ComplaintType(null, "complaintCode", null))
            .build();
    }

    public AuthenticatedUser getCitizen() {
        return AuthenticatedUser.builder().id(1L).type(UserType.CITIZEN).build();
    }

    @Test
    public void testGetServiceRequests() throws Exception {
        String crn = "1234";
        String complaintType = "abc";
        String complainant = "kumar";
        String receivingMode = "MANUAL";
        String receivingCenter = "Commissioner Office";
        String location = "Election Ward No 1";
        String childLocation = "Gadu Veedhi";
        String stateId = "1";
        String assigneeId = "2";
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
        final Complainant domainComplainant = new Complainant("kumar", null, null, "mico layout", "user", "tenantId");
        final ComplaintLocation complaintLocation = new ComplaintLocation(new Coordinates(0.0, 0.0, "tenantId"), null, "34", "tenantId");
        Complaint complaint = Complaint.builder()
            .authenticatedUser(user)
            .crn(crn)
            .complaintType(new ComplaintType("abc", "complaintCode", "tenantId"))
            .address(address)
            .mediaUrls(mediaUrls)
            .complaintLocation(complaintLocation)
            .complainant(domainComplainant)
            .tenantId(jurisdictionId)
            .description(description)
            .state(stateId)
            .assignee(assigneeId)
            .receivingCenter(receivingCenter)
            .receivingMode(receivingMode)
            .complaintStatus("FORWARDED")
            .childLocation("Gadu Veedhi")
            .department("3")
            .tenantId("tenantId")
            .build();
        ComplaintSearchCriteria criteria = ComplaintSearchCriteria.builder()
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
            .build();

        List<Complaint> complaints = new ArrayList<>(Collections.singletonList(complaint));
        when(complaintService.findAll(criteria)).thenReturn(complaints);

        String content = new TestResourceReader().readResource("getServiceRequests.json");
        String expectedContent = String.format(content, crn, complaintType, complainant, receivingMode, receivingCenter,
            location, childLocation);

        mockMvc.perform(
            post("/seva/_search?tenantId=tenantId&serviceRequestId=serid_123&serviceCode=serviceCode_123&status" +
                "=REGISTERED,FORWARDED&assignmentId=10&userId=10&name=kumar&emailId=abc@gmail" +
                ".com&mobileNumber=74742487428&receivingMode=5&locationId=4&childLocationId=5")
                .content(resources.getFileContents("requestinfobody.json"))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(content().json(expectedContent));
    }

    @Test
    public void testGetServiceRequestsFailsWithoutTenantId() throws Exception {
        mockMvc.perform( post("/seva/_search?service_request_id=serid_123&service_code=serviceCode_123&status" +
            "=REGISTERED,FORWARDED&assignment_id=10&user_id=10&name=kumar&email_id=abc@gmail" +
            ".com&mobile_number=74742487428&receiving_mode=5&location_id=4&child_location_id=5")
            .content(resources.getFileContents("requestinfobody.json"))
            .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testShouldUpdateLastAccessedTime() throws Exception {
        mockMvc.perform(
            post("/seva/updateLastAccessedTime")
                .param("serviceRequestId", "crn")
                .param("tenantId", "tenantId")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(resources.getFileContents("updateLastAccessTimeRequest.json"))
        ).andExpect(status().isOk());
        verify(complaintService).updateLastAccessedTime("crn","tenantId");
    }
}