package org.egov.pgr.employee.enrichment.service;

import org.egov.pgr.employee.enrichment.DateConverter;
import org.egov.pgr.employee.enrichment.factory.DateFactory;
import org.egov.pgr.employee.enrichment.model.SevaRequest;
import org.egov.pgr.employee.enrichment.repository.ComplaintTypeRepository;
import org.egov.pgr.employee.enrichment.repository.EscalationHoursRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.Date;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EscalationDateServiceTest {

    @InjectMocks
    private EscalationDateService escalationDateService;

    @Mock
    private EscalationHoursRepository escalationHoursRepository;

    @Mock
    private ComplaintTypeRepository complaintTypeRepository;

    @Mock
    private DateFactory dateFactory;

    @Mock
    private SevaRequest sevaRequest;

    @Test
    public void test_should_set_escalation_date_to_seva_request() {
        final int escalationHours = 3;
        final LocalDateTime currentDateTime = LocalDateTime.of(2016, 1, 2, 3, 4, 5);
        final LocalDateTime expectedDateTime = currentDateTime.plusHours(escalationHours);
        Date expectedDate = new DateConverter(expectedDateTime).toDate();
        Date now = new DateConverter(currentDateTime).toDate();
        final String designationId = "designationId";
        final String tenantId = "tenantId";
        when(sevaRequest.getTenantId()).thenReturn(tenantId);
        final long assigneeId = 2L;
        when(sevaRequest.getCurrentPositionId()).thenReturn(assigneeId);
        when(sevaRequest.getDesignation()).thenReturn(designationId);
        final String complaintTypeId = "complaintTypeId";
        final String complaintTypeCode = "complaintTypeCode";
        when(sevaRequest.getComplaintTypeCode()).thenReturn(complaintTypeCode);
        when(complaintTypeRepository.getComplaintTypeId(complaintTypeCode, tenantId)).thenReturn(complaintTypeId);
        when(escalationHoursRepository.getEscalationHours(tenantId, complaintTypeId, designationId))
            .thenReturn(escalationHours);
        when(dateFactory.now()).thenReturn(now);

        escalationDateService.enrichRequestWithEscalationDate(sevaRequest);

        verify(sevaRequest).setEscalationDate(expectedDate);
        verify(sevaRequest).setEscalationHours(String.valueOf(escalationHours));
    }

}