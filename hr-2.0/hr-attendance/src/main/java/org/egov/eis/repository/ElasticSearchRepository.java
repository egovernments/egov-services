package org.egov.eis.repository;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ElasticSearchRepository {

    private final RestTemplate restTemplate;
    private final String indexServiceHost;

    public ElasticSearchRepository(final RestTemplate restTemplate,
            @Value("${egov.services.esindexer.host}") final String indexServiceHost) {
        this.restTemplate = restTemplate;
        this.indexServiceHost = indexServiceHost;
    }

    public void index(final String indexName, final String indexId, final Object indexObject) {
        final String url = indexServiceHost + indexName + "/" + indexName + "/" + indexId;
        restTemplate.postForObject(url, indexObject, Map.class);
    }

}