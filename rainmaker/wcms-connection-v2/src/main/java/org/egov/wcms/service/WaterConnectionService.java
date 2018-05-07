package org.egov.wcms.service;

import java.util.UUID;

import org.egov.common.contract.request.RequestInfo;
import org.egov.wcms.config.WaterConnectionConfig;
import org.egov.wcms.producer.Producer;
import org.egov.wcms.repository.Repository;
import org.egov.wcms.util.ResponseInfoFactory;
import org.egov.wcms.util.WaterConnectionServiceUtils;
import org.egov.wcms.util.WaterConnectionConstants;
import org.egov.wcms.web.models.AuditDetails;
import org.egov.wcms.web.models.Connection;
import org.egov.wcms.web.models.SearcherRequest;
import org.egov.wcms.web.models.WaterConnectionReq;
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
	private WaterConnectionServiceUtils wCServiceUtils;
	
	@Autowired
	private Repository wcRepository;
	
	@Autowired
	private Producer waterConnectionProducer;
	
	@Autowired
	private WaterConnectionConfig mainConfiguration;
	
	@Autowired
	private ResponseInfoFactory responseInfoFactory;
	
	/**
	 * Searches Water Connections based on the criteria in WaterConnectionSearchCriteria.
	 * 
	 * @param WaterConnectionSearchCriteria
	 * @param requestInfo
	 * @return WaterConnectionRes
	 */
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
			log.error("Exception: " + e);
			return wCServiceUtils.getDefaultWaterConnectionResponse(requestInfo);
		}
		return waterConnectionRes;
	}
	
	/**
	 * Creates water connection in the postgres db through persister and pushes the same to elasticsearch through indexer. Both consumers pick from the same topic.
	 * @param connections
	 * @return WaterConnectionRes
	 */
	public WaterConnectionRes createWaterConnections(WaterConnectionReq connections) {
		
		enrichCreateRequest(connections);
		waterConnectionProducer.push(mainConfiguration.getSaveWaterConnectionTopic(), connections);
		return WaterConnectionRes.builder().connections(connections.getConnections())
				.responseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(connections.getRequestInfo(), true))
				.build();
	}
	
	/**
	 * Request enrichment for create flow based on the following use-cases:
	 * 1. Generate ids for each request using idGen. 
	 * @param connections
	 */
	public void enrichCreateRequest(WaterConnectionReq connections) {
		// (1)
		RequestInfo requestInfo = connections.getRequestInfo();
		AuditDetails auditDetails = wCServiceUtils.getAuditDetails(requestInfo.getUserInfo().getId().toString(), true);
		
		for(Connection connection: connections.getConnections()) {
			connection.setConnectionNumber(wCServiceUtils.generateConnectonNumber());
			connection.setId(UUID.randomUUID().toString());
			connection.getMeter().setId(UUID.randomUUID().toString());
			connection.getAddress().setId(UUID.randomUUID().toString());
			connection.setAuditDetails(auditDetails);
		}
	}
	
	/**
	 * Updates the water connection in postgres db through persister and pushes the same to elasticsearch through indexer. Both consumers pick from the same topic.
	 * @param connections
	 * @return
	 */
	public WaterConnectionRes updateWaterConnections(WaterConnectionReq connections) {
		enrichUpdateRequest(connections);
		waterConnectionProducer.push(mainConfiguration.getUpdateWaterConnectionTopic(), connections);
		return WaterConnectionRes.builder().connections(connections.getConnections()).
				responseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(connections.getRequestInfo(), true)).build();	
	}

	/**
	 * Request enrichment for update flow based on the following use-cases:
	 * 
	 * @param connections
	 */
	public void enrichUpdateRequest(WaterConnectionReq connections) {
		
	}

}
