package org.egov.pgr.employee.enrichment.service;

import org.apache.commons.lang3.time.DateUtils;
import org.egov.pgr.employee.enrichment.factory.DateFactory;
import org.egov.pgr.employee.enrichment.model.SevaRequest;
import org.egov.pgr.employee.enrichment.repository.ComplaintTypeRepository;
import org.egov.pgr.employee.enrichment.repository.EscalationHoursRepository;
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
        sevaRequest.setEscalationHours(String.valueOf(escalationHours));
        sevaRequest.setEscalationDate(escalationDate);
    }

    private int getEscalationHours(SevaRequest sevaRequest) {
        final String complaintTypeId = complaintTypeRepository
            .getComplaintTypeId(sevaRequest.getComplaintTypeCode(), sevaRequest.getTenantId());
        return escalationHoursRepository.getEscalationHours(sevaRequest.getTenantId(),
            complaintTypeId, sevaRequest.getDesignation());
    }
}

