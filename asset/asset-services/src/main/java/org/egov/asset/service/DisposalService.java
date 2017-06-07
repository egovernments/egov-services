package org.egov.asset.service;

import java.util.ArrayList;
import java.util.List;

import org.egov.asset.config.ApplicationProperties;
import org.egov.asset.contract.DisposalRequest;
import org.egov.asset.contract.DisposalResponse;
import org.egov.asset.model.Disposal;
import org.egov.asset.model.DisposalCriteria;
import org.egov.asset.producers.AssetProducer;
import org.egov.asset.repository.DisposalRepository;
import org.egov.asset.web.wrapperfactory.ResponseInfoFactory;
import org.egov.common.contract.request.RequestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class DisposalService {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(DisposalRepository.class);
	
	@Autowired
	private DisposalRepository disposalRepository;
	
	@Autowired
	private AssetProducer assetProducer;
	
	@Autowired
	private ApplicationProperties applicationProperties;
	
	@Autowired
	private ResponseInfoFactory responseInfoFactory;
	
	public DisposalResponse search(DisposalCriteria disposalCriteria, RequestInfo requestInfo){
		List<Disposal> disposals = null;
		
		try{
			disposals = disposalRepository.search(disposalCriteria);
		} catch(Exception ex) {
			LOGGER.info("DisposalService:",ex);
			throw new RuntimeException(ex);
		}
		return getResponse(disposals, requestInfo);
	}
	
	public void create(DisposalRequest disposalRequest){
		disposalRepository.create(disposalRequest);
	}
	
	public DisposalResponse createAsync(DisposalRequest disposalRequest) {
		
		ObjectMapper objectMapper = new ObjectMapper();
		disposalRequest.getDisposal().setId(Long.valueOf(disposalRepository.getNextDisposalId().longValue()));
		
		String value = null;
		try {
			value = objectMapper.writeValueAsString(disposalRequest);
		} catch (JsonProcessingException ex) {
			LOGGER.info("DisposalService:",ex);
			throw new RuntimeException(ex);
		}
		
		try{
			assetProducer.sendMessage(applicationProperties.getCreateAssetDisposalTopicName(), "save-disposal", value);
		} catch (Exception ex) {
			LOGGER.info("DisposalService:",ex);
			throw new RuntimeException(ex);
		}
		List<Disposal> disposals = new ArrayList<Disposal>();
		disposals.add(disposalRequest.getDisposal());
		return getResponse(disposals, disposalRequest.getRequestInfo());
	}
	
	public DisposalResponse getResponse(List<Disposal> disposals, RequestInfo requestInfo){
		DisposalResponse disposalResponse = new DisposalResponse();
		disposalResponse.setDisposals(disposals);
		
		return disposalResponse;
	}

}
