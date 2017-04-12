package org.egov.eis.indexer.repository;

import java.util.Map;

import org.egov.eis.indexer.model.es.EmployeeIndex;
import org.egov.eis.indexer.model.es.EmployeeIndexGetWrapper;
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
	public void index(String indexName, String typeName, long id, Object indexObject) {
		String url = this.indexServiceHost + "/" + indexName + "/" + typeName + "/" + id;
		System.err.println("Create & Update ES URL : " + url);
		try {
			Map response = restTemplate.postForObject(url, indexObject, Map.class);
			System.out.println("response=" + response);
		} catch (RestClientException rce) {
			rce.getRootCause().printStackTrace();
			rce.printStackTrace();
		}
	}

	public EmployeeIndex getIndex(String indexName, String typeName, long id) {
		String url = this.indexServiceHost + "/" + indexName + "/" + typeName + "/" + id;
		System.err.println("Search ES URL : " + url);
		try {
			EmployeeIndexGetWrapper employeeIndexGetWrapper = restTemplate.getForObject(url, EmployeeIndexGetWrapper.class);
			System.out.println("response=" + employeeIndexGetWrapper);
			return employeeIndexGetWrapper.get_source(); // returns employeeIndex
		} catch (RestClientException rce) {
			rce.getRootCause().printStackTrace();
			rce.printStackTrace();
			return null;
		}
	}
}