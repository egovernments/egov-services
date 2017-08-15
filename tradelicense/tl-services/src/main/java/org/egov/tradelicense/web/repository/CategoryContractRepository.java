package org.egov.tradelicense.web.repository;

import java.util.Date;

import org.egov.tl.commons.web.requests.CategoryResponse;
import org.egov.tradelicense.common.config.PropertiesManager;
import org.egov.tl.commons.web.requests.RequestInfoWrapper;
import org.egov.tradelicense.domain.model.TradeLicense;
import org.egov.tradelicense.web.requests.TlMasterRequestInfo;
import org.egov.tradelicense.web.requests.TlMasterRequestInfoWrapper;
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

	public CategoryResponse findByCategoryId(TradeLicense tradeLicense, RequestInfoWrapper requestInfoWrapper) {
		String hostUrl = propertiesManger.getTradeLicenseMasterServiceHostName() + propertiesManger.getTradeLicenseMasterServiceBasePath();
		String searchUrl = propertiesManger.getCategoryServiceSearchPath();
		String url = String.format("%s%s", hostUrl, searchUrl);
		StringBuffer content = new StringBuffer();
		if (tradeLicense.getCategoryId() != null) {
			content.append("ids=" + Long.valueOf(tradeLicense.getCategoryId()));
		}

		if (tradeLicense.getTenantId() != null) {
			content.append("&tenantId=" + tradeLicense.getTenantId());
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

	public CategoryResponse findBySubCategoryId(TradeLicense tradeLicense, RequestInfoWrapper requestInfoWrapper) {
		String hostUrl = propertiesManger.getTradeLicenseMasterServiceHostName() + propertiesManger.getTradeLicenseMasterServiceBasePath();
		String searchUrl = propertiesManger.getCategoryServiceSearchPath();
		String url = String.format("%s%s", hostUrl, searchUrl);
		StringBuffer content = new StringBuffer();
		if (tradeLicense.getSubCategoryId() != null) {
			content.append("ids=" + Long.valueOf(tradeLicense.getSubCategoryId()));
		}

		if (tradeLicense.getTenantId() != null) {
			content.append("&tenantId=" + tradeLicense.getTenantId());
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

	public CategoryResponse findBySubCategoryUomId(TradeLicense tradeLicense, RequestInfoWrapper requestInfoWrapper) {
		String hostUrl = propertiesManger.getTradeLicenseMasterServiceHostName() + propertiesManger.getTradeLicenseMasterServiceBasePath();
		String searchUrl = propertiesManger.getCategoryServiceSearchPath();
		String url = String.format("%s%s", hostUrl, searchUrl);
		StringBuffer content = new StringBuffer();
		if (tradeLicense.getSubCategoryId() != null) {
			content.append("ids=" + Long.valueOf(tradeLicense.getSubCategoryId()));
		}

		if (tradeLicense.getTenantId() != null) {
			content.append("&tenantId=" + tradeLicense.getTenantId());
		}

		if (tradeLicense.getTenantId() != null) {
			content.append("&type=subcategoy");
		}

		if (tradeLicense.getTenantId() != null) {
			content.append("&businessNature=" + tradeLicense.getTradeType());
		}
		TlMasterRequestInfoWrapper tlMasterRequestInfoWrapper = getTlMasterRequestInfoWrapper(requestInfoWrapper);
		url = url + content.toString();
		CategoryResponse categoryResponse = null;

		try {
			categoryResponse = restTemplate.postForObject(url, tlMasterRequestInfoWrapper, CategoryResponse.class);
		} catch (Exception e) {
			log.error("Error while connecting to the category end point");
		}

		if (categoryResponse.getCategories() != null && categoryResponse.getCategories().size() > 0) {
			return categoryResponse;
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