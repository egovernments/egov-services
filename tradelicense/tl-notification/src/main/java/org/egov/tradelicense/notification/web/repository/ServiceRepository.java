package org.egov.tradelicense.notification.web.repository;

import org.egov.tl.commons.web.contract.NoticeDocumentSearchResponse;
import org.egov.tl.commons.web.contract.RequestInfo;
import org.egov.tl.commons.web.requests.RequestInfoWrapper;
import org.egov.tradelicense.notification.config.PropertiesManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ServiceRepository {

	private RestTemplate restTemplate;

	@Autowired
	PropertiesManager propertiesManager;

	public ServiceRepository(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;

	}

	public String findByApplicationNumber(String tenantId, String applicationNumber, RequestInfo requestInfo) {

		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);

		String hostUrl = propertiesManager.getTradeLicenseServicesHostName()
				+ propertiesManager.getTradeLicenseServicesBasePath();
		String searchUrl = propertiesManager.getNoticeDocumentServiceSearchPath();

		String url = String.format("%s%s", hostUrl, searchUrl);
		StringBuffer content = new StringBuffer();

		if (tenantId != null) {
			content.append("tenantId=" + tenantId);
		}

		if (applicationNumber != null) {
			content.append("&applicationNumber=" + applicationNumber);
		}

		content.append("&documentType=REJECTION_LETTER");

		url = url + content.toString();
		NoticeDocumentSearchResponse noticeDocumentSearchResponse = null;

		try {

			noticeDocumentSearchResponse = restTemplate.postForObject(url, requestInfoWrapper,
					NoticeDocumentSearchResponse.class);

		} catch (Exception e) {

			log.error("Error connecting to Notice document end point :" + url);
		}

		if (noticeDocumentSearchResponse != null && noticeDocumentSearchResponse.getNoticeDocument() != null
				&& noticeDocumentSearchResponse.getNoticeDocument().size() > 0) {

			return noticeDocumentSearchResponse.getNoticeDocument().get(0).getFileStoreId();

		} else {

			return null;
		}

	}

}