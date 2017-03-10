package org.egov.pgr.employee.enrichment.repository;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.egov.pgr.employee.enrichment.Resources;
import org.egov.pgr.employee.enrichment.repository.contract.Attribute;
import org.egov.pgr.employee.enrichment.repository.contract.WorkflowRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(MockitoJUnitRunner.class)
public class WorkflowRepositoryTest {

    private MockRestServiceServer server;
    private WorkflowRepository workflowRepository;
    private Resources resources = new Resources();
    private final String workflowHostname = "http://host/";
    private final String workflowCreatePath = "workflow/create";
    private final String workflowClosePath = "workflow/close";
    private final String workflowUpdate="workflow/update";

    @Before
    public void before() {
        RestTemplate restTemplate = new RestTemplate();
        workflowRepository = new WorkflowRepository(workflowHostname, workflowCreatePath, workflowClosePath,workflowUpdate, restTemplate);
        server = MockRestServiceServer.bindTo(restTemplate).build();
    }

    @Test
    public void test_should_send_request_to_create_workflow() {
        server.expect(once(), requestTo("http://host/workflow/create"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().string(resources.getFileContents("createWorkflowRequest.json")))
                .andRespond(withSuccess(getFileContents("createWorkflowResponse.json"),
                        MediaType.APPLICATION_JSON_UTF8));

        workflowRepository.create(getCreateWorkflowRequest());

        server.verify();
    }

    @Test
    public void test_should_send_request_to_close_workflow() {
        server.expect(once(), requestTo("http://host/workflow/close"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().string(getFileContents("closeWorkflowRequest.json")))
                .andRespond(
                        withSuccess(getFileContents("closeWorkflowResponse.json"),
                                MediaType.APPLICATION_JSON_UTF8));

        workflowRepository.close(getCreateWorkflowRequest());

        server.verify();
    }

    @Test
    public void testShouldSendRequestToUpdateWorkflow() {
        server.expect(once(), requestTo("http://host/workflow/update"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().string(getFileContents("updateWorkflowRequest.json")))
                .andRespond(
                        withSuccess(getFileContents("updateWorkflowResponse.json"),
                                MediaType.APPLICATION_JSON_UTF8));
        workflowRepository.update(getCreateWorkflowRequest());

        server.verify();
    }
    private WorkflowRequest getCreateWorkflowRequest() {
        Map<String, Attribute> valuesToSet = new HashMap<>();
        valuesToSet.put("complaintTypeCode", Attribute.asStringAttr("complaintTypeCode", "PKMG"));
        valuesToSet.put("boundaryId", Attribute.asStringAttr("boundaryId", "12"));
        valuesToSet.put("stateDetails", Attribute.asStringAttr("stateDetails", StringUtils.EMPTY));
        return WorkflowRequest.builder()
                .type("Complaint")
                .status("Registered")
                .action("create")
                .businessKey("Complaint")
                .assignee(2L)
                .senderName("Harry")
                .values(valuesToSet)
                .build();
    }

    private String getFileContents(String fileName) {
        try {
            return IOUtils.toString(this.getClass().getClassLoader()
                    .getResourceAsStream(fileName), "UTF-8")
                    .replace(" ", "").replace("\n", "");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}