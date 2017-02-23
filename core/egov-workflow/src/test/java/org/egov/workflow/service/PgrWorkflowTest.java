package org.egov.workflow.service;

import org.egov.workflow.repository.entity.State;
import org.egov.workflow.web.contract.Attribute;
import org.egov.workflow.web.contract.ProcessInstance;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PgrWorkflowTest {

    private static final String JURISDICTION_ID = "jurisdiction";

    @Mock
    private StateService stateService;

    @InjectMocks
    private PgrWorkflowImpl workflow;

    @Test
    public void test_should_close_workflow() throws Exception {
        final State expectedState = new State();
        expectedState.setId(119L);
        expectedState.setComments("Workflow Terminated");

        final ProcessInstance expectedProcessInstance = ProcessInstance.builder()
                .type("Complaint")
                .description("Workflow Terminated")
                .assignee(2L)
                .values(new HashMap<String,Attribute>())
                .businessKey("765")
                .build();
        expectedProcessInstance.setStateId(119L);

        when(stateService.getStateById(119L))
                .thenReturn(expectedState);

        workflow.end(JURISDICTION_ID,expectedProcessInstance);
    }
}