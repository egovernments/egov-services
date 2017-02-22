package org.egov.pgr.employee.enrichment.repository;

import org.egov.pgr.employee.enrichment.Resources;
import org.egov.pgr.employee.enrichment.repository.contract.WorkflowRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(MockitoJUnitRunner.class)
public class WorkflowRepositoryTest {

    private static final String HOST = "http://host";
    private static final String CREATE_URL = "/workflow/create";
    private MockRestServiceServer server;
    private WorkflowRepository workflowRepository;
    private Resources resources = new Resources();

    @Before
    public void before() {
        RestTemplate restTemplate = new RestTemplate();
        workflowRepository = new WorkflowRepository(restTemplate, HOST, CREATE_URL);
        server = MockRestServiceServer.bindTo(restTemplate).build();
    }

    @Test
    public void test_should_send_request_to_create_workflow() {
        server.expect(once(), requestTo("http://host/workflow/create"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().string(resources.getFileContents("createWorkflowRequest.json")))
                .andRespond(
                        withSuccess(resources.getFileContents("createWorkflowResponse.json"),
                                MediaType.APPLICATION_JSON_UTF8));
        final WorkflowRequest workflowRequest = WorkflowRequest.builder().build();

        workflowRepository.create(workflowRequest);

        server.verify();
    }
}

