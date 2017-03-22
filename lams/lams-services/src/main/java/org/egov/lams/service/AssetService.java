package org.egov.lams.service;

import org.egov.lams.config.PropertiesManager;
import org.egov.lams.contract.AssetResponse;
import org.egov.lams.model.RequestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AssetService {

	public static final Logger logger = LoggerFactory.getLogger(AssetService.class);

	@Autowired
	private PropertiesManager propertiesManager;

	@Autowired
	private RestTemplate restTemplate;

	public AssetResponse getAssets(String queryString, RequestInfo requestInfo) {
		String url = null;
		AssetResponse assetResponse = null;
		try {
			url = propertiesManager.getAssetServiceHostName() + propertiesManager.getAssetServiceBasePAth()
					+ propertiesManager.getAssetServiceSearchPath() + "?" + queryString;

			logger.info(url.toString());
			assetResponse = restTemplate.postForObject(url, requestInfo, AssetResponse.class);
		} catch (Exception e) {
			logger.debug("exception in AssetService restTemplate",assetResponse);
			throw new RuntimeException("check if entered asset API url is correct or the asset service is running");
		}
		logger.info(assetResponse.toString());
		return assetResponse;
	}

}
