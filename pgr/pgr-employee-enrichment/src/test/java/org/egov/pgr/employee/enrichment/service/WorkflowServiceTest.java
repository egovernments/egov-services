package org.egov.pgr.employee.enrichment.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.egov.pgr.employee.enrichment.consumer.contract.RequestInfo;
import org.egov.pgr.employee.enrichment.model.SevaRequest;
import org.egov.pgr.employee.enrichment.repository.ComplaintRestRepository;
import org.egov.pgr.employee.enrichment.repository.WorkflowRepository;
import org.egov.pgr.employee.enrichment.repository.contract.Attribute;
import org.egov.pgr.employee.enrichment.repository.contract.ServiceRequest;
import org.egov.pgr.employee.enrichment.repository.contract.Value;
import org.egov.pgr.employee.enrichment.repository.contract.WorkflowRequest;
import org.egov.pgr.employee.enrichment.repository.contract.WorkflowResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class WorkflowServiceTest {

    public static final String ASSIGNEE = "1";
    public static final String STATE_ID = "2";
    public static final String STATE_ID_KEY = "stateId";
    @Mock
    private WorkflowRepository workflowRepository;

    @InjectMocks
    private WorkflowService workflowService;

    @Mock
    private ComplaintRestRepository complaintRestRepository;
    
    
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
        final RequestInfo requestInfo = RequestInfo.builder().action("POST").build();
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

    @Test
    public void testShouldUpdateSevaRequestWithUpdateWorkflowResponseWithDifferentDepartment() {
        final HashMap<String, Object> complaintRequestMap = new HashMap<>();
        final HashMap<String, String> valuesMap = new HashMap<>();
        valuesMap.put("locationId", "1");
        valuesMap.put("status", "PROCESSING");
        valuesMap.put("assignment_id","6");
        valuesMap.put("stateId","6");
        valuesMap.put("departmentId", "19");
        valuesMap.put("approvalComments","Testing complaint update");
        final HashMap<String, Object> serviceRequestMap = new HashMap<>();
        serviceRequestMap.put("values", valuesMap);
        serviceRequestMap.put("service_code", "serviceCode");
        serviceRequestMap.put("service_request_id", "00015-2016-AP");
        complaintRequestMap.put("ServiceRequest", serviceRequestMap);
        final RequestInfo requestInfo = RequestInfo.builder().build();
        complaintRequestMap.put("RequestInfo", requestInfo);
        final SevaRequest sevaRequest = new SevaRequest(complaintRequestMap);
        final WorkflowResponse workflowResponse = new WorkflowResponse(ASSIGNEE, getValuesWithStateId());
        when(workflowRepository.update(any(WorkflowRequest.class))).thenReturn(workflowResponse);
        Map<String,String> responseValues = new HashMap<String,String>();
        responseValues.put("locationId", "1");
        responseValues.put("departmentId", "20");
        responseValues.put("assigneeId", "6");
        responseValues.put("complaintStatus", "PROCESSING");
        when(complaintRestRepository.getComplaintByCrn(1L, serviceRequestMap.get("service_request_id").toString()))
        .thenReturn(ServiceRequest.builder().values(responseValues).description("Testing complaint update").complaintTypeCode("serviceCode").build());
        
        final SevaRequest enrichedSevaRequest = workflowService.enrichWorkflow(sevaRequest);

        assertEquals(ASSIGNEE, enrichedSevaRequest.getValues().get("assignment_id") );
        assertEquals(STATE_ID, enrichedSevaRequest.getValues().get("stateId"));
    }
    
   @Test
    public void testShouldUpdateSevaRequestWithUpdateWorkflowResponseWithSameDepartment() {
	   final HashMap<String, Object> complaintRequestMap = new HashMap<>();
       final HashMap<String, String> valuesMap = new HashMap<>();
       valuesMap.put("locationId", "1");
       valuesMap.put("status", "PROCESSING");
       valuesMap.put("assignment_id","6");
       valuesMap.put("stateId","6");
       valuesMap.put("departmentId", "20");
       valuesMap.put("approvalComments","Testing complaint update");
       final HashMap<String, Object> serviceRequestMap = new HashMap<>();
       serviceRequestMap.put("values", valuesMap);
       serviceRequestMap.put("service_code", "serviceCode");
       
       serviceRequestMap.put("service_request_id", "00015-2016-AP");
       complaintRequestMap.put("ServiceRequest", serviceRequestMap);
       final RequestInfo requestInfo = RequestInfo.builder().build();
       complaintRequestMap.put("RequestInfo", requestInfo);
       final SevaRequest sevaRequest = new SevaRequest(complaintRequestMap);
       final WorkflowResponse workflowResponse = new WorkflowResponse(ASSIGNEE, getValuesWithStateId());
       when(workflowRepository.update(any(WorkflowRequest.class))).thenReturn(workflowResponse);
      Map<String,String> responseValues = new HashMap<String,String>();
       responseValues.put("locationId", "1");
       responseValues.put("departmentId", "20");
       responseValues.put("assigneeId", "6");
       responseValues.put("complaintStatus", "PROCESSING");
       when(complaintRestRepository.getComplaintByCrn(1L, serviceRequestMap.get("service_request_id").toString()))
       .thenReturn(ServiceRequest.builder().values(responseValues).description("test").complaintTypeCode("serviceCode").build());

       final SevaRequest enrichedSevaRequest = workflowService.enrichWorkflow(sevaRequest);

       assertEquals(ASSIGNEE, enrichedSevaRequest.getValues().get("assignment_id") );
       assertEquals(STATE_ID, enrichedSevaRequest.getValues().get("stateId"));    }


    @Test
    public void testShouldUpdateSevaRequestWithUpdateWorkflowResponseWithDifferentLocation() {
        final HashMap<String, Object> complaintRequestMap = new HashMap<>();
        final HashMap<String, String> valuesMap = new HashMap<>();
        valuesMap.put("locationId", "2");
        valuesMap.put("status", "PROCESSING");
        valuesMap.put("assignment_id","6");
        valuesMap.put("stateId","6");
        valuesMap.put("departmentId", "20");
        valuesMap.put("approvalComments","Testing complaint update");
        final HashMap<String, Object> serviceRequestMap = new HashMap<>();
        serviceRequestMap.put("values", valuesMap);
        serviceRequestMap.put("service_code", "serviceCode");

        serviceRequestMap.put("service_request_id", "00015-2016-AP");
        complaintRequestMap.put("ServiceRequest", serviceRequestMap);
        final RequestInfo requestInfo = RequestInfo.builder().build();
        complaintRequestMap.put("RequestInfo", requestInfo);
        final SevaRequest sevaRequest = new SevaRequest(complaintRequestMap);
        final WorkflowResponse workflowResponse = new WorkflowResponse(ASSIGNEE, getValuesWithStateId());
        when(workflowRepository.update(any(WorkflowRequest.class))).thenReturn(workflowResponse);
        Map<String,String> responseValues = new HashMap<String,String>();
        responseValues.put("locationId", "1");
        responseValues.put("departmentId", "20");
        responseValues.put("assigneeId", "6");
        responseValues.put("complaintStatus", "PROCESSING");
        when(complaintRestRepository.getComplaintByCrn(1L, serviceRequestMap.get("service_request_id").toString()))
            .thenReturn(ServiceRequest.builder().values(responseValues).description("test").complaintTypeCode("serviceCode").build());

        final SevaRequest enrichedSevaRequest = workflowService.enrichWorkflow(sevaRequest);

        assertEquals(ASSIGNEE, enrichedSevaRequest.getValues().get("assignment_id") );
        assertEquals(STATE_ID, enrichedSevaRequest.getValues().get("stateId"));    }

    @Test
    public void testShouldUpdateSevaRequestWithUpdateWorkflowResponseWithDifferentServiceCode() {
        final HashMap<String, Object> complaintRequestMap = new HashMap<>();
        final HashMap<String, String> valuesMap = new HashMap<>();
        valuesMap.put("locationId", "2");
        valuesMap.put("status", "PROCESSING");
        valuesMap.put("assignment_id","6");
        valuesMap.put("stateId","6");
        valuesMap.put("departmentId", "20");
        valuesMap.put("approvalComments","Testing complaint update");
        final HashMap<String, Object> serviceRequestMap = new HashMap<>();
        serviceRequestMap.put("values", valuesMap);
        serviceRequestMap.put("service_code", "servicecodetest");

        serviceRequestMap.put("service_request_id", "00015-2016-AP");
        complaintRequestMap.put("ServiceRequest", serviceRequestMap);
        final RequestInfo requestInfo = RequestInfo.builder().build();
        complaintRequestMap.put("RequestInfo", requestInfo);
        final SevaRequest sevaRequest = new SevaRequest(complaintRequestMap);
        final WorkflowResponse workflowResponse = new WorkflowResponse(ASSIGNEE, getValuesWithStateId());
        when(workflowRepository.update(any(WorkflowRequest.class))).thenReturn(workflowResponse);
        Map<String,String> responseValues = new HashMap<String,String>();
        responseValues.put("locationId", "1");
        responseValues.put("departmentId", "20");
        responseValues.put("assigneeId", "6");
        responseValues.put("complaintStatus", "PROCESSING");
        responseValues.put("complaintTypeCode","BOG");
        when(complaintRestRepository.getComplaintByCrn(1L, serviceRequestMap.get("service_request_id").toString()))
            .thenReturn(ServiceRequest.builder().values(responseValues).description("test").complaintTypeCode("serviceCode").build());

        final SevaRequest enrichedSevaRequest = workflowService.enrichWorkflow(sevaRequest);

        assertEquals(ASSIGNEE, enrichedSevaRequest.getValues().get("assignment_id") );
        assertEquals(STATE_ID, enrichedSevaRequest.getValues().get("stateId"));    }

    @Test
    public void testShouldUpdateSevaRequestWithUpdateWorkflowResponseWithDifferentStatus() {
        final HashMap<String, Object> complaintRequestMap = new HashMap<>();
        final HashMap<String, String> valuesMap = new HashMap<>();
        valuesMap.put("locationId", "2");
        valuesMap.put("status", "FORWARDED");
        valuesMap.put("assignment_id","6");
        valuesMap.put("stateId","6");
        valuesMap.put("departmentId", "20");
        valuesMap.put("approvalComments","test");
        final HashMap<String, Object> serviceRequestMap = new HashMap<>();
        serviceRequestMap.put("values", valuesMap);
        serviceRequestMap.put("service_code", "serviceCode");

        serviceRequestMap.put("service_request_id", "00015-2016-AP");
        complaintRequestMap.put("ServiceRequest", serviceRequestMap);
        final RequestInfo requestInfo = RequestInfo.builder().build();
        complaintRequestMap.put("RequestInfo", requestInfo);
        final SevaRequest sevaRequest = new SevaRequest(complaintRequestMap);
        final WorkflowResponse workflowResponse = new WorkflowResponse(ASSIGNEE, getValuesWithStateId());
        when(workflowRepository.update(any(WorkflowRequest.class))).thenReturn(workflowResponse);
        Map<String,String> responseValues = new HashMap<String,String>();
        responseValues.put("locationId", "2");
        responseValues.put("departmentId", "20");
        responseValues.put("assigneeId", "6");
        responseValues.put("complaintStatus", "PROCESSING");

        when(complaintRestRepository.getComplaintByCrn(1L, serviceRequestMap.get("service_request_id").toString()))
            .thenReturn(ServiceRequest.builder().values(responseValues).description("test").complaintTypeCode("serviceCode").build());

        final SevaRequest enrichedSevaRequest = workflowService.enrichWorkflow(sevaRequest);

        assertEquals(ASSIGNEE, enrichedSevaRequest.getValues().get("assignment_id") );
        assertEquals(STATE_ID, enrichedSevaRequest.getValues().get("stateId"));    }

    @Test
    public void testShouldUpdateSevaRequestWithUpdateWorkflowResponseWithDifferentAssignments() {
        final HashMap<String, Object> complaintRequestMap = new HashMap<>();
        final HashMap<String, String> valuesMap = new HashMap<>();
        valuesMap.put("locationId", "2");
        valuesMap.put("status", "PROCESSING");
        valuesMap.put("assignment_id","8");
        valuesMap.put("stateId","6");
        valuesMap.put("departmentId", "20");
        valuesMap.put("approvalComments","test");
        final HashMap<String, Object> serviceRequestMap = new HashMap<>();
        serviceRequestMap.put("values", valuesMap);
        serviceRequestMap.put("service_code", "serviceCode");

        serviceRequestMap.put("service_request_id", "00015-2016-AP");
        complaintRequestMap.put("ServiceRequest", serviceRequestMap);
        final RequestInfo requestInfo = RequestInfo.builder().build();
        complaintRequestMap.put("RequestInfo", requestInfo);
        final SevaRequest sevaRequest = new SevaRequest(complaintRequestMap);
        final WorkflowResponse workflowResponse = new WorkflowResponse(ASSIGNEE, getValuesWithStateId());
        when(workflowRepository.update(any(WorkflowRequest.class))).thenReturn(workflowResponse);
        Map<String,String> responseValues = new HashMap<String,String>();
        responseValues.put("locationId", "2");
        responseValues.put("departmentId", "20");
        responseValues.put("assigneeId", "6");
        responseValues.put("complaintStatus", "PROCESSING");

        when(complaintRestRepository.getComplaintByCrn(1L, serviceRequestMap.get("service_request_id").toString()))
            .thenReturn(ServiceRequest.builder().values(responseValues).description("test").complaintTypeCode("serviceCode").build());

        final SevaRequest enrichedSevaRequest = workflowService.enrichWorkflow(sevaRequest);

        assertEquals(ASSIGNEE, enrichedSevaRequest.getValues().get("assignment_id") );
        assertEquals(STATE_ID, enrichedSevaRequest.getValues().get("stateId"));    }

    @Test
    public void testShouldUpdateSevaRequestWithUpdateWorkflowResponseWithDifferentComments() {
        final HashMap<String, Object> complaintRequestMap = new HashMap<>();
        final HashMap<String, String> valuesMap = new HashMap<>();
        valuesMap.put("locationId", "2");
        valuesMap.put("status", "PROCESSING");
        valuesMap.put("assignment_id","6");
        valuesMap.put("stateId","6");
        valuesMap.put("departmentId", "20");
        valuesMap.put("approvalComments","Testing complaint update");
        final HashMap<String, Object> serviceRequestMap = new HashMap<>();
        serviceRequestMap.put("values", valuesMap);
        serviceRequestMap.put("service_code", "serviceCode");

        serviceRequestMap.put("service_request_id", "00015-2016-AP");
        complaintRequestMap.put("ServiceRequest", serviceRequestMap);
        final RequestInfo requestInfo = RequestInfo.builder().build();
        complaintRequestMap.put("RequestInfo", requestInfo);
        final SevaRequest sevaRequest = new SevaRequest(complaintRequestMap);
        final WorkflowResponse workflowResponse = new WorkflowResponse(ASSIGNEE, getValuesWithStateId());
        when(workflowRepository.update(any(WorkflowRequest.class))).thenReturn(workflowResponse);
        Map<String,String> responseValues = new HashMap<String,String>();
        responseValues.put("locationId", "2");
        responseValues.put("departmentId", "20");
        responseValues.put("assigneeId", "6");
        responseValues.put("complaintStatus", "PROCESSING");

        when(complaintRestRepository.getComplaintByCrn(1L, serviceRequestMap.get("service_request_id").toString()))
            .thenReturn(ServiceRequest.builder().values(responseValues).description("test").complaintTypeCode("serviceCode").build());

        final SevaRequest enrichedSevaRequest = workflowService.enrichWorkflow(sevaRequest);

        assertEquals(ASSIGNEE, enrichedSevaRequest.getValues().get("assignment_id") );
        assertEquals(STATE_ID, enrichedSevaRequest.getValues().get("stateId"));    }

    private Map<String, Attribute> getValuesWithStateId() {
        Map<String, Attribute> valueMap = new HashMap<>();
        List<Value> stateIdValues = new ArrayList<>();
        stateIdValues.add(new Value(STATE_ID_KEY, STATE_ID));
        valueMap.put(STATE_ID_KEY, new Attribute(Boolean.TRUE, STATE_ID_KEY, "String", Boolean.FALSE, StringUtils.EMPTY, stateIdValues));
        return valueMap;
    }


}