package org.egov.pgr.employee.enrichment.service;

import org.apache.commons.lang.StringUtils;
import org.egov.pgr.employee.enrichment.consumer.contract.RequestInfo;
import org.egov.pgr.employee.enrichment.model.SevaRequest;
import org.egov.pgr.employee.enrichment.repository.contract.Attribute;
import org.egov.pgr.employee.enrichment.repository.contract.Value;
import org.egov.pgr.employee.enrichment.repository.contract.WorkflowResponse;
import org.egov.pgr.employee.enrichment.repository.WorkflowRepository;
import org.egov.pgr.employee.enrichment.repository.contract.WorkflowRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class WorkflowServiceTest {

    public static final String ASSIGNEE = "1";
    public static final String STATE_ID = "2";
    public static final String STATE_ID_KEY = "stateId";
    @Mock
    private WorkflowRepository workflowRepository;

    @InjectMocks
    private WorkflowService workflowService;

    @Test
    public void test_should_update_seva_request_with_create_workflow_response() {
        final HashMap<String, Object> complaintRequestMap = new HashMap<>();
        final HashMap<String, String> valuesMap = new HashMap<>();
        valuesMap.put("location_id", "locationId");
        valuesMap.put("status", "REGISTERED");
        final HashMap<String, Object> serviceRequestMap = new HashMap<>();
        serviceRequestMap.put("values", valuesMap);
        serviceRequestMap.put("service_code", "serviceCode");
        serviceRequestMap.put("status", "REGISTERED");
        complaintRequestMap.put("ServiceRequest", serviceRequestMap);
        final RequestInfo requestInfo = RequestInfo.builder().build();
        complaintRequestMap.put("RequestInfo", requestInfo);
        final SevaRequest sevaRequest = new SevaRequest(complaintRequestMap);
        final WorkflowResponse workflowResponse = new WorkflowResponse(ASSIGNEE, getValuesWithStateId());
        when(workflowRepository.create(any(WorkflowRequest.class))).thenReturn(workflowResponse);

        final SevaRequest enrichedSevaRequest = workflowService.enrichWorkflow(sevaRequest);

        assertEquals(ASSIGNEE, enrichedSevaRequest.getValues().get("assignment_id") );
        assertEquals(STATE_ID, enrichedSevaRequest.getValues().get("stateId"));
    }

    @Test
    public void test_should_update_seva_request_with_close_workflow_response_for_completed() {
        final HashMap<String, Object> complaintRequestMap = new HashMap<>();
        final HashMap<String, String> valuesMap = new HashMap<>();
        valuesMap.put("location_id", "locationId");
        valuesMap.put("status", "COMPLETED");
        final HashMap<String, Object> serviceRequestMap = new HashMap<>();
        serviceRequestMap.put("values", valuesMap);
        serviceRequestMap.put("service_code", "serviceCode");
        complaintRequestMap.put("ServiceRequest", serviceRequestMap);
        final RequestInfo requestInfo = RequestInfo.builder().build();
        complaintRequestMap.put("RequestInfo", requestInfo);
        final SevaRequest sevaRequest = new SevaRequest(complaintRequestMap);
        final WorkflowResponse workflowResponse = new WorkflowResponse(ASSIGNEE, getValuesWithStateId());
        when(workflowRepository.close(any(WorkflowRequest.class))).thenReturn(workflowResponse);

        final SevaRequest enrichedSevaRequest = workflowService.enrichWorkflow(sevaRequest);

        assertEquals(ASSIGNEE, enrichedSevaRequest.getValues().get("assignment_id") );
        assertEquals(STATE_ID, enrichedSevaRequest.getValues().get("stateId"));
    }

    @Test
    public void test_should_update_seva_request_with_close_workflow_response_for_rejected() {
        final HashMap<String, Object> complaintRequestMap = new HashMap<>();
        final HashMap<String, String> valuesMap = new HashMap<>();
        valuesMap.put("location_id", "locationId");
        valuesMap.put("status", "REJECTED");
        final HashMap<String, Object> serviceRequestMap = new HashMap<>();
        serviceRequestMap.put("values", valuesMap);
        serviceRequestMap.put("service_code", "serviceCode");
        complaintRequestMap.put("ServiceRequest", serviceRequestMap);
        final RequestInfo requestInfo = RequestInfo.builder().build();
        complaintRequestMap.put("RequestInfo", requestInfo);
        final SevaRequest sevaRequest = new SevaRequest(complaintRequestMap);
        final WorkflowResponse workflowResponse = new WorkflowResponse(ASSIGNEE, getValuesWithStateId());
        when(workflowRepository.close(any(WorkflowRequest.class))).thenReturn(workflowResponse);

        final SevaRequest enrichedSevaRequest = workflowService.enrichWorkflow(sevaRequest);

        assertEquals(ASSIGNEE, enrichedSevaRequest.getValues().get("assignment_id") );
        assertEquals(STATE_ID, enrichedSevaRequest.getValues().get("stateId"));
    }

    @Test
    public void test_should_update_seva_request_with_close_workflow_response_for_withdrawn() {
        final HashMap<String, Object> complaintRequestMap = new HashMap<>();
        final HashMap<String, String> valuesMap = new HashMap<>();
        valuesMap.put("location_id", "locationId");
        valuesMap.put("status", "WITHDRAWN");
        final HashMap<String, Object> serviceRequestMap = new HashMap<>();
        serviceRequestMap.put("values", valuesMap);
        serviceRequestMap.put("service_code", "serviceCode");
        complaintRequestMap.put("ServiceRequest", serviceRequestMap);
        final RequestInfo requestInfo = RequestInfo.builder().build();
        complaintRequestMap.put("RequestInfo", requestInfo);
        final SevaRequest sevaRequest = new SevaRequest(complaintRequestMap);
        final WorkflowResponse workflowResponse = new WorkflowResponse(ASSIGNEE, getValuesWithStateId());
        when(workflowRepository.close(any(WorkflowRequest.class))).thenReturn(workflowResponse);

        final SevaRequest enrichedSevaRequest = workflowService.enrichWorkflow(sevaRequest);

        assertEquals(ASSIGNEE, enrichedSevaRequest.getValues().get("assignment_id") );
        assertEquals(STATE_ID, enrichedSevaRequest.getValues().get("stateId"));
    }


    private Map<String, Attribute> getValuesWithStateId() {
        Map<String, Attribute> valueMap = new HashMap<>();
        List<Value> stateIdValues = new ArrayList<>();
        stateIdValues.add(new Value(STATE_ID_KEY, STATE_ID));
        valueMap.put(STATE_ID_KEY, new Attribute(Boolean.TRUE, STATE_ID_KEY, "String", Boolean.FALSE, StringUtils.EMPTY, stateIdValues));
        return valueMap;
    }


}