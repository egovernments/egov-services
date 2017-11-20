package org.egov.web.indexer.repository;

import org.apache.tomcat.util.codec.binary.Base64;
import org.egov.web.indexer.repository.contract.ServiceRequestDocument;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Map;

@Service
public class ElasticSearchRepository {

    private static final String AUTHORIZATION = "Authorization";
    private static final String US_ASCII = "US-ASCII";
    private static final String BASIC_AUTH = "Basic %s";
    private final RestTemplate restTemplate;
    private final String indexServiceHost;
    private final String userName;
    private final String password;
    private final String indexName;
    private final String documentType;

    public ElasticSearchRepository(RestTemplate restTemplate,
                                   @Value("${egov.services.esindexer.host}") String indexServiceHost,
                                   @Value("${egov.services.esindexer.username}") String userName,
                                   @Value("${egov.services.esindexer.password}") String password,
                                   @Value("${es.index.name}") String indexName,
                                   @Value("${es.document.type}") String documentType) {
        this.restTemplate = restTemplate;
        this.indexServiceHost = indexServiceHost;
        this.userName = userName;
        this.password = password;
        this.indexName = indexName;
        this.documentType = documentType;
    }

    public void index(ServiceRequestDocument document) throws UnsupportedEncodingException {
        String url = String.format("%s%s/%s/%s", this.indexServiceHost, indexName, documentType, URLEncoder.encode(document.getId(), "UTF-8"));
        HttpHeaders headers = getHttpHeaders();
        restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(document, headers), Map.class);
    }

    private HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTHORIZATION, getBase64Value(userName, password));
        return headers;
    }

    private String getBase64Value(String userName, String password) {
        String authString = String.format("%s:%s", userName, password);
        byte[] encodedAuthString = Base64.encodeBase64(authString.getBytes(Charset.forName(US_ASCII)));
        return String.format(BASIC_AUTH, new String(encodedAuthString));
    }
}