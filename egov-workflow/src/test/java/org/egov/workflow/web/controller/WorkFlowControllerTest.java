package org.egov.workflow.web.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.egov.workflow.domain.model.RequestContext;
import org.egov.workflow.repository.entity.Task;
import org.egov.workflow.service.Workflow;
import org.egov.workflow.web.contract.ProcessInstance;
import org.junit.Test;
import org.junit.runner.RunWith;
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
    public void test_should_accept_correlation_id() throws Exception {
        when(workflow.start(eq(TENANT_ID), any(ProcessInstance.class)))
                .thenReturn(new ProcessInstance());

        mockMvc.perform(post("/create")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(getFileContents("startWorkflowRequest.json"))
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
                .param("jurisdiction", TENANT_ID)
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
                //.createdDate(new Date("2016-08-31T10:46:22.083"))
                .build();
        final Task history2 = Task.builder()
                .owner("Owner2")
                .sender("sender2")
                .status("Closed")
                .comments("Got workflow history 2")
                //.createdDate(new Date("2016-08-31T10:46:22.083"))
                .build();
        return Arrays.asList(history1, history2);
    }

}