package org.egov.collection.repository;

import java.util.Arrays;

import org.egov.collection.config.ApplicationProperties;
import org.egov.collection.model.Instrument;
import org.egov.collection.model.InstrumentRequest;
import org.egov.collection.model.RequestInfoWrapper;
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
	
	public String getAccountCodeId(RequestInfo requestinfo,
			Instrument instrument, String tenantId){
		String glcode = null;
		StringBuilder builder = new StringBuilder();
		String hostname = applicationProperties.getInstrumentServiceHost();
		String baseUri = applicationProperties.getSearchAccountCodes();
		String searchCriteria = "?tenantId="+tenantId+"&name="+instrument.getInstrumentType().getName();
		builder.append(hostname).append(baseUri).append(searchCriteria);
		
		Object response = null;
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestinfo);

		LOGGER.info("URI account code id search: " + builder.toString());
		LOGGER.info("Request account code id search: " + requestinfo);
		
			response = restTemplate.postForObject(builder.toString(),
					requestInfoWrapper, Object.class);
			
			glcode = JsonPath.read(response, "$.instrumentAccountCodes[0].accountCode.glcode");
		LOGGER.info("Response from instrument service: " + response.toString());
		
		
		return glcode;
	}

}
