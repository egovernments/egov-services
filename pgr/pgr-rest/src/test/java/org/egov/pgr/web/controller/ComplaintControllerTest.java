package org.egov.pgr.web.controller;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.*;

import org.egov.pgr.Resources;
import org.egov.pgr.domain.exception.InvalidComplaintException;
import org.egov.pgr.domain.model.*;
import org.egov.pgr.domain.service.ComplaintService;
import org.egov.pgr.domain.service.UserService;
import org.egov.pgr.TestResourceReader;
import org.egov.pgr.persistence.queue.contract.SevaRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(ComplaintController.class)
public class ComplaintControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private Resources resources = new Resources();

    @MockBean
    private UserService userService;

    @MockBean
    private ComplaintService complaintService;

    @Test
    public void test_should_return_error_response_when_tenant_id_is_not_present_on_creating_a_complaint()
            throws Exception {
        when(userService.getUser("authToken")).thenReturn(getCitizen());
        Complaint invalidComplaint = getComplaintWithNoTenantId();
        doThrow(new InvalidComplaintException(invalidComplaint))
                .when(complaintService).save(any(Complaint.class), any(SevaRequest.class));

        mockMvc.perform(
                post("/seva")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(resources.getFileContents("createComplaintRequest.json")))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(resources.getFileContents("createComplaintErrorResponse.json")));
    }

    public Complaint getComplaintWithNoTenantId() {
        final ComplaintLocation complaintLocation = ComplaintLocation.builder()
                .coordinates(new Coordinates(12.22d, 11.22d))
                .build();
        final Complainant complainant = Complainant.builder()
                .userId("userId")
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
        return AuthenticatedUser.builder()
                .id(1)
                .type(Collections.singletonList(UserType.CITIZEN))
                .build();
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

        final HashMap<String, String> additionalValues = new HashMap<>();
        additionalValues.put("ReceivingMode", receivingMode);
        additionalValues.put("LocationId", location);
        additionalValues.put("ChildLocationId", childLocation);
        additionalValues.put("ReceivingCenter", receivingCenter);
        additionalValues.put("StateId", stateId);

        Complaint complaint = Complaint.builder().crn(crn).closed(false)
                .complaintType(new ComplaintType(complaintType, "complaintCode"))
                .complainant(new Complainant(complainant, null, null, null, null))
                .complaintLocation(new ComplaintLocation(new Coordinates(0d, 0d), null, null))
                .additionalValues(additionalValues).authenticatedUser(AuthenticatedUser.createAnonymousUser()).build();

        List<Complaint> complaints = new ArrayList<>(Collections.singletonList(complaint));
        when(complaintService.findAll(any(ComplaintSearchCriteria.class))).thenReturn(complaints);

        String content = new TestResourceReader().readResource("getServiceRequests.txt");
        String expectedContent = String.format(content, crn, complaintType, complainant, receivingMode, receivingCenter,
                location, childLocation);

        mockMvc.perform(get("/seva?jurisdiction_id=1")
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8")).header("api_id", "api_id")
                .header("ver", "ver").header("ts", "ts").header("action", "action").header("did", "did")
                .header("msg_id", "msg_id").header("requester_id", "requester_id").header("auth_token", "auth_token"))
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
                get("/seva?jurisdiction_id=1").accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                        .header("ts", "ts").header("action", "action").header("did", "did").header("msg_id", "msg_id")
                        .header("auth_token", "auth_token"))
                .andExpect(status().isNotFound());
    }
}