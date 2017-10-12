package org.egov.tradelicense.web.repository;

import java.util.Date;

import org.egov.tl.commons.web.requests.RequestInfoWrapper;
import org.egov.tl.commons.web.response.LicenseStatusResponse;
import org.egov.tradelicense.common.config.PropertiesManager;
import org.egov.tradelicense.web.requests.TlMasterRequestInfo;
import org.egov.tradelicense.web.requests.TlMasterRequestInfoWrapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class StatusRepository {

	private RestTemplate restTemplate;

	@Autowired
	private PropertiesManager propertiesManger;

	public StatusRepository(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;

	}

	public LicenseStatusResponse findByCodes(String tenantId, String codes, RequestInfoWrapper requestInfoWrapper) {

		String hostUrl = propertiesManger.getTradeLicenseMasterServiceHostName()
				+ propertiesManger.getTradeLicenseMasterServiceBasePath();
		String searchUrl = propertiesManger.getStatusServiceSearchPath();
		String url = String.format("%s%s", hostUrl, searchUrl);
		StringBuffer content = new StringBuffer();
		
		if (codes != null && !codes.isEmpty()) {
			content.append("codes=" + codes);
		}

		if (tenantId != null && !tenantId.isEmpty()) {
			content.append("&tenantId=" + tenantId);
		}
		url = url + content.toString();
		TlMasterRequestInfoWrapper tlMasterRequestInfoWrapper = getRequestInfoWrapper(requestInfoWrapper);
		LicenseStatusResponse licenseStatusResponse = null;

		try {

			licenseStatusResponse = restTemplate.postForObject(url, tlMasterRequestInfoWrapper,
					LicenseStatusResponse.class);
		} catch (Exception e) {
			log.error(propertiesManger.getCatEndPointError());
		}
		if (licenseStatusResponse != null && licenseStatusResponse.getLicenseStatuses() != null
				&& licenseStatusResponse.getLicenseStatuses().size() > 0) {
			return licenseStatusResponse;
		} else {
			return null;
		}

	}
	
	public LicenseStatusResponse findByModuleTypeAndCode(String tenantId, String moduleType,String code, RequestInfoWrapper requestInfoWrapper) {

		String hostUrl = propertiesManger.getTradeLicenseMasterServiceHostName()
				+ propertiesManger.getTradeLicenseMasterServiceBasePath();
		String searchUrl = propertiesManger.getStatusServiceSearchPath();
		String url = String.format("%s%s", hostUrl, searchUrl);
		StringBuffer content = new StringBuffer();
		
		if (moduleType != null && !moduleType.isEmpty()) {
			content.append("moduleType=" + moduleType);
		}
		
		if (code != null && !code.isEmpty()) {
			content.append("&codes=" + code);
		}

		if (tenantId != null && !tenantId.isEmpty()) {
			content.append("&tenantId=" + tenantId);
		}
		url = url + content.toString();
		TlMasterRequestInfoWrapper tlMasterRequestInfoWrapper = getRequestInfoWrapper(requestInfoWrapper);
		LicenseStatusResponse licenseStatusResponse = null;

		try {

			licenseStatusResponse = restTemplate.postForObject(url, tlMasterRequestInfoWrapper,
					LicenseStatusResponse.class);
		} catch (Exception e) {
			log.error(propertiesManger.getCatEndPointError());
		}
		if (licenseStatusResponse != null && licenseStatusResponse.getLicenseStatuses() != null
				&& licenseStatusResponse.getLicenseStatuses().size() > 0) {
			return licenseStatusResponse;
		} else {
			return null;
		}

	}

	public TlMasterRequestInfoWrapper getRequestInfoWrapper(RequestInfoWrapper requestInfoWrapper) {

		TlMasterRequestInfoWrapper tlMasterRequestInfoWrapper = new TlMasterRequestInfoWrapper();
		TlMasterRequestInfo tlMasterRequestInfo = new TlMasterRequestInfo();
		ModelMapper mapper = new ModelMapper();
		mapper.map(requestInfoWrapper.getRequestInfo(), tlMasterRequestInfo);
		tlMasterRequestInfo.setTs(new Date().getTime());
		tlMasterRequestInfoWrapper.setRequestInfo(tlMasterRequestInfo);

		return tlMasterRequestInfoWrapper;
	}

}