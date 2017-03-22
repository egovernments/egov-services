package org.egov.hr.emp.indexer.service;

import java.net.URI;

import org.egov.hr.emp.indexer.config.PropertiesManager;
import org.egov.hr.emp.indexer.contract.EmployeeTypeResponse;
import org.egov.hr.emp.indexer.contract.RecruitmentModeResponse;
import org.egov.hr.emp.indexer.contract.RecruitmentQuotaResponse;
import org.egov.hr.emp.indexer.contract.RecruitmentTypeResponse;
import org.egov.hr.emp.indexer.contract.RequestInfo;
import org.egov.hr.emp.indexer.model.EmployeeType;
import org.egov.hr.emp.indexer.model.RecruitmentMode;
import org.egov.hr.emp.indexer.model.RecruitmentQuota;
import org.egov.hr.emp.indexer.model.RecruitmentType;
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
	private RestTemplate restTemplate;
	
	public static final Logger logger = LoggerFactory.getLogger(BoundaryService.class);
	
	// EmployeeType
	public EmployeeType getEmployeeType(Long id, RequestInfo requestInfo) {
		URI url = null;
		EmployeeTypeResponse employeeTypeResponse = null;
		try { //http://egov-micro-dev.egovernments.org/user/_search
			// url = new URI(propertiesManager.getBoundaryServiceHostName() + "?id=" + id);
			url = new URI( propertiesManager.getHrMasterServiceSearchUri()
					+ propertiesManager.getEmployeetypeSearchContextPath() 
					+ "?id=" + id + "&tenantId=1");
			logger.info(url.toString());
			employeeTypeResponse = restTemplate.postForObject(url, requestInfo, EmployeeTypeResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
			// FIXME log error to getstacktrace
			return null;
		}
		return employeeTypeResponse.getEmployeeType().get(0);
	}	
	
	// RecruitmentMode
	public RecruitmentMode getRecruitmentMode(Long id, RequestInfo requestInfo) {
		URI url = null;
		RecruitmentModeResponse recruitmentModeResponse = null;
		try { //http://egov-micro-dev.egovernments.org/user/_search
			// url = new URI(propertiesManager.getBoundaryServiceHostName() + "?id=" + id);
			url = new URI( propertiesManager.getHrMasterServiceSearchUri()
					+ propertiesManager.getRecruitmentmodeSearchContextPath()
					+ "?id=" + id + "&tenantId=1");
			logger.info(url.toString());
			recruitmentModeResponse = restTemplate.postForObject(url, requestInfo, RecruitmentModeResponse.class);
		} catch (Exception e) {
			// FIXME log error to getstacktrace
			return null;
		}
		return recruitmentModeResponse.getRecruitmentMode().get(0);
	}
	
	// RecruitmentQuota
	public RecruitmentQuota getRecruitmentQuota(Long id, RequestInfo requestInfo) {
		URI url = null;
		RecruitmentQuotaResponse recruitmentQuotaResponse = null;
		try { //http://egov-micro-dev.egovernments.org/user/_search
			// url = new URI(propertiesManager.getBoundaryServiceHostName() + "?id=" + id);
			url = new URI( propertiesManager.getHrMasterServiceSearchUri()
					+ propertiesManager.getRecruitmentquotaSearchContextPath()
					+ "?id=" + id + "&tenantId=1");
			logger.info(url.toString());
			recruitmentQuotaResponse = restTemplate.postForObject(url, requestInfo, RecruitmentQuotaResponse.class);
		} catch (Exception e) {
			// FIXME log error to getstacktrace
			return null;
		}
		return recruitmentQuotaResponse.getRecruitmentQuota().get(0);
	}
	
	
	// getRecruitmentType
	public RecruitmentType getRecruitmentType(Long id, RequestInfo requestInfo) {
		URI url = null;
		RecruitmentTypeResponse recruitmentTypeResponse = null;
		try { //http://egov-micro-dev.egovernments.org/user/_search
			// url = new URI(propertiesManager.getBoundaryServiceHostName() + "?id=" + id);
			url = new URI( propertiesManager.getHrMasterServiceSearchUri()
					+ propertiesManager.getRecruitmenttypeSearchContextPath()
					+ "?id=" + id + "&tenantId=1");
			logger.info(url.toString());
			recruitmentTypeResponse = restTemplate.postForObject(url, requestInfo, RecruitmentTypeResponse.class);
		} catch (Exception e) {
			// FIXME log error to getstacktrace
			return null;
		}
		return recruitmentTypeResponse.getRecruitmentType().get(0);
	}
	
}
