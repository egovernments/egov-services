package org.egov.pgr.employee.enrichment.model;

import org.apache.commons.lang3.StringUtils;
import org.egov.pgr.employee.enrichment.DateConverter;
import org.egov.pgr.employee.enrichment.repository.contract.WorkflowRequest;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
        assertEquals(Long.valueOf(23), workFlowRequest.getPositionId());
    }

    @Test
    public void test_should_return_assignee_id() {
        final SevaRequest sevaRequest = createSevaRequest();

        assertEquals(Long.valueOf(23), sevaRequest.getCurrentPositionId());
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
    public void test_should_update_seva_request_map_with_positions() {
        final SevaRequest sevaRequest = createSevaRequest();
        final String designationId = "2";
        final String departmentId = "3";
        final String designationIdKey = "systemDesignationId";
        final String departmentIdKey = "systemDepartmentId";

        sevaRequest.update(new Position(designationId, departmentId));

        assertEquals(designationId, sevaRequest.getDynamicSingleValue(designationIdKey));
        assertEquals(departmentId, sevaRequest.getDynamicSingleValue(departmentIdKey));
    }

    @Test
    public void test_should_set_escalation_date() {
        final SevaRequest sevaRequest = createSevaRequest();
        final LocalDateTime dateTime = LocalDateTime.of(2017, 1, 2, 3, 4);
        Date expectedDate = new DateConverter(dateTime).toDate();

        sevaRequest.setEscalationDate(expectedDate);

        final HashMap<String, Object> requestMap = sevaRequest.getRequestMap();
        @SuppressWarnings("unchecked")
        final HashMap<String, Object> serviceRequest = (HashMap<String, Object>) requestMap.get("serviceRequest");
        assertEquals("02-01-2017 03:04:00", serviceRequest.get("expectedDatetime"));
    }

    private SevaRequest createSevaRequest() {
        final HashMap<String, Object> sevaRequestMap = new HashMap<>();
        final SevaRequest sevaRequest = new SevaRequest(sevaRequestMap);
        final HashMap<String, Object> serviceRequestMap = new HashMap<>();
        final Map<String, Object> userInfoMap = new HashMap<>();
        userInfoMap.put("type", "EMPLOYEE");
        final Map<String, Object> requestInfoMap = new HashMap<>();
        requestInfoMap.put("userInfo", userInfoMap);
        requestInfoMap.put("action","POST");
        serviceRequestMap.put(SERVICE_CODE, "PKJB");
        serviceRequestMap.put(STATUS, "REGISTERED");
        serviceRequestMap.put("tenantId", "tenantId");
        final ArrayList<HashMap<String, String>> attributeValues = new ArrayList<>();
        final HashMap<String, String> stateIdEntry = new HashMap<>();
        stateIdEntry.put("key", VALUES_STATE_ID);
        stateIdEntry.put("name", "233");
        attributeValues.add(stateIdEntry);
        final HashMap<String, String> locationIdEntry = new HashMap<>();
        locationIdEntry.put("key", VALUES_LOCATION_ID);
        locationIdEntry.put("name", "18");
        attributeValues.add(locationIdEntry);
        final HashMap<String, String> commentEntry = new HashMap<>();
        commentEntry.put("key", VALUES_APPROVAL_COMMENT);
        commentEntry.put("name", "this is approved");
        attributeValues.add(commentEntry);
        final HashMap<String, String> assigneeEntry = new HashMap<>();
        assigneeEntry.put("key", VALUES_POSITION_ID);
        assigneeEntry.put("name", "23");
        attributeValues.add(assigneeEntry);
        final HashMap<String, String> registeredEntry = new HashMap<>();
        registeredEntry.put("key", STATUS);
        registeredEntry.put("name", "REGISTERED");
        attributeValues.add(registeredEntry);
        serviceRequestMap.put("attribValues", attributeValues);
        sevaRequestMap.put(SERVICE_REQUEST, serviceRequestMap);
        sevaRequestMap.put(REQUEST_INFO, requestInfoMap);
        return sevaRequest;
    }
}
