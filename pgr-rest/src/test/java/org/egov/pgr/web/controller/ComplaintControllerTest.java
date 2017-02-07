package org.egov.pgr.web.controller;

import org.egov.pgr.domain.model.*;
import org.egov.pgr.domain.service.ComplaintService;
import org.egov.pgr.domain.service.UserService;
import org.helper.TestResourceReader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ComplaintController.class)
public class ComplaintControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private ComplaintService mockComplaintService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Test
    public void testGetServiceRequests() throws Exception {
        String crn = "1234";
        String complaintType = "abc";
        String complainant = "kumar";
        String receivingMode = "MANUAL";
        String receivingCenter = "Commissioner Office";
        String location = "Election Ward No 1";
        String childLocation = "Gadu Veedhi";

        final HashMap<String, String> additionalValues = new HashMap<>();
        additionalValues.put("ReceivingMode", receivingMode);
        additionalValues.put("LocationId", location);
        additionalValues.put("ChildLocationId", childLocation);
        additionalValues.put("ReceivingCenter", receivingCenter);

        Complaint complaint = Complaint.builder()
                .crn(crn)
                .closed(false)
                .complaintType(new ComplaintType(complaintType, "complaintCode"))
                .complainant(new Complainant(complainant, "phone", "email"))
                .complaintLocation(new ComplaintLocation(new Coordinates(0d, 0d), null))
                .additionalValues(additionalValues)
                .authenticatedUser(AuthenticatedUser.createAnonymousUser())
                .build();

        List<Complaint> complaints = new ArrayList<>(Collections.singletonList(complaint));
        when(mockComplaintService.findAll(any(ComplaintSearchCriteria.class))).thenReturn(complaints);

        String content = new TestResourceReader().readResource("getServiceRequests.txt");
        String expectedContent = String.format(
                content, crn,
                complaintType, complainant, receivingMode, receivingCenter,
                location, childLocation);

        mockMvc.perform(get("/seva?jurisdiction_id=1")
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .header("api_id", "api_id")
                .header("ver", "ver")
                .header("ts", "ts")
                .header("action", "action")
                .header("did", "did")
                .header("msg_id", "msg_id")
                .header("requester_id", "requester_id")
                .header("auth_token", "auth_token"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedContent));
    }

    @Test
    public void testGetServiceRequestsFailsWithoutJurisdictionId() throws Exception {
        mockMvc.perform(get("/seva")
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .header("api_id", "api_id")
                .header("ver", "ver")
                .header("ts", "ts")
                .header("action", "action")
                .header("did", "did")
                .header("msg_id", "msg_id")
                .header("requester_id", "requester_id")
                .header("auth_token", "auth_token"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testGetServiceRequestsFailsWithoutRequiredHeaders() throws Exception {
        mockMvc.perform(get("/seva?jurisdiction_id=1")
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .header("ts", "ts")
                .header("action", "action")
                .header("did", "did")
                .header("msg_id", "msg_id")
                .header("auth_token", "auth_token"))
                .andExpect(status().isNotFound());
    }
}