package org.egov.workflow.persistence.repository;

import org.egov.common.contract.request.RequestInfo;
import org.egov.workflow.web.contract.PositionResponse;
import org.egov.workflow.web.contract.PositionServiceResponse;
import org.egov.workflow.web.contract.RequestInfoWrapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PositionRepository {

    private final RestTemplate restTemplate;
    private final String positionsByIdUrl;

    public PositionRepository(final RestTemplate restTemplate,
                              @Value("${egov.services.hrmasters.host}") final String eisServiceHostname,
                              @Value("${egov.services.hr.position_by_id}") final String positionsByIdUrl) {
        this.restTemplate = restTemplate;
        this.positionsByIdUrl = eisServiceHostname + positionsByIdUrl;
    }

    public PositionResponse getById(final Long id, final String tenantId) {
        RequestInfoWrapper wrapper = RequestInfoWrapper.builder().RequestInfo(RequestInfo.builder().build()).build();
        final PositionServiceResponse positionServiceResponse = restTemplate.postForObject(positionsByIdUrl, wrapper,
            PositionServiceResponse.class, id, tenantId);
        return positionServiceResponse.getPositions().get(0);
    }

}
