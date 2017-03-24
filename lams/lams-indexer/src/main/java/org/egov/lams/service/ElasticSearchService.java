package org.egov.lams.service;

import java.util.Map;

import org.egov.lams.config.PropertiesManager;
import org.egov.lams.contract.AgreementDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ElasticSearchService {

	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private PropertiesManager propertiesManager;
	
	public static final Logger LOGGER = LoggerFactory.getLogger(ElasticSearchService.class);
	
	public void saveAgreement(AgreementDetails agreementDetails){
		
		String indexServiceHost = propertiesManager.getIndexServiceHostUrl();
		String indexSericeName = propertiesManager.getIndexServiceIndexName();
		// check for both index name and type name and id before confirming the url 
		String url = indexServiceHost+indexSericeName; 
		  //  for index id + indexId;
		try{
	    restTemplate.postForObject(url, agreementDetails, Map.class);
		}catch (Exception e) {
			LOGGER.error(e.toString());
			throw e;
		}
		LOGGER.info("ElasticSearchService saveagreement post agrementindexed in elasticsearch");
	}
	
	public void updateAgreement(AgreementDetails agreementDetails) {

		String indexServiceHost = propertiesManager.getIndexServiceHostUrl();
		String indexSericeName = propertiesManager.getIndexServiceIndexName();
		String id = agreementDetails.getAckNumber();
		String url = indexServiceHost+indexSericeName + "/" + id;
		// TODO add unique id
		try{
		restTemplate.postForObject(url, agreementDetails, Map.class);
	}catch (Exception e) {
		LOGGER.error(e.toString());
		throw e;
	}
		LOGGER.info("ElasticSearchService saveagreement post agrementindexed in elasticsearch");
	}
}
