package org.egov.wcms.service;

import org.egov.common.contract.request.RequestInfo;
import org.egov.wcms.repository.WCRepository;
import org.egov.wcms.util.WCServiceUtils;
import org.egov.wcms.util.WaterConnectionConstants;
import org.egov.wcms.web.models.SearcherRequest;
import org.egov.wcms.web.models.WaterConnectionRes;
import org.egov.wcms.web.models.WaterConnectionSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class WaterConnectionService {
	
	@Autowired
	private WCServiceUtils wCServiceUtils;
	
	@Autowired
	private WCRepository wcRepository;
	
	public WaterConnectionRes getWaterConnections(WaterConnectionSearchCriteria WaterConnectionSearchCriteria, RequestInfo requestInfo) {
		StringBuilder uri = new StringBuilder();
		Object response = null;
		WaterConnectionRes waterConnectionRes = null;
		ObjectMapper mapper = wCServiceUtils.getObjectMapper();
		SearcherRequest searcherRequest = wCServiceUtils.getSearcherRequest(uri, WaterConnectionSearchCriteria, requestInfo, WaterConnectionConstants.SEARCHER_WC_SEARCH_DEF_NAME);
		try {
			response = wcRepository.fetchResult(uri, searcherRequest);
			if(null == response) {
				return wCServiceUtils.getDefaultWaterConnectionResponse(requestInfo);
			}
			waterConnectionRes = mapper.convertValue(response, WaterConnectionRes.class);
		}catch(Exception e) {
			return wCServiceUtils.getDefaultWaterConnectionResponse(requestInfo);
		}
		return waterConnectionRes;
	}

}
