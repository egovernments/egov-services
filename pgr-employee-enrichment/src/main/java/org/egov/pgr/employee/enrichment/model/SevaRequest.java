package org.egov.pgr.employee.enrichment.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.pgr.employee.enrichment.consumer.contract.RequestInfo;
import org.egov.pgr.employee.enrichment.repository.contract.WorkflowRequest;
import org.egov.pgr.employee.enrichment.repository.contract.WorkflowResponse;

import java.util.HashMap;
import java.util.Map;

public class SevaRequest {

    private final static String SERVICE_REQUEST = "ServiceRequest";
    private final static String REQUEST_INFO = "RequestInfo";
    private static final String VALUES_ASSIGNEE_ID = "assignment_id";
    private static final String VALUES_STATE_ID = "state_id";
    private static final String VALUES = "values";
    private static final String VALUES_COMLAINT_TYPE_CODE = "complaint_type_code";
    private static final String BOUNDARY_ID = "boundary_id";
    private static final String WORKFLOW_TYPE = "Complaint";
    private static final String STATUS = "status";
    private static final String SERVICE_CODE = "service_code";
    private static final String VALUES_LOCATION_ID = "location_id";

    private HashMap<String, Object> sevaRequestMap;

    public SevaRequest(HashMap<String, Object> sevaRequestMap) {
        this.sevaRequestMap = sevaRequestMap;
    }

    @SuppressWarnings("unchecked")
    public HashMap<String, String> getValues() {
        HashMap<String, Object> serviceRequest = (HashMap<String, Object>) sevaRequestMap.get(SERVICE_REQUEST);
        return (HashMap<String, String>) serviceRequest.get(VALUES);
    }

    private void setAssignee(String assignee) {
        getValues().put(VALUES_ASSIGNEE_ID, assignee);
    }

    private void setStateId(String stateId) {
        getValues().put(VALUES_STATE_ID, stateId);
    }

    public HashMap<String, Object> getRequestMap() {
        return sevaRequestMap;
    }

    public String getCorrelationId() {
        final RequestInfo requestInfo = (RequestInfo) sevaRequestMap.get(REQUEST_INFO);
        return requestInfo.getMsgId();
    }

    @SuppressWarnings("unchecked")
    public WorkflowRequest getWorkFlowRequest() {
        HashMap<String, Object> serviceRequest = (HashMap<String, Object>) sevaRequestMap.get(SERVICE_REQUEST);
        RequestInfo requestInfo = new ObjectMapper().convertValue(sevaRequestMap.get(REQUEST_INFO), RequestInfo.class);
        HashMap<String, String> values = (HashMap<String, String>) serviceRequest.get(VALUES);
        String complaintType = (String) serviceRequest.get(SERVICE_CODE);
        Map<String, String> valuesToSet = getWorkFlowRequestValues(values, complaintType);
        return WorkflowRequest.builder()
                .assignee(getCurrentAssignee(values))
                .action(requestInfo.getAction())
                .requestInfo(requestInfo)
                .values(valuesToSet)
                .status(values.get(STATUS))
                .type(WORKFLOW_TYPE).build();
    }

    private Map<String, String> getWorkFlowRequestValues(HashMap<String, String> values, String complaintType) {
        Map<String, String > valuesToSet = new HashMap<>();
        valuesToSet.put(VALUES_COMLAINT_TYPE_CODE, complaintType);
        valuesToSet.put(BOUNDARY_ID, values.get(VALUES_LOCATION_ID));
        return valuesToSet;
    }

    private Long getCurrentAssignee(HashMap<String, String> values) {
        final String assignee = values.get(VALUES_ASSIGNEE_ID);
        return assignee != null ? Long.valueOf(String.valueOf(assignee)) : null;
    }

    public void update(WorkflowResponse workflowResponse) {
        setAssignee(workflowResponse.getAssignee());
        setStateId(workflowResponse.getStateId());
    }
}