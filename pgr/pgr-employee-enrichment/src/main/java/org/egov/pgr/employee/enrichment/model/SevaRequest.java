package org.egov.pgr.employee.enrichment.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
import org.egov.pgr.employee.enrichment.consumer.contract.RequestInfo;
import org.egov.pgr.employee.enrichment.repository.contract.Attribute;
import org.egov.pgr.employee.enrichment.repository.contract.WorkflowRequest;
import org.egov.pgr.employee.enrichment.repository.contract.WorkflowResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.egov.pgr.employee.enrichment.repository.contract.WorkflowResponse.STATE_ID;

public class SevaRequest {

    public final static String SERVICE_REQUEST = "ServiceRequest";
    public final static String REQUEST_INFO = "RequestInfo";
    public static final String VALUES_ASSIGNEE_ID = "assignment_id";
    public static final String VALUES_STATE_ID = "stateId";
    public static final String VALUES = "values";
    public static final String VALUES_COMLAINT_TYPE_CODE = "complaintTypeCode";
    public static final String BOUNDARY_ID = "boundaryId";
    public static final String STATE_DETAILS = "stateDetails";
    public static final String WORKFLOW_TYPE = "Complaint";
    public static final String STATUS = "status";
    public static final String SERVICE_CODE = "service_code";
    public static final String VALUES_LOCATION_ID = "locationId";
    public static final String MSG_ID = "msg_id";
    public static final String VALUES_APPROVAL_COMMENT = "approvalComments";
    public static final String USER_ROLE = "userRole";
    public static final String SERVICE_REQUEST_ID = "service_request_id";

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

    public RequestInfo getRequestInfo() {
        return new ObjectMapper().convertValue(sevaRequestMap.get(REQUEST_INFO), RequestInfo.class);
    }

    public HashMap<String, Object> getRequestMap() {
        return sevaRequestMap;
    }

    @SuppressWarnings("unchecked")
    public String getCorrelationId() {
        final HashMap<String, Object> requestInfo = (HashMap<String, Object>) sevaRequestMap.get(REQUEST_INFO);
        return String.valueOf(requestInfo.get(MSG_ID));
    }

    @SuppressWarnings("unchecked")
    public WorkflowRequest getWorkFlowRequest() {
        HashMap<String, Object> serviceRequest = (HashMap<String, Object>) sevaRequestMap.get(SERVICE_REQUEST);
        RequestInfo requestInfo = getRequestInfo();
        HashMap<String, String> values = (HashMap<String, String>) serviceRequest.get(VALUES);
        String complaintType = (String) serviceRequest.get(SERVICE_CODE);
		String crn = (String) serviceRequest.get(SERVICE_REQUEST_ID);
        Map<String, Attribute> valuesToSet = getWorkFlowRequestValues(values, complaintType);
        WorkflowRequest.WorkflowRequestBuilder workflowRequestBuilder = WorkflowRequest.builder()
                .assignee(getCurrentAssignee(values))
                .action(WorkflowRequest.Action.forComplaintStatus(values.get(STATUS)))
                .requestInfo(requestInfo)
                .values(valuesToSet)
                .status(values.get(STATUS))
                .type(WORKFLOW_TYPE)
                .businessKey(WORKFLOW_TYPE)
                .crn(crn);

        return workflowRequestBuilder.build();
    }

    private Map<String, Attribute> getWorkFlowRequestValues(HashMap<String, String> values, String complaintType) {
        Map<String, Attribute> valuesToSet = new HashMap<>();
        valuesToSet.put(VALUES_COMLAINT_TYPE_CODE, Attribute.asStringAttr(VALUES_COMLAINT_TYPE_CODE, complaintType));
        valuesToSet.put(BOUNDARY_ID, Attribute.asStringAttr(BOUNDARY_ID, values.get(VALUES_LOCATION_ID)));
        valuesToSet.put(STATE_DETAILS, Attribute.asStringAttr(STATE_DETAILS, StringUtils.EMPTY));
        valuesToSet.put(USER_ROLE, Attribute.asStringAttr(USER_ROLE, getRequestInfo().getUserType()));
        valuesToSet.put(VALUES_STATE_ID, Attribute.asStringAttr(VALUES_STATE_ID, getCurrentStateId(values)));
        valuesToSet.put(VALUES_APPROVAL_COMMENT, Attribute.asStringAttr(VALUES_APPROVAL_COMMENT, values.get(VALUES_APPROVAL_COMMENT)));
        return valuesToSet;
    }

    private String getCurrentStateId(HashMap<String, String> values) {
        return Objects.isNull(values.get(VALUES_STATE_ID)) ? null : values.get(VALUES_STATE_ID);
    }

    private Long getCurrentAssignee(HashMap<String, String> values) {
        final String assignee = values.get(VALUES_ASSIGNEE_ID);
        return assignee != null ? Long.valueOf(String.valueOf(assignee)) : null;
    }

    public void update(WorkflowResponse workflowResponse) {
        setAssignee(workflowResponse.getAssignee());
        setStateId(workflowResponse.getValueForKey(STATE_ID));
    }
}