package org.egov.web.indexer.eis.service;

import java.net.URI;

import org.egov.eis.model.Category;
import org.egov.eis.model.Community;
import org.egov.eis.model.Language;
import org.egov.eis.model.Religion;
import org.egov.web.contract.CategoryResponse;
import org.egov.web.contract.CommunityResponse;

import org.egov.web.contract.LanguageResponse;
import org.egov.web.contract.ReligionResponse;
import org.egov.web.contract.RequestInfo;
import org.egov.web.indexer.config.PropertiesManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CommonMasterService {
	
	@Autowired
	PropertiesManager propertiesManager;

	@Autowired
	RestTemplate restTemplate;
	
	public static final Logger logger = LoggerFactory.getLogger(BoundaryService.class);
	
	public Religion getReligion(Long id) {
		
			URI url = null;
			ReligionResponse religionResponse = null;
			try { //http://egov-micro-dev.egovernments.org/user/_search
				// url = new URI(propertiesManager.getBoundaryServiceHostName() + "?id=" + id);
				url = new URI(propertiesManager.getCommonMasterServiceSearchUri() 
						+ propertiesManager.getReligionSearchContextPath() 
						+ "?id=" + id + "&tenantId=1");
				logger.info(url.toString());
				religionResponse = restTemplate.postForObject(url, getRequestInfo(), ReligionResponse.class);
			} catch (Exception e) {
				// FIXME log error to getstacktrace
				e.printStackTrace();
				return null;
			}
			return religionResponse.getReligion().get(0);
		}
		
//Language
	public Language getLanguage(Long id) {
		
		URI url = null;
		LanguageResponse languageResponse = null;
		try { //http://egov-micro-dev.egovernments.org/user/_search
			// url = new URI(propertiesManager.getBoundaryServiceHostName() + "?id=" + id);
			url = new URI(propertiesManager.getCommonMasterServiceSearchUri() 
					+ propertiesManager.getLanguageSearchContextPath() 
					+ "?id=" + id + "&tenantId=1");
			logger.info(url.toString());
			languageResponse = restTemplate.postForObject(url, getRequestInfo(), LanguageResponse.class);
		} catch (Exception e) {
			// FIXME log error to getstacktrace
			return null;
		}
		return languageResponse.getLanguage().get(0);
	}
	
	
	
	// Community
	public Community getCommunity(Long id) {
		
		URI url = null;
		CommunityResponse communityResponse = null;
		try { //http://egov-micro-dev.egovernments.org/user/_search
			// url = new URI(propertiesManager.getBoundaryServiceHostName() + "?id=" + id);
			url = new URI(propertiesManager.getCommonMasterServiceSearchUri() 
					+ propertiesManager.getCommunitySearchContextPath() 
					+ "?id=" + id + "&tenantId=1");
			logger.info(url.toString());
			communityResponse = restTemplate.postForObject(url, getRequestInfo(), CommunityResponse.class);
		} catch (Exception e) {
			// FIXME log error to getstacktrace
			return null;
		}
		return communityResponse.getCommunity().get(0);
	}
	
	//Category
	public Category getCategory(Long id) {
		
		URI url = null;
		CategoryResponse categoryResponse = null;
		try { //http://egov-micro-dev.egovernments.org/user/_search
			// url = new URI(propertiesManager.getBoundaryServiceHostName() + "?id=" + id);
			url = new URI(propertiesManager.getCommonMasterServiceSearchUri() 
					+ propertiesManager.getCategoryrSearchContextPath()
					+ "?id=" + id + "&tenantId=1");
			logger.info(url.toString());
			categoryResponse = restTemplate.postForObject(url, getRequestInfo(), CategoryResponse.class);
		} catch (Exception e) {
			// FIXME log error to getstacktrace
			return null;
		}
		return categoryResponse.getCategory().get(0);
	}
	
	private RequestInfo getRequestInfo() {
		RequestInfo requestInfo = new RequestInfo();
		requestInfo.setApiId("emp");
		requestInfo.setVer("1.0");
		requestInfo.setTs("10/03/2017");
		requestInfo.setAction("create");
		requestInfo.setDid("1");
		requestInfo.setKey("abcdkey");
		requestInfo.setMsgId("20170310130900");
		requestInfo.setRequesterId("rajesh");
		requestInfo.setAuthToken("123");
		
		return requestInfo;
	}
	

}
