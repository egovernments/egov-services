package org.egov.hr.emp.indexer.service;

import java.net.URI;

import org.egov.hr.emp.indexer.config.PropertiesManager;
import org.egov.hr.emp.indexer.contract.CategoryResponse;
import org.egov.hr.emp.indexer.contract.CommunityResponse;
import org.egov.hr.emp.indexer.contract.LanguageResponse;
import org.egov.hr.emp.indexer.contract.ReligionResponse;
import org.egov.hr.emp.indexer.contract.RequestInfo;
import org.egov.hr.emp.indexer.model.Category;
import org.egov.hr.emp.indexer.model.Community;
import org.egov.hr.emp.indexer.model.Language;
import org.egov.hr.emp.indexer.model.Religion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CommonMasterService {
	
	@Autowired
	private PropertiesManager propertiesManager;

	@Autowired
	private RestTemplate restTemplate;
	
	public static final Logger logger = LoggerFactory.getLogger(BoundaryService.class);
	
	public Religion getReligion(Long id, RequestInfo requestInfo) {
		
			URI url = null;
			ReligionResponse religionResponse = null;
			try { //http://egov-micro-dev.egovernments.org/user/_search
				// url = new URI(propertiesManager.getBoundaryServiceHostName() + "?id=" + id);
				url = new URI(propertiesManager.getCommonMasterServiceSearchUri() 
						+ propertiesManager.getReligionSearchContextPath() 
						+ "?id=" + id + "&tenantId=1");
				logger.info(url.toString());
				religionResponse = restTemplate.postForObject(url, requestInfo, ReligionResponse.class);
			} catch (Exception e) {
				// FIXME log error to getstacktrace
				e.printStackTrace();
				return null;
			}
			return religionResponse.getReligion().get(0);
		}
		
//Language
	public Language getLanguage(Long id, RequestInfo requestInfo) {
		
		URI url = null;
		LanguageResponse languageResponse = null;
		try { //http://egov-micro-dev.egovernments.org/user/_search
			// url = new URI(propertiesManager.getBoundaryServiceHostName() + "?id=" + id);
			url = new URI(propertiesManager.getCommonMasterServiceSearchUri() 
					+ propertiesManager.getLanguageSearchContextPath() 
					+ "?id=" + id + "&tenantId=1");
			logger.info(url.toString());
			languageResponse = restTemplate.postForObject(url, requestInfo, LanguageResponse.class);
		} catch (Exception e) {
			// FIXME log error to getstacktrace
			return null;
		}
		return languageResponse.getLanguage().get(0);
	}
	
	
	
	// Community
	public Community getCommunity(Long id, RequestInfo requestInfo) {
		
		URI url = null;
		CommunityResponse communityResponse = null;
		try { //http://egov-micro-dev.egovernments.org/user/_search
			// url = new URI(propertiesManager.getBoundaryServiceHostName() + "?id=" + id);
			url = new URI(propertiesManager.getCommonMasterServiceSearchUri() 
					+ propertiesManager.getCommunitySearchContextPath() 
					+ "?id=" + id + "&tenantId=1");
			logger.info(url.toString());
			communityResponse = restTemplate.postForObject(url, requestInfo, CommunityResponse.class);
		} catch (Exception e) {
			// FIXME log error to getstacktrace
			return null;
		}
		return communityResponse.getCommunity().get(0);
	}
	
	//Category
	public Category getCategory(Long id, RequestInfo requestInfo) {
		
		URI url = null;
		CategoryResponse categoryResponse = null;
		try { //http://egov-micro-dev.egovernments.org/user/_search
			// url = new URI(propertiesManager.getBoundaryServiceHostName() + "?id=" + id);
			url = new URI(propertiesManager.getCommonMasterServiceSearchUri() 
					+ propertiesManager.getCategoryrSearchContextPath()
					+ "?id=" + id + "&tenantId=1");
			logger.info(url.toString());
			categoryResponse = restTemplate.postForObject(url, requestInfo, CategoryResponse.class);
		} catch (Exception e) {
			// FIXME log error to getstacktrace
			return null;
		}
		return categoryResponse.getCategory().get(0);
	}
	

	

}
