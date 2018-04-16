package org.egov.eis.indexer.repository;

import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.egov.eis.indexer.model.EmployeeIndex;
import org.egov.eis.indexer.model.EmployeeIndexGetWrapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.springframework.util.ObjectUtils.isEmpty;

@Slf4j
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
		log.info("Create & Update ES URL :: " + url);
		try {
			Map response = restTemplate.postForObject(url, indexObject, Map.class);
			log.info("ES Create Response :: " + response);
		} catch (RestClientException rce) {
			rce.getRootCause().printStackTrace();
			rce.printStackTrace();
		}
	}

	public EmployeeIndex getIndex(String indexName, String typeName, long id) {
		String url = this.indexServiceHost + "/" + indexName + "/" + typeName + "/" + id;
		log.info("Search ES URL :: " + url);
		try {
			EmployeeIndexGetWrapper employeeIndexGetWrapper = restTemplate.getForObject(url, EmployeeIndexGetWrapper.class);
			log.info("ES Get Response :: " + employeeIndexGetWrapper);
			if (isEmpty(employeeIndexGetWrapper))
				return null;
			return employeeIndexGetWrapper.get_source(); // returns employeeIndex
		} catch (RestClientException rce) {
			rce.getRootCause().printStackTrace();
			rce.printStackTrace();
			return null;
		}
	}
}