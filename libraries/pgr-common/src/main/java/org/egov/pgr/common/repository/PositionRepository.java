package org.egov.pgr.common.repository;

import org.egov.common.contract.request.RequestInfo;
import org.egov.pgr.common.model.Position;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PositionRepository {
    private final RestTemplate restTemplate;
    private final String url;
    private final static String POSITION_BY_ID = "hr-masters/positions/_search?id={id}&tenantId={tenantId}";

    public PositionRepository(RestTemplate restTemplate,
                              @Value("${hrmaster.host}") final String hrMasterServiceHostname) {
        this.restTemplate = restTemplate;
        this.url = hrMasterServiceHostname + POSITION_BY_ID;
    }

    public Position getPositionById(Long id, String tenantId) {
        final PositionResponse positionResponse = this.restTemplate
            .postForObject(this.url, buildRequestInfo(), PositionResponse.class, id, tenantId);
        return positionResponse != null ? positionResponse.toDomain() : null;
    }

    private RequestInfoWrapper buildRequestInfo() {
        final RequestInfo requestInfo = RequestInfo.builder().build();
        return RequestInfoWrapper.builder()
            .RequestInfo(requestInfo)
            .build();
    }
}

