package org.egov.asset.indexer.repository;

import java.util.Map;

import org.egov.asset.contract.AssetDetails;
import org.egov.asset.indexer.config.PropertiesManager;
//import org.egov.indexer.repository.ElasticSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Repository
public class ElasticSearchRepository {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private PropertiesManager propertiesManager;

	public static final Logger LOGGER = LoggerFactory.getLogger(ElasticSearchRepository.class);

	public void saveAsset(AssetDetails assetDetails) {

		// check for both index name and type name and id before confirming the
		// url
		String id = assetDetails.getAssetCode();
		String url = propertiesManager.getAssetESIndexUrl() + "/" + propertiesManager.getAssetESIndexUrlType() + "/" + id;
		try {
			restTemplate.postForObject(url, assetDetails, Map.class);
			LOGGER.info("Record saved in elastic search");
		} catch (RestClientException rce) {
			rce.getRootCause().printStackTrace();
			rce.printStackTrace();
		}
	}

}
