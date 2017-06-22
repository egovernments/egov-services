package org.egov.demand.service;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.demand.config.ApplicationProperties;
import org.egov.demand.model.TaxHeadMaster;
import org.egov.demand.model.TaxHeadMasterCriteria;
import org.egov.demand.producer.TaxHeadMasterProducer;
import org.egov.demand.repository.TaxHeadMasterRepository;
import org.egov.demand.web.contract.TaxHeadMasterRequest;
import org.egov.demand.web.contract.TaxHeadMasterResponse;
import org.egov.demand.web.contract.factory.ResponseFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class TaxHeadMasterService {

	private static final Logger logger = LoggerFactory.getLogger(TaxHeadMasterService.class);
	
	@Autowired
	private ResponseFactory responseInfoFactory;
	
	@Autowired
	private TaxHeadMasterRepository taxHeadMasterRepository;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private TaxHeadMasterProducer taxHeadMasterProducer;
	
	@Autowired
	private ApplicationProperties applicationProperties;
	
	public TaxHeadMasterResponse getTaxHeads(TaxHeadMasterCriteria searchTaxHead, RequestInfo requestInfo) {
		logger.info("TaxHeadMasterService getTaxHeads");
		List<TaxHeadMaster> taxHeadMaster= taxHeadMasterRepository.findForCriteria(searchTaxHead);
		return getTaxHeadMasterResponse(taxHeadMaster,requestInfo);
	}
	
	public TaxHeadMasterResponse create(TaxHeadMasterRequest taxHeadMasterRequest) {
		List<TaxHeadMaster> taxHeadMaster = taxHeadMasterRepository.create(taxHeadMasterRequest);
		return getTaxHeadMasterResponse(taxHeadMaster,taxHeadMasterRequest.getRequestInfo());
	}
	
	/*public TaxHeadMasterResponse createAsync(TaxHeadMasterRequest taxHeadMasterRequest) {

		taxHeadMasterRequest.getTaxHeadMaster().setCode(taxHeadMasterRepository.getTaxHeadMasterCode());
		taxHeadMasterRequest.getTaxHeadMaster().setId(Long.valueOf(taxHeadMasterRepository.getNextTaxHeadMasterId().longValue()));

		logger.info("taxHeadMasterRequest createAsync::" + taxHeadMasterRequest);
		String value = null;

		try {
			value = objectMapper.writeValueAsString(taxHeadMasterRequest);
		} catch (JsonProcessingException e) {
			logger.info("JsonProcessingException taxHeadMasterRequest for kafka : " + e);
		}
		logger.info("taxHeadMasterRequest value::" + value);

		taxHeadMasterProducer.sendMessage(applicationProperties.getCreateTaxHeadMasterTopicName(),applicationProperties.getCreateTaxHeadMasterTopicKey(), value);

		List<TaxHeadMaster> taxHeadMaster = new ArrayList<>();
		taxHeadMaster.add(taxHeadMasterRequest.getTaxHeadMaster());
		return getTaxHeadMasterResponse(taxHeadMaster,taxHeadMasterRequest.getRequestInfo());
	}*/

	public TaxHeadMasterResponse update(TaxHeadMasterRequest taxHeadMasterRequest) {

		List<TaxHeadMaster> taxHeadMaster = taxHeadMasterRepository.update(taxHeadMasterRequest);
		
		return getTaxHeadMasterResponse(taxHeadMaster,taxHeadMasterRequest.getRequestInfo());
	}
	
	private TaxHeadMasterResponse getTaxHeadMasterResponse(List<TaxHeadMaster> taxHeadMaster, RequestInfo requestInfo) {
		TaxHeadMasterResponse taxHeadMasterResponse = new TaxHeadMasterResponse();
		taxHeadMasterResponse.setTaxHeadMasters(taxHeadMaster);
		taxHeadMasterResponse.setResponseInfo(responseInfoFactory.getResponseInfo(requestInfo,HttpStatus.OK));
		return taxHeadMasterResponse;
	}
}
