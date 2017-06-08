package org.egov.lams.repository;

import java.util.ArrayList;
import java.util.List;

import org.egov.lams.config.PropertiesManager;
import org.egov.lams.contract.AllotteeResponse;
import org.egov.lams.contract.RequestInfo;
import org.egov.lams.contract.UserSearchRequest;
import org.egov.lams.model.Allottee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AllotteeRepository {

	public static final Logger LOGGER = LoggerFactory.getLogger(AllotteeRepository.class);

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private PropertiesManager propertiesManager;

	public Allottee getAllottee(Long allotteeId,String tenantId,RequestInfo requestInfo) {

		String url = propertiesManager.getAllotteeApiHostUrl()
					+ propertiesManager.getAllotteeApiSearchPath();
		UserSearchRequest searchRequest = new UserSearchRequest();
		List<Long> ids = new ArrayList<>();
		ids.add(allotteeId);
		searchRequest.setId(ids);
		searchRequest.setTenantId(tenantId);
		searchRequest.setRequestInfo(requestInfo);
		
		LOGGER.info("ALLOTTEE API URL FROM LAMSINDEXER : "+url);
		AllotteeResponse allotteeResponse = null;
		try {
			allotteeResponse = restTemplate.postForObject(url,searchRequest, AllotteeResponse.class);
		} catch (Exception e) {
			LOGGER.info(e.getMessage(), e);
			throw e;// FIXME throw custom exception
		}
		return allotteeResponse.getAllottees().get(0);
	}
}
