package org.egov.pgr.employee.enrichment.service;

import org.apache.commons.lang.time.DateUtils;
import org.egov.pgr.employee.enrichment.factory.DateFactory;
import org.egov.pgr.employee.enrichment.model.SevaRequest;
import org.egov.pgr.employee.enrichment.repository.ComplaintTypeRepository;
import org.egov.pgr.employee.enrichment.repository.EscalationHoursRepository;
import org.egov.pgr.employee.enrichment.repository.PositionRepository;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class EscalationDateService {
    private PositionRepository positionRepository;
    private ComplaintTypeRepository complaintTypeRepository;
    private EscalationHoursRepository escalationHoursRepository;
    private DateFactory dateFactory;

    public EscalationDateService(PositionRepository positionRepository,
                                 ComplaintTypeRepository complaintTypeRepository,
                                 EscalationHoursRepository escalationHoursRepository,
                                 DateFactory dateFactory) {
        this.positionRepository = positionRepository;
        this.complaintTypeRepository = complaintTypeRepository;
        this.escalationHoursRepository = escalationHoursRepository;
        this.dateFactory = dateFactory;
    }

    public void enrichRequest(SevaRequest sevaRequest) {
        final int escalationHours = getEscalationHours(sevaRequest);
        final Date escalationDate = DateUtils.addHours(dateFactory.now(), escalationHours);
        sevaRequest.setEscalationDate(escalationDate);
    }

    private int getEscalationHours(SevaRequest sevaRequest) {
        final String complaintTypeId = complaintTypeRepository
            .getComplaintTypeId(sevaRequest.getComplaintTypeCode(), sevaRequest.getTenantId());
        final String designationId = positionRepository
            .getDesignationIdForAssignee(sevaRequest.getTenantId(), sevaRequest.getAssignee());
        return escalationHoursRepository.getEscalationHours(sevaRequest.getTenantId(),
            complaintTypeId, designationId);
    }
}
