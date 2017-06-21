package org.egov.lams.repository;

import java.util.Map;

import org.egov.lams.config.PropertiesManager;
import org.egov.lams.contract.AgreementDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ElasticSearchRepository {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private PropertiesManager propertiesManager;

	public static final Logger LOGGER = LoggerFactory.getLogger(ElasticSearchRepository.class);

	public void saveAgreement(AgreementDetails agreementDetails) {
		// check for both index name and type name and id before confirming the
		// url
		String url = propertiesManager.getIndexServiceHostUrl() + propertiesManager.getIndexServiceIndexName() + "/"
				+ agreementDetails.getAgreementId();
		LOGGER.info("the url for posting new agreement object in index ::: " + url);
		try {
			restTemplate.postForObject(url, agreementDetails, Map.class);
		} catch (Exception e) {
			LOGGER.error(e.toString());
			throw e;
		}
		LOGGER.info("ElasticSearchService saveagreement post agrementindexed in elasticsearch");
	}

	public void updateAgreement(AgreementDetails agreementDetails) {

		String url = propertiesManager.getIndexServiceHostUrl() + propertiesManager.getIndexServiceIndexName() + "/"
				+ agreementDetails.getAgreementId();
		LOGGER.info("the url for posting new agreement object in index ::: " + url);
		// TODO add unique id
		try {
			restTemplate.postForObject(url, agreementDetails, Map.class);
		} catch (Exception e) {
			LOGGER.error(e.toString());
			throw e;
		}
		LOGGER.info("ElasticSearchService saveagreement post agrementindexed in elasticsearch");
	}
}
