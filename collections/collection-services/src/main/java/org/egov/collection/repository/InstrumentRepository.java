package org.egov.collection.repository;

import java.util.ArrayList;
import java.util.List;

import org.egov.collection.config.ApplicationProperties;
import org.egov.collection.model.Instrument;
import org.egov.collection.model.InstrumentRequest;
import org.egov.collection.web.contract.Receipt;
import org.egov.collection.web.contract.ReceiptReq;
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
	
	public String getInstrumentId(ReceiptReq receiptReq) {
		String instrumentId = null;
		StringBuilder builder = new StringBuilder();
		String hostname = applicationProperties.getInstrumentServiceHost();
		String baseUri = applicationProperties.getCreateInstrument();
		builder.append(hostname).append(baseUri);
		Receipt receipt = receiptReq.getReceipt().get(0);
		Instrument instrument = receipt.getInstrument();
		List<Instrument> instruments = new ArrayList<>();
		instruments.add(instrument);
		InstrumentRequest instrumentRequest = new InstrumentRequest();
		instrumentRequest.setRequestInfo(receiptReq.getRequestInfo());
		instrumentRequest.setInstruments(instruments);
		Object response = null;
		
		LOGGER.info("Request to instrument create: "+instrumentRequest.toString());
		LOGGER.info("URI Instrument create: "+builder.toString());
		try {
			response = restTemplate.postForObject(builder.toString(),
					instrumentRequest, Object.class);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error(
					"Couldn't create instrument in the instrument service.",
					e.getCause());
			return instrumentId;
		}
		LOGGER.info("Response from instrument service: " + response.toString());
		
		instrumentId = JsonPath.read(response, "$.instruments[0].id");
		return instrumentId;
	}
	
	

}
