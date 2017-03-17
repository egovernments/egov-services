package org.egov.web.indexer.eis.service;

import java.net.URI;

import org.egov.eis.model.EmployeeType;
import org.egov.eis.model.RecruitmentMode;
import org.egov.eis.model.RecruitmentQuota;
import org.egov.eis.model.RecruitmentType;

import org.egov.web.contract.EmployeeTypeResponse;
import org.egov.web.contract.RecruitmentModeResponse;
import org.egov.web.contract.RecruitmentQuotaResponse;
import org.egov.web.contract.RecruitmentTypeResponse;
import org.egov.web.contract.RequestInfo;
import org.egov.web.indexer.config.PropertiesManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class HRMasterService {

	@Autowired
	private PropertiesManager propertiesManager;
	
	@Autowired
	RestTemplate restTemplate;
	
	public static final Logger logger = LoggerFactory.getLogger(BoundaryService.class);
	
	// EmployeeType
	public EmployeeType getEmployeeType(Long id) {
		URI url = null;
		EmployeeTypeResponse employeeTypeResponse = null;
		try { //http://egov-micro-dev.egovernments.org/user/_search
			// url = new URI(propertiesManager.getBoundaryServiceHostName() + "?id=" + id);
			url = new URI( propertiesManager.getHrMasterServiceSearchUri()
					+ propertiesManager.getEmployeetypeSearchContextPath() 
					+ "?id=" + id + "&tenantId=1");
			logger.info(url.toString());
			employeeTypeResponse = restTemplate.postForObject(url, getRequestInfo(), EmployeeTypeResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
			// FIXME log error to getstacktrace
			return null;
		}
		return employeeTypeResponse.getEmployeeType().get(0);
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
	
	
	// RecruitmentMode
	public RecruitmentMode getRecruitmentMode(Long id) {
		URI url = null;
		RecruitmentModeResponse recruitmentModeResponse = null;
		try { //http://egov-micro-dev.egovernments.org/user/_search
			// url = new URI(propertiesManager.getBoundaryServiceHostName() + "?id=" + id);
			url = new URI( propertiesManager.getHrMasterServiceSearchUri()
					+ propertiesManager.getRecruitmentmodeSearchContextPath()
					+ "?id=" + id + "&tenantId=1");
			logger.info(url.toString());
			recruitmentModeResponse = restTemplate.postForObject(url, getRequestInfo(), RecruitmentModeResponse.class);
		} catch (Exception e) {
			// FIXME log error to getstacktrace
			return null;
		}
		return recruitmentModeResponse.getRecruitmentMode().get(0);
	}
	
	// RecruitmentQuota
	public RecruitmentQuota getRecruitmentQuota(Long id) {
		URI url = null;
		RecruitmentQuotaResponse recruitmentQuotaResponse = null;
		try { //http://egov-micro-dev.egovernments.org/user/_search
			// url = new URI(propertiesManager.getBoundaryServiceHostName() + "?id=" + id);
			url = new URI( propertiesManager.getHrMasterServiceSearchUri()
					+ propertiesManager.getRecruitmentquotaSearchContextPath()
					+ "?id=" + id + "&tenantId=1");
			logger.info(url.toString());
			recruitmentQuotaResponse = restTemplate.postForObject(url, getRequestInfo(), RecruitmentQuotaResponse.class);
		} catch (Exception e) {
			// FIXME log error to getstacktrace
			return null;
		}
		return recruitmentQuotaResponse.getRecruitmentQuota().get(0);
	}
	
	
	// getRecruitmentType
	public RecruitmentType getRecruitmentType(Long id) {
		URI url = null;
		RecruitmentTypeResponse recruitmentTypeResponse = null;
		try { //http://egov-micro-dev.egovernments.org/user/_search
			// url = new URI(propertiesManager.getBoundaryServiceHostName() + "?id=" + id);
			url = new URI( propertiesManager.getHrMasterServiceSearchUri()
					+ propertiesManager.getRecruitmenttypeSearchContextPath()
					+ "?id=" + id + "&tenantId=1");
			logger.info(url.toString());
			recruitmentTypeResponse = restTemplate.postForObject(url, getRequestInfo(), RecruitmentTypeResponse.class);
		} catch (Exception e) {
			// FIXME log error to getstacktrace
			return null;
		}
		return recruitmentTypeResponse.getRecruitmentType().get(0);
	}
	
}
