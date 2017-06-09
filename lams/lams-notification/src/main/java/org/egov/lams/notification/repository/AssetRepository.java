package org.egov.lams.notification.repository;


import org.egov.lams.notification.config.PropertiesManager;
import org.egov.lams.notification.model.Asset;
import org.egov.lams.notification.web.contract.AssetResponse;
import org.egov.lams.notification.web.contract.RequestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AssetRepository {

	public static final Logger LOGGER = LoggerFactory.getLogger(AssetRepository.class);

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private PropertiesManager propertiesManager;

	public Asset getAsset(Long assetId,String tenantId) {

		String url = propertiesManager.getAssetApiHostUrl()
					+propertiesManager.getAssetApiSearchPath()
					+ "?id=" + assetId + "&tenantId=" + tenantId;
		LOGGER.info("ASSET API URL : "+url);
		AssetResponse assetResponse = null;
		try {
			assetResponse = restTemplate.postForObject(url,new RequestInfo(), AssetResponse.class);
		} catch (Exception e) {
			LOGGER.info(e.getMessage(), e);
			throw e;// FIXME exception custom
		}
		System.out.println("the object of response : "+assetResponse);
		return assetResponse.getAssets().get(0);
	}

}
