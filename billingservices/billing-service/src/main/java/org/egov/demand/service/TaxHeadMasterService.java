package org.egov.demand.service;

import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.demand.config.ApplicationProperties;
import org.egov.demand.model.TaxHeadMaster;
import org.egov.demand.model.TaxHeadMasterCriteria;
import org.egov.demand.model.TaxPeriod;
import org.egov.demand.repository.TaxHeadMasterRepository;
import org.egov.demand.util.SequenceGenService;
import org.egov.demand.web.contract.TaxHeadMasterRequest;
import org.egov.demand.web.contract.TaxHeadMasterResponse;
import org.egov.demand.web.contract.TaxPeriodCriteria;
import org.egov.demand.web.contract.factory.ResponseFactory;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

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
	private TaxPeriodService taxPeriodService;
	
	@Autowired
	private SequenceGenService sequenceGenService;
	
	@Autowired
	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;
	
	@Autowired
	private ApplicationProperties applicationProperties;
	
	public TaxHeadMasterResponse getTaxHeads(TaxHeadMasterCriteria searchTaxHead, RequestInfo requestInfo) {
		logger.info("TaxHeadMasterService getTaxHeads");
		TaxPeriodCriteria taxPeriodCriteria=new TaxPeriodCriteria();
		taxPeriodCriteria.setTenantId(searchTaxHead.getTenantId());
		taxPeriodCriteria.setService(searchTaxHead.getService());
		List<TaxHeadMaster> taxHeadMaster= taxHeadMasterRepository.findForCriteria(searchTaxHead);
		//List<TaxPeriod> taxPeriod= taxPeriodService.searchTaxPeriods(taxPeriodCriteria, requestInfo).getTaxPeriods();
		/*for(int i=0;i<taxHeadMaster.size();i++){
			taxHeadMaster.get(i).setTaxPeriod(taxPeriod.get(i));
		}*/
		return getTaxHeadMasterResponse(taxHeadMaster,requestInfo);
	}
	
	public void create(TaxHeadMasterRequest taxHeadMasterRequest) {
		//List<TaxHeadMaster> taxHeadMaster=taxHeadMasterRequest.getTaxHeadMasters();
		taxHeadMasterRepository.create(taxHeadMasterRequest);
		//return getTaxHeadMasterResponse(taxHeadMaster,taxHeadMasterRequest.getRequestInfo());
	}
	
	public TaxHeadMasterResponse createAsync(TaxHeadMasterRequest taxHeadMasterRequest) {
		List<TaxHeadMaster> taxHeadMaster = taxHeadMasterRequest.getTaxHeadMasters();
		
		List<String> taxHeadIds = sequenceGenService.getIds(taxHeadMaster.size(),applicationProperties.getTaxHeadSeqName());
		List<String> taxHeadCodes = sequenceGenService.getIds(taxHeadMaster.size(),applicationProperties.getTaxHeadCodeSeqName());
		
		for(int i=0;i<taxHeadMaster.size();i++){
			taxHeadMaster.get(i).setId(taxHeadIds.get(i));
			taxHeadMaster.get(i).setCode(taxHeadCodes.get(i));
		}
		taxHeadMasterRequest.setTaxHeadMasters(taxHeadMaster);

		logger.info("taxHeadMasterRequest createAsync::" + taxHeadMasterRequest);

		kafkaTemplate.send(applicationProperties.getCreateTaxHeadMasterTopicName(),applicationProperties.getCreateTaxHeadMasterTopicKey(),taxHeadMasterRequest);
		
		return getTaxHeadMasterResponse(taxHeadMaster,taxHeadMasterRequest.getRequestInfo());
	}

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