package org.egov.pgr.employee.enrichment.service;

import org.egov.pgr.employee.enrichment.model.SevaRequest;
import org.egov.pgr.employee.enrichment.repository.PositionRepository;
import org.springframework.stereotype.Service;

@Service
public class DepartmentService {
    private PositionRepository positionRepository;

    public DepartmentService(PositionRepository positionRepository) {
        this.positionRepository = positionRepository;
    }

    public void enrichRequestWithDesignation(SevaRequest sevaRequest) {
        final String designationId = positionRepository
            .getDesignationIdForAssignee(sevaRequest.getTenantId(), sevaRequest.getAssignee());
        sevaRequest.setDesignation(designationId);
    }
}
