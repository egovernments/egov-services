package org.egov.collection.repository;

import java.util.Arrays;

import org.egov.collection.config.ApplicationProperties;
import org.egov.collection.model.Instrument;
import org.egov.collection.model.InstrumentRequest;
import org.egov.common.contract.request.RequestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.jayway.jsonpath.JsonPath;

@Service
public class InstrumentRepository {

	public static final Logger LOGGER = LoggerFactory
			.getLogger(BillingServiceRepository.class);
	@Autowired
	public RestTemplate restTemplate;
	@Autowired
	private ApplicationProperties applicationProperties;

	public String createInstrument(RequestInfo requestinfo,
			Instrument instrument) {
		String instrumentId = null;
		StringBuilder builder = new StringBuilder();
		String hostname = applicationProperties.getInstrumentServiceHost();
		String baseUri = applicationProperties.getCreateInstrument();
		builder.append(hostname).append(baseUri);
		InstrumentRequest instrumentRequest = new InstrumentRequest();
		instrumentRequest.setRequestInfo(requestinfo);
		instrumentRequest.setInstruments(Arrays.asList(instrument));
		Object response = null;

		LOGGER.info("Request to instrument create: "
				+ instrumentRequest.toString());
		LOGGER.info("URI Instrument create: " + builder.toString());
			response = restTemplate.postForObject(builder.toString(),
					instrumentRequest, Object.class);
			instrumentId = JsonPath.read(response, "$.instruments[0].id");
		LOGGER.info("Response from instrument service: " + response.toString());

		return instrumentId;
	}

}
