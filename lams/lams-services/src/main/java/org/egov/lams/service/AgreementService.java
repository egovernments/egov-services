package org.egov.lams.service;

import java.util.List;
import java.util.stream.Collectors;

import org.egov.lams.config.PropertiesManager;
import org.egov.lams.web.contract.AgreementRequest;
import org.egov.lams.web.contract.DemandResponse;
import org.egov.lams.model.Agreement;
import org.egov.lams.model.AgreementCriteria;
import org.egov.lams.model.Demand;
import org.egov.lams.model.DemandReason;
import org.egov.lams.producers.AgreementProducer;
import org.egov.lams.repository.AgreementRepository;
import org.egov.lams.repository.DemandRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AgreementService {
	public static final Logger logger = LoggerFactory.getLogger(AgreementService.class);

	@Autowired
	private AgreementRepository agreementRepository;
	
	@Autowired
	private AgreementProducer agreementProducer;
	
	@Autowired
	private DemandRepository demandRepository;
	
	@Autowired
	private AcknowledgementNumberService acknowledgementNumberService;	
	
	@Autowired
	private PropertiesManager propertiesManager;

	public List<Agreement> searchAgreement(AgreementCriteria agreementCriteria) {
		/*
		 * three boolean variables isAgreementNull,isAssetNull and
		 * isAllotteeNull declared to indicate whether criteria arguments for
		 * each of the Agreement,Asset and Allottee objects are given or not.
		 */
		boolean isAgreementNull = agreementCriteria.getAgreementId() == null
				&& agreementCriteria.getAgreementNumber() == null && agreementCriteria.getStatus() == null
				&& (agreementCriteria.getFromDate() == null && agreementCriteria.getToDate() == null)
				&& agreementCriteria.getTenderNumber() == null && agreementCriteria.getTinNumber() == null
				&& agreementCriteria.getTradelicenseNumber() == null && agreementCriteria.getAsset() == null
				&& agreementCriteria.getAllottee() == null  && agreementCriteria.getTenantId() == null;

		boolean isAllotteeNull = agreementCriteria.getAllotteeName() == null
				&& agreementCriteria.getMobilenumber() == null;

		boolean isAssetNull = agreementCriteria.getAssetCategory() == null
				&& agreementCriteria.getShoppingComplexNo() == null && agreementCriteria.getAssetCode() == null
				&& agreementCriteria.getLocality() == null && agreementCriteria.getRevenueWard() == null
				&& agreementCriteria.getElectionWard() == null && agreementCriteria.getDoorno() == null;

		if (!isAgreementNull && !isAssetNull && !isAllotteeNull) {
			logger.info("agreementRepository.findByAllotee");
			return agreementRepository.findByAllotee(agreementCriteria);
			
		} else if (!isAgreementNull && isAssetNull && !isAllotteeNull) {
			logger.info("agreementRepository.findByAllotee");
			return agreementRepository.findByAgreementAndAllotee(agreementCriteria);

		} else if (!isAgreementNull && !isAssetNull && isAllotteeNull) {
			logger.info("agreementRepository.findByAgreementAndAsset : both agreement and ");
			return agreementRepository.findByAgreementAndAsset(agreementCriteria);

		} else if ((isAgreementNull && isAssetNull && !isAllotteeNull)
				|| (isAgreementNull && !isAssetNull && !isAllotteeNull)) {
			logger.info("agreementRepository.findByAllotee : only allottee || allotte and asset");
			return agreementRepository.findByAllotee(agreementCriteria);

		} else if (isAgreementNull && !isAssetNull && isAllotteeNull) {
			logger.info("agreementRepository.findByAsset : only asset");
			return agreementRepository.findByAsset(agreementCriteria);

		} else if (!isAgreementNull && isAssetNull && isAllotteeNull) {
			logger.info("agreementRepository.findByAgreement : only agreement");
			return agreementRepository.findByAgreement(agreementCriteria);
		} else {
			// if no values are given for all the three criteria objects
			// (isAgreementNull && isAssetNull && isAllotteeNull)
			logger.info("agreementRepository.findByAgreement : all values null");
			return agreementRepository.findByAgreement(agreementCriteria);
		}
	}
	
	/*
	 * This method is used to create new agreement
	 * 
	 * @return Agreement, return the agreement details with current status
	 * 
	 * @param agreement, hold agreement details 
	 * 
	 * */
	
	public Agreement createAgreement(AgreementRequest agreementRequest){
		
		Agreement agreement = agreementRequest.getAgreement();
		logger.info("createAgreement service::"+agreement);
		
		ObjectMapper mapper = new ObjectMapper();
		String agreementValue = null;
	    
		List<DemandReason> demandReasons = demandRepository.getDemandReason(agreementRequest);
		if(demandReasons.isEmpty())
			throw new RuntimeException("No demand reason found for given criteria");
		
		List<Demand> demands = demandRepository.getDemandList(agreementRequest, demandReasons);
		DemandResponse demandResponse = demandRepository.createDemand(demands,agreementRequest.getRequestInfo());
		List<String> demandIdList = demandResponse.getDemands().stream().map(demand -> demand.getId())
				.collect(Collectors.toList());
		agreement.setDemands(demandIdList);
		agreement.setAcknowledgementNumber(acknowledgementNumberService.generateAcknowledgeNumber());
		logger.info(agreement.getAcknowledgementNumber());
		try {
				agreementValue = mapper.writeValueAsString(agreementRequest);
				logger.info("agreementValue::"+agreementValue);
		} catch (JsonProcessingException JsonProcessingException) {
				logger.info("AgreementService : "+JsonProcessingException.getMessage(),JsonProcessingException);
				throw new RuntimeException(JsonProcessingException.getMessage());
		}
		try {
				agreementProducer.sendMessage(propertiesManager.getStartWorkflowTopic(), "save-agreement", agreementValue);
		}catch(Exception exception){
			logger.info("AgreementService : "+exception.getMessage(),exception);
				throw exception;
		}
		return agreement;
	}

	/***
	 * method to update agreementNumber using acknowledgeNumber
	 * 
	 * @param agreement
	 * @return
	 */
	public Agreement updateAgreement(AgreementRequest agreementRequest) {
		
		Agreement agreement = agreementRequest.getAgreement();
		logger.info("createAgreement service::" + agreement);
		ObjectMapper mapper = new ObjectMapper();
		String agreementValue = null;

		// TODO  FIXME put agreement number generator here and change
		try {
			agreementValue = mapper.writeValueAsString(agreementRequest);
			logger.info("agreementValue::" + agreementValue);
		} catch (JsonProcessingException jsonProcessingException) {
			logger.info("AgreementService : " + jsonProcessingException.getMessage(), jsonProcessingException);
			throw new RuntimeException(jsonProcessingException.getMessage());
		}

		try {
			agreementProducer.sendMessage(propertiesManager.getUpdateWorkflowTopic(), "save-agreement", agreementValue);
		} catch (Exception exception) {
			logger.info("AgreementService : " + exception.getMessage(), exception);
			throw exception;
		}
		return agreement;
	}
}
