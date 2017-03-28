package org.egov.workflow.persistence.repository;

import org.egov.workflow.domain.model.EscalationHoursSearchCriteria;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EscalationRepository {

    private int defaultEscalationHours;
    private EscalationJpaRepository escalationJpaRepository;

    public EscalationRepository(@Value("${defaults.escalationHours}") int defaultEscalationHours,
                                EscalationJpaRepository escalationJpaRepository) {
        this.defaultEscalationHours = defaultEscalationHours;
        this.escalationJpaRepository = escalationJpaRepository;
    }

    public int getEscalationHours(EscalationHoursSearchCriteria searchCriteria) {
        final Integer configuredEscalationHours = escalationJpaRepository.
            findBy(searchCriteria.getDesignationId(), searchCriteria.getComplaintTypeId(),
                searchCriteria.getTenantId());
        return Optional.ofNullable(configuredEscalationHours)
            .orElse(defaultEscalationHours);
    }
}
