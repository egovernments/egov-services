package org.pgr.batch.service;

import org.pgr.batch.repository.PositionRepository;
import org.pgr.batch.repository.contract.ServiceRequest;
import org.pgr.batch.service.model.Position;
import org.springframework.stereotype.Service;

@Service
public class PositionService {
    private PositionRepository positionRepository;

    public PositionService(PositionRepository positionRepository) {
        this.positionRepository = positionRepository;
    }

    public void enrichRequestWithPosition(ServiceRequest serviceRequest) {
        final Position position = positionRepository
            .getDesignationIdForAssignee(serviceRequest.getTenantId(), Long.valueOf(serviceRequest.getPositionId()));
        serviceRequest.update(position);
    }
}
