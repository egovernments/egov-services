package org.egov.tradelicense.web.repository;

import java.util.Date;

import org.egov.tl.commons.web.requests.RequestInfoWrapper;
import org.egov.tl.commons.web.response.CategorySearchResponse;
import org.egov.tl.commons.web.response.LicenseStatusResponse;
import org.egov.tl.commons.web.response.UOMResponse;
import org.egov.tradelicense.common.config.PropertiesManager;
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

	public CategorySearchResponse findByCategoryCode(TradeLicense tradeLicense, RequestInfoWrapper requestInfoWrapper) {
		
		String hostUrl = propertiesManger.getTradeLicenseMasterServiceHostName()
				+ propertiesManger.getTradeLicenseMasterServiceBasePath();
		String searchUrl = propertiesManger.getCategoryServiceSearchPath();
		String url = String.format("%s%s", hostUrl, searchUrl);
		StringBuffer content = new StringBuffer();
		
		if (tradeLicense.getCategory() != null && !tradeLicense.getCategory().isEmpty()) {
			content.append("codes=" + tradeLicense.getCategory());
		}

		if (tradeLicense.getTenantId() != null && !tradeLicense.getTenantId().isEmpty()) {
			content.append("&tenantId=" + tradeLicense.getTenantId());
		}
		url = url + content.toString();
		TlMasterRequestInfoWrapper tlMasterRequestInfoWrapper = getTlMasterRequestInfoWrapper(requestInfoWrapper);
		CategorySearchResponse categoryResponse = null;

		try {

			categoryResponse = restTemplate.postForObject(url, tlMasterRequestInfoWrapper,
					CategorySearchResponse.class);
		} catch (Exception e) {
			log.error(propertiesManger.getCatEndPointError());
		}
		if (categoryResponse != null && categoryResponse.getCategories() != null
				&& categoryResponse.getCategories().size() > 0) {
			return categoryResponse;
		} else {
			return null;
		}

	}

	public CategorySearchResponse findBySubCategoryCode(TradeLicense tradeLicense,
			RequestInfoWrapper requestInfoWrapper) {
		
		String hostUrl = propertiesManger.getTradeLicenseMasterServiceHostName()
				+ propertiesManger.getTradeLicenseMasterServiceBasePath();
		String searchUrl = propertiesManger.getCategoryServiceSearchPath();
		String url = String.format("%s%s", hostUrl, searchUrl);
		StringBuffer content = new StringBuffer();
		
		if (tradeLicense.getSubCategory() != null && !tradeLicense.getSubCategory().isEmpty()) {
			content.append("codes=" + tradeLicense.getSubCategory());
			content.append("&type=SUBCATEGORY");
		}

		if (tradeLicense.getTenantId() != null && !tradeLicense.getTenantId().isEmpty()) {
			content.append("&tenantId=" + tradeLicense.getTenantId());
		}
		
		url = url + content.toString();
		TlMasterRequestInfoWrapper tlMasterRequestInfoWrapper = getTlMasterRequestInfoWrapper(requestInfoWrapper);
		CategorySearchResponse categoryResponse = null;

		try {

			categoryResponse = restTemplate.postForObject(url, tlMasterRequestInfoWrapper,
					CategorySearchResponse.class);
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

	public CategorySearchResponse findBySubCategoryUomCode(TradeLicense tradeLicense,
			RequestInfoWrapper requestInfoWrapper) {
		
		String hostUrl = propertiesManger.getTradeLicenseMasterServiceHostName()
				+ propertiesManger.getTradeLicenseMasterServiceBasePath();
		String searchUrl = propertiesManger.getCategoryServiceSearchPath();
		String url = String.format("%s%s", hostUrl, searchUrl);
		StringBuffer content = new StringBuffer();
		
		if (tradeLicense.getSubCategory() != null && !tradeLicense.getSubCategory().isEmpty()) {
			content.append("codes=" + tradeLicense.getSubCategory());
		}

		if (tradeLicense.getTenantId() != null && !tradeLicense.getTenantId().isEmpty()) {
			content.append("&tenantId=" + tradeLicense.getTenantId());
			content.append("&type=SUBCATEGORY");
		}

		// if (tradeLicense.getTenantId() != null) {
		// content.append("&businessNature=" + tradeLicense.getTradeType());
		// }

		TlMasterRequestInfoWrapper tlMasterRequestInfoWrapper = getTlMasterRequestInfoWrapper(requestInfoWrapper);
		url = url + content.toString();
		CategorySearchResponse categoryResponse = null;

		try {
			categoryResponse = restTemplate.postForObject(url, tlMasterRequestInfoWrapper,
					CategorySearchResponse.class);
		} catch (Exception e) {
			log.error(propertiesManger.getCatEndPointError());
		}

		if (categoryResponse.getCategories() != null && categoryResponse.getCategories().size() > 0) {
			return categoryResponse;
		} else {
			return null;
		}

	}

	public CategorySearchResponse findByCategoryCodes(String tenantId, String codes,
			RequestInfoWrapper requestInfoWrapper) {
		
		String hostUrl = propertiesManger.getTradeLicenseMasterServiceHostName()
				+ propertiesManger.getTradeLicenseMasterServiceBasePath();
		String searchUrl = propertiesManger.getCategoryServiceSearchPath();
		String url = String.format("%s%s", hostUrl, searchUrl);
		StringBuffer content = new StringBuffer();
		
		if (codes != null && !codes.isEmpty()) {
			content.append("codes=" + codes);
		}

		if (tenantId != null && !tenantId.isEmpty()) {
			content.append("&tenantId=" + tenantId);
		}
		url = url + content.toString();
		TlMasterRequestInfoWrapper tlMasterRequestInfoWrapper = getTlMasterRequestInfoWrapper(requestInfoWrapper);
		CategorySearchResponse categoryResponse = null;

		try {

			categoryResponse = restTemplate.postForObject(url, tlMasterRequestInfoWrapper,
					CategorySearchResponse.class);
		} catch (Exception e) {
			log.error(propertiesManger.getCatEndPointError());
		}
		if (categoryResponse != null && categoryResponse.getCategories() != null
				&& categoryResponse.getCategories().size() > 0) {
			return categoryResponse;
		} else {
			return null;
		}

	}
	
	public CategorySearchResponse findBySubCategoryCodes(String tenantId, String codes,
			RequestInfoWrapper requestInfoWrapper) {
		
		String hostUrl = propertiesManger.getTradeLicenseMasterServiceHostName()
				+ propertiesManger.getTradeLicenseMasterServiceBasePath();
		String searchUrl = propertiesManger.getCategoryServiceSearchPath();
		String url = String.format("%s%s", hostUrl, searchUrl);
		StringBuffer content = new StringBuffer();
		
		if (codes != null && !codes.isEmpty()) {
			content.append("codes=" + codes);
			content.append("&type=SUBCATEGORY");
		}

		if (tenantId != null && !tenantId.isEmpty()) {
			content.append("&tenantId=" + tenantId);
		}
		url = url + content.toString();
		TlMasterRequestInfoWrapper tlMasterRequestInfoWrapper = getTlMasterRequestInfoWrapper(requestInfoWrapper);
		CategorySearchResponse categoryResponse = null;

		try {

			categoryResponse = restTemplate.postForObject(url, tlMasterRequestInfoWrapper,
					CategorySearchResponse.class);
		} catch (Exception e) {
			log.error(propertiesManger.getCatEndPointError());
		}
		if (categoryResponse != null && categoryResponse.getCategories() != null
				&& categoryResponse.getCategories().size() > 0) {
			return categoryResponse;
		} else {
			return null;
		}

	}

	public UOMResponse findByUomCodes(String tenantId, String codes, RequestInfoWrapper requestInfoWrapper) {

		String hostUrl = propertiesManger.getTradeLicenseMasterServiceHostName()
				+ propertiesManger.getTradeLicenseMasterServiceBasePath();
		String searchUrl = propertiesManger.getUomServiceSearchPath();
		String url = String.format("%s%s", hostUrl, searchUrl);
		StringBuffer content = new StringBuffer();
		
		if (codes != null && !codes.isEmpty()) {
			content.append("codes=" + codes);
		}

		if (tenantId != null && !tenantId.isEmpty()) {
			content.append("&tenantId=" + tenantId);
		}
		url = url + content.toString();
		TlMasterRequestInfoWrapper tlMasterRequestInfoWrapper = getTlMasterRequestInfoWrapper(requestInfoWrapper);
		UOMResponse uomResponse = null;

		try {

			uomResponse = restTemplate.postForObject(url, tlMasterRequestInfoWrapper, UOMResponse.class);
		} catch (Exception e) {
			log.error(propertiesManger.getCatEndPointError());
		}
		if (uomResponse != null && uomResponse.getUoms() != null && uomResponse.getUoms().size() > 0) {
			return uomResponse;
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