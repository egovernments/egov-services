package org.pgr.batch.service;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.pgr.common.contract.AttributeEntry;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.pgr.batch.repository.ComplaintMessageQueueRepository;
import org.pgr.batch.repository.ComplaintRestRepository;
import org.pgr.batch.repository.TenantRepository;
import org.pgr.batch.repository.contract.SearchTenantResponse;
import org.pgr.batch.repository.contract.ServiceRequest;
import org.pgr.batch.repository.contract.ServiceResponse;
import org.pgr.batch.repository.contract.Tenant;
import org.pgr.batch.service.model.Position;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collections;
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
    private PositionService positionService;

    @Mock
    private EscalationDateService escalationDateService;

    @Mock
    private ComplaintRestRepository complaintRestRepository;

    @Mock
    private ComplaintMessageQueueRepository complaintMessageQueueRepository;

    @Mock
    private TenantRepository tenantRepository;

    @InjectMocks
    private  EscalationService escalationService;

    @Captor
    ArgumentCaptor<RequestInfo> requestInfoArgumentCaptor;

    @Test
    public void test_should_check_that_complaint_gets_escalated() {
        List<ServiceRequest> serviceRequestList = asList(getServiceRequest(), getServiceRequest());
        ServiceResponse serviceResponse = new ServiceResponse(null,serviceRequestList);
        when(tenantRepository.getAllTenants()).thenReturn(getTenantDetails());
        when(complaintRestRepository.getComplaintsEligibleForEscalation("default")).thenReturn(serviceResponse);
        when(userService.getUserByUserName("system","default")).thenReturn(User.builder().id(1L).build());

        escalationService.escalateComplaintForAllTenants();

        verify(workflowService).enrichWorkflowForEscalation(eq(serviceRequestList.get(0)), requestInfoArgumentCaptor.capture());
        verify(workflowService).enrichWorkflowForEscalation(eq(serviceRequestList.get(1)), requestInfoArgumentCaptor.capture());

        List<RequestInfo> requestInfoList = requestInfoArgumentCaptor.getAllValues();
    }

	@Test
	public void test_should_continue_escalating_remaining_complaints_when_failure_of_a_single_complaint_happens() {
		List<ServiceRequest> serviceRequestList = asList(getServiceRequest(), getServiceRequest());
		ServiceResponse serviceResponse = new ServiceResponse(null,serviceRequestList);
		when(tenantRepository.getAllTenants()).thenReturn(getTenantDetails());
		when(complaintRestRepository.getComplaintsEligibleForEscalation("default")).thenReturn(serviceResponse);
		when(userService.getUserByUserName("system","default")).thenReturn(User.builder().id(1L).build());
		when(workflowService.enrichWorkflowForEscalation(eq(serviceRequestList.get(0)), any())).thenThrow(new RuntimeException());

		escalationService.escalateComplaintForAllTenants();

		verify(workflowService).enrichWorkflowForEscalation(eq(serviceRequestList.get(0)), any());
		verify(workflowService).enrichWorkflowForEscalation(eq(serviceRequestList.get(1)), any());
	}

    private ServiceRequest getServiceRequest(){

        List<AttributeEntry> values = new ArrayList<>();
        values.add(new AttributeEntry("keyword","Complaint"));
        values.add(new AttributeEntry("assignmentId","1L"));

        return ServiceRequest.builder()
                .crn("1234")
                .tenantId("default")
                .attribValues(values)
                .build();
    }

    private SearchTenantResponse getTenantDetails(){
        Tenant tenant = Tenant.builder()
                .code("default")
                .description("default")
                .build();

        return new SearchTenantResponse(null, Collections.singletonList(tenant));
    }
}