package org.egov.lams.service;

import org.egov.lams.config.PropertiesManager;
import org.egov.lams.contract.AssetResponse;
import org.egov.lams.model.RequestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AssetService {

	public static final Logger logger = LoggerFactory.getLogger(AssetService.class);

	@Autowired
	private PropertiesManager propertiesManager;

	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

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
	
	public boolean isAssetAvailable(Long assetId) {
		
		final String sql = "select id from elams_agreement where asset="+assetId;
		//FIXME table name from config 
		Long agreementId = null;
		try{
			agreementId = jdbcTemplate.queryForObject(sql,Long.class);
		}catch (Exception exception) {
			logger.debug("aseetService isassetAvailable : ",exception);
			throw exception;
			// TODO: handle exception
		}
		return agreementId == null;
	}

}
