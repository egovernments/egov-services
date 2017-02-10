package org.egov.pgr.employee.enrichment.service;

import org.egov.pgr.employee.enrichment.repository.WorkflowRepository;
import org.egov.pgr.employee.enrichment.repository.contract.WorkflowRequest;
import org.egov.pgr.employee.enrichment.repository.contract.WorkflowResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

import static org.egov.pgr.employee.enrichment.consumer.contract.RequestInfo.ACTION;
import static org.egov.pgr.employee.enrichment.consumer.contract.ServiceRequest.*;
import static org.egov.pgr.employee.enrichment.consumer.contract.SevaRequest.SERVICE_REQUEST;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class WorkflowServiceTest {

    @Mock
    private WorkflowRepository workflowRepository;
    private WorkflowService workflowService;
    private String location;
    private String complaintType;
    private Map sevaRequestHash;
    private WorkflowResponse workflowResponse;

    @Before
    public void setUp() throws Exception {
        location = "12";
        complaintType = "PCMF";
        workflowService = new WorkflowService(workflowRepository);
        this.workflowResponse = buildSuccessWorkflowResponse();
        when(workflowRepository.triggerWorkflow(any(WorkflowRequest.class))).thenReturn(workflowResponse);
    }

    @Test
    public void testWorkflowIdAndAssigneeIdAreReturnedAndEnriched() throws Exception {
        Map sevaRequestMap = buildSevaRequestMap();
        Map map = workflowService.enrichWorkflowDetails(sevaRequestMap);
        long expectedAssigneeId = 10L;
        long expectedStateId = 22L;

        assertEquals(Long.valueOf(expectedAssigneeId), extractValue(map, VALUES_ASSIGNMENT_ID));
        assertEquals(Long.valueOf(expectedStateId), extractValue(map, VALUES_STATE_ID));
    }

    private WorkflowResponse buildSuccessWorkflowResponse() {
        long assignee = 10L;
        long stateId = 22L;
        return new WorkflowResponse(stateId, assignee);
    }

    private Long extractValue(Map map, String keyName) {
        Map serviceRequest = (Map) map.get(SERVICE_REQUEST);
        Map values = (Map) serviceRequest.get(VALUES);
        return (Long) values.get(keyName);
    }

    private Map buildSevaRequestMap() {
        Map sevaRequest = new HashMap();
        Map serviceRequest = new HashMap();
        Map values = new HashMap();
        Map requestInfo = new HashMap();
        requestInfo.put(ACTION, "create");
        values.put(VALUES_LOCATION_ID, location);
        serviceRequest.put(SERVICE_CODE, complaintType);
        serviceRequest.put(VALUES, values);
        sevaRequest.put(SERVICE_REQUEST, serviceRequest);
        sevaRequest.put(REQUEST_INFO, requestInfo);
        return sevaRequest;
    }
}