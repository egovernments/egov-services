package org.egov.lams.service;

import org.egov.lams.config.PropertiesManager;
import org.egov.lams.contract.AllotteeResponse;
import org.egov.lams.contract.RequestInfo;
import org.egov.lams.model.Allottee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AllotteeService {

	public static final Logger LOGGER = LoggerFactory.getLogger(AllotteeService.class);

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private PropertiesManager propertiesManager;

	public Allottee getAllottee(Long allotteeId) {

		String url = propertiesManager.getAllotteeApiHostUrl()
					+ propertiesManager.getAllotteeApiSearchPath() + "?"
					+ "id=" + allotteeId;
		LOGGER.info("ALLOTTEE API URL FROM LAMSINDEXER : "+url);
		AllotteeResponse allotteeResponse = null;
		try {
			allotteeResponse = restTemplate.postForObject(url,new RequestInfo(), AllotteeResponse.class);
		} catch (Exception e) {
			LOGGER.info(e.getMessage(), e);
			throw e;// FIXME throw custom exception
		}
		return allotteeResponse.getAllottees().get(0);
	}
}
