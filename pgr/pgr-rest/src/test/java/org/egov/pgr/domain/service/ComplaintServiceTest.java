package org.egov.pgr.domain.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.egov.pgr.domain.model.AuthenticatedUser;
import org.egov.pgr.domain.model.Complainant;
import org.egov.pgr.domain.model.Complaint;
import org.egov.pgr.domain.model.ComplaintLocation;
import org.egov.pgr.domain.model.ComplaintSearchCriteria;
import org.egov.pgr.domain.model.ComplaintType;
import org.egov.pgr.domain.model.Coordinates;
import org.egov.pgr.domain.model.UserType;
import org.egov.pgr.persistence.queue.contract.RequestInfo;
import org.egov.pgr.persistence.queue.contract.ServiceRequest;
import org.egov.pgr.persistence.queue.contract.SevaRequest;
import org.egov.pgr.persistence.repository.ComplaintRepository;
import org.egov.pgr.persistence.repository.DepartmentRepository;
import org.egov.pgr.web.contract.Department;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ComplaintServiceTest {

    private static final String CRN = "crn";
    private static final String TENANT_ID = "tenantId";
    @Mock
    private ComplaintRepository complaintRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private SevaNumberGeneratorService sevaNumberGeneratorService;

    @InjectMocks
    private ComplaintService complaintService;

    @Test
    public void test_should_validate_complaint_on_save() {
        final Complaint complaint = mock(Complaint.class);
        when(complaint.getAuthenticatedUser()).thenReturn(getCitizen());
        final SevaRequest sevaRequest = getSevaRequest();

        complaintService.save(complaint, sevaRequest);

        verify(complaint, times(1)).validate();
    }

    @Test
    public void test_should_validate_complaint_on_update() {
        final Complaint complaint = mock(Complaint.class);
        when(complaint.getAuthenticatedUser()).thenReturn(getCitizen());
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
        final ServiceRequest serviceRequest = getServiceRequest();
        final SevaRequest sevaRequest = new SevaRequest(new RequestInfo(), serviceRequest);
        when(sevaNumberGeneratorService.generate()).thenReturn(CRN);

        complaintService.save(complaint, sevaRequest);

        verify(complaintRepository).save(sevaRequest);
    }

    @Test
    public void test_should_persist_seva_request_on_update() {
        final Complaint complaint = getComplaint();
        final ServiceRequest serviceRequest = getServiceRequest();
        final SevaRequest sevaRequest = new SevaRequest(new RequestInfo(), serviceRequest);

        complaintService.update(complaint, sevaRequest);

        verify(complaintRepository).update(sevaRequest);
    }

    @Test
    public void test_should_find_all_complaints_by_search_criteria() {
        final ComplaintSearchCriteria searchCriteria = ComplaintSearchCriteria.builder().build();
        final Complaint expectedComplaint = getComplaint();
        final Department expectedDepartment = getDepartment();
        when(complaintRepository.findAll(searchCriteria)).thenReturn(Collections.singletonList(expectedComplaint));
        when(departmentRepository.getAll()).thenReturn(Collections.singletonList(expectedDepartment));

        final List<Complaint> actualComplaints = complaintService.findAll(searchCriteria);

        assertEquals(1, actualComplaints.size());
        assertEquals(expectedComplaint, actualComplaints.get(0));
    }

    private Complaint getComplaint() {
        final Coordinates coordinates = new Coordinates(0d, 0d);
        final ComplaintLocation complaintLocation = new ComplaintLocation(coordinates, "id", null);
        final Complainant complainant = Complainant.builder()
                .userId("userId")
                .build();
        return Complaint.builder()
                .complainant(complainant)
                .authenticatedUser(getCitizen())
                .complaintLocation(complaintLocation)
                .tenantId(TENANT_ID)
                .description("description")
                .crn("crn")
                .department(2L)
                .complaintType(new ComplaintType(null, "complaintCode"))
                .build();
    }
    
    private Department getDepartment() {
        return Department.builder()
                .id(2L)
                .name("Accounts")
                .code("A")
                .build();
    }

    private AuthenticatedUser getCitizen() {
        return AuthenticatedUser.builder()
                .id(1)
                .type(UserType.CITIZEN)
                .build();
    }

    private SevaRequest getSevaRequest() {
        final ServiceRequest serviceRequest = ServiceRequest.builder().build();
        return new SevaRequest(new RequestInfo(), serviceRequest);
    }

    private ServiceRequest getServiceRequest() {
        return ServiceRequest.builder().build();
    }

}