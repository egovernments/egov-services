package org.pgr.batch.service;

import org.apache.commons.lang.time.DateUtils;
import org.pgr.batch.factory.DateFactory;
import org.pgr.batch.repository.ComplaintTypeRepository;
import org.pgr.batch.repository.EscalationHoursRepository;
import org.pgr.batch.repository.contract.ServiceRequest;
import org.pgr.batch.service.model.SevaRequest;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class EscalationDateService {
    private ComplaintTypeRepository complaintTypeRepository;
    private EscalationHoursRepository escalationHoursRepository;
    private DateFactory dateFactory;

    public EscalationDateService(ComplaintTypeRepository complaintTypeRepository,
                                 EscalationHoursRepository escalationHoursRepository,
                                 DateFactory dateFactory) {
        this.complaintTypeRepository = complaintTypeRepository;
        this.escalationHoursRepository = escalationHoursRepository;
        this.dateFactory = dateFactory;
    }

    public void enrichRequestWithEscalationDate(SevaRequest sevaRequest) {
        final int escalationHours = getEscalationHours(sevaRequest);
        final Date escalationDate = DateUtils.addHours(dateFactory.now(), escalationHours);
        sevaRequest.getServiceRequest().setEscalationHours(String.valueOf(escalationHours));
        sevaRequest.getServiceRequest().setEscalationDate(escalationDate);
        sevaRequest.getServiceRequest().setLastModifiedDate(new Date());
    }

    private int getEscalationHours(SevaRequest sevaRequest) {
        final ServiceRequest request = sevaRequest.getServiceRequest();
        final String complaintTypeId = complaintTypeRepository
            .getComplaintTypeId(request.getComplaintTypeCode(), request.getTenantId());
        return escalationHoursRepository.getEscalationHours(request.getTenantId(),
            complaintTypeId, request.getDesignation());
    }
}