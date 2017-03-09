package org.egov.lams.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PropertiesManager {
	
	@Value("${egov.services.asset_service.hostname}")
	private String assetServiceHostName;

	public String getAssetServiceHostName() {
		return assetServiceHostName;
	}

	public void setAssetServiceHostName(String assetServiceHostName) {
		this.assetServiceHostName = assetServiceHostName;
	}
	
	

}
