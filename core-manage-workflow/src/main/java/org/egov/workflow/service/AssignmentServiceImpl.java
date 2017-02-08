package org.egov.workflow.service;

import org.egov.workflow.model.PositionResponse;
import org.egov.workflow.model.PositionsResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AssignmentServiceImpl implements AssignmentService {

    @Value("${egov.services.employee_service.host}")
    private String EmployeeServiceHost;

    @Override
    public PositionResponse getPositionsForUser(final String userId) {
        final String url = EmployeeServiceHost + "v1/eis/employeepositions?userId={userId}";
        final PositionsResponse position = getPositionResponse(url, userId);
        return position != null ? position.getPositions().get(0) : null;
    }

    private PositionsResponse getPositionResponse(final String url, final String userId) {
        final RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(url, PositionsResponse.class, userId);
    }
}
