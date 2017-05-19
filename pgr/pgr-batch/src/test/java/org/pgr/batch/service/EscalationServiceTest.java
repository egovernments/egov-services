package org.pgr.batch.service;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.pgr.batch.repository.ComplaintRestRepository;
import org.pgr.batch.repository.contract.ServiceRequest;
import org.pgr.batch.repository.contract.ServiceResponse;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class EscalationServiceTest {

    @Mock
    private WorkflowService workflowService;

    @Mock
    private UserService userService;

    @Mock
    private ComplaintRestRepository complaintRestRepository;

    @InjectMocks
    private  EscalationService escalationService;

    @Captor
    ArgumentCaptor<RequestInfo> requestInfoArgumentCaptor;

    @Test
    public void test_should_check_that_complaint_gets_escalated() throws Exception {
        List<ServiceRequest> serviceRequestList = asList(new ServiceRequest(), new ServiceRequest());
        ServiceResponse serviceResponse = new ServiceResponse(null,serviceRequestList);
        when(complaintRestRepository.getComplaintsEligibleForEscalation("default")).thenReturn(serviceResponse);
        when(userService.getUserByUserName("system","default")).thenReturn(User.builder().id(1L).build());

        escalationService.escalateComplaint();

        verify(workflowService).enrichWorkflowForEscalation(eq(serviceRequestList.get(0)), requestInfoArgumentCaptor.capture());
        verify(workflowService).enrichWorkflowForEscalation(eq(serviceRequestList.get(1)), requestInfoArgumentCaptor.capture());

        List<RequestInfo> requestInfoList = requestInfoArgumentCaptor.getAllValues();


    }

}