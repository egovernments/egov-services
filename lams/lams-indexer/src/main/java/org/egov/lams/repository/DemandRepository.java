package org.egov.lams.repository;


import org.egov.lams.config.PropertiesManager;
import org.egov.lams.contract.DemandResponse;
import org.egov.lams.contract.RequestInfo;
import org.egov.lams.model.Demand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class DemandRepository {
	
	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private PropertiesManager propertiesManager;
	
	//private static final log log = log.getlog(DemandRepository.class);
	
	public Demand getDemandBySearch(String demandID, String tenantId) {

		String url = propertiesManager.getDemandServiceHostName() + propertiesManager.getDemandSearchServicepath()
				+ "?demandId=" +demandID;
		log.info("the url of demand search API call ::: is " + url);
		log.info("search for demand with id :" +demandID);

		DemandResponse demandResponse = null;
		try {
			demandResponse = restTemplate.postForObject(url, new RequestInfo(), DemandResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
			log.info("the exception thrown from demand search api call ::: " + e);
		}
		log.info("the response form demand search api call ::: " + demandResponse);
		return demandResponse.getDemands().get(0);
	}
	
}
