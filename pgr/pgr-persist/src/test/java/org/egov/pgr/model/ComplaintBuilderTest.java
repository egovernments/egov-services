package org.egov.pgr.model;

import org.apache.commons.lang3.StringUtils;
import org.egov.pgr.contracts.grievance.ServiceRequest;
import org.egov.pgr.entity.Complaint;
import org.egov.pgr.entity.ComplaintType;
import org.egov.pgr.entity.enums.ComplaintStatus;
import org.egov.pgr.repository.PositionRepository;
import org.egov.pgr.service.ComplaintStatusService;
import org.egov.pgr.service.ComplaintTypeService;
import org.egov.pgr.service.EscalationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class ComplaintBuilderTest {

    @Mock
    private ComplaintTypeService complaintTypeService;
    @Mock
    private ComplaintStatusService complaintStatusService;
    @Mock
    private EscalationService escalationService;
    @Mock
    private PositionRepository positionRepository;
    private ServiceRequest serviceRequest;

    @Before
    public void setUp() throws Exception {
        this.serviceRequest = new ServiceRequest();
    }

    @Test
    public void testNewComplaintShouldBeCreatedWithPassedInServiceRequest() throws Exception {
        setupMocks();
        String withdrawn = "WITHDRAWN";
        org.egov.pgr.entity.ComplaintStatus withdrawnStatus = mock(org.egov.pgr.entity.ComplaintStatus.class);
        withdrawnStatus.setName(withdrawn);
        when(complaintStatusService.getByName(withdrawn)).thenReturn(withdrawnStatus);
        Complaint complaint = new Complaint();
        org.egov.pgr.entity.ComplaintStatus status = new org.egov.pgr.entity.ComplaintStatus();
        status.setName(ComplaintStatus.PROCESSING.toString());
        complaint.setStatus(status);
        ComplaintBuilder complaintBuilder = complaintBuilderWithComplaint(complaint);
        Map<String, String> valuesMap = new HashMap<>();
        valuesMap.put("status", withdrawn);
        this.serviceRequest.setValues(valuesMap);
        Complaint builtComplaint = complaintBuilder.build();

        assertEquals(withdrawnStatus, builtComplaint.getStatus());
    }

    @Test
    public void testComplaintIsUpdatedWithValidStatus() throws Exception {
        String withdrawn = "WITHDRAWN";
        org.egov.pgr.entity.ComplaintStatus withdrawnStatus = new org.egov.pgr.entity.ComplaintStatus();
        withdrawnStatus.setName(withdrawn);
        when(complaintStatusService.getByName(withdrawn)).thenReturn(withdrawnStatus);
        Complaint complaint = new Complaint();
        org.egov.pgr.entity.ComplaintStatus status = new org.egov.pgr.entity.ComplaintStatus();
        status.setName(ComplaintStatus.PROCESSING.toString());
        complaint.setStatus(status);
        ComplaintBuilder complaintBuilder = complaintBuilderWithComplaint(complaint);
        Map<String, String> valuesMap = new HashMap<>();
        valuesMap.put("status", withdrawn);
        this.serviceRequest.setValues(valuesMap);
        Complaint builtComplaint = complaintBuilder.build();

        assertEquals(withdrawnStatus, builtComplaint.getStatus());
    }

    @Test
    public void testThatInvalidStatusIsIgnoredWhileUpdatingComplaint() throws Exception {
        String invalidStatus = "INVALID_STATUS";
        Complaint complaint = new Complaint();
        org.egov.pgr.entity.ComplaintStatus originalStatus = new org.egov.pgr.entity.ComplaintStatus();
        originalStatus.setName(ComplaintStatus.PROCESSING.toString());
        complaint.setStatus(originalStatus);
        ComplaintBuilder complaintBuilder = complaintBuilderWithComplaint(complaint);
        Map<String, String> valuesMap = new HashMap<>();
        valuesMap.put("status", invalidStatus);
        this.serviceRequest.setValues(valuesMap);
        Complaint builtComplaint = complaintBuilder.build();

        verify(complaintStatusService, never()).getByName(invalidStatus);
        assertEquals(originalStatus, builtComplaint.getStatus());
    }

    @Test
    public void testThatStatusIsUpdatedForNewComplaint() throws Exception {
        String processing = "PROCESSING";
        org.egov.pgr.entity.ComplaintStatus processingStatus = new org.egov.pgr.entity.ComplaintStatus();
        processingStatus.setName(processing);
        when(complaintStatusService.getByName(processing)).thenReturn(processingStatus);
        ComplaintBuilder complaintBuilder = complaintBuilderWithComplaint(null);
        Map<String, String> valuesMap = new HashMap<>();
        valuesMap.put("status", processing);
        this.serviceRequest.setValues(valuesMap);
        Complaint builtComplaint = complaintBuilder.build();

        assertEquals(processingStatus, builtComplaint.getStatus());
    }

    private void setupMocks() {
        setupStatusMock();
        setupComplaintTypeMock();
        setupPositionMock();
        setupEscalationDateMock();
    }

    private void setupEscalationDateMock() {
        long designationId = 12L;
        Calendar c = Calendar.getInstance();
        c.set(2017, 02, 28, 0, 0);
        when(escalationService.getExpiryDate(any(Complaint.class), eq(designationId))).thenReturn(c.getTime());
    }

    private void setupPositionMock() {
        long assigneeId = 123L;
        long designationId = 12L;
        when(positionRepository.designationIdForAssignee(StringUtils.EMPTY, assigneeId)).thenReturn(designationId);
    }

    private void setupComplaintTypeMock() {
        ComplaintType complaintType = new ComplaintType();
        complaintType.setName("magical powers");
        when(complaintTypeService.findByCode("MAGIC")).thenReturn(complaintType);
    }

    private void setupStatusMock() {
        String processing = "PROCESSING";
        org.egov.pgr.entity.ComplaintStatus processingStatus = new org.egov.pgr.entity.ComplaintStatus();
        processingStatus.setName(processing);
        when(complaintStatusService.getByName(processing)).thenReturn(processingStatus);
    }

    private ComplaintBuilder complaintBuilderWithComplaint(Complaint complaint) {
        return new ComplaintBuilder(complaint, serviceRequest, complaintTypeService, complaintStatusService,
                escalationService, positionRepository);
    }
}