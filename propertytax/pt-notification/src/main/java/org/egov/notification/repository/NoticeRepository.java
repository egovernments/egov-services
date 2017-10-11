package org.egov.notification.repository;

import org.egov.models.NoticeSearchResponse;
import org.egov.notification.config.PropertiesManager;
import org.egov.tracer.http.LogAwareRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class NoticeRepository {
	
	@Autowired
	private LogAwareRestTemplate restTemplate;

	
	@Autowired
	PropertiesManager propertiesManager;
	
	/**
	 * 
	 * 
	 * @param tenantId
	 * @param applicationNo
	 * @param documentType
	 * @throws Exception
	 * @return 
	 */
	public String getfileStoreId(String tenantId, String applicationNo, String noticeType) throws Exception{

		String hostUrl = propertiesManager.getPropertyHostName() + propertiesManager.getPropertyBasePath();
		String searchUrl = propertiesManager.getSearchNotice();
		String url = String.format("%s%s", hostUrl, searchUrl);
		
		StringBuffer content = new StringBuffer();
		if (tenantId != null) {
			content.append("tenantId=" + tenantId);
		}
		if (applicationNo != null) {
			content.append("&applicationNo=" + applicationNo);
		}
		content.append("&noticeType=" + noticeType);
		url = url + content.toString();		
		NoticeSearchResponse noticeSearchResponse = null;
		try {

			noticeSearchResponse = restTemplate.postForObject(url, null, NoticeSearchResponse.class);
		} catch (Exception e) {

			log.error("Error connecting to Notice document end point :" + url);
		}
		
		if (noticeSearchResponse != null && noticeSearchResponse.getNotices() != null
				&& noticeSearchResponse.getNotices().size() > 0) {

			return noticeSearchResponse.getNotices().get(0).getFileStoreId();

		} else {

			return null;
		}
	}	
}
