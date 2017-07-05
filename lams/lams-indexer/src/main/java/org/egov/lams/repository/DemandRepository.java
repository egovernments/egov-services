package org.egov.lams.repository;


import org.egov.lams.config.PropertiesManager;
import org.egov.lams.contract.DemandResponse;
import org.egov.lams.contract.RequestInfo;
import org.egov.lams.model.Demand;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

@Repository
public class DemandRepository {
	
	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private PropertiesManager propertiesManager;
	
	private static final Logger LOGGER = Logger.getLogger(DemandRepository.class);
	
	public Demand getDemandBySearch(String demandID, String tenantId) {

		String url = propertiesManager.getDemandServiceHostName() + propertiesManager.getDemandSearchServicepath()
				+ "?demandId=" +demandID +"&tenantId=" + tenantId;
		LOGGER.info("the url of demand search API call ::: is " + url);

		DemandResponse demandResponse = null;
		try {
			demandResponse = restTemplate.postForObject(url, new RequestInfo(), DemandResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("the exception thrown from demand search api call ::: " + e);
		}
		LOGGER.info("the response form demand search api call ::: " + demandResponse);
		return demandResponse.getDemands().get(0);
	}
	
}
