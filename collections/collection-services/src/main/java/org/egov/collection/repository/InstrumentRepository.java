package org.egov.collection.repository;

import com.jayway.jsonpath.JsonPath;
import org.egov.collection.config.ApplicationProperties;
import org.egov.collection.model.Instrument;
import org.egov.collection.model.InstrumentRequest;
import org.egov.collection.model.RequestInfoWrapper;
import org.egov.collection.web.contract.InstrumentResponse;
import org.egov.common.contract.request.RequestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class InstrumentRepository {

	public static final Logger LOGGER = LoggerFactory
			.getLogger(BillingServiceRepository.class);
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private ApplicationProperties applicationProperties;

	public Instrument createInstrument(RequestInfo requestinfo,
			Instrument instrument) {
		
		if (!Objects.isNull(instrument.getBank()))
			instrument.getBank().setTenantId(instrument.getTenantId());

		StringBuilder builder = new StringBuilder();
		String hostname = applicationProperties.getInstrumentServiceHost();
		String baseUri = applicationProperties.getCreateInstrument();
		builder.append(hostname).append(baseUri);
		InstrumentRequest instrumentRequest = new InstrumentRequest();
		instrumentRequest.setRequestInfo(requestinfo);
		instrumentRequest.setInstruments(Collections.singletonList(instrument));

		LOGGER.info("Request to instrument create: "
				+ instrumentRequest.toString());
		LOGGER.info("URI Instrument create: " + builder.toString());
			List<Instrument> instruments = restTemplate.postForObject(builder.toString(),
                    instrumentRequest, InstrumentResponse.class).getInstruments();

		LOGGER.info("Response from instrument service: " + instruments);

		return !instruments.isEmpty() ? instruments.get(0) : null ;
	}

    Instrument searchInstruments(final String instrumentHeader, final String tenantId, final RequestInfo requestInfo) {
        StringBuilder builder = new StringBuilder();
        String hostname = applicationProperties.getInstrumentServiceHost();
        String baseUri = applicationProperties.getSearchInstrument();
        builder.append(hostname).append(baseUri);

        LOGGER.info("Request to instrument search: "
                + baseUri.toString());
        LOGGER.info("URI Instrument search: " + builder.toString());
        List<Instrument> instrumentList = restTemplate.postForObject(builder.toString(),
                requestInfo, InstrumentResponse.class, instrumentHeader,tenantId).getInstruments();
        LOGGER.info("Response from instrument service: " + instrumentList);
        return !instrumentList.isEmpty() ? instrumentList.get(0) : null;
    }

    public List<Instrument> searchInstrumentsByPaymentMode(final String paymentMode,final String tenantId,final RequestInfo requestInfo) {
        StringBuilder builder = new StringBuilder();
        String hostname = applicationProperties.getInstrumentServiceHost();
        String baseUri = applicationProperties.getSearchInstrumentByPaymentMode();
        builder.append(hostname).append(baseUri);

        LOGGER.info("Request to instrument search: "
                + baseUri.toString());
        LOGGER.info("URI Instrument search: " + builder.toString());
        List<Instrument> instrumentList = restTemplate.postForObject(builder.toString(),
                requestInfo, InstrumentResponse.class, paymentMode,tenantId).getInstruments();
        LOGGER.info("Response from instrument service: " + instrumentList);
        return instrumentList;

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
