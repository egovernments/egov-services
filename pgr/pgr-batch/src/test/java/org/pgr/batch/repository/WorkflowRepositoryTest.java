package org.pgr.batch.repository;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.pgr.batch.repository.contract.Attribute;
import org.pgr.batch.repository.contract.WorkflowRequest;
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

    @Before
    public void before(){
        RestTemplate restTemplate = new RestTemplate();
        String workflowHostName = "http://host/";
        String workflowUpdate = "workflow/task";
        workflowRepository = new WorkflowRepository(workflowHostName,workflowUpdate,restTemplate);
        server = MockRestServiceServer.bindTo(restTemplate).build();
    }

    @Test
    public void test_should_send_request_to_update_workflow_for_escalation() throws Exception {
        server.expect(once(), requestTo("http://host/workflow/task"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().string(getFileContents("updateWorkflowRequest.json")))
                .andRespond(
                        withSuccess(getFileContents("updateWorkflowResponse.json"),
                        MediaType.APPLICATION_JSON_UTF8));

        workflowRepository.update(getUpdateWorkflowRequest());

        server.verify();
    }


    private WorkflowRequest getUpdateWorkflowRequest() {
        Map<String, Attribute> valuesToSet = new HashMap<>();
        valuesToSet.put("stateId", Attribute.asStringAttr("stateId", "125"));
        valuesToSet.put("previousAssignee", Attribute.asStringAttr("previousAssignee", "2"));
        valuesToSet.put("stateDetails", Attribute.asStringAttr("stateDetails", StringUtils.EMPTY));
        valuesToSet.put("approvalComments", Attribute.asStringAttr("approvalComments", "Complaintisescalated"));
        return WorkflowRequest.builder()
                .requestInfo(getRequestInfo())
                .type("Complaint")
                .status("INPROGRESS")
                .action("update")
                .businessKey("Complaint")
                .assignee(null)
                .senderName("system")
                .tenantId("default")
                .values(valuesToSet)
                .build();
    }

    private RequestInfo getRequestInfo(){
        User userInfo = User.builder()
                .id(2L)
                .userName("system")
                .type("SYSTEM")
                .mobileNumber("0000000000")
                .name("System")
                .emailId("system@gmail.com")
                .build();

        return RequestInfo.builder()
                .action("update")
                .userInfo(userInfo)
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