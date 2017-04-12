package org.egov.eis.indexer.service;

import java.net.URI;

import org.egov.core.web.contract.RequestInfoWrapper;
import org.egov.eis.indexer.config.PropertiesManager;
import org.egov.eis.model.Designation;
import org.egov.eis.model.EmployeeType;
import org.egov.eis.model.Grade;
import org.egov.eis.model.Group;
import org.egov.eis.model.Position;
import org.egov.eis.model.RecruitmentMode;
import org.egov.eis.model.RecruitmentQuota;
import org.egov.eis.model.RecruitmentType;
import org.egov.eis.web.contract.DesignationResponse;
import org.egov.eis.web.contract.EmployeeTypeResponse;
import org.egov.eis.web.contract.GradeResponse;
import org.egov.eis.web.contract.GroupResponse;
import org.egov.eis.web.contract.PositionResponse;
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

	public static final Logger LOGGER = LoggerFactory.getLogger(BoundaryService.class);

	// EmployeeType
	public EmployeeType getEmployeeType(Long id, String tenantId, RequestInfoWrapper requestInfoWrapper) {
		URI url = null;
		EmployeeTypeResponse employeeTypeResponse = null;
		try {
			url = new URI(propertiesManager.getHrMastersServiceHost() + propertiesManager.getHrMastersServiceBasepath()
					+ propertiesManager.getHrMastersServiceEmployeeTypeSearchPath() + "?id=" + id + "&tenantId="
					+ tenantId);
			LOGGER.debug(url.toString());
			try {
				employeeTypeResponse = restTemplate.postForObject(url, getRequestInfoAsHttpEntity(requestInfoWrapper),
						EmployeeTypeResponse.class);
			} catch (HttpClientErrorException e) {
				String errorResponseBody = e.getResponseBodyAsString();
				LOGGER.error(errorResponseBody);
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Following exception occurred while generating URI : " + e.getMessage());
			return null;
		}
		return employeeTypeResponse.getEmployeeType().get(0);
	}

	// RecruitmentMode
	public RecruitmentMode getRecruitmentMode(Long id, String tenantId, RequestInfoWrapper requestInfoWrapper) {
		URI url = null;
		RecruitmentModeResponse recruitmentModeResponse = null;
		try {
			url = new URI(propertiesManager.getHrMastersServiceHost() + propertiesManager.getHrMastersServiceBasepath()
					+ propertiesManager.getHrMastersServiceRecruitmentModeSearchPath() + "?id=" + id + "&tenantId="
					+ tenantId);
			LOGGER.debug(url.toString());
			recruitmentModeResponse = restTemplate.postForObject(url, getRequestInfoAsHttpEntity(requestInfoWrapper),
					RecruitmentModeResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Following exception occurred while generating URI : " + e.getMessage());
			return null;
		}
		return recruitmentModeResponse.getRecruitmentMode().get(0);
	}

	// RecruitmentQuota
	public RecruitmentQuota getRecruitmentQuota(Long id, String tenantId, RequestInfoWrapper requestInfoWrapper) {
		URI url = null;
		RecruitmentQuotaResponse recruitmentQuotaResponse = null;
		try {
			url = new URI(propertiesManager.getHrMastersServiceHost() + propertiesManager.getHrMastersServiceBasepath()
					+ propertiesManager.getHrMastersServiceRecruitmentQuotaSearchPath() + "?id=" + id + "&tenantId="
					+ tenantId);
			LOGGER.debug(url.toString());
			recruitmentQuotaResponse = restTemplate.postForObject(url, getRequestInfoAsHttpEntity(requestInfoWrapper),
					RecruitmentQuotaResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Following exception occurred while generating URI : " + e.getMessage());
			return null;
		}
		return recruitmentQuotaResponse.getRecruitmentQuota().get(0);
	}

	// RecruitmentType
	public RecruitmentType getRecruitmentType(Long id, String tenantId, RequestInfoWrapper requestInfoWrapper) {
		URI url = null;
		RecruitmentTypeResponse recruitmentTypeResponse = null;
		try {
			url = new URI(propertiesManager.getHrMastersServiceHost() + propertiesManager.getHrMastersServiceBasepath()
					+ propertiesManager.getHrMastersServiceRecruitmentTypeSearchPath() + "?id=" + id + "&tenantId="
					+ tenantId);
			LOGGER.info(url.toString());
			recruitmentTypeResponse = restTemplate.postForObject(url, getRequestInfoAsHttpEntity(requestInfoWrapper),
					RecruitmentTypeResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Following exception occurred while generating URI : " + e.getMessage());
			return null;
		}
		return recruitmentTypeResponse.getRecruitmentType().get(0);
	}

	// Grade
	public Grade getGrade(Long id, String tenantId, RequestInfoWrapper requestInfoWrapper) {
		URI url = null;
		GradeResponse gradeResponse = null;
		try {
			url = new URI(propertiesManager.getHrMastersServiceHost() + propertiesManager.getHrMastersServiceBasepath()
					+ propertiesManager.getHrMastersServiceGradeSearchPath() + "?id=" + id + "&tenantId=" + tenantId);
			LOGGER.info(url.toString());
			gradeResponse = restTemplate.postForObject(url, getRequestInfoAsHttpEntity(requestInfoWrapper),
					GradeResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Following exception occurred while generating URI : " + e.getMessage());
			return null;
		}
		return gradeResponse.getGrade().get(0);
	}

	// EmployeeGroup
	public Group getGroup(Long id, String tenantId, RequestInfoWrapper requestInfoWrapper) {
		URI url = null;
		GroupResponse groupResponse = null;
		try {
			url = new URI(propertiesManager.getHrMastersServiceHost() + propertiesManager.getHrMastersServiceBasepath()
					+ propertiesManager.getHrMastersServiceGroupSearchPath() + "?id=" + id + "&tenantId=" + tenantId);
			LOGGER.info(url.toString());
			groupResponse = restTemplate.postForObject(url, getRequestInfoAsHttpEntity(requestInfoWrapper),
					GroupResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Following exception occurred while generating URI : " + e.getMessage());
			return null;
		}
		return groupResponse.getGroup().get(0);
	}

	// Position
	public Position getPosition(Long id, String tenantId, RequestInfoWrapper requestInfoWrapper) {
		URI url = null;
		PositionResponse positionResponse = null;
		try {
			url = new URI(propertiesManager.getHrMastersServiceHost() + propertiesManager.getHrMastersServiceBasepath()
					+ propertiesManager.getHrMastersServicePositionSearchPath() + "?id=" + id + "&tenantId="
					+ tenantId);
			LOGGER.info(url.toString());
			positionResponse = restTemplate.postForObject(url, getRequestInfoAsHttpEntity(requestInfoWrapper),
					PositionResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Following exception occurred while generating URI : " + e.getMessage());
			return null;
		}
		return positionResponse.getPosition().get(0);
	}

	// Designation
	public Designation getDesignation(Long id, String tenantId, RequestInfoWrapper requestInfoWrapper) {
		URI url = null;
		DesignationResponse designationResponse = null;
		try {
			url = new URI(propertiesManager.getHrMastersServiceHost() + propertiesManager.getHrMastersServiceBasepath()
					+ propertiesManager.getHrMastersServiceDesignationSearchPath() + "?id=" + id + "&tenantId="
					+ tenantId);
			LOGGER.info(url.toString());
			designationResponse = restTemplate.postForObject(url, getRequestInfoAsHttpEntity(requestInfoWrapper),
					DesignationResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Following exception occurred while generating URI : " + e.getMessage());
			return null;
		}
		return designationResponse.getDesignation().get(0);
	}

	private HttpEntity<RequestInfoWrapper> getRequestInfoAsHttpEntity(RequestInfoWrapper requestInfoWrapper) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<RequestInfoWrapper> httpEntityRequest = new HttpEntity<RequestInfoWrapper>(requestInfoWrapper,
				headers);
		return httpEntityRequest;
	}
}
