package org.egov.eis.indexer.service;

import java.net.URI;

import org.egov.commons.model.Category;
import org.egov.commons.model.Community;
import org.egov.commons.model.Language;
import org.egov.commons.model.Religion;
import org.egov.commons.web.contract.CategoryResponse;
import org.egov.commons.web.contract.CommunityResponse;
import org.egov.commons.web.contract.LanguageResponse;
import org.egov.commons.web.contract.ReligionResponse;
import org.egov.core.web.contract.RequestInfo;
import org.egov.eis.indexer.config.PropertiesManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CommonMasterService {
	
	@Autowired
	private PropertiesManager propertiesManager;

	@Autowired
	private RestTemplate restTemplate;
	
	public static final Logger logger = LoggerFactory.getLogger(BoundaryService.class);
	
	public Religion getReligion(Long id, String tenantId, RequestInfo requestInfo) {
		
			URI url = null;
			ReligionResponse religionResponse = null;
			try { //http://egov-micro-dev.egovernments.org/user/_search
				// url = new URI(propertiesManager.getBoundaryServiceHostName() + "?id=" + id);
				url = new URI(propertiesManager.getCommonMasterServiceSearchUri() 
						+ propertiesManager.getReligionSearchContextPath() 
						+ "?id=" + id + "&tenantId=" + tenantId);
				logger.debug(url.toString());
				religionResponse = restTemplate.postForObject(url, getRequestInfoAsHttpEntity(requestInfo), ReligionResponse.class);
			} catch (Exception e) {
				// FIXME log error to getstacktrace
				e.printStackTrace();
				return null;
			}
			return religionResponse.getReligion().get(0);
		}
		
//Language
	public Language getLanguage(Long id, String tenantId, RequestInfo requestInfo) {
		
		URI url = null;
		LanguageResponse languageResponse = null;
		try { //http://egov-micro-dev.egovernments.org/user/_search
			// url = new URI(propertiesManager.getBoundaryServiceHostName() + "?id=" + id);
			url = new URI(propertiesManager.getCommonMasterServiceSearchUri() 
					+ propertiesManager.getLanguageSearchContextPath() 
					+ "?id=" + id + "&tenantId=" + tenantId);
			logger.debug(url.toString());
			languageResponse = restTemplate.postForObject(url, getRequestInfoAsHttpEntity(requestInfo), LanguageResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
			// FIXME log error to getstacktrace
			return null;
		}
		return languageResponse.getLanguage().get(0);
	}
	
	
	
	// Community
	public Community getCommunity(Long id, String tenantId, RequestInfo requestInfo) {
		
		URI url = null;
		CommunityResponse communityResponse = null;
		try { //http://egov-micro-dev.egovernments.org/user/_search
			// url = new URI(propertiesManager.getBoundaryServiceHostName() + "?id=" + id);
			url = new URI(propertiesManager.getCommonMasterServiceSearchUri() 
					+ propertiesManager.getCommunitySearchContextPath() 
					+ "?id=" + id + "&tenantId=" + tenantId);
			logger.debug(url.toString());
			communityResponse = restTemplate.postForObject(url, getRequestInfoAsHttpEntity(requestInfo), CommunityResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
			// FIXME log error to getstacktrace
			return null;
		}
		return communityResponse.getCommunity().get(0);
	}
	
	//Category
	public Category getCategory(Long id, String tenantId, RequestInfo requestInfo) {
		
		URI url = null;
		CategoryResponse categoryResponse = null;
		try { //http://egov-micro-dev.egovernments.org/user/_search
			// url = new URI(propertiesManager.getBoundaryServiceHostName() + "?id=" + id);
			url = new URI(propertiesManager.getCommonMasterServiceSearchUri() 
					+ propertiesManager.getCategoryrSearchContextPath()
					+ "?id=" + id + "&tenantId=" + tenantId);
			logger.debug(url.toString());
			categoryResponse = restTemplate.postForObject(url, getRequestInfoAsHttpEntity(requestInfo), CategoryResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
			// FIXME log error to getstacktrace
			return null;
		}
		return categoryResponse.getCategory().get(0);
	}
	
	private HttpEntity<RequestInfo> getRequestInfoAsHttpEntity(RequestInfo requestInfo) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		// FIXME : Passing auth-token for testing locally. Remove before actual deployment.
		headers.add("auth-token", requestInfo.getAuthToken());
		HttpEntity<RequestInfo> httpEntityRequest = new HttpEntity<RequestInfo>(requestInfo, headers);
		return httpEntityRequest;
	}

	

}
