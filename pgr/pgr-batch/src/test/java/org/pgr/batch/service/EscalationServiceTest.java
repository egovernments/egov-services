package org.pgr.batch.service;

import org.egov.common.contract.request.User;
import org.egov.pgr.common.contract.AttributeEntry;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.pgr.batch.repository.ComplaintMessageQueueRepository;
import org.pgr.batch.repository.ComplaintRestRepository;
import org.pgr.batch.repository.TenantRepository;
import org.pgr.batch.repository.contract.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.pgr.batch.repository.contract.ServiceRequest.PREVIOUS_ASSIGNEE;

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
    private EscalationService escalationService;

    @Test
    public void test_should_check_that_complaint_gets_escalated() {
        List<ServiceRequest> serviceRequestList = asList(
                getServiceRequest(null),
                getServiceRequest(null)
        );
        ServiceResponse serviceResponse = new ServiceResponse(null, serviceRequestList);
        when(tenantRepository.getAllTenants()).thenReturn(getTenantDetails());
        when(userService.getUserByUserName("system", "default")).thenReturn(getUser());
        when(complaintRestRepository.getComplaintsEligibleForEscalation("default", 1L)).thenReturn(serviceResponse);
        whenInvokedUpdateAssignee(serviceRequestList.get(0), "3");
        whenInvokedUpdateAssignee(serviceRequestList.get(1), "3");
        escalationService.escalateComplaintForAllTenants();

        verify(complaintMessageQueueRepository, times(2)).save(any());
        assertTrue(serviceRequestList.get(0).isEscalated());
        assertTrue(serviceRequestList.get(1).isEscalated());
    }

    @Test
    public void test_should_not_push_complaint_to_message_queue_when_new_assignee_same_as_previous_assignee() {
        List<ServiceRequest> serviceRequestList = Collections.singletonList(getServiceRequest(null));
        ServiceResponse serviceResponse = new ServiceResponse(null, serviceRequestList);
        when(tenantRepository.getAllTenants()).thenReturn(getTenantDetails());
        when(complaintRestRepository.getComplaintsEligibleForEscalation("default", 1L)).thenReturn(serviceResponse);
        when(userService.getUserByUserName("system", "default")).thenReturn(getUser());
        when(workflowService.enrichWorkflowForEscalation(eq(serviceRequestList.get(0)), any()))
                .thenReturn(getServiceRequest("1"));

        escalationService.escalateComplaintForAllTenants();

        verify(complaintMessageQueueRepository, never()).save(any());
    }

    @Test
    public void test_should_continue_escalating_remaining_complaints_when_failure_of_a_single_complaint_happens() {
        List<ServiceRequest> serviceRequestList = asList(
                getServiceRequest(null),
                getServiceRequest(null)
        );
        ServiceResponse serviceResponse = new ServiceResponse(null, serviceRequestList);
        when(tenantRepository.getAllTenants()).thenReturn(getTenantDetails());
        when(complaintRestRepository.getComplaintsEligibleForEscalation("default", 1L))
                .thenReturn(serviceResponse);
        when(userService.getUserByUserName("system", "default")).thenReturn(getUser());
        when(workflowService.enrichWorkflowForEscalation(eq(serviceRequestList.get(0)), any()))
                .thenThrow(new RuntimeException());

        escalationService.escalateComplaintForAllTenants();

        verify(workflowService).enrichWorkflowForEscalation(eq(serviceRequestList.get(0)), any());
        verify(workflowService).enrichWorkflowForEscalation(eq(serviceRequestList.get(1)), any());
    }

    private ServiceRequest getServiceRequest(String previousAssignee) {

        List<AttributeEntry> values = new ArrayList<>();
        values.add(new AttributeEntry("keyword", "Complaint"));
        values.add(new AttributeEntry("assignmentId", "1"));
        if (previousAssignee != null)
            values.add(new AttributeEntry(PREVIOUS_ASSIGNEE, previousAssignee));

        return ServiceRequest.builder()
                .crn("1234")
                .tenantId("default")
                .attribValues(values)
                .build();
    }

    private SearchTenantResponse getTenantDetails() {
        Tenant tenant = Tenant.builder()
                .code("default")
                .description("default")
                .build();

        return new SearchTenantResponse(null, Collections.singletonList(tenant));
    }

    private void whenInvokedUpdateAssignee(ServiceRequest serviceRequest, String assignee) {
        when(workflowService.enrichWorkflowForEscalation(eq(serviceRequest), any()))
                .thenAnswer(invocationOnMock -> {
                    ServiceRequest serviceRequestParameter = invocationOnMock
                            .getArgumentAt(0, ServiceRequest.class);
                    final WorkflowResponse workflowResponse = new WorkflowResponse(assignee, Collections.emptyMap());
                    serviceRequestParameter.update(workflowResponse);
                    return serviceRequestParameter;
                });
    }

    private User getUser() {
        return User.builder().id(1L).build();
    }

}