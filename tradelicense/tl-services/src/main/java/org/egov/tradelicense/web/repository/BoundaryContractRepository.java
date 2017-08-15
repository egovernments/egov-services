package org.egov.tradelicense.web.repository;

import org.egov.tl.commons.web.requests.RequestInfoWrapper;
import org.egov.tradelicense.common.config.PropertiesManager;
import org.egov.tradelicense.domain.model.TradeLicense;
import org.egov.tradelicense.web.requests.BoundaryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BoundaryContractRepository {

	private RestTemplate restTemplate;
	private String hostUrl;
	private String searchUrl ;
	
	@Autowired
	private PropertiesManager propertiesManger;

	public BoundaryContractRepository(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
		this.hostUrl = propertiesManger.getLocationServiceHostName() + propertiesManger.getLocationServiceBasePath();
		this.searchUrl = propertiesManger.getLocationServiceSearchPath();
	}

	public BoundaryResponse findByLocalityId(TradeLicense tradeLicense, RequestInfoWrapper requestInfoWrapper) {

		String url = String.format("%s%s", hostUrl, searchUrl);
		StringBuffer content = new StringBuffer();
		if (tradeLicense.getLocalityId() != null) {
			content.append("boundaryIds=" + tradeLicense.getLocalityId());
		}

		if (tradeLicense.getTenantId() != null) {
			content.append("&tenantId=" + tradeLicense.getTenantId());
		}
		url = url + content.toString();
		BoundaryResponse boundaryResponse = null;
		try {

			boundaryResponse = restTemplate.postForObject(url, requestInfoWrapper, BoundaryResponse.class);

		} catch (Exception e) {
			log.error("Error while connecting to the location end point");
		}

		if (boundaryResponse != null && boundaryResponse.getBoundarys() != null
				&& boundaryResponse.getBoundarys().size() > 0) {
			return boundaryResponse;
		} else {
			return null;
		}

	}

	public BoundaryResponse findByRevenueWardId(TradeLicense tradeLicense, RequestInfoWrapper requestInfoWrapper) {

		String url = String.format("%s%s", hostUrl, searchUrl);
		StringBuffer content = new StringBuffer();
		if (tradeLicense.getRevenueWardId() != null) {
			content.append("boundaryIds=" + tradeLicense.getRevenueWardId());
		}

		if (tradeLicense.getTenantId() != null) {
			content.append("&tenantId=" + tradeLicense.getTenantId());
		}
		url = url + content.toString();
		BoundaryResponse boundaryResponse = null;
		try {

			boundaryResponse = restTemplate.postForObject(url, requestInfoWrapper, BoundaryResponse.class);

		} catch (Exception e) {
			log.error("Error while connecting to the location end point");
		}

		if (boundaryResponse != null && boundaryResponse.getBoundarys() != null
				&& boundaryResponse.getBoundarys().size() > 0) {
			return boundaryResponse;
		} else {
			return null;
		}

	}
	public BoundaryResponse findByAdminWardId(TradeLicense tradeLicense, RequestInfoWrapper requestInfoWrapper) {

		String url = String.format("%s%s", hostUrl, searchUrl);
		StringBuffer content = new StringBuffer();
		if (tradeLicense.getRevenueWardId() != null) {
			content.append("boundaryIds=" + tradeLicense.getAdminWardId());
		}

		if (tradeLicense.getTenantId() != null) {
			content.append("&tenantId=" + tradeLicense.getTenantId());
		}
		url = url + content.toString();
		BoundaryResponse boundaryResponse = null;
		try {

			boundaryResponse = restTemplate.postForObject(url, requestInfoWrapper, BoundaryResponse.class);

		} catch (Exception e) {
			log.error("Error while connecting to the location end point");
		}

		if (boundaryResponse != null && boundaryResponse.getBoundarys() != null
				&& boundaryResponse.getBoundarys().size() > 0) {
			return boundaryResponse;
		} else {
			return null;
		}

	}
}