package org.egov.pgr.controller;

import org.egov.pgr.entity.Complaint;
import org.egov.pgr.entity.enums.ComplaintStatus;
import org.egov.pgr.producer.GrievanceProducer;
import org.egov.pgr.service.ComplaintService;
import org.egov.pgr.service.SevaNumberGeneratorService;
import org.egov.pgr.repository.specification.SevaSpecification;
import org.helper.ComplaintBuilder;
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
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ServiceRequestController.class)
public class ServiceRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SevaNumberGeneratorService mockSevaNumberGeneratorService;

    @MockBean
    private GrievanceProducer mockGrievanceProducer;

    @MockBean
    private ComplaintService mockComplaintService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Test
    public void testGetServiceRequests() throws Exception {
        String crn = "1234";
        String complaintType = "abc";
        String complainant = "kumar";

        Complaint complaint = new ComplaintBuilder()
                .crn(crn)
                .complaintStatus(ComplaintStatus.REGISTERED)
                .complaintType(complaintType)
                .complainant(complainant)
                .buildComplaint();
        List<Complaint> complaints = new ArrayList<>(Collections.singletonList(complaint));
        when(mockComplaintService.findAll(any(SevaSpecification.class))).thenReturn(complaints);

        String content = new TestResourceReader().readResource("getServiceRequests.txt");
        String expectedContent = String.format(content, crn, String.valueOf(ComplaintStatus.REGISTERED),
                complaintType, complainant);

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