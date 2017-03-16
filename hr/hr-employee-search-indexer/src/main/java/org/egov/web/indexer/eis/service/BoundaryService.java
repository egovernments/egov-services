package org.egov.web.indexer.eis.service;

import java.net.URI;
import java.util.List;

import org.egov.eis.model.Boundary;
import org.egov.web.contract.BoundaryResponse;

import org.egov.web.contract.RequestInfo;
import org.egov.web.indexer.config.PropertiesManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BoundaryService {
	
	@Autowired
	PropertiesManager propertiesManager;
	
	@Autowired
	RestTemplate restTemplate;
	
	public static final Logger logger = LoggerFactory.getLogger(BoundaryService.class);
	
	public static final String BOUNDARY_SEARCH_BY_ID_QUERY_PARAM = "Boundary.id";
	
	public List<Boundary> getBoundary(String ids) {
		URI url = null;
		BoundaryResponse boundaryResponse = null;
		try { //http://egov-micro-dev.egovernments.org/user/_search
			// url = new URI(propertiesManager.getBoundaryServiceHostName() + "?id=" + id);
			url = new URI(propertiesManager.getBoundaryServiceSearchUri() + propertiesManager.getBoundarySearchContextPath() +"?"+ BOUNDARY_SEARCH_BY_ID_QUERY_PARAM + "=" + ids);
			logger.info(url.toString());
			boundaryResponse = restTemplate.postForObject(url, getRequestInfo(), BoundaryResponse.class);
		} catch (Exception e) {
			// FIXME log error to getstacktrace
			return null;
		}
		return boundaryResponse.getBoundarys();
	}
	
	private RequestInfo getRequestInfo() {
		RequestInfo requestInfo = new RequestInfo();
		requestInfo.setApiId("emp");
		requestInfo.setVer("1.0");
		requestInfo.setTs("10/03/2017");
		requestInfo.setAction("create");
		requestInfo.setDid("1");
		requestInfo.setKey("abcdkey");
		requestInfo.setMsgId("20170310130900");
		requestInfo.setRequesterId("rajesh");
		requestInfo.setAuthToken("123");
		
		return requestInfo;
	}
	

}
