package org.egov.pgrrest.read.persistence.repository;

import org.egov.pgrrest.read.domain.model.RouterResponse;
import org.egov.pgrrest.read.web.contract.RequestInfoBody;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RouterRestRepository {
    private final RestTemplate restTemplate;
    private final String url;

    public RouterRestRepository(final RestTemplate restTemplate,
                                @Value("${egov.workflow.host}") final String workFlowHost,
                                @Value("${egov.workflow.url}") final String workFlowUrl) {
        this.restTemplate = restTemplate;
        this.url = workFlowHost + workFlowUrl;
    }

    public RouterResponse getRouter(String tenantId, String hierarchyType) {
        RequestInfoBody requestBody = buildRequestInfo();
        return restTemplate.postForObject(url, requestBody, RouterResponse.class, tenantId, hierarchyType);
    }

    private RequestInfoBody buildRequestInfo() {
        return new RequestInfoBody(null);
    }
}
