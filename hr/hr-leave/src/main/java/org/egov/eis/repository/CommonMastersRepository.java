package org.egov.eis.repository;

import org.egov.eis.web.contract.CalendarYearResponse;
import org.egov.eis.web.contract.RequestInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CommonMastersRepository {

	private final RestTemplate restTemplate;
	private final String commonMastersServiceHost;

	public CommonMastersRepository(final RestTemplate restTemplate,
			@Value("${egov.services.common_masters.host}") final String commonMastersServiceHost) {
		this.restTemplate = restTemplate;
		this.commonMastersServiceHost = commonMastersServiceHost;
	}

	public CalendarYearResponse getCalendaryears(RequestInfo requestInfo, String tenantId) {
		final String url = commonMastersServiceHost + "/egov-common-masters/calendaryears/_search?tenantId=" + tenantId
				+ "&pageSize=500";
		return restTemplate.postForObject(url, requestInfo, CalendarYearResponse.class);
	}

}