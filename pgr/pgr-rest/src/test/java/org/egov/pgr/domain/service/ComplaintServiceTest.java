package org.egov.pgr.domain.service;

import org.egov.pgr.domain.exception.InvalidComplaintException;
import org.egov.pgr.domain.model.*;
import org.egov.pgr.persistence.queue.contract.RequestInfo;
import org.egov.pgr.persistence.queue.contract.ServiceRequest;
import org.egov.pgr.persistence.queue.contract.SevaRequest;
import org.egov.pgr.persistence.repository.ComplaintRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ComplaintServiceTest {

    private static final String CRN = "crn";
    private static final String TENANT_ID = "tenantId";
    @Mock
    private ComplaintRepository complaintRepository;

    @Mock
    private SevaNumberGeneratorService sevaNumberGeneratorService;

    @InjectMocks
    private ComplaintService complaintService;

    @Test
    public void test_should_validate_complaint_on_save() {
        final Complaint complaint = mock(Complaint.class);
        when(complaint.getAuthenticatedUser()).thenReturn(getAuthenticatedUser());
        final SevaRequest sevaRequest = getSevaRequest();

        complaintService.save(complaint, sevaRequest);

        verify(complaint, times(1)).validate();
    }

    @Test
    public void test_should_validate_complaint_on_update() {
        final Complaint complaint = mock(Complaint.class);
        when(complaint.getAuthenticatedUser()).thenReturn(getAuthenticatedUser());
        final SevaRequest sevaRequest = getSevaRequest();

        complaintService.update(complaint, sevaRequest);

        verify(complaint, times(1)).validate();
    }

    @Test
    public void test_should_update_seva_request_with_domain_complaint_on_save() {
        final Complaint complaint = getComplaint();
        final SevaRequest sevaRequest = mock(SevaRequest.class);

        complaintService.save(complaint, sevaRequest);

        verify(sevaRequest).update(complaint);
    }

    @Test
    public void test_should_update_seva_request_with_domain_complaint_on_update() {
        final Complaint complaint = getComplaint();
        final SevaRequest sevaRequest = mock(SevaRequest.class);

        complaintService.update(complaint, sevaRequest);

        verify(sevaRequest).update(complaint);
    }

    @Test
    public void test_should_set_generated_crn_to_domain_complaint_on_save() {
        final Complaint complaint = getComplaint();
        final SevaRequest sevaRequest = getSevaRequest();
        when(sevaNumberGeneratorService.generate()).thenReturn(CRN);

        complaintService.save(complaint, sevaRequest);

        assertEquals(CRN, complaint.getCrn());
    }

    @Test
    public void test_should_persist_seva_request_on_save() {
        final Complaint complaint = getComplaint();
        final SevaRequest sevaRequest = getSevaRequest();
        sevaRequest.setServiceRequest(getServiceRequest());
        when(sevaNumberGeneratorService.generate()).thenReturn(CRN);

        complaintService.save(complaint, sevaRequest);

        verify(complaintRepository).save(sevaRequest);
    }

    @Test
    public void test_should_persist_seva_request_on_update() {
        final Complaint complaint = getComplaint();
        final SevaRequest sevaRequest = getSevaRequest();
        sevaRequest.setServiceRequest(getServiceRequest());

        complaintService.update(complaint, sevaRequest);

        verify(complaintRepository).update(sevaRequest);
    }

    @Test
    public void test_should_find_all_complaints_by_search_criteria() {
        final ComplaintSearchCriteria searchCriteria = ComplaintSearchCriteria.builder().build();
        final Complaint expectedComplaint = getComplaint();
        when(complaintRepository.findAll(searchCriteria)).thenReturn(Collections.singletonList(expectedComplaint));

        final List<Complaint> actualComplaints = complaintService.findAll(searchCriteria);

        assertEquals(1, actualComplaints.size());
        assertEquals(expectedComplaint, actualComplaints.get(0));
    }

    private Complaint getComplaint() {
        final AuthenticatedUser user = getAuthenticatedUser();
        return Complaint.builder()
                .tenantId(TENANT_ID)
                .authenticatedUser(user)
                .complainant(Complainant.builder().build())
                .complaintLocation(new ComplaintLocation(new Coordinates(0d, 0d), "id", null))
                .build();
    }

    private AuthenticatedUser getAuthenticatedUser() {
        return AuthenticatedUser.builder()
                .id(1)
                .type(Collections.singletonList(UserType.CITIZEN))
                .build();
    }

    private SevaRequest getSevaRequest() {
        final SevaRequest sevaRequest = new SevaRequest();
        sevaRequest.setRequestInfo(new RequestInfo());
        sevaRequest.setServiceRequest(ServiceRequest.builder().build());
        return sevaRequest;
    }

    private ServiceRequest getServiceRequest() {
        return ServiceRequest.builder().build();
    }

}