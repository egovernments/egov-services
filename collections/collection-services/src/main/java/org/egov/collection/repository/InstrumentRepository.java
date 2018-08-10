package org.egov.collection.repository;

import com.jayway.jsonpath.JsonPath;
import lombok.extern.slf4j.Slf4j;
import org.egov.collection.config.ApplicationProperties;
import org.egov.collection.model.Instrument;
import org.egov.collection.model.InstrumentRequest;
import org.egov.collection.web.contract.InstrumentResponse;
import org.egov.collection.web.contract.factory.RequestInfoWrapper;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.egov.tracer.model.ServiceCallException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Repository
@Slf4j
public class InstrumentRepository {

    public static final Logger LOGGER = LoggerFactory.getLogger(BillingServiceRepository.class);

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ApplicationProperties applicationProperties;

    public Instrument createInstrument(RequestInfo requestinfo, Instrument instrument) {
		
		if (!Objects.isNull(instrument.getBank()))
			instrument.getBank().setTenantId(instrument.getTenantId());

		StringBuilder builder = new StringBuilder();
		String hostname = applicationProperties.getInstrumentServiceHost();
		String baseUri = applicationProperties.getCreateInstrument();
		builder.append(hostname).append(baseUri);
		InstrumentRequest instrumentRequest = new InstrumentRequest();
		instrumentRequest.setRequestInfo(requestinfo);
		instrumentRequest.setInstruments(Collections.singletonList(instrument));

        try {
            List<Instrument> instruments = restTemplate.postForObject(builder.toString(), instrumentRequest,
                    InstrumentResponse.class).getInstruments();

            if (instruments.isEmpty())
                throw new CustomException("INSTRUMENT_SERVICE_ERROR", "Unable to create instrument");
            else
                return instruments.get(0);

        } catch (HttpClientErrorException e) {
            log.error("Unable to create instrument, " + instrument, e);
            throw new ServiceCallException(e.getResponseBodyAsString());
        } catch (Exception e) {
            log.error("Unable to create instrument, " + instrument, e);
            throw new CustomException("INSTRUMENT_SERVICE_ERROR", "Unable to create instrument, unknown error " +
                    "occurred");
        }
	}

    Instrument searchInstruments(final String instrumentHeader, final String tenantId, final RequestInfo requestInfo) {
        StringBuilder builder = new StringBuilder();
        String hostname = applicationProperties.getInstrumentServiceHost();
        String baseUri = applicationProperties.getSearchInstrument();
        builder.append(hostname).append(baseUri);

        RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper(requestInfo);

        try {
            List<Instrument> instruments = restTemplate.postForObject(builder.toString(),
                    requestInfoWrapper, InstrumentResponse.class, instrumentHeader, tenantId).getInstruments();

            if (instruments.isEmpty())
                return null;
            else
                return instruments.get(0);

        } catch (HttpClientErrorException e) {
            log.error("Unable to fetch instrument, {} in tenant {}", instrumentHeader, tenantId, e);
            throw new ServiceCallException(e.getResponseBodyAsString());
        } catch (Exception e) {
            log.error("Unable to fetch instrument, {} in tenant {}", instrumentHeader, tenantId, e);
            throw new CustomException("INSTRUMENT_SERVICE_ERROR", "Unable to fetch instrument, unknown error " +
                    "occurred");
        }

    }

    public List<Instrument> searchInstrumentsByPaymentMode(final String paymentMode,final String tenantId,final RequestInfo requestInfo) {
        StringBuilder builder = new StringBuilder();
        String hostname = applicationProperties.getInstrumentServiceHost();
        String baseUri = applicationProperties.getSearchInstrumentByPaymentMode();
        builder.append(hostname).append(baseUri);

        RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper(requestInfo);

        LOGGER.info("Request to instrument search: "
                + baseUri);
        LOGGER.info("URI Instrument search: " + builder.toString());
        List<Instrument> instrumentList = restTemplate.postForObject(builder.toString(),
                requestInfoWrapper, InstrumentResponse.class, paymentMode, tenantId).getInstruments();
        LOGGER.info("Response from instrument service: " + instrumentList);
        return instrumentList;

    }

    public String getAccountCodeId(RequestInfo requestinfo, Instrument instrument, String tenantId) {
		String glcode = null;
		StringBuilder builder = new StringBuilder();
		String hostname = applicationProperties.getInstrumentServiceHost();
		String baseUri = applicationProperties.getSearchAccountCodes();
		String searchCriteria = "?tenantId="+tenantId+"&name="+instrument.getInstrumentType().getName();
		builder.append(hostname).append(baseUri).append(searchCriteria);

        RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper(requestinfo);

		LOGGER.info("URI account code id search: " + builder.toString());
		LOGGER.info("Request account code id search: " + requestinfo);

        Object response = restTemplate.postForObject(builder.toString(),
					requestInfoWrapper, Object.class);
			
			glcode = JsonPath.read(response, "$.instrumentAccountCodes[0].accountCode.glcode");
		LOGGER.info("Response from instrument service: " + response.toString());
		
		
		return glcode;
	}

}
