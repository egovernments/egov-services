package org.egov.eis.indexer.service;

import java.net.URI;
import java.util.List;

import org.egov.commons.model.Category;
import org.egov.commons.model.Community;
import org.egov.commons.model.Department;
import org.egov.commons.model.Language;
import org.egov.commons.model.Religion;
import org.egov.commons.web.contract.CategoryResponse;
import org.egov.commons.web.contract.CommunityResponse;
import org.egov.commons.web.contract.DepartmentResponse;
import org.egov.commons.web.contract.LanguageResponse;
import org.egov.commons.web.contract.ReligionResponse;
import org.egov.eis.web.contract.RequestInfoWrapper;
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

	public static final Logger LOGGER = LoggerFactory.getLogger(CommonMasterService.class);

	public Religion getReligion(Long id, String tenantId, RequestInfoWrapper requestInfoWrapper) {

		URI url = null;
		ReligionResponse religionResponse = null;
		try {
			url = new URI(propertiesManager.getEgovCommonMastersServiceHost()
					+ propertiesManager.getEgovCommonMastersServiceBasepath()
					+ propertiesManager.getEgovCommonMastersServiceReligionSearchPath() + "?id=" + id + "&tenantId="
					+ tenantId);
			LOGGER.debug(url.toString());
			religionResponse = restTemplate.postForObject(url, getRequestInfoAsHttpEntity(requestInfoWrapper),
					ReligionResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Following exception occurred while accessing Religion API : " + e.getMessage());
			return null;
		}
		return religionResponse.getReligion().get(0);
	}

	// Language
	public List<Language> getLanguages(List<Long> ids, String tenantId, RequestInfoWrapper requestInfoWrapper) {
		String idsAsCSV = getIdsAsCSV(ids);

		URI url = null;
		LanguageResponse languageResponse = null;
		try {
			url = new URI(propertiesManager.getEgovCommonMastersServiceHost()
					+ propertiesManager.getEgovCommonMastersServiceBasepath()
					+ propertiesManager.getEgovCommonMastersServiceLanguageSearchPath()
					+ "?id=" + idsAsCSV + "&tenantId=" + tenantId);
			LOGGER.debug(url.toString());
			languageResponse = restTemplate.postForObject(url, getRequestInfoAsHttpEntity(requestInfoWrapper),
					LanguageResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Following exception occurred while accessing Language API : " + e.getMessage());
			return null;
		}

		return languageResponse.getLanguage();
	}

	// Community
	public Community getCommunity(Long id, String tenantId, RequestInfoWrapper requestInfoWrapper) {

		URI url = null;
		CommunityResponse communityResponse = null;
		try {
			url = new URI(propertiesManager.getEgovCommonMastersServiceHost()
					+ propertiesManager.getEgovCommonMastersServiceBasepath()
					+ propertiesManager.getEgovCommonMastersServiceCommunitySearchPath() + "?id=" + id + "&tenantId="
					+ tenantId);
			LOGGER.debug(url.toString());
			communityResponse = restTemplate.postForObject(url, getRequestInfoAsHttpEntity(requestInfoWrapper),
					CommunityResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Following exception occurred while accessing Community API : " + e.getMessage());
			return null;
		}
		return communityResponse.getCommunity().get(0);
	}

	// Category
	public Category getCategory(Long id, String tenantId, RequestInfoWrapper requestInfoWrapper) {

		URI url = null;
		CategoryResponse categoryResponse = null;
		try {
			url = new URI(propertiesManager.getEgovCommonMastersServiceHost()
					+ propertiesManager.getEgovCommonMastersServiceBasepath()
					+ propertiesManager.getEgovCommonMastersServiceCategorySearchPath() + "?id=" + id + "&tenantId="
					+ tenantId);
			LOGGER.debug(url.toString());
			categoryResponse = restTemplate.postForObject(url, getRequestInfoAsHttpEntity(requestInfoWrapper),
					CategoryResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Following exception occurred while accessing Category API : " + e.getMessage());
			return null;
		}
		return categoryResponse.getCategory().get(0);
	}

	// Department
	public Department getDepartment(Long id, String tenantId, RequestInfoWrapper requestInfoWrapper) {

		URI url = null;
		DepartmentResponse departmentResponse = null;
		try {
			url = new URI(propertiesManager.getEgovCommonMastersServiceHost()
					+ propertiesManager.getEgovCommonMastersServiceBasepath()
					+ propertiesManager.getEgovCommonMastersServiceDepartmentSearchPath() + "?id=" + id + "&tenantId="
					+ tenantId);
			LOGGER.debug(url.toString());
			departmentResponse = restTemplate.postForObject(url, getRequestInfoAsHttpEntity(requestInfoWrapper),
					DepartmentResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Following exception occurred while accessing Department API : " + e.getMessage());
			return null;
		}
		return departmentResponse.getDepartment().get(0);
	}

	private String getIdsAsCSV(List<Long> ids) {
		return ids.toString().replaceAll(" ", "").replace("[", "").replace("]", "");
	}

	private HttpEntity<RequestInfoWrapper> getRequestInfoAsHttpEntity(RequestInfoWrapper requestInfoWrapper) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<RequestInfoWrapper> httpEntityRequest = new HttpEntity<RequestInfoWrapper>(requestInfoWrapper,
				headers);
		return httpEntityRequest;
	}
}