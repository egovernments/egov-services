package org.egov.citizen.repository;

import org.egov.citizen.config.ApplicationProperties;
import org.egov.citizen.web.contract.ReceiptReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

@Repository
public class CollectionRepository {
	
	public static final Logger LOGGER = LoggerFactory
			.getLogger(CollectionRepository.class);
	
	@Autowired
	public RestTemplate restTemplate;
	
	@Autowired
	private ApplicationProperties applicationProperties;
	
	
	public Object createReceipt(ReceiptReq receiptRequest) {
		LOGGER.info("Creating receipt for bil...");
		StringBuilder uri = new StringBuilder();
		uri.append(applicationProperties.getCollectionServiceHostName())
		   .append(applicationProperties.getCreateReceiptURI());
		   
		LOGGER.info("URI creating receipt in collection service: "
				+ uri.toString());
		LOGGER.info("receiptRequest: "+receiptRequest.toString());
		Object response = null;
		response = restTemplate.postForObject(uri.toString(),
					receiptRequest, Object.class);
		LOGGER.info("Response from collection service: " + response);
		return response;
	}

}
