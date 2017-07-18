package org.egov.eis.indexer.service;

import org.egov.common.contract.response.ResponseInfo;
import org.egov.commons.model.*;
import org.egov.commons.web.contract.*;
import org.egov.eis.web.contract.RequestInfoWrapper;
import org.egov.eis.indexer.config.PropertiesManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CommonMasterServiceTest {

	@Mock
	private PropertiesManager propertiesManager;

	@Mock
	private RestTemplate restTemplate;

	@InjectMocks
	private CommonMasterService commonMasterService;

	@Test
	public void testGetReligion() {

		ResponseInfo responseInfo = new ResponseInfo("emp", "1.0", "2017-01-18T07:18:23.130Z", "uief87324",
				"20170310130900", "200");
		Religion religion = new Religion().builder().id(100L).name("employee1").tenantId("1").build();
		List<Religion> religions = new ArrayList<>();
		religions.add(religion);
		ReligionResponse religionResponse = new ReligionResponse().builder().responseInfo(responseInfo)
				.religion(religions).build();

		when(propertiesManager.getEgovCommonMastersServiceHost()).thenReturn("http://egov-micro-dev.egovernments.org");
		when(propertiesManager.getEgovCommonMastersServiceBasepath()).thenReturn("/egov-common-masters");
		when(propertiesManager.getEgovCommonMastersServiceReligionSearchPath()).thenReturn("/religions/_search");

		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		URI uri = null;
		try {
			uri = new URI(
					"http://egov-micro-dev.egovernments.org/egov-common-masters/religions/_search?id=100&tenantId=1");
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		when(restTemplate.postForObject(uri, getRequestInfoAsHttpEntity(requestInfoWrapper), ReligionResponse.class))
				.thenReturn(religionResponse);

		Religion insertedReligion = commonMasterService.getReligion(100L, "1", new RequestInfoWrapper());
		assertEquals(insertedReligion, religion);
	}

	@Test
	public void testGetLanguage() {

		ResponseInfo responseInfo = new ResponseInfo("emp", "1.0", "2017-01-18T07:18:23.130Z", "uief87324",
				"20170310130900", "200");
		Language language = new Language().builder().id(100L).name("employee1").tenantId("1").build();
		List<Language> languages = new ArrayList<>();
		languages.add(language);
		LanguageResponse languageResponse = LanguageResponse.builder().responseInfo(responseInfo)
				.language(languages).build();

		when(propertiesManager.getEgovCommonMastersServiceHost()).thenReturn("http://egov-micro-dev.egovernments.org");
		when(propertiesManager.getEgovCommonMastersServiceBasepath()).thenReturn("/egov-common-masters");
		when(propertiesManager.getEgovCommonMastersServiceLanguageSearchPath()).thenReturn("/languages/_search");

		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		URI uri = null;
		try {
			uri = new URI(
					"http://egov-micro-dev.egovernments.org/egov-common-masters/languages/_search?id=100&tenantId=1");
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		when(restTemplate.postForObject(uri, getRequestInfoAsHttpEntity(requestInfoWrapper), LanguageResponse.class))
				.thenReturn(languageResponse);

/*
		Language insertedLanguage = commonMasterService.getLanguages(Arrays.asList(100L), "1", new RequestInfoWrapper()).get(0);
		assertEquals(insertedLanguage, language);
*/
	}

	@Test
	public void testGetCommunity() {

		ResponseInfo responseInfo = new ResponseInfo("emp", "1.0", "2017-01-18T07:18:23.130Z", "uief87324",
				"20170310130900", "200");
		Community community = new Community().builder().id(100L).name("employee1").tenantId("1").build();
		List<Community> communities = new ArrayList<>();
		communities.add(community);
		CommunityResponse communityResponse = new CommunityResponse().builder().responseInfo(responseInfo)
				.community(communities).build();

		when(propertiesManager.getEgovCommonMastersServiceHost()).thenReturn("http://egov-micro-dev.egovernments.org");
		when(propertiesManager.getEgovCommonMastersServiceBasepath()).thenReturn("/egov-common-masters");
		when(propertiesManager.getEgovCommonMastersServiceCommunitySearchPath()).thenReturn("/communities/_search");

		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		URI uri = null;
		try {
			uri = new URI(
					"http://egov-micro-dev.egovernments.org/egov-common-masters/communities/_search?id=100&tenantId=1");
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		when(restTemplate.postForObject(uri, getRequestInfoAsHttpEntity(requestInfoWrapper), CommunityResponse.class))
				.thenReturn(communityResponse);

		Community insertedCommunity = commonMasterService.getCommunity(100L, "1", new RequestInfoWrapper());
		assertEquals(insertedCommunity, community);
	}

	@Test
	public void testGetCategory() {

		ResponseInfo responseInfo = new ResponseInfo("emp", "1.0", "2017-01-18T07:18:23.130Z", "uief87324",
				"20170310130900", "200");
		Category category = new Category().builder().id(100L).name("employee1").tenantId("1").build();
		List<Category> categories = new ArrayList<>();
		categories.add(category);
		CategoryResponse categoryResponse = new CategoryResponse().builder().responseInfo(responseInfo)
				.category(categories).build();

		when(propertiesManager.getEgovCommonMastersServiceHost()).thenReturn("http://egov-micro-dev.egovernments.org");
		when(propertiesManager.getEgovCommonMastersServiceBasepath()).thenReturn("/egov-common-masters");
		when(propertiesManager.getEgovCommonMastersServiceCategorySearchPath()).thenReturn("/categories/_search");

		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		URI uri = null;
		try {
			uri = new URI(
					"http://egov-micro-dev.egovernments.org/egov-common-masters/categories/_search?id=100&tenantId=1");
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		when(restTemplate.postForObject(uri, getRequestInfoAsHttpEntity(requestInfoWrapper), CategoryResponse.class))
				.thenReturn(categoryResponse);

		Category insertedCategory = commonMasterService.getCategory(100L, "1", new RequestInfoWrapper());
		assertEquals(insertedCategory, category);
	}

	@Test
	public void testGetDepartment() {

		ResponseInfo responseInfo = new ResponseInfo("emp", "1.0", "2017-01-18T07:18:23.130Z", "uief87324",
				"20170310130900", "200");
		Department department = new Department().builder().id(100L).name("employee1").tenantId("1").build();
		List<Department> departments = new ArrayList<>();
		departments.add(department);
		DepartmentResponse departmentResponse = new DepartmentResponse().builder().responseInfo(responseInfo)
				.department(departments).build();

		when(propertiesManager.getEgovCommonMastersServiceHost()).thenReturn("http://egov-micro-dev.egovernments.org");
		when(propertiesManager.getEgovCommonMastersServiceBasepath()).thenReturn("/egov-common-masters");
		when(propertiesManager.getEgovCommonMastersServiceDepartmentSearchPath()).thenReturn("/departments/_search");

		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		URI uri = null;
		try {
			uri = new URI(
					"http://egov-micro-dev.egovernments.org/egov-common-masters/departments/_search?id=100&tenantId=1");
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		when(restTemplate.postForObject(uri, getRequestInfoAsHttpEntity(requestInfoWrapper), DepartmentResponse.class))
				.thenReturn(departmentResponse);

		Department insertedDepartment = commonMasterService.getDepartment(100L, "1", new RequestInfoWrapper());
		assertEquals(insertedDepartment, department);
	}

	private Object getRequestInfoAsHttpEntity(RequestInfoWrapper requestInfoWrapper) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<RequestInfoWrapper> httpEntityRequest = new HttpEntity<RequestInfoWrapper>(requestInfoWrapper,
				headers);
		return httpEntityRequest;
	}
}
