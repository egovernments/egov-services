package org.egov.lams.service;

import java.util.List;
import java.util.Map;

import org.egov.lams.config.PropertiesManager;
import org.egov.lams.web.contract.AssetResponse;
import org.egov.lams.web.contract.RequestInfo;
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
		url = propertiesManager.getAssetServiceHostName() + propertiesManager.getAssetServiceBasePAth()
				+ propertiesManager.getAssetServiceSearchPath() + "?" + queryString;

		logger.info("the url of asset api call : "+url,url);
		try {
			assetResponse = restTemplate.postForObject(url, requestInfo, AssetResponse.class);
		} catch (Exception e) {
			logger.debug("exception in AssetService restTemplate", assetResponse);
			throw new RuntimeException("check if entered asset API url is correct or the asset service is running");
		}
		logger.info("the list of assets from assetresponse obtained by asset api call : "+assetResponse.getAssets(),assetResponse.getAssets());
		return assetResponse;
	}
	
	public boolean isAssetAvailable(Long assetId) {
		
		final String sql = "select id from eglams_agreement where asset="+assetId;
		logger.info("the url for asset api call to check isAssetAvalable : "+sql,sql);
		//FIXME table name from config 
		List<Map<String, Object>> resultSet = null;
		try{
			resultSet = jdbcTemplate.queryForList(sql);
		}catch (Exception exception) {
			logger.debug("aseetService isassetAvailable : " + exception,exception);
			throw exception;
			// TODO: handle exception
		}
		logger.info("the list of assets from assetresponse obtained by asset api call : "+resultSet,resultSet);
		return resultSet.isEmpty();
	}

}
