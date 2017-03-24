package org.egov.workflow.web.interceptor;

import org.egov.workflow.domain.model.RequestContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

public class CorrelationIdAwareRestTemplate extends RestTemplate {

    public CorrelationIdAwareRestTemplate() {
        this.setInterceptors(customInterceptors());
    }

    private List<ClientHttpRequestInterceptor> customInterceptors() {
        return Collections.singletonList(correlationIdInterceptor());
    }

    private ClientHttpRequestInterceptor correlationIdInterceptor() {
        return (httpRequest, bytes, clientHttpRequestExecution) -> {
            final HttpHeaders headers = httpRequest.getHeaders();
            headers.put(RequestContext.CORRELATION_ID, Collections.singletonList(RequestContext.getId()));

            return clientHttpRequestExecution.execute(httpRequest, bytes);
        };
    }
}
