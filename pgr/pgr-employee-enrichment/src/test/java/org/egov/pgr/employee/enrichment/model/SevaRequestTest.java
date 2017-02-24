package org.egov.pgr.employee.enrichment.model;

import org.apache.commons.lang.StringUtils;
import org.egov.pgr.employee.enrichment.repository.contract.WorkflowRequest;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.egov.pgr.employee.enrichment.model.SevaRequest.*;
import static org.junit.Assert.assertEquals;

public class SevaRequestTest {

    private static final String CORRELATION_ID = "correlationId";

    @Test
    public void testShouldRetrieveCorrelationId() {
        final HashMap<String, Object> sevaRequestMap = new HashMap<>();
        final HashMap<String, Object> requestInfo = new HashMap<>();
        requestInfo.put("msg_id", CORRELATION_ID);
        sevaRequestMap.put("RequestInfo", requestInfo);
        final SevaRequest sevaRequest = new SevaRequest(sevaRequestMap);

        assertEquals(CORRELATION_ID, sevaRequest.getCorrelationId());
    }

    @Test
    public void testWorkflowRequestIsCreatedFromSevaRequest() throws Exception {
        SevaRequest sevaRequest = getSevaRequestWithNeededFields();
        WorkflowRequest workFlowRequest = sevaRequest.getWorkFlowRequest();

        assertEquals("Complaint", workFlowRequest.getBusinessKey());
        assertEquals("Complaint", workFlowRequest.getType());
        assertEquals("create", workFlowRequest.getAction());
        assertEquals("PKJB", workFlowRequest.getValueForKey(VALUES_COMLAINT_TYPE_CODE));
        assertEquals(StringUtils.EMPTY, workFlowRequest.getValueForKey(STATE_DETAILS));
        assertEquals("233", workFlowRequest.getValueForKey(VALUES_STATE_ID));
        assertEquals("18", workFlowRequest.getValueForKey(BOUNDARY_ID));
        assertEquals("EMPLOYEE", workFlowRequest.getValueForKey(USER_ROLE));
        assertEquals("this is approved", workFlowRequest.getValueForKey(VALUES_APPROVAL_COMMENT));
        assertEquals(Long.valueOf(23), workFlowRequest.getAssignee());
    }

    private SevaRequest getSevaRequestWithNeededFields() {
        final HashMap<String, Object> sevaRequestMap = new HashMap<>();
        final SevaRequest sevaRequest = new SevaRequest(sevaRequestMap);
        final HashMap<String, Object> serviceRequestMap = new HashMap<>();
        final Map<String, String> valueMap = new HashMap<>();
        final Map<String, Object> requestInfoMap = new HashMap();
        valueMap.put(VALUES_STATE_ID, "233");
        valueMap.put(VALUES_LOCATION_ID, "18");
        valueMap.put(VALUES_APPROVAL_COMMENT, "this is approved");
        valueMap.put(VALUES_ASSIGNEE_ID, "23");
        valueMap.put(STATUS, "REGISTERED");
        requestInfoMap.put("userType", "EMPLOYEE");
        serviceRequestMap.put(SERVICE_CODE, "PKJB");
        serviceRequestMap.put(STATUS, "REGISTERED");
        serviceRequestMap.put(VALUES, valueMap);
        sevaRequestMap.put(SERVICE_REQUEST, serviceRequestMap);
        sevaRequestMap.put(REQUEST_INFO, requestInfoMap);
        return sevaRequest;
    }
}
