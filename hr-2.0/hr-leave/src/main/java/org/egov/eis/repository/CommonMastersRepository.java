package org.egov.eis.repository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.eis.web.contract.CalendarYearResponse;
import org.egov.eis.web.contract.Holiday;
import org.egov.eis.web.contract.HolidayResponse;
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
		final String url = commonMastersServiceHost + "egov-common-masters/calendaryears/_search?tenantId=" + tenantId
				+ "&pageSize=500";
		return restTemplate.postForObject(url, requestInfo, CalendarYearResponse.class);
	}

	public List<Holiday> getHolidayByDate(RequestInfo requestInfo, Date applicableOn, String tenantId) {
		final String url = commonMastersServiceHost + "/egov-common-masters/holidays/_search?tenantId=" + tenantId;
		final StringBuilder searchURL = new StringBuilder(url);

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String fromDate = simpleDateFormat.format(applicableOn);
		searchURL.append("&applicableOn=" + fromDate);
		final HolidayResponse holidayResponse = restTemplate.postForObject(searchURL.toString(), requestInfo,
				HolidayResponse.class);

		return holidayResponse.getHoliday();

	}

	public List<Holiday> getHolidayByDateRange(RequestInfo requestInfo, Date fromDate, Date toDate, String tenantId) {
		final String url = commonMastersServiceHost + "/egov-common-masters/holidays/_search?tenantId=" + tenantId;
		final StringBuilder searchURL = new StringBuilder(url);

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String fromdate = simpleDateFormat.format(fromDate);
		searchURL.append("&fromDate=" + fromdate);
		String todate = simpleDateFormat.format(toDate);
		searchURL.append("&toDate=" + todate);
		final HolidayResponse holidayResponse = restTemplate.postForObject(searchURL.toString(), requestInfo,
				HolidayResponse.class);

		return holidayResponse.getHoliday();

	}

}