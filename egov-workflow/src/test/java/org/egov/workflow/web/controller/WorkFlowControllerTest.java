package org.egov.workflow.web.controller;


import org.apache.commons.io.IOUtils;
import org.egov.workflow.domain.model.RequestContext;
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

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    private String getFileContents(String fileName) {
        try {
            return IOUtils.toString(this.getClass().getClassLoader()
                    .getResourceAsStream(fileName), "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}