package org.egov.asset.indexer.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import lombok.Getter;


@Component
@Getter
public class PropertiesManager {
	
	/*@Autowired
	 private Environment environment;*/
	
	
	/*@Value("${egov.services.lams.indexer.host}")
	 String assetApiHostUrl;*/
	 
	
	 /*@Value("${egov.services.allottee_service.hostname}")
	 private String allotteeApiHostUrl;
	 
	 @Value("${egov.services.boundary_service.hostname}")
	 private String boundaryApiHostUrl;*/
	 
	/* @Value("${egov.services.lams.indexer.host}")
	  String indexServiceHostUrl;*/
	
	@Value("${egov.services.lams.indexer.host}")
	 String assetESIndexUrl;
	
	@Value("${egov.services.lams.indexer.type}")
	 String assetESIndexUrlType;
	
	
	 @Value("${egov.services.asset_service.hostname}")
	 String assetApiHostUrl;
	 
	 @Value("${egov.services.assetcategory_service.hostname}")
	 String assetCategoryApiHostUrl;
	 
	 @Value("${egov.services.lams.indexer.type}")
	  String indexServiceType;
	 
	 
	
	 

}
