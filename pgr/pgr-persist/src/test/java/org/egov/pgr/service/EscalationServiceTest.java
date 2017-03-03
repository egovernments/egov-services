package org.egov.pgr.service;

import org.egov.pgr.entity.Complaint;
import org.egov.pgr.entity.ComplaintType;
import org.egov.pgr.entity.Escalation;
import org.egov.pgr.repository.EscalationRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EscalationServiceTest {


    @Mock
    private EscalationRepository escalationRepository;
    private Complaint complaint;
    private Integer defaultEscalationHours = 48;
    private EscalationService escalationService;
    private long complaintTypeId;
    private long designationId;

    @Before
    public void setUp() throws Exception {
        this.escalationService = new EscalationService(escalationRepository, defaultEscalationHours);
        this.complaint = new Complaint();
        designationId = 12L;
        complaintTypeId = 21L;
        ComplaintType complaintType = new ComplaintType();
        complaintType.setId(complaintTypeId);
        this.complaint.setComplaintType(complaintType);
    }

    @Test
    public void testShouldAddDefaultEscalationHours() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2017, 2, 22);
        Date inputDate = calendar.getTime();
        calendar.set(2017, 2, 24);
        Date expectedDate = calendar.getTime();

        this.complaint.setEscalationDate(inputDate);
        Date expiryDate = this.escalationService.getExpiryDate(complaint, designationId);

        verify(escalationRepository).findByDesignationAndComplaintType(designationId, complaintTypeId);
        assertEquals(expectedDate, expiryDate);
    }

    @Test
    public void testShouldAddEscalationTimeByDesignationAndComplaintType() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2017, 2, 22);
        Date inputDate = calendar.getTime();
        calendar.set(2017, 2, 23);
        Date expectedDate = calendar.getTime();
        Escalation escalation = new Escalation();
        escalation.setNoOfHrs(24);
        when(escalationRepository.findByDesignationAndComplaintType(designationId, complaintTypeId)).thenReturn(escalation);

        this.complaint.setEscalationDate(inputDate);
        Date expiryDate = this.escalationService.getExpiryDate(complaint, designationId);

        assertEquals(expectedDate, expiryDate);
    }
}