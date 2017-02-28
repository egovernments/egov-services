package org.egov.workflow.persistence.repository;

import java.util.List;

import org.egov.workflow.web.contract.PositionResponse;
import org.egov.workflow.web.contract.PositionServiceResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PositionRepository {

    private final RestTemplate restTemplate;
    private final String positionsByIdUrl;
    private final String positionsForEmployeeCodeUrl;

    public PositionRepository(final RestTemplate restTemplate,
            @Value("${egov.services.eis.host}") final String eisServiceHostname,
            @Value("${egov.services.eis.position_by_id}") final String positionsByIdUrl,
            @Value("${egov.services.eis.position_by_employee_code}") final String positionsForEmployeeCodeUrl) {
        this.restTemplate = restTemplate;
        this.positionsByIdUrl = eisServiceHostname + positionsByIdUrl;
        this.positionsForEmployeeCodeUrl = eisServiceHostname + positionsForEmployeeCodeUrl;

    }

    public PositionResponse getById(final Long id) {
        final PositionServiceResponse positionServiceResponse = restTemplate.getForObject(positionsByIdUrl,
                PositionServiceResponse.class, id);
        return positionServiceResponse.getPositions().get(0);
    }

    public List<PositionResponse> getByEmployeeCode(final String code) {
        final PositionServiceResponse positionServiceResponse = restTemplate.getForObject(positionsForEmployeeCodeUrl,
                PositionServiceResponse.class, code);
        return positionServiceResponse.getPositions();
    }

}
