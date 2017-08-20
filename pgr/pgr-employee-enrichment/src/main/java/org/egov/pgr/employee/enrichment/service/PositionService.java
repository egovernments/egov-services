package org.egov.pgr.employee.enrichment.service;

import org.egov.pgr.employee.enrichment.model.Position;
import org.egov.pgr.employee.enrichment.model.SevaRequest;
import org.egov.pgr.employee.enrichment.repository.PositionRepository;
import org.springframework.stereotype.Service;

@Service
public class PositionService {
    private PositionRepository positionRepository;

    public PositionService(PositionRepository positionRepository) {
        this.positionRepository = positionRepository;
    }

    public void enrichRequestWithPosition(SevaRequest sevaRequest) {
        final Position position = positionRepository
            .getDesignationIdForAssignee(sevaRequest.getTenantId(), sevaRequest.getCurrentPositionId());
        sevaRequest.update(position);
    }
}
