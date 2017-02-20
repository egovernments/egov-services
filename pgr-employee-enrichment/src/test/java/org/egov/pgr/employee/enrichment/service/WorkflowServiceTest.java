package org.egov.pgr.employee.enrichment.service;

import org.egov.pgr.employee.enrichment.model.RequestInfo;
import org.egov.pgr.employee.enrichment.model.SevaRequest;
import org.egov.pgr.employee.enrichment.model.WorkflowResponse;
import org.egov.pgr.employee.enrichment.repository.WorkflowRepository;
import org.egov.pgr.employee.enrichment.repository.contract.WorkflowRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class WorkflowServiceTest {

    public static final String ASSIGNEE = "1";
    public static final String STATE_ID = "2";
    @Mock
    private WorkflowRepository workflowRepository;

    @InjectMocks
    private WorkflowService workflowService;

    @Test
    public void test_should_update_seva_request_with_create_workflow_response() {
        final HashMap<String, Object> complaintRequestMap = new HashMap<>();
        final HashMap<String, String> valuesMap = new HashMap<>();
        valuesMap.put("status", "CREATE");
        valuesMap.put("location_id", "locationId");
        final HashMap<String, Object> serviceRequestMap = new HashMap<>();
        serviceRequestMap.put("values", valuesMap);
        serviceRequestMap.put("service_code", "serviceCode");
        complaintRequestMap.put("ServiceRequest", serviceRequestMap);
        final RequestInfo requestInfo = RequestInfo.builder().build();
        complaintRequestMap.put("RequestInfo", requestInfo);
        final SevaRequest sevaRequest = new SevaRequest(complaintRequestMap);
        final WorkflowResponse workflowResponse = new WorkflowResponse(STATE_ID, ASSIGNEE);
        when(workflowRepository.create(any(WorkflowRequest.class))).thenReturn(workflowResponse);

        final SevaRequest enrichedSevaRequest = workflowService.enrichWorkflow(sevaRequest);

        assertEquals(ASSIGNEE, enrichedSevaRequest.getValues().get("assignment_id") );
        assertEquals(STATE_ID, enrichedSevaRequest.getValues().get("state_id"));
    }



}