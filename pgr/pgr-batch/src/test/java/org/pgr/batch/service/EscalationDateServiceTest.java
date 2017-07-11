package org.pgr.batch.service;


import org.egov.pgr.common.contract.AttributeEntry;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.pgr.batch.DateConverter;
import org.pgr.batch.factory.DateFactory;
import org.pgr.batch.repository.ComplaintTypeRepository;
import org.pgr.batch.repository.EscalationHoursRepository;
import org.pgr.batch.repository.contract.ServiceRequest;
import org.pgr.batch.service.model.SevaRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.assertEquals;
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


    @Test
    public void test_should_set_escalation_date_to_seva_request() {
        final String escalationHours = "3";
        final LocalDateTime currentDateTime = LocalDateTime.of(2016, 1, 2, 3, 4, 5);
        final LocalDateTime expectedDateTime = currentDateTime.plusHours(Long.parseLong(escalationHours));
        Date expectedDate = new DateConverter(expectedDateTime).toDate();
        Date now = new DateConverter(currentDateTime).toDate();
        final String designationId = "designationId";
        final String tenantId = "tenantId";
        SevaRequest sevaRequest = getSevaRequest();
        ServiceRequest request = sevaRequest.getServiceRequest();
        final String complaintTypeId = "complaintTypeId";
        final String complaintTypeCode = "complaintTypeCode";
        when(complaintTypeRepository.getComplaintTypeId(complaintTypeCode, tenantId)).thenReturn(complaintTypeId);
        when(escalationHoursRepository.getEscalationHours(tenantId, complaintTypeId, designationId))
                .thenReturn(Integer.valueOf(escalationHours));
        when(dateFactory.now()).thenReturn(now);

        escalationDateService.enrichRequestWithEscalationDate(sevaRequest);

        assertEquals(sevaRequest.getServiceRequest().getEscalationDate(),expectedDate);
        assertEquals(sevaRequest.getServiceRequest().getEscalationHours(),escalationHours);

    }

    private SevaRequest getSevaRequest(){
        final ServiceRequest serviceRequest = ServiceRequest.builder()
                .tenantId("tenantId")
                .complaintTypeCode("complaintTypeCode")
                .attribValues(new ArrayList<AttributeEntry>())
                .build();

        serviceRequest.setDesignation("designationId");
        serviceRequest.setPositionId("2");

        return new SevaRequest(null,serviceRequest);
    }
}