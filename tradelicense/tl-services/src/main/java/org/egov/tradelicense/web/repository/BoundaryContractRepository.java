package org.egov.tradelicense.web.repository;

import org.egov.tradelicense.common.domain.model.RequestInfoWrapper;
import org.egov.tradelicense.domain.model.TradeLicense;
import org.egov.tradelicense.web.requests.BoundaryResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BoundaryContractRepository {

	private RestTemplate restTemplate;
	private String hostUrl;
	public static final String SEARCH_URL = "egov-location/boundarys/_search?";

	public BoundaryContractRepository(@Value("${egov.services.egov-location.hostname}") String hostUrl,
			RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
		this.hostUrl = hostUrl;
	}

	public BoundaryResponse findByLocalityId(TradeLicense tradeLicense, RequestInfoWrapper requestInfoWrapper) {

		String url = String.format("%s%s", hostUrl, SEARCH_URL);
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

		String url = String.format("%s%s", hostUrl, SEARCH_URL);
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
}