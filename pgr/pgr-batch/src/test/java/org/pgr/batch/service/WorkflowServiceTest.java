package org.pgr.batch.service;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.pgr.common.contract.AttributeEntry;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.pgr.batch.repository.WorkflowRepository;
import org.pgr.batch.repository.contract.*;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class WorkflowServiceTest {

    public static final String IN_PROGRESS = "IN PROGRESS";
    public static final String COMPLAINT = "complaint";
    public static final String REGISTERED = "REGISTERED";
    public static final String COMPLAINT_STATUS = "systemStatus";
    public static final String UPDATE = "update";
    @Mock
    private WorkflowRepository workflowRepository;

    @InjectMocks
    private WorkflowService workflowService;

    @Test
    public void test_should_update_workflow_for_escalated_complaints() throws Exception {

        ServiceRequest expectedServiceRequest = ServiceRequest.builder()
                .complaintTypeCode("BRKBN")
                .build();

        WorkflowRequest escalationRequest = WorkflowRequest.builder()
                .businessKey(COMPLAINT)
                .action(UPDATE)
                .status(REGISTERED)
                .type(COMPLAINT)
                .build();

        WorkflowResponse expectedWorkflowResponse = WorkflowResponse.builder()
                .assignee("1")
                .values(getExpectedValues())
                .build();

        when(workflowRepository.update(any())).thenReturn(expectedWorkflowResponse);

        ServiceRequest request = workflowService.enrichWorkflowForEscalation(getServiceRequest(),getRequestInfo());

        assertEquals(request.getComplaintTypeCode(),expectedServiceRequest.getComplaintTypeCode());
        assertEquals(IN_PROGRESS,expectedWorkflowResponse.getValueForKey(COMPLAINT_STATUS));

    }

    private Map<String,Attribute> getExpectedValues(){
        Attribute complaintStatusAttribute = Attribute.builder()
                .values(Collections.singletonList(new Value(COMPLAINT_STATUS, IN_PROGRESS)))
                .build();

        Map<String,Attribute>  values = new HashMap<>();
        values.put("systemStatus",complaintStatusAttribute);

        return values;
    }

    private ServiceRequest getServiceRequest(){

        List<AttributeEntry> attribValues = new ArrayList<>();
        attribValues.add(new AttributeEntry(COMPLAINT_STATUS,REGISTERED));
        attribValues.add(new AttributeEntry("systemStateId","1"));

     return ServiceRequest.builder()
             .address("central city")
             .complaintTypeCode("BRKBN")
             .complaintTypeName("Broken Bin")
             .attribValues(attribValues)
             .build();
    }

    private RequestInfo getRequestInfo(){
        User userInfo = User.builder()
                .userName("system")
                .mobileNumber("0000000000")
                .name("System")
                .emailId("system@admin.com")
                .build();

        return RequestInfo.builder()
                .action(UPDATE)
                .userInfo(userInfo)
                .build();
    }

}