package org.pgr.batch.repository;

import org.egov.common.contract.request.RequestInfo;
import org.pgr.batch.repository.contract.PositionsResponse;
import org.pgr.batch.repository.contract.RequestInfoWrapper;
import org.pgr.batch.service.model.Position;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

@Service
public class PositionRepository {

    private final String url;
    private RestTemplate restTemplate;

    public PositionRepository(RestTemplate restTemplate,
                              @Value("${egov.services.position.host}") String host,
                              @Value("${egov.services.position.designation}") String endpointUrl) {
        this.url = host + endpointUrl;
        this.restTemplate = restTemplate;
    }

    public Position getDesignationIdForAssignee(String tenantId, long assigneeId) {
        RequestInfoWrapper wrapper = RequestInfoWrapper.builder().requestInfo(RequestInfo.builder().apiId("apiId").ver("ver").ts(new Date()).build()).build();
        final HttpEntity<RequestInfoWrapper> request = new HttpEntity<>(wrapper);
        PositionsResponse response = restTemplate
            .postForObject(this.url, request,PositionsResponse.class, tenantId, assigneeId);
        return response.toDomain();
    }

}


