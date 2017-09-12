package org.egov.tradelicense.domain.service;

import java.util.ArrayList;
import java.util.List;

import org.egov.tl.commons.web.contract.RequestInfo;
import org.egov.tradelicense.common.config.PropertiesManager;
import org.egov.tradelicense.common.domain.exception.IdGenerationException;
import org.egov.tradelicense.web.contract.AckIdRequest;
import org.egov.tradelicense.web.contract.AckNoGenerationRequest;
import org.egov.tradelicense.web.contract.AckNoGenerationResponse;
import org.egov.tradelicense.web.contract.IdGenErrorRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Service to generate common application numbers
 * 
 * @author Manoj Kulkarni
 *
 */
@Service
public class IdGenService {

	@Autowired
	private PropertiesManager propertiesManager;

	@Autowired
	private RestTemplate restTemplate;

	public String generate(final String tenantId, final String nameServiceTopic, final String formatServiceTopic,
			RequestInfo requestInfo) {
		StringBuilder url = new StringBuilder();
		String ackNumber = null;
		url.append(propertiesManager.getIdGenServiceBasePathTopic())
				.append(propertiesManager.getIdGenServiceCreatePathTopic());
		List<AckIdRequest> idRequests = new ArrayList<>();
		AckIdRequest idrequest = new AckIdRequest();

		idrequest.setIdName(nameServiceTopic);
		idrequest.setTenantId(tenantId);
		idrequest.setFormat(formatServiceTopic);
		AckNoGenerationRequest idGeneration = new AckNoGenerationRequest();
		idRequests.add(idrequest);
		idGeneration.setIdRequests(idRequests);
		idGeneration.setRequestInfo(requestInfo);
		String response = null;
		try {
			response = restTemplate.postForObject(url.toString(), idGeneration, String.class);
		} catch (Exception ex) {
			throw new IdGenerationException("Error While generating " + nameServiceTopic + " number",
					"Error While generating " + nameServiceTopic + " number", requestInfo);
		}
		Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
		IdGenErrorRes errorResponse = gson.fromJson(response, IdGenErrorRes.class);
		AckNoGenerationResponse idResponse = gson.fromJson(response, AckNoGenerationResponse.class);
		if (!errorResponse.getErrors().isEmpty()) {
			throw new IdGenerationException("Error While generating " + nameServiceTopic + " number",
					"Error While generating " + nameServiceTopic + " number", requestInfo);
		} else if (idResponse.getResponseInfo() != null) {
			if (idResponse.getResponseInfo().getStatus().toString().equalsIgnoreCase("SUCCESSFUL")) {
				if (idResponse.getIdResponses() != null && idResponse.getIdResponses().size() > 0)
					ackNumber = idResponse.getIdResponses().get(0).getId();
			}
		}

		return ackNumber;
	}
}
