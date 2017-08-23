package org.egov.tl.indexer.web.repository;

import java.util.Date;

import org.egov.tl.commons.web.requests.RequestInfoWrapper;
import org.egov.tl.commons.web.response.CategoryResponse;
import org.egov.tl.commons.web.response.LicenseStatusResponse;
import org.egov.tl.commons.web.response.UOMResponse;
import org.egov.tl.indexer.config.PropertiesManager;
import org.egov.tl.indexer.web.requests.TlMasterRequestInfo;
import org.egov.tl.indexer.web.requests.TlMasterRequestInfoWrapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CategoryContractRepository {

	private RestTemplate restTemplate;

	@Autowired
	private PropertiesManager propertiesManger;

	public CategoryContractRepository(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;

	}

	public CategoryResponse findByCategoryIds(String tenantId, String ids, RequestInfoWrapper requestInfoWrapper) {
		String hostUrl = propertiesManger.getTradeLicenseMasterServiceHostName()
				+ propertiesManger.getTradeLicenseMasterServiceBasePath();
		String searchUrl = propertiesManger.getCategoryServiceSearchPath();
		String url = String.format("%s%s", hostUrl, searchUrl);
		StringBuffer content = new StringBuffer();
		if (ids != null) {
			content.append("ids=" + ids);
		}

		if (tenantId != null) {
			content.append("&tenantId=" + tenantId);
		}
		url = url + content.toString();
		TlMasterRequestInfoWrapper tlMasterRequestInfoWrapper = getTlMasterRequestInfoWrapper(requestInfoWrapper);
		CategoryResponse categoryResponse = null;

		try {

			categoryResponse = restTemplate.postForObject(url, tlMasterRequestInfoWrapper, CategoryResponse.class);
		} catch (Exception e) {
			log.error("Error while connecting to the category end point");
		}
		if (categoryResponse != null && categoryResponse.getCategories() != null
				&& categoryResponse.getCategories().size() > 0) {
			return categoryResponse;
		} else {
			return null;
		}

	}

	public UOMResponse findByUomIds(String tenantId, String ids, RequestInfoWrapper requestInfoWrapper) {

		String hostUrl = propertiesManger.getTradeLicenseMasterServiceHostName()
				+ propertiesManger.getTradeLicenseMasterServiceBasePath();
		String searchUrl = propertiesManger.getUomServiceSearchPath();
		String url = String.format("%s%s", hostUrl, searchUrl);
		StringBuffer content = new StringBuffer();
		if (ids != null) {
			content.append("ids=" + ids);
		}

		if (tenantId != null) {
			content.append("&tenantId=" + tenantId);
		}
		url = url + content.toString();
		TlMasterRequestInfoWrapper tlMasterRequestInfoWrapper = getTlMasterRequestInfoWrapper(requestInfoWrapper);
		UOMResponse uomResponse = null;

		try {

			uomResponse = restTemplate.postForObject(url, tlMasterRequestInfoWrapper, UOMResponse.class);
		} catch (Exception e) {
			log.error("Error while connecting to the category end point");
		}
		if (uomResponse != null && uomResponse.getUoms() != null && uomResponse.getUoms().size() > 0) {
			return uomResponse;
		} else {
			return null;
		}

	}

	public LicenseStatusResponse findByStatusIds(String tenantId, String ids, RequestInfoWrapper requestInfoWrapper) {

		String hostUrl = propertiesManger.getTradeLicenseMasterServiceHostName()
				+ propertiesManger.getTradeLicenseMasterServiceBasePath();
		String searchUrl = propertiesManger.getStatusServiceSearchPath();
		String url = String.format("%s%s", hostUrl, searchUrl);
		StringBuffer content = new StringBuffer();
		if (ids != null) {
			content.append("ids=" + ids);
		}

		if (tenantId != null) {
			content.append("&tenantId=" + tenantId);
		}
		url = url + content.toString();
		TlMasterRequestInfoWrapper tlMasterRequestInfoWrapper = getTlMasterRequestInfoWrapper(requestInfoWrapper);
		LicenseStatusResponse licenseStatusResponse = null;

		try {

			licenseStatusResponse = restTemplate.postForObject(url, tlMasterRequestInfoWrapper,
					LicenseStatusResponse.class);
		} catch (Exception e) {
			log.error("Error while connecting to the category end point");
		}
		if (licenseStatusResponse != null && licenseStatusResponse.getLicenseStatuses() != null
				&& licenseStatusResponse.getLicenseStatuses().size() > 0) {
			return licenseStatusResponse;
		} else {
			return null;
		}

	}

	public TlMasterRequestInfoWrapper getTlMasterRequestInfoWrapper(RequestInfoWrapper requestInfoWrapper) {

		TlMasterRequestInfoWrapper tlMasterRequestInfoWrapper = new TlMasterRequestInfoWrapper();
		TlMasterRequestInfo tlMasterRequestInfo = new TlMasterRequestInfo();
		ModelMapper mapper = new ModelMapper();
		mapper.map(requestInfoWrapper.getRequestInfo(), tlMasterRequestInfo);
		tlMasterRequestInfo.setTs(new Date().getTime());
		tlMasterRequestInfoWrapper.setRequestInfo(tlMasterRequestInfo);

		return tlMasterRequestInfoWrapper;
	}

}