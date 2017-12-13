package org.egov.works.workorder.domain.repository.builder;

import java.util.Arrays;
import java.util.HashMap;

import org.egov.tracer.model.CustomException;
import org.egov.works.commons.web.contract.IdRequest;
import org.egov.works.workorder.config.Constants;
import org.egov.works.workorder.config.PropertiesManager;
import org.egov.works.workorder.web.contract.IdGenerationRequest;
import org.egov.works.workorder.web.contract.RequestInfo;
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

	public String generateLOANumber(final String tenantId, final RequestInfo requestInfo) {
		Object response = null;
		HashMap<String, String> messages = new HashMap<>();
		IdGenerationRequest idGenerationRequest = new IdGenerationRequest();
		IdRequest idRequest = new IdRequest();
		idRequest.setTenantId(tenantId);
		idRequest.setFormat(propertiesManager.getLoaNumberFormat());
		idRequest.setIdName(propertiesManager.getWorksLOANumber());
		idGenerationRequest.setIdRequests(Arrays.asList(idRequest));
		idGenerationRequest.setRequestInfo(requestInfo);
		try {
			response = restTemplate.postForObject(url, idGenerationRequest, Object.class);
		} catch (Exception e) {
			messages.put(Constants.LOA_NUMBER_GENERATION_ERROR,
					Constants.LOA_NUMBER_GENERATION_ERROR);
			throw new CustomException(messages);

		}
		log.info("Response from id gen service: " + response.toString());

		return JsonPath.read(response, "$.idResponses[0].id");
	}

	public String generateWorkOrderNumber(final String tenantId, final RequestInfo requestInfo) {
		Object response = null;
		HashMap<String, String> messages = new HashMap<>();
		IdGenerationRequest idGenerationRequest = new IdGenerationRequest();
		IdRequest idRequest = new IdRequest();
		idRequest.setTenantId(tenantId);
		idRequest.setFormat(propertiesManager.getWorkOrderNumberFormat());
		idRequest.setIdName(propertiesManager.getWorksWorkOrderNumber());
		idGenerationRequest.setIdRequests(Arrays.asList(idRequest));
		idGenerationRequest.setRequestInfo(requestInfo);
		try {
			response = restTemplate.postForObject(url, idGenerationRequest, Object.class);
		} catch (Exception e) {
			messages.put(Constants.WORKORDER_NUMBER_GENERATION_ERROR,
					Constants.WORKORDER_NUMBER_GENERATION_ERROR);
			throw new CustomException(messages);

		}
		log.info("Response from id gen service: " + response.toString());

		return JsonPath.read(response, "$.idResponses[0].id");
	}

}
