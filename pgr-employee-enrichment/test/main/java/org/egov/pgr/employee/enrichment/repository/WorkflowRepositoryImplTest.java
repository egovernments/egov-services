package org.egov.pgr.employee.enrichment.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.pgr.employee.enrichment.config.PropertiesManager;
import org.egov.pgr.employee.enrichment.consumer.contract.RequestInfo;
import org.egov.pgr.employee.enrichment.repository.contract.WorkflowRequest;
import org.egov.pgr.employee.enrichment.repository.contract.WorkflowResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(MockitoJUnitRunner.class)
public class WorkflowRepositoryImplTest {

    @Mock
    private PropertiesManager propertiesManager;
    private WorkflowRepository workflowRepository;
    private String workflowResponse;
    private MockRestServiceServer server;
    private String expectedUri = "http://workflow/create";

    @Before
    public void setUp() throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        workflowRepository = new WorkflowRepositoryImpl(restTemplate, propertiesManager);
        server = MockRestServiceServer.bindTo(restTemplate).build();
        when(propertiesManager.getCreateWorkflowUrl()).thenReturn(expectedUri);
    }

    @Test
    public void testAssigneeIsFetchedByBoundaryIdAndComplaintType() throws Exception {
        this.workflowResponse = buildSuccessResponse();
        server.expect(once(), requestTo(expectedUri))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().string(getExpectedContent()))
                .andRespond(withSuccess(this.workflowResponse, MediaType.APPLICATION_JSON));
        WorkflowResponse workflowResponse = workflowRepository.triggerWorkflow(getWorkflowRequest());

        assertEquals(Long.valueOf(333L), workflowResponse.getAssignee());
        assertEquals(Long.valueOf(312L), workflowResponse.getStateId());
    }

    private String getExpectedContent() throws JsonProcessingException {
        WorkflowRequest request = getWorkflowRequest();
        return new ObjectMapper().writeValueAsString(request);
    }

    private WorkflowRequest getWorkflowRequest() {
        RequestInfo requestInfo = new RequestInfo();
        String type = "Complaint";
        Date createdDate = new Date();
        Map<String, String> values = new HashMap<>();
        values.put("complaint_type_code", "PCMG");
        values.put("boundary_id", "12");
        return WorkflowRequest.builder().requestInfo(requestInfo).type(type).action("create")
                .createdDate(createdDate).senderName("james").status("Registered").values(values).build();
    }

    private String buildSuccessResponse() throws JsonProcessingException {
        long assigneeId = 333L;
        long stateId = 312L;
        return new ObjectMapper().writeValueAsString(new WorkflowResponse(stateId, assigneeId));
    }

}