package org.egov.pgr.employee.enrichment.model;

import org.apache.commons.lang.StringUtils;
import org.egov.pgr.employee.enrichment.DateConverter;
import org.egov.pgr.employee.enrichment.repository.contract.WorkflowRequest;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.egov.pgr.employee.enrichment.model.SevaRequest.*;
import static org.junit.Assert.assertEquals;

public class SevaRequestTest {

    @Test
    public void testWorkflowRequestIsCreatedFromSevaRequest() throws Exception {
        SevaRequest sevaRequest = createSevaRequest();
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

    @Test
    public void test_should_return_assignee_id() {
        final SevaRequest sevaRequest = createSevaRequest();

        assertEquals(Long.valueOf(23), sevaRequest.getAssignee());
    }

    @Test
    public void test_should_return_complaint_type_cde() {
        final SevaRequest sevaRequest = createSevaRequest();

        assertEquals("PKJB", sevaRequest.getComplaintTypeCode());
    }

    @Test
    public void test_should_return_tenant_id_from_seva_request_map() {
        final SevaRequest sevaRequest = createSevaRequest();

        assertEquals("tenantId", sevaRequest.getTenantId());
    }

    @Test
    public void test_should_set_designation_id_to_seva_request_map() {
        final SevaRequest sevaRequest = createSevaRequest();
        final String designationId = "2";
        final String designationIdKey = "designationId";

        sevaRequest.setDesignation(designationId);

        assertEquals(designationId, sevaRequest.getValues().get(designationIdKey));
    }

    @Test
    public void test_should_set_escalation_date() {
        final SevaRequest sevaRequest = createSevaRequest();
        final LocalDateTime dateTime = LocalDateTime.of(2017, 1, 2, 3, 4);
        Date expectedDate = new DateConverter(dateTime).toDate();

        sevaRequest.setEscalationDate(expectedDate);

        final HashMap<String, Object> requestMap = sevaRequest.getRequestMap();
        @SuppressWarnings("unchecked")
        final HashMap<String, Object> serviceRequest = (HashMap<String, Object>) requestMap.get("ServiceRequest");
        assertEquals(expectedDate, serviceRequest.get("expected_datetime"));
    }

    private SevaRequest createSevaRequest() {
        final HashMap<String, Object> sevaRequestMap = new HashMap<>();
        final SevaRequest sevaRequest = new SevaRequest(sevaRequestMap);
        final HashMap<String, Object> serviceRequestMap = new HashMap<>();
        final Map<String, String> valueMap = new HashMap<>();
        final Map<String, Object> requestInfoMap = new HashMap<>();
        valueMap.put(VALUES_STATE_ID, "233");
        valueMap.put(VALUES_LOCATION_ID, "18");
        valueMap.put(VALUES_APPROVAL_COMMENT, "this is approved");
        valueMap.put(VALUES_ASSIGNEE_ID, "23");
        valueMap.put(STATUS, "REGISTERED");
        requestInfoMap.put("userType", "EMPLOYEE");
        serviceRequestMap.put(SERVICE_CODE, "PKJB");
        serviceRequestMap.put(STATUS, "REGISTERED");
        serviceRequestMap.put(VALUES, valueMap);
        serviceRequestMap.put("tenantId", "tenantId");
        sevaRequestMap.put(SERVICE_REQUEST, serviceRequestMap);
        sevaRequestMap.put(REQUEST_INFO, requestInfoMap);
        return sevaRequest;
    }
}
