package org.egov.web.indexer.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class ElasticSearchIndexerService {

    private RestTemplate restTemplate;
    private final String indexServiceHost;

    public ElasticSearchIndexerService(RestTemplate restTemplate,
                                       @Value("${egov.services.esindexer.host}") String indexServiceHost) {
        this.restTemplate = restTemplate;
        this.indexServiceHost = indexServiceHost;
    }

    public void index(String indexName, String indexId, Object indexObject) {
        String url = this.indexServiceHost + indexName + "/" + indexName + "/" + indexId;
        restTemplate.postForObject(url, indexObject, Map.class);
    }

}