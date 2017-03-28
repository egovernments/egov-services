package org.egov.workflow.domain.service;

import org.egov.workflow.domain.model.EscalationHoursSearchCriteria;
import org.egov.workflow.persistence.repository.EscalationRepository;
import org.springframework.stereotype.Service;

@Service
public class EscalationService {
    private EscalationRepository escalationRepository;

    public EscalationService(EscalationRepository escalationRepository) {
        this.escalationRepository = escalationRepository;
    }

    public int getEscalationHours(EscalationHoursSearchCriteria searchCriteria) {
        return escalationRepository.getEscalationHours(searchCriteria);
    }
}
