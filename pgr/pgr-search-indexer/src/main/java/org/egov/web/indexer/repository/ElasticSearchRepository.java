package org.egov.web.indexer.repository;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.Map;

@Service
public class ElasticSearchRepository {

    private RestTemplate restTemplate;
    private final String indexServiceHost;
    private String indexServiceUsername;
    private String indexServicePassword;

    public ElasticSearchRepository(RestTemplate restTemplate,
                                   @Value("${egov.services.esindexer.host}") String indexServiceHost,
                                   @Value("${egov.services.esindexer.username}") String indexServiceUsername,
                                   @Value("${egov.services.esindexer.password}") String indexServicePassword) {
        this.restTemplate = restTemplate;
        this.indexServiceHost = indexServiceHost;
        this.indexServiceUsername = indexServiceUsername;
        this.indexServicePassword = indexServicePassword;
    }

    public void index(String indexName, String indexId, Object indexObject) {
        String url = this.indexServiceHost + indexName + "/" + indexName + "/" + indexId;
        HttpHeaders headers = new HttpHeaders() {{
            set("Authorization", getAuthHeaderValue(indexServiceUsername, indexServicePassword));
        }};

        restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(indexObject, headers), Map.class);
    }

    private String getAuthHeaderValue(String userName, String password) {
        String authString = String.format("%s:%s", userName, password);
        byte[] encodedAuthString = Base64.encodeBase64(authString.getBytes(Charset.forName("US-ASCII")));
        return String.format("Basic %s", new String(encodedAuthString));
    }
}