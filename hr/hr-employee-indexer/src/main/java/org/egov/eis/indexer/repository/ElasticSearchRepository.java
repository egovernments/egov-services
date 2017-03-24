package org.egov.eis.indexer.repository;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class ElasticSearchRepository {

    private RestTemplate restTemplate;
    private final String indexServiceHost;

    public ElasticSearchRepository(RestTemplate restTemplate,
                                   @Value("${egov.services.esindexer.host}") String indexServiceHost) {
        this.restTemplate = restTemplate;
        this.indexServiceHost = indexServiceHost;
    }

	@SuppressWarnings("rawtypes")
	public void index(String indexName, String typeName, long i, Object indexObject) {
		String url = this.indexServiceHost + indexName + "/" + typeName + "/" + i;
		try {
			Map response = restTemplate.postForObject(url, indexObject, Map.class);
			System.out.println("response=" + response);
		} catch (RestClientException rce) {
			rce.getRootCause().printStackTrace();
			rce.printStackTrace();
		}
	}

}