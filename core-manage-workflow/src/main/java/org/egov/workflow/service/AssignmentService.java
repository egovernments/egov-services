package org.egov.workflow.service;

import org.egov.workflow.model.PositionResponse;

public interface AssignmentService {
    
    PositionResponse getPositionsForUser(Long userId);

}
