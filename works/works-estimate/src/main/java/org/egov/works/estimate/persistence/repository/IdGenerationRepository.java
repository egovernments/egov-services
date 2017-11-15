package org.egov.works.estimate.persistence.repository;

import java.util.Arrays;
import java.util.HashMap;

import org.egov.tracer.model.CustomException;
import org.egov.works.commons.web.contract.IdRequest;
import org.egov.works.estimate.config.Constants;
import org.egov.works.estimate.config.PropertiesManager;
import org.egov.works.estimate.web.contract.IdGenerationRequest;
import org.egov.works.estimate.web.contract.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.jayway.jsonpath.JsonPath;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class IdGenerationRepository {

	@Autowired
	private RestTemplate restTemplate;

	private String url;

	@Autowired
	private PropertiesManager propertiesManager;

	public IdGenerationRepository(final RestTemplate restTemplate,
			@Value("${egov.idgen.hostname}") final String idGenHostName,
			@Value("${works.numbergeneration.uri}") final String url) {
		this.restTemplate = restTemplate;
		this.url = idGenHostName + url;
	}

	public String generateAbstractEstimateNumber(final String tenantId, final RequestInfo requestInfo) {
		Object response = null;
		HashMap<String, String> messages = new HashMap<>();
		IdGenerationRequest idGenerationRequest = new IdGenerationRequest();
		IdRequest idRequest = new IdRequest();
		idRequest.setTenantId(tenantId);
		idRequest.setFormat(propertiesManager.getWorksAbstractEstimateNumberFormat());
		idRequest.setIdName(propertiesManager.getWorksAbstractEstimateNumber());
		idGenerationRequest.setIdRequests(Arrays.asList(idRequest));
		idGenerationRequest.setRequestInfo(requestInfo);
		try {
			response = restTemplate.postForObject(url, idGenerationRequest, Object.class);
		} catch (Exception e) {
			messages.put(Constants.ABSTRACT_ESTIMATE_NUMBER_GENERATION_ERROR,
					Constants.ABSTRACT_ESTIMATE_NUMBER_GENERATION_ERROR);
			throw new CustomException(messages);

		}
		log.info("Response from id gen service: " + response.toString());

		return JsonPath.read(response, "$.idResponses[0].id");
	}

	public String generateDetailedEstimateNumber(final String tenantId, final RequestInfo requestInfo) {
		Object response = null;
		HashMap<String, String> messages = new HashMap<>();
		IdGenerationRequest idGenerationRequest = new IdGenerationRequest();
		IdRequest idRequest = new IdRequest();
		idRequest.setTenantId(tenantId);
		idRequest.setFormat(propertiesManager.getWorksDetailedEstimateNumberFormat());
		idRequest.setIdName(propertiesManager.getWorksDetailedEstimateNumber());
		idGenerationRequest.setIdRequests(Arrays.asList(idRequest));
		idGenerationRequest.setRequestInfo(requestInfo);
		try {
			response = restTemplate.postForObject(url, idGenerationRequest, Object.class);
		} catch (Exception e) {
			messages.put(Constants.DETAILED_ESTIMATE_NUMBER_GENERATION_ERROR,
					Constants.DETAILED_ESTIMATE_NUMBER_GENERATION_ERROR);
			throw new CustomException(messages);
		}
		log.info("Response from id gen service: " + response.toString());

		return JsonPath.read(response, "$.idResponses[0].id");
	}

	public String generateWorkIdentificationNumber(final String tenantId, final RequestInfo requestInfo) {
		Object response = null;
		HashMap<String, String> messages = new HashMap<>();
		IdGenerationRequest idGenerationRequest = new IdGenerationRequest();
		IdRequest idRequest = new IdRequest();
		idRequest.setTenantId(tenantId);
		idRequest.setFormat(propertiesManager.getWorksWorkIdentificationNumberFormat());
		idRequest.setIdName(propertiesManager.getWorksWorkIdentificationNumber());
		idGenerationRequest.setIdRequests(Arrays.asList(idRequest));
		idGenerationRequest.setRequestInfo(requestInfo);
		try {
			response = restTemplate.postForObject(url, idGenerationRequest, Object.class);
		} catch (Exception e) {
			messages.put(Constants.WORK_IDENTIFICATION_NUMBER_GENERATION_ERROR,
					Constants.WORK_IDENTIFICATION_NUMBER_GENERATION_ERROR);
			throw new CustomException(messages);
		}
		log.info("Response from id gen service: " + response.toString());

		return JsonPath.read(response, "$.idResponses[0].id");
	}
}
