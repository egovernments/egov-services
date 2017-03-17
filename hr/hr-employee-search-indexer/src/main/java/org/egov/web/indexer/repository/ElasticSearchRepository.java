package org.egov.web.indexer.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class ElasticSearchRepository {

    private RestTemplate restTemplate;
    private final String indexServiceHost;

    public ElasticSearchRepository(RestTemplate restTemplate,
                                   @Value("${egov.services.esindexer.host}") String indexServiceHost) {
        this.restTemplate = restTemplate;
        this.indexServiceHost = indexServiceHost;
    }

    public void index(String indexName, String typeName, long i, Object indexObject) {
        String url = this.indexServiceHost + indexName + "/" + typeName + "/" + i;
        Map response = restTemplate.postForObject(url, indexObject, Map.class);
        System.out.println("response=" + response);
    }

}