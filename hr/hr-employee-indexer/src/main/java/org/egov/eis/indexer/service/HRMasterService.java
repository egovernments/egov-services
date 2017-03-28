package org.egov.eis.indexer.service;

import java.net.URI;

import org.egov.core.web.contract.RequestInfo;
import org.egov.eis.indexer.config.PropertiesManager;
import org.egov.eis.model.EmployeeType;
import org.egov.eis.model.RecruitmentMode;
import org.egov.eis.model.RecruitmentQuota;
import org.egov.eis.model.RecruitmentType;
import org.egov.eis.web.contract.EmployeeTypeResponse;
import org.egov.eis.web.contract.RecruitmentModeResponse;
import org.egov.eis.web.contract.RecruitmentQuotaResponse;
import org.egov.eis.web.contract.RecruitmentTypeResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class HRMasterService {

	@Autowired
	private PropertiesManager propertiesManager;
	
	@Autowired
	private RestTemplate restTemplate;
	
	public static final Logger logger = LoggerFactory.getLogger(BoundaryService.class);
	
	// EmployeeType
	public EmployeeType getEmployeeType(Long id, String tenantId, RequestInfo requestInfo) {
		URI url = null;
		EmployeeTypeResponse employeeTypeResponse = null;
		try { //http://egov-micro-dev.egovernments.org/user/_search
			// url = new URI(propertiesManager.getBoundaryServiceHostName() + "?id=" + id);
			url = new URI( propertiesManager.getHrMasterServiceSearchUri()
					+ propertiesManager.getEmployeetypeSearchContextPath() 
					+ "?id=" + id + "&tenantId=" + tenantId);
			logger.debug(url.toString());
			try {
			employeeTypeResponse = restTemplate.postForObject(url, 
					getRequestInfoAsHttpEntity(requestInfo), EmployeeTypeResponse.class);
			} catch (HttpClientErrorException e) {
				String errorResponseBody = e.getResponseBodyAsString();
				logger.error(errorResponseBody);
			}
		} catch (Exception e) {
			e.printStackTrace();
			// FIXME log error to getstacktrace
			return null;
		}
		return employeeTypeResponse.getEmployeeType().get(0);
	}	
	
	private HttpEntity<RequestInfo> getRequestInfoAsHttpEntity(RequestInfo requestInfo) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		// FIXME : Passing auth-token for testing locally. Remove before actual deployment.
		headers.add("auth-token", requestInfo.getAuthToken());
		HttpEntity<RequestInfo> httpEntityRequest = new HttpEntity<RequestInfo>(requestInfo, headers);
		return httpEntityRequest;
	}

	// RecruitmentMode
	public RecruitmentMode getRecruitmentMode(Long id, String tenantId, RequestInfo requestInfo) {
		URI url = null;
		RecruitmentModeResponse recruitmentModeResponse = null;
		try { //http://egov-micro-dev.egovernments.org/user/_search
			// url = new URI(propertiesManager.getBoundaryServiceHostName() + "?id=" + id);
			url = new URI( propertiesManager.getHrMasterServiceSearchUri()
					+ propertiesManager.getRecruitmentmodeSearchContextPath()
					+ "?id=" + id + "&tenantId=" + tenantId);
			logger.debug(url.toString());
			recruitmentModeResponse = restTemplate.postForObject(url, getRequestInfoAsHttpEntity(requestInfo), RecruitmentModeResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
			// FIXME log error to getstacktrace
			return null;
		}
		return recruitmentModeResponse.getRecruitmentMode().get(0);
	}
	
	// RecruitmentQuota
	public RecruitmentQuota getRecruitmentQuota(Long id, String tenantId, RequestInfo requestInfo) {
		URI url = null;
		RecruitmentQuotaResponse recruitmentQuotaResponse = null;
		try { //http://egov-micro-dev.egovernments.org/user/_search
			// url = new URI(propertiesManager.getBoundaryServiceHostName() + "?id=" + id);
			url = new URI( propertiesManager.getHrMasterServiceSearchUri()
					+ propertiesManager.getRecruitmentquotaSearchContextPath()
					+ "?id=" + id + "&tenantId=" + tenantId);
			logger.debug(url.toString());
			recruitmentQuotaResponse = restTemplate.postForObject(url, getRequestInfoAsHttpEntity(requestInfo), RecruitmentQuotaResponse.class);
		} catch (Exception e) {
			// FIXME log error to getstacktrace
			e.printStackTrace();
			return null;
		}
		return recruitmentQuotaResponse.getRecruitmentQuota().get(0);
	}

	// getRecruitmentType
	public RecruitmentType getRecruitmentType(Long id, String tenantId, RequestInfo requestInfo) {
		URI url = null;
		RecruitmentTypeResponse recruitmentTypeResponse = null;
		try { //http://egov-micro-dev.egovernments.org/user/_search
			// url = new URI(propertiesManager.getBoundaryServiceHostName() + "?id=" + id);
			url = new URI( propertiesManager.getHrMasterServiceSearchUri()
					+ propertiesManager.getRecruitmenttypeSearchContextPath()
					+ "?id=" + id + "&tenantId=" + tenantId);
			logger.info(url.toString());
			recruitmentTypeResponse = restTemplate.postForObject(url, getRequestInfoAsHttpEntity(requestInfo), RecruitmentTypeResponse.class);
		} catch (Exception e) {
			// FIXME log error to getstacktrace
			return null;
		}
		return recruitmentTypeResponse.getRecruitmentType().get(0);
	}
	
}
