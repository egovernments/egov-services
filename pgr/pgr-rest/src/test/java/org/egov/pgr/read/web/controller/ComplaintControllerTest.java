package org.egov.pgr.read.web.controller;

import org.egov.pgr.Resources;
import org.egov.pgr.TestConfiguration;
import org.egov.pgr.TestResourceReader;
import org.egov.pgr.common.contract.SevaRequest;
import org.egov.pgr.common.model.AuthenticatedUser;
import org.egov.pgr.common.model.Complainant;
import org.egov.pgr.common.model.UserType;
import org.egov.pgr.common.repository.UserRepository;
import org.egov.pgr.read.domain.exception.InvalidComplaintException;
import org.egov.pgr.read.domain.model.*;
import org.egov.pgr.read.domain.service.ComplaintService;
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

        mockMvc.perform(post("/seva")
            .param("foo", "b1", "b2")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(resources.getFileContents("createComplaintRequest.json")))
            .andExpect(status().isBadRequest())
            .andExpect(content().json(resources.getFileContents("createComplaintErrorResponse.json")));
    }

    public Complaint getComplaintWithNoTenantId() {
        final ComplaintLocation complaintLocation = ComplaintLocation.builder()
            .coordinates(new Coordinates(11.22d, 12.22d)).build();
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
            .complaintType(new ComplaintType(null, "complaintCode"))
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
            .build();
        final Complainant domainComplainant = new Complainant("kumar", null, null, "mico layout", "user");
        final ComplaintLocation complaintLocation = new ComplaintLocation(new Coordinates(0.0, 0.0), null, "34");
        Complaint complaint = Complaint.builder()
            .authenticatedUser(user)
            .crn(crn)
            .complaintType(new ComplaintType("abc", "complaintCode"))
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
            .build();
        ComplaintSearchCriteria criteria = ComplaintSearchCriteria.builder()
            .assignmentId(10L)
            .endDate(null)
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
            .build();

        List<Complaint> complaints = new ArrayList<>(Collections.singletonList(complaint));
        when(complaintService.findAll(criteria)).thenReturn(complaints);

        String content = new TestResourceReader().readResource("getServiceRequests.json");
        String expectedContent = String.format(content, crn, complaintType, complainant, receivingMode, receivingCenter,
            location, childLocation);

        mockMvc.perform(
            get("/seva?jurisdiction_id=1&service_request_id=serid_123&service_code=serviceCode_123&status" +
                "=REGISTERED,FORWARDED&assignment_id=10&user_id=10&name=kumar&email_id=abc@gmail" +
                ".com&mobile_number=74742487428&receiving_mode=5&location_id=4&child_location_id=5")
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8")).header("api_id", "api_id")
                .header("ver", "ver").header("ts", "ts").header("action", "action").header("did", "did")
                .header("msg_id", "msg_id").header("requester_id", "requester_id")
                .header("auth_token", "auth_token"))
            .andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(content().json(expectedContent));
    }

    @Test
    public void testGetServiceRequestsFailsWithoutJurisdictionId() throws Exception {
        mockMvc.perform(get("/seva").accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
            .header("api_id", "api_id").header("ver", "ver").header("ts", "ts").header("action", "action")
            .header("did", "did").header("msg_id", "msg_id").header("requester_id", "requester_id")
            .header("auth_token", "auth_token")).andExpect(status().isBadRequest())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testGetServiceRequestsFailsWithoutRequiredHeaders() throws Exception {
        mockMvc.perform(
            get("/seva")
                .param("jurisdiction_id", "1")
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .header("ts", "ts").header("action", "action")
                .header("did", "did").header("msg_id", "msg_id")
                .header("auth_token", "auth_token"))
            .andExpect(status().isNotFound());
    }

    @Test
    public void testShouldUpdateLastAccessedTime() throws Exception {
        mockMvc.perform(
            post("/seva/updateLastAccessedTime")
                .param("serviceRequestId", "crn")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(resources.getFileContents("updateLastAccessTimeRequest.json"))
        ).andExpect(status().isOk());
        verify(complaintService).updateLastAccessedTime("crn");
    }
}