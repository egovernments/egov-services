package org.egov.pgr.employee.enrichment.service;

import org.apache.commons.lang3.StringUtils;
import org.egov.common.contract.request.RequestInfo;
import org.egov.pgr.common.contract.AttributeEntry;
import org.egov.pgr.employee.enrichment.model.SevaRequest;
import org.egov.pgr.employee.enrichment.repository.ComplaintRestRepository;
import org.egov.pgr.employee.enrichment.repository.WorkflowRepository;
import org.egov.pgr.employee.enrichment.repository.contract.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.egov.pgr.employee.enrichment.model.SevaRequest.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class WorkflowServiceTest {

    public static final String ASSIGNEE = "1";
    public static final String STATE_ID = "2";
    public static final String STATE_ID_KEY = "systemStateId";
    @Mock
    private WorkflowRepository workflowRepository;

    @InjectMocks
    private WorkflowService workflowService;

    @Mock
    private ComplaintRestRepository complaintRestRepository;


    @Test
    public void test_should_update_seva_request_with_create_workflow_response() {
        final HashMap<String, Object> complaintRequestMap = new HashMap<>();
        final HashMap<String, Object> serviceRequestMap = new HashMap<>();
        serviceRequestMap.put("serviceCode", "serviceCode");
        serviceRequestMap.put("systemStatus", "REGISTERED");
        serviceRequestMap.put("isAttribValuesPopulated", false);
        final ArrayList<HashMap<String, String>> attributeValues = new ArrayList<>();
        final HashMap<String, String> locationIdEntry = new HashMap<>();
        locationIdEntry.put("key", VALUES_LOCATION_ID);
        locationIdEntry.put("name", "systemLocationId");
        attributeValues.add(locationIdEntry);
        final HashMap<String, String> statusEntry = new HashMap<>();
        statusEntry.put("key", STATUS);
        statusEntry.put("name", "REGISTERED");
        attributeValues.add(statusEntry);
        serviceRequestMap.put("attribValues", attributeValues);
        complaintRequestMap.put("serviceRequest", serviceRequestMap);
        final RequestInfo requestInfo = RequestInfo.builder().action("POST").build();
        complaintRequestMap.put("RequestInfo", requestInfo);
        final SevaRequest sevaRequest = new SevaRequest(complaintRequestMap);
        final WorkflowResponse workflowResponse = new WorkflowResponse(ASSIGNEE, getValuesWithStateId());
        when(workflowRepository.create(any(WorkflowRequest.class))).thenReturn(workflowResponse);

        final SevaRequest enrichedSevaRequest = workflowService.enrichWorkflow(sevaRequest);

        assertEquals(ASSIGNEE, enrichedSevaRequest.getDynamicSingleValue("systemPositionId"));
        assertEquals(STATE_ID, enrichedSevaRequest.getDynamicSingleValue("systemStateId"));
    }

    @Test
    public void test_should_update_seva_request_with_close_workflow_response_for_completed() {
        final HashMap<String, Object> complaintRequestMap = new HashMap<>();
        final HashMap<String, String> valuesMap = new HashMap<>();
        valuesMap.put("systemLocationId", "locationId");
        valuesMap.put("systemStatus", "COMPLETED");
        final HashMap<String, Object> serviceRequestMap = new HashMap<>();
        serviceRequestMap.put("values", valuesMap);
        serviceRequestMap.put("serviceCode", "serviceCode");
        final ArrayList<HashMap<String, String>> attributeValues = new ArrayList<>();
        final HashMap<String, String> locationIdEntry = new HashMap<>();
        locationIdEntry.put("key", VALUES_LOCATION_ID);
        locationIdEntry.put("name", "locationId");
        attributeValues.add(locationIdEntry);
        final HashMap<String, String> registeredEntry = new HashMap<>();
        registeredEntry.put("key", STATUS);
        registeredEntry.put("name", "COMPLETED");
        attributeValues.add(registeredEntry);
        final HashMap<String, String> departmentIdEntry = new HashMap<>();
        departmentIdEntry.put("key", VALUES_DEPARTMENT_ID);
        departmentIdEntry.put("name", "20");
        attributeValues.add(departmentIdEntry);
        serviceRequestMap.put("attribValues", attributeValues);
        serviceRequestMap.put("attribValues", attributeValues);
        complaintRequestMap.put("serviceRequest", serviceRequestMap);
        final RequestInfo requestInfo = RequestInfo.builder().build();
        complaintRequestMap.put("RequestInfo", requestInfo);
        final SevaRequest sevaRequest = new SevaRequest(complaintRequestMap);
        final WorkflowResponse workflowResponse = new WorkflowResponse(ASSIGNEE, getValuesWithStateId());
        when(workflowRepository.close(any(WorkflowRequest.class))).thenReturn(workflowResponse);

        final SevaRequest enrichedSevaRequest = workflowService.enrichWorkflow(sevaRequest);

        assertEquals(ASSIGNEE, enrichedSevaRequest.getDynamicSingleValue("systemPositionId"));
        assertEquals(STATE_ID, enrichedSevaRequest.getDynamicSingleValue("systemStateId"));
    }

    @Test
    public void test_should_update_seva_request_with_close_workflow_response_for_rejected() {
        final HashMap<String, Object> complaintRequestMap = new HashMap<>();
        final HashMap<String, String> valuesMap = new HashMap<>();
        valuesMap.put("locationId", "locationId");
        valuesMap.put("status", "REJECTED");
        final HashMap<String, Object> serviceRequestMap = new HashMap<>();
        serviceRequestMap.put("values", valuesMap);
        serviceRequestMap.put("serviceCode", "serviceCode");
        final ArrayList<HashMap<String, String>> attributeValues = new ArrayList<>();
        final HashMap<String, String> locationIdEntry = new HashMap<>();
        locationIdEntry.put("key", VALUES_LOCATION_ID);
        locationIdEntry.put("name", "locationId");
        attributeValues.add(locationIdEntry);
        final HashMap<String, String> statusEntry = new HashMap<>();
        statusEntry.put("key", STATUS);
        statusEntry.put("name", "REJECTED");
        attributeValues.add(statusEntry);
        serviceRequestMap.put("attribValues", attributeValues);
        complaintRequestMap.put("serviceRequest", serviceRequestMap);
        final RequestInfo requestInfo = RequestInfo.builder().build();
        complaintRequestMap.put("RequestInfo", requestInfo);
        final SevaRequest sevaRequest = new SevaRequest(complaintRequestMap);
        final WorkflowResponse workflowResponse = new WorkflowResponse(ASSIGNEE, getValuesWithStateId());
        when(workflowRepository.close(any(WorkflowRequest.class))).thenReturn(workflowResponse);

        final SevaRequest enrichedSevaRequest = workflowService.enrichWorkflow(sevaRequest);

        assertEquals(ASSIGNEE, enrichedSevaRequest.getDynamicSingleValue("systemPositionId"));
        assertEquals(STATE_ID, enrichedSevaRequest.getDynamicSingleValue("systemStateId"));
    }

    @Test
    public void test_should_update_seva_request_with_close_workflow_response_for_withdrawn() {
        final HashMap<String, Object> complaintRequestMap = new HashMap<>();
        final HashMap<String, Object> serviceRequestMap = new HashMap<>();
        serviceRequestMap.put("serviceCode", "serviceCode");
        final ArrayList<HashMap<String, String>> attributeValues = new ArrayList<>();
        final HashMap<String, String> locationIdEntry = new HashMap<>();
        locationIdEntry.put("key", VALUES_LOCATION_ID);
        locationIdEntry.put("name", "locationId");
        attributeValues.add(locationIdEntry);
        final HashMap<String, String> statusEntry = new HashMap<>();
        statusEntry.put("key", STATUS);
        statusEntry.put("name", "WITHDRAWN");
        attributeValues.add(statusEntry);
        serviceRequestMap.put("attribValues", attributeValues);
        complaintRequestMap.put("serviceRequest", serviceRequestMap);
        final RequestInfo requestInfo = RequestInfo.builder().build();
        complaintRequestMap.put("RequestInfo", requestInfo);
        final SevaRequest sevaRequest = new SevaRequest(complaintRequestMap);
        final WorkflowResponse workflowResponse = new WorkflowResponse(ASSIGNEE, getValuesWithStateId());
        when(workflowRepository.close(any(WorkflowRequest.class))).thenReturn(workflowResponse);

        final SevaRequest enrichedSevaRequest = workflowService.enrichWorkflow(sevaRequest);

        assertEquals(ASSIGNEE, enrichedSevaRequest.getDynamicSingleValue("systemPositionId"));
        assertEquals(STATE_ID, enrichedSevaRequest.getDynamicSingleValue("systemStateId"));
    }

    @Test
    public void testShouldUpdateSevaRequestWithUpdateWorkflowResponseWithDifferentDepartment() {
        final HashMap<String, Object> complaintRequestMap = new HashMap<>();
        final HashMap<String, Object> serviceRequestMap = new HashMap<>();
        serviceRequestMap.put("serviceCode", "serviceCode");
        serviceRequestMap.put("serviceRequestId", "00015-2016-AP");
        serviceRequestMap.put("tenantId", "ap.public");
        final ArrayList<HashMap<String, String>> attributeValues = new ArrayList<>();
        final HashMap<String, String> stateIdEntry = new HashMap<>();
        stateIdEntry.put("key", VALUES_STATE_ID);
        stateIdEntry.put("name", "6");
        attributeValues.add(stateIdEntry);
        final HashMap<String, String> locationIdEntry = new HashMap<>();
        locationIdEntry.put("key", VALUES_LOCATION_ID);
        locationIdEntry.put("name", "1");
        attributeValues.add(locationIdEntry);
        final HashMap<String, String> commentEntry = new HashMap<>();
        commentEntry.put("key", VALUES_APPROVAL_COMMENT);
        commentEntry.put("name", "Testing complaint update");
        attributeValues.add(commentEntry);
        final HashMap<String, String> assigneeEntry = new HashMap<>();
        assigneeEntry.put("key", VALUES_POSITION_ID);
        assigneeEntry.put("name", "6");
        attributeValues.add(assigneeEntry);
        final HashMap<String, String> registeredEntry = new HashMap<>();
        registeredEntry.put("key", STATUS);
        registeredEntry.put("name", "PROCESSING");
        attributeValues.add(registeredEntry);
        final HashMap<String, String> departmentIdEntry = new HashMap<>();
        departmentIdEntry.put("key", VALUES_DEPARTMENT_ID);
        departmentIdEntry.put("name", "19");
        attributeValues.add(departmentIdEntry);
        serviceRequestMap.put("attribValues", attributeValues);
        serviceRequestMap.put("attribValues", attributeValues);
        complaintRequestMap.put("serviceRequest", serviceRequestMap);
        final RequestInfo requestInfo = RequestInfo.builder().build();
        complaintRequestMap.put("RequestInfo", requestInfo);
        final SevaRequest sevaRequest = new SevaRequest(complaintRequestMap);
        final WorkflowResponse workflowResponse = new WorkflowResponse(ASSIGNEE, getValuesWithStateId());
        when(workflowRepository.update(any(WorkflowRequest.class))).thenReturn(workflowResponse);
        List<AttributeEntry> responseValues = new ArrayList<>();
        responseValues.add(new AttributeEntry("systemLocationId", "1"));
        responseValues.add(new AttributeEntry("systemDepartmentId", "20"));
        responseValues.add(new AttributeEntry("systemPositionId", "6"));
        responseValues.add(new AttributeEntry("systemComplaintStatus", "PROCESSING"));
        when(complaintRestRepository.getComplaintByCrn("ap.public", serviceRequestMap.get("serviceRequestId")
            .toString()))
            .thenReturn(ServiceRequest.builder().attribValues(responseValues).description("Testing complaint update")
                .complaintTypeCode("serviceCode").build());

        final SevaRequest enrichedSevaRequest = workflowService.enrichWorkflow(sevaRequest);

        assertEquals(ASSIGNEE, enrichedSevaRequest.getDynamicSingleValue("systemPositionId"));
        assertEquals(STATE_ID, enrichedSevaRequest.getDynamicSingleValue("systemStateId"));
    }

    @Test
    public void testShouldUpdateSevaRequestWithUpdateWorkflowResponseWithSameDepartment() {
        final HashMap<String, Object> complaintRequestMap = new HashMap<>();
        final HashMap<String, Object> serviceRequestMap = new HashMap<>();
        serviceRequestMap.put("serviceCode", "serviceCode");
        serviceRequestMap.put("serviceRequestId", "00015-2016-AP");
        serviceRequestMap.put("tenantId", "ap.public");
        serviceRequestMap.put("isAttribValuesPopulated", false);
        final ArrayList<HashMap<String, String>> attributeValues = new ArrayList<>();
        final HashMap<String, String> stateIdEntry = new HashMap<>();
        stateIdEntry.put("key", VALUES_STATE_ID);
        stateIdEntry.put("name", "6");
        attributeValues.add(stateIdEntry);
        final HashMap<String, String> locationIdEntry = new HashMap<>();
        locationIdEntry.put("key", VALUES_LOCATION_ID);
        locationIdEntry.put("name", "1");
        attributeValues.add(locationIdEntry);
        final HashMap<String, String> commentEntry = new HashMap<>();
        commentEntry.put("key", VALUES_APPROVAL_COMMENT);
        commentEntry.put("name", "Testing complaint update");
        attributeValues.add(commentEntry);
        final HashMap<String, String> assigneeEntry = new HashMap<>();
        assigneeEntry.put("key", VALUES_POSITION_ID);
        assigneeEntry.put("name", "6");
        attributeValues.add(assigneeEntry);
        final HashMap<String, String> registeredEntry = new HashMap<>();
        registeredEntry.put("key", STATUS);
        registeredEntry.put("name", "PROCESSING");
        attributeValues.add(registeredEntry);
        final HashMap<String, String> departmentIdEntry = new HashMap<>();
        departmentIdEntry.put("key", VALUES_DEPARTMENT_ID);
        departmentIdEntry.put("name", "20");
        attributeValues.add(departmentIdEntry);
        serviceRequestMap.put("attribValues", attributeValues);
        complaintRequestMap.put("serviceRequest", serviceRequestMap);
        final RequestInfo requestInfo = RequestInfo.builder().build();
        complaintRequestMap.put("RequestInfo", requestInfo);
        final SevaRequest sevaRequest = new SevaRequest(complaintRequestMap);
        final WorkflowResponse workflowResponse = new WorkflowResponse(ASSIGNEE, getValuesWithStateId());
        when(workflowRepository.update(any(WorkflowRequest.class))).thenReturn(workflowResponse);
        List<AttributeEntry> responseValues = new ArrayList<>();
        responseValues.add(new AttributeEntry("systemLocationId", "1"));
        responseValues.add(new AttributeEntry("systemDepartmentId", "20"));
        responseValues.add(new AttributeEntry("systemPositionId", "6"));
        responseValues.add(new AttributeEntry("systemStatus", "PROCESSING"));
        final ServiceRequest complaint = ServiceRequest.builder()
            .attribValues(responseValues)
            .description("test")
            .complaintTypeCode("serviceCode")
            .build();
        when(complaintRestRepository.getComplaintByCrn("ap.public", "00015-2016-AP"))
            .thenReturn(complaint);

        final SevaRequest enrichedSevaRequest = workflowService.enrichWorkflow(sevaRequest);

        assertEquals(ASSIGNEE, enrichedSevaRequest.getDynamicSingleValue("systemPositionId"));
        assertEquals(STATE_ID, enrichedSevaRequest.getDynamicSingleValue("systemStateId"));
    }

    @Test
    public void testShouldUpdateSevaRequestWithUpdateWorkflowResponseWithDifferentLocation() {
        final HashMap<String, Object> complaintRequestMap = new HashMap<>();
        final HashMap<String, Object> serviceRequestMap = new HashMap<>();
        serviceRequestMap.put("serviceCode", "serviceCode");

        serviceRequestMap.put("serviceRequestId", "00015-2016-AP");
        serviceRequestMap.put("tenantId", "ap.public");
        final ArrayList<HashMap<String, String>> attributeValues = new ArrayList<>();
        final HashMap<String, String> stateIdEntry = new HashMap<>();
        stateIdEntry.put("key", VALUES_STATE_ID);
        stateIdEntry.put("name", "6");
        attributeValues.add(stateIdEntry);
        final HashMap<String, String> locationIdEntry = new HashMap<>();
        locationIdEntry.put("key", VALUES_LOCATION_ID);
        locationIdEntry.put("name", "2");
        attributeValues.add(locationIdEntry);
        final HashMap<String, String> commentEntry = new HashMap<>();
        commentEntry.put("key", VALUES_APPROVAL_COMMENT);
        commentEntry.put("name", "Testing complaint update");
        attributeValues.add(commentEntry);
        final HashMap<String, String> assigneeEntry = new HashMap<>();
        assigneeEntry.put("key", VALUES_POSITION_ID);
        assigneeEntry.put("name", "6");
        attributeValues.add(assigneeEntry);
        final HashMap<String, String> registeredEntry = new HashMap<>();
        registeredEntry.put("key", STATUS);
        registeredEntry.put("name", "PROCESSING");
        attributeValues.add(registeredEntry);
        final HashMap<String, String> departmentIdEntry = new HashMap<>();
        departmentIdEntry.put("key", VALUES_DEPARTMENT_ID);
        departmentIdEntry.put("name", "20");
        attributeValues.add(departmentIdEntry);
        serviceRequestMap.put("attribValues", attributeValues);
        complaintRequestMap.put("serviceRequest", serviceRequestMap);
        final RequestInfo requestInfo = RequestInfo.builder().build();
        complaintRequestMap.put("RequestInfo", requestInfo);
        final SevaRequest sevaRequest = new SevaRequest(complaintRequestMap);
        final WorkflowResponse workflowResponse = new WorkflowResponse(ASSIGNEE, getValuesWithStateId());
        when(workflowRepository.update(any(WorkflowRequest.class))).thenReturn(workflowResponse);
        List<AttributeEntry> responseValues = new ArrayList<>();
        responseValues.add(new AttributeEntry("systemLocationId", "1"));
        responseValues.add(new AttributeEntry("systemDepartmentId", "20"));
        responseValues.add(new AttributeEntry("systemPositionId", "6"));
        responseValues.add(new AttributeEntry("systemStatus", "PROCESSING"));
        when(complaintRestRepository.getComplaintByCrn("ap.public", serviceRequestMap.get("serviceRequestId")
            .toString())).thenReturn(ServiceRequest.builder()
                .attribValues(responseValues)
                .description("test")
                .complaintTypeCode("serviceCode")
                .build());

        final SevaRequest enrichedSevaRequest = workflowService.enrichWorkflow(sevaRequest);

        assertEquals(ASSIGNEE, enrichedSevaRequest.getDynamicSingleValue("systemPositionId"));
        assertEquals(STATE_ID, enrichedSevaRequest.getDynamicSingleValue("systemStateId"));
    }

    @Test
    public void testShouldUpdateSevaRequestWithUpdateWorkflowResponseWithDifferentServiceCode() {
        final HashMap<String, Object> complaintRequestMap = new HashMap<>();
        final HashMap<String, Object> serviceRequestMap = new HashMap<>();
        serviceRequestMap.put("service_code", "servicecodetest");
        serviceRequestMap.put("serviceRequestId", "00015-2016-AP");
        serviceRequestMap.put("tenantId", "ap.public");
        final ArrayList<HashMap<String, String>> attributeValues = new ArrayList<>();
        final HashMap<String, String> stateIdEntry = new HashMap<>();
        stateIdEntry.put("key", VALUES_STATE_ID);
        stateIdEntry.put("name", "6");
        attributeValues.add(stateIdEntry);
        final HashMap<String, String> locationIdEntry = new HashMap<>();
        locationIdEntry.put("key", VALUES_LOCATION_ID);
        locationIdEntry.put("name", "2");
        attributeValues.add(locationIdEntry);
        final HashMap<String, String> commentEntry = new HashMap<>();
        commentEntry.put("key", VALUES_APPROVAL_COMMENT);
        commentEntry.put("name", "Testing complaint update");
        attributeValues.add(commentEntry);
        final HashMap<String, String> assigneeEntry = new HashMap<>();
        assigneeEntry.put("key", VALUES_POSITION_ID);
        assigneeEntry.put("name", "6");
        attributeValues.add(assigneeEntry);
        final HashMap<String, String> registeredEntry = new HashMap<>();
        registeredEntry.put("key", STATUS);
        registeredEntry.put("name", "PROCESSING");
        attributeValues.add(registeredEntry);
        final HashMap<String, String> departmentIdEntry = new HashMap<>();
        departmentIdEntry.put("key", VALUES_DEPARTMENT_ID);
        departmentIdEntry.put("name", "20");
        attributeValues.add(departmentIdEntry);
        serviceRequestMap.put("attribValues", attributeValues);
        serviceRequestMap.put("attribValues", attributeValues);
        complaintRequestMap.put("serviceRequest", serviceRequestMap);
        final RequestInfo requestInfo = RequestInfo.builder().build();
        complaintRequestMap.put("RequestInfo", requestInfo);
        final SevaRequest sevaRequest = new SevaRequest(complaintRequestMap);
        final WorkflowResponse workflowResponse = new WorkflowResponse(ASSIGNEE, getValuesWithStateId());
        when(workflowRepository.update(any(WorkflowRequest.class))).thenReturn(workflowResponse);
        List<AttributeEntry> responseValues = new ArrayList<>();
        responseValues.add(new AttributeEntry("systemLocationId", "1"));
        responseValues.add(new AttributeEntry("systemDepartmentId", "20"));
        responseValues.add(new AttributeEntry("systemPositionId", "6"));
        responseValues.add(new AttributeEntry("systemComplaintStatus", "PROCESSING"));
        responseValues.add(new AttributeEntry("systemComplaintTypeCode", "BOG"));
        final ServiceRequest complaint = ServiceRequest.builder()
            .attribValues(responseValues)
            .description("test")
            .complaintTypeCode("serviceCode")
            .build();
        when(complaintRestRepository.getComplaintByCrn("ap.public", "00015-2016-AP"))
            .thenReturn(complaint);

        final SevaRequest enrichedSevaRequest = workflowService.enrichWorkflow(sevaRequest);

        assertEquals(ASSIGNEE, enrichedSevaRequest.getDynamicSingleValue("systemPositionId"));
        assertEquals(STATE_ID, enrichedSevaRequest.getDynamicSingleValue("systemStateId"));
    }

    @Test
    public void testShouldUpdateSevaRequestWithUpdateWorkflowResponseWithDifferentStatus() {
        final HashMap<String, Object> complaintRequestMap = new HashMap<>();
        final HashMap<String, String> valuesMap = new HashMap<>();
        valuesMap.put("systemLocationId", "2");
        valuesMap.put("systemStatus", "FORWARDED");
        valuesMap.put("systemPositionId", "6");
        valuesMap.put("systemStateId", "6");
        valuesMap.put("systemDepartmentId", "20");
        valuesMap.put("systemApprovalComments", "test");
        final HashMap<String, Object> serviceRequestMap = new HashMap<>();
        serviceRequestMap.put("values", valuesMap);
        serviceRequestMap.put("serviceCode", "serviceCode");

        serviceRequestMap.put("serviceRequestId", "00015-2016-AP");
        serviceRequestMap.put("tenantId", "ap.public");
        complaintRequestMap.put("serviceRequest", serviceRequestMap);
        serviceRequestMap.put("isAttribValuesPopulated", false);
        final ArrayList<HashMap<String, String>> attributeValues = new ArrayList<>();
        final HashMap<String, String> stateIdEntry = new HashMap<>();
        stateIdEntry.put("key", VALUES_STATE_ID);
        stateIdEntry.put("name", "6");
        attributeValues.add(stateIdEntry);
        final HashMap<String, String> locationIdEntry = new HashMap<>();
        locationIdEntry.put("key", VALUES_LOCATION_ID);
        locationIdEntry.put("name", "2");
        attributeValues.add(locationIdEntry);
        final HashMap<String, String> commentEntry = new HashMap<>();
        commentEntry.put("key", VALUES_APPROVAL_COMMENT);
        commentEntry.put("name", "Testing complaint update");
        attributeValues.add(commentEntry);
        final HashMap<String, String> assigneeEntry = new HashMap<>();
        assigneeEntry.put("key", VALUES_POSITION_ID);
        assigneeEntry.put("name", "6");
        attributeValues.add(assigneeEntry);
        final HashMap<String, String> registeredEntry = new HashMap<>();
        registeredEntry.put("key", STATUS);
        registeredEntry.put("name", "PROCESSING");
        attributeValues.add(registeredEntry);
        final HashMap<String, String> departmentIdEntry = new HashMap<>();
        departmentIdEntry.put("key", VALUES_DEPARTMENT_ID);
        departmentIdEntry.put("name", "20");
        attributeValues.add(departmentIdEntry);
        serviceRequestMap.put("attribValues", attributeValues);
        serviceRequestMap.put("attribValues", attributeValues);
        final RequestInfo requestInfo = RequestInfo.builder().build();
        complaintRequestMap.put("RequestInfo", requestInfo);
        final SevaRequest sevaRequest = new SevaRequest(complaintRequestMap);
        final WorkflowResponse workflowResponse = new WorkflowResponse(ASSIGNEE, getValuesWithStateId());
        when(workflowRepository.update(any(WorkflowRequest.class))).thenReturn(workflowResponse);
        List<AttributeEntry> responseValues = new ArrayList<>();
        responseValues.add(new AttributeEntry("systemLocationId", "2"));
        responseValues.add(new AttributeEntry("systemDepartmentId", "20"));
        responseValues.add(new AttributeEntry("systemPositionId", "6"));
        responseValues.add(new AttributeEntry("systemStatus", "PROCESSING"));
        when(complaintRestRepository.getComplaintByCrn("ap.public", serviceRequestMap.get("serviceRequestId")
            .toString()))
            .thenReturn(ServiceRequest.builder().attribValues(responseValues).description("test").complaintTypeCode
                ("serviceCode").build());

        final SevaRequest enrichedSevaRequest = workflowService.enrichWorkflow(sevaRequest);

        assertEquals(ASSIGNEE, enrichedSevaRequest.getDynamicSingleValue("systemPositionId"));
        assertEquals(STATE_ID, enrichedSevaRequest.getDynamicSingleValue("systemStateId"));
    }

    @Test
    public void testShouldUpdateSevaRequestWithUpdateWorkflowResponseWithDifferentAssignments() {
        final HashMap<String, Object> complaintRequestMap = new HashMap<>();
        final HashMap<String, Object> serviceRequestMap = new HashMap<>();
        serviceRequestMap.put("serviceCode", "serviceCode");
        serviceRequestMap.put("serviceRequestId", "00015-2016-AP");
        serviceRequestMap.put("tenantId", "ap.public");
        final ArrayList<HashMap<String, String>> attributeValues = new ArrayList<>();
        final HashMap<String, String> stateIdEntry = new HashMap<>();
        stateIdEntry.put("key", VALUES_STATE_ID);
        stateIdEntry.put("name", "6");
        attributeValues.add(stateIdEntry);
        final HashMap<String, String> locationIdEntry = new HashMap<>();
        locationIdEntry.put("key", VALUES_LOCATION_ID);
        locationIdEntry.put("name", "2");
        attributeValues.add(locationIdEntry);
        final HashMap<String, String> commentEntry = new HashMap<>();
        commentEntry.put("key", VALUES_APPROVAL_COMMENT);
        commentEntry.put("name", "test");
        attributeValues.add(commentEntry);
        final HashMap<String, String> assigneeEntry = new HashMap<>();
        assigneeEntry.put("key", VALUES_POSITION_ID);
        assigneeEntry.put("name", "8");
        attributeValues.add(assigneeEntry);
        final HashMap<String, String> registeredEntry = new HashMap<>();
        registeredEntry.put("key", STATUS);
        registeredEntry.put("name", "PROCESSING");
        attributeValues.add(registeredEntry);
        final HashMap<String, String> departmentIdEntry = new HashMap<>();
        departmentIdEntry.put("key", VALUES_DEPARTMENT_ID);
        departmentIdEntry.put("name", "20");
        attributeValues.add(departmentIdEntry);
        serviceRequestMap.put("attribValues", attributeValues);
        complaintRequestMap.put("serviceRequest", serviceRequestMap);
        final RequestInfo requestInfo = RequestInfo.builder().build();
        complaintRequestMap.put("RequestInfo", requestInfo);
        final SevaRequest sevaRequest = new SevaRequest(complaintRequestMap);
        final WorkflowResponse workflowResponse = new WorkflowResponse(ASSIGNEE, getValuesWithStateId());
        when(workflowRepository.update(any(WorkflowRequest.class))).thenReturn(workflowResponse);
        List<AttributeEntry> responseValues = new ArrayList<>();
        responseValues.add(new AttributeEntry("systemLocationId", "2"));
        responseValues.add(new AttributeEntry("systemDepartmentId", "20"));
        responseValues.add(new AttributeEntry("systemPositionId", "6"));
        responseValues.add(new AttributeEntry("systemStatus", "PROCESSING"));

        final ServiceRequest complaint = ServiceRequest.builder()
            .attribValues(responseValues)
            .description("test")
            .complaintTypeCode("serviceCode")
            .build();
        when(complaintRestRepository.getComplaintByCrn("ap.public", "00015-2016-AP"))
            .thenReturn(complaint);

        final SevaRequest enrichedSevaRequest = workflowService.enrichWorkflow(sevaRequest);

        assertEquals(ASSIGNEE, enrichedSevaRequest.getDynamicSingleValue("systemPositionId"));
        assertEquals(STATE_ID, enrichedSevaRequest.getDynamicSingleValue("systemStateId"));
    }

    @Test
    public void testShouldUpdateSevaRequestWithUpdateWorkflowResponseWithDifferentComments() {
        final HashMap<String, Object> complaintRequestMap = new HashMap<>();
        final HashMap<String, Object> serviceRequestMap = new HashMap<>();
        serviceRequestMap.put("serviceCode", "serviceCode");
        serviceRequestMap.put("serviceRequestId", "00015-2016-AP");
        serviceRequestMap.put("tenantId", "ap.public");
        final ArrayList<HashMap<String, String>> attributeValues = new ArrayList<>();
        final HashMap<String, String> stateIdEntry = new HashMap<>();
        stateIdEntry.put("key", VALUES_STATE_ID);
        stateIdEntry.put("name", "6");
        attributeValues.add(stateIdEntry);
        final HashMap<String, String> locationIdEntry = new HashMap<>();
        locationIdEntry.put("key", VALUES_LOCATION_ID);
        locationIdEntry.put("name", "2");
        attributeValues.add(locationIdEntry);
        final HashMap<String, String> commentEntry = new HashMap<>();
        commentEntry.put("key", VALUES_APPROVAL_COMMENT);
        commentEntry.put("name", "Testing complaint update");
        attributeValues.add(commentEntry);
        final HashMap<String, String> assigneeEntry = new HashMap<>();
        assigneeEntry.put("key", VALUES_POSITION_ID);
        assigneeEntry.put("name", "6");
        attributeValues.add(assigneeEntry);
        final HashMap<String, String> registeredEntry = new HashMap<>();
        registeredEntry.put("key", STATUS);
        registeredEntry.put("name", "PROCESSING");
        attributeValues.add(registeredEntry);
        final HashMap<String, String> departmentIdEntry = new HashMap<>();
        departmentIdEntry.put("key", VALUES_DEPARTMENT_ID);
        departmentIdEntry.put("name", "20");
        attributeValues.add(departmentIdEntry);
        serviceRequestMap.put("attribValues", attributeValues);
        complaintRequestMap.put("serviceRequest", serviceRequestMap);
        final RequestInfo requestInfo = RequestInfo.builder().build();
        complaintRequestMap.put("RequestInfo", requestInfo);
        final SevaRequest sevaRequest = new SevaRequest(complaintRequestMap);
        final WorkflowResponse workflowResponse = new WorkflowResponse(ASSIGNEE, getValuesWithStateId());
        when(workflowRepository.update(any(WorkflowRequest.class))).thenReturn(workflowResponse);
        List<AttributeEntry> responseValues = new ArrayList<>();
        responseValues.add(new AttributeEntry("systemLocationId", "2"));
        responseValues.add(new AttributeEntry("systemDepartmentId", "20"));
        responseValues.add(new AttributeEntry("systemPositionId", "6"));
        responseValues.add(new AttributeEntry("systemStatus", "PROCESSING"));

        when(complaintRestRepository.getComplaintByCrn("ap.public", serviceRequestMap.get("serviceRequestId")
            .toString()))
            .thenReturn(ServiceRequest.builder().attribValues(responseValues).description("test").complaintTypeCode
                ("serviceCode").build());

        final SevaRequest enrichedSevaRequest = workflowService.enrichWorkflow(sevaRequest);

        assertEquals(ASSIGNEE, enrichedSevaRequest.getDynamicSingleValue("systemPositionId"));
        assertEquals(STATE_ID, enrichedSevaRequest.getDynamicSingleValue("systemStateId"));
    }

    private Map<String, Attribute> getValuesWithStateId() {
        Map<String, Attribute> valueMap = new HashMap<>();
        List<Value> stateIdValues = new ArrayList<>();
        stateIdValues.add(new Value(STATE_ID_KEY, STATE_ID));
        final Attribute attribute =
            new Attribute(Boolean.TRUE, STATE_ID_KEY, "String", Boolean.FALSE, StringUtils.EMPTY, stateIdValues);
        valueMap.put(STATE_ID_KEY, attribute);
        return valueMap;
    }


}