package org.egov.workflow.web.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.egov.workflow.domain.model.RequestContext;
import org.egov.workflow.repository.entity.Task;
import org.egov.workflow.service.Workflow;
import org.egov.workflow.web.contract.Attribute;
import org.egov.workflow.web.contract.ProcessInstance;
import org.egov.workflow.web.contract.Value;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(WorkFlowController.class)
public class WorkFlowControllerTest {

    private static final String TENANT_ID = "tenantId";
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private Workflow workflow;

    @Test
    public void test_should_create_workflow() throws Exception {
        final ProcessInstance expectedProcessInstance = ProcessInstance.builder()
                .action("create")
                .assignee(2L)
                .businessKey("765")
                .type("Complaint")
                .description("Grievance registered successfully")
                .senderName("harry")
                .build();

        when(workflow.start(eq(TENANT_ID), argThat(new ProcessInstanceMatcher(expectedProcessInstance))))
                .thenReturn(expectedProcessInstance);

        mockMvc.perform(post("/create")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(getFileContents("startWorkflowRequest.json"))
                .header("X-CORRELATION-ID", "someId"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));

        assertEquals("someId", RequestContext.getId());
    }

    @Test
    public void test_should_close_workflow() throws Exception {
        final ProcessInstance expectedProcessInstance = ProcessInstance.builder()
                .action("END")
                .assignee(2L)
                .businessKey("765")
                .type("Complaint")
                .description("Workflow Terminated")
                .senderName("garry")
                .build();

        when(workflow.end(eq(TENANT_ID), argThat(new processInstanceMatcherForCloseWorkflow(expectedProcessInstance))))
                .thenReturn(expectedProcessInstance);

        mockMvc.perform(post("/close")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(getFileContents("closeWorkflowRequest.json"))
                .header("X-CORRELATION-ID", "someId"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));

        assertEquals("someId", RequestContext.getId());
    }

    private String getFileContents(final String fileName) {
        try {
            return IOUtils.toString(this.getClass().getClassLoader()
                    .getResourceAsStream(fileName), "UTF-8");
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    class ProcessInstanceMatcher extends ArgumentMatcher<ProcessInstance> {

        public static final String BOUNDARY_ID = "boundaryId";
        public static final String COMPLAINT_TYPE_CODE = "complaintTypeCode";

        private ProcessInstance expectedProcessInstance;

        public ProcessInstanceMatcher(ProcessInstance expectedProcessInstance) {

            this.expectedProcessInstance = expectedProcessInstance;
        }

        @Override
        public boolean matches(Object o) {
            final ProcessInstance actualProcessInstance = (ProcessInstance) o;
            return expectedProcessInstance.getAction().equals(actualProcessInstance.getAction()) &&
                    expectedProcessInstance.getAssignee().equals(actualProcessInstance.getAssignee()) &&
                    expectedProcessInstance.getBusinessKey().equals(actualProcessInstance.getBusinessKey()) &&
                    expectedProcessInstance.getType().equals(actualProcessInstance.getType()) &&
                    expectedProcessInstance.getDescription().equals(actualProcessInstance.getDescription()) &&
                    expectedProcessInstance.getSenderName().equals(actualProcessInstance.getSenderName()) &&
                    isValuesValid(actualProcessInstance);
        }

        private boolean isValuesValid(ProcessInstance processInstance) {
            Map<String, Attribute> attributesMap = processInstance.getValues();
            if (attributesMap.get(COMPLAINT_TYPE_CODE).getValues().size() != 1)
                return false;
            Value complaintType = attributesMap.get(COMPLAINT_TYPE_CODE).getValues().get(0);
            if (attributesMap.get(BOUNDARY_ID).getValues().size() != 1)
                return false;
            Value boundary = attributesMap.get(BOUNDARY_ID).getValues().get(0);
            return (complaintType.getName().equals("PHDMG") && boundary.getName().equals("173"));
        }
    }

    class processInstanceMatcherForCloseWorkflow extends ArgumentMatcher<ProcessInstance> {

        public static final String APPROVAL_COMMENTS = "approvalComments";
        public static final String STATE_ID = "stateId";
        private ProcessInstance expectedProcessInstance;

        public processInstanceMatcherForCloseWorkflow(ProcessInstance expectedProcessInstance) {

            this.expectedProcessInstance = expectedProcessInstance;
        }

        @Override
        public boolean matches(Object o) {
            final ProcessInstance actualProcessInstance = (ProcessInstance) o;
            return expectedProcessInstance.getAction().equals(actualProcessInstance.getAction()) &&
                    expectedProcessInstance.getAssignee().equals(actualProcessInstance.getAssignee()) &&
                    expectedProcessInstance.getBusinessKey().equals(actualProcessInstance.getBusinessKey()) &&
                    expectedProcessInstance.getType().equals(actualProcessInstance.getType()) &&
                    expectedProcessInstance.getDescription().equals(actualProcessInstance.getDescription()) &&
                    expectedProcessInstance.getSenderName().equals(actualProcessInstance.getSenderName()) &&
                    isValuesValid(actualProcessInstance);
        }

        private boolean isValuesValid(ProcessInstance processInstance) {
            Map<String, Attribute> attributesMap = processInstance.getValues();
            if (attributesMap.get(APPROVAL_COMMENTS).getValues().size() != 1)
                return false;
            Value comments = attributesMap.get(APPROVAL_COMMENTS).getValues().get(0);
            if (attributesMap.get(STATE_ID).getValues().size() != 1)
                return false;
            Value state = attributesMap.get(STATE_ID).getValues().get(0);
            return (comments.getName().equals("complaint closed") && state.getName().equals("119"));
        }
    }

    @Test
    public void testGetWorkFlowHistoryFailsWithoutJurisdictionId() throws Exception {
        mockMvc.perform(get("/history")).andExpect(status().isBadRequest());
    }

    @Test
    public void testGetWorkFlowHistoryById() throws Exception {
        final List<Task> history = getWorkFlowHistory();
        when(workflow.getHistoryDetail(TENANT_ID, "2"))
                .thenReturn(history);

        mockMvc.perform(get("/history")
                .header("X-CORRELATION-ID", "someId")
                .param("tenantId", TENANT_ID)
                .param("workflowId", "2"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(content().json(getFileContents("historyResponse.json")));

        assertEquals("someId", RequestContext.getId());
    }

    private List<Task> getWorkFlowHistory() {
        final Task history1 = Task.builder()
                .owner("Owner1")
                .sender("sender1")
                .status("Created")
                .comments("Got workflow history 1")
                // .createdDate(new Date("2016-08-31T10:46:22.083"))
                .build();
        final Task history2 = Task.builder()
                .owner("Owner2")
                .sender("sender2")
                .status("Closed")
                .comments("Got workflow history 2")
                // .createdDate(new Date("2016-08-31T10:46:22.083"))
                .build();
        return Arrays.asList(history1, history2);
    }

}