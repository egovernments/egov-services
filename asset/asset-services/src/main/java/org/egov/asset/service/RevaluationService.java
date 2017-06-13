package org.egov.asset.service;

import java.util.ArrayList;
import java.util.List;

import org.egov.asset.config.ApplicationProperties;
import org.egov.asset.contract.RevaluationRequest;
import org.egov.asset.contract.RevaluationResponse;
import org.egov.asset.model.Asset;
import org.egov.asset.model.AssetCategory;
import org.egov.asset.model.ChartOfAccountContractResponse;
import org.egov.asset.model.CreateVoucherHelper;
import org.egov.asset.model.Revaluation;
import org.egov.asset.model.RevaluationCriteria;
import org.egov.asset.model.VouchercreateAccountCodeDetails;
import org.egov.asset.model.enums.TypeOfChangeEnum;
import org.egov.asset.producers.AssetProducer;
import org.egov.asset.repository.RevaluationRepository;
import org.egov.common.contract.request.RequestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class RevaluationService {
	
	@Autowired
	private RevaluationRepository revaluationRepository;
	
	@Autowired
	private AssetProducer assetProducer;
	
	@Autowired
	private ApplicationProperties applicationProperties;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private AssetCurrentAmountService assetCurrentAmountService;
	
	@Autowired
	private RestTemplate restTemplate;
	
	private static final Logger logger = LoggerFactory.getLogger(RevaluationService.class);
	
	public RevaluationResponse createAsync(RevaluationRequest revaluationRequest) {
		
		logger.info("RevaluationService createAsync revaluationRequest:"+revaluationRequest);
		
		revaluationRequest.getRevaluation().setId(Long.valueOf(revaluationRepository.getNextRevaluationId().longValue()));
		 
		String json = null;
		
		try {
			json = objectMapper.writeValueAsString(revaluationRequest);
		} catch (JsonProcessingException ex) {
			logger.info("RevaluationService createAsync catch block:"+ex.getMessage());
		}
		
		//Send data to kafka for persist into db
		try{
			if(json != null)
			assetProducer.sendMessage(applicationProperties.getCreateAssetRevaluationTopicName(),"save-revaluation", json);
		} catch (Exception ex){
			logger.info("RevaluationService send kafka createAsync:"+ex.getMessage());
		}
		
		List<Revaluation> revaluations = new ArrayList<Revaluation>();
		revaluations.add(revaluationRequest.getRevaluation());
		return getRevaluationResponse(revaluations);
	}
	
	public void create(RevaluationRequest revaluationRequest) {
		revaluationRepository.create(revaluationRequest);
	}
	
	public RevaluationResponse search(RevaluationCriteria revaluationCriteria) {
		List<Revaluation> revaluations = null;
		try{
			revaluations = revaluationRepository.search(revaluationCriteria);
		} catch (Exception ex){
			ex.printStackTrace();
		}
		return getRevaluationResponse(revaluations);
	}
	
	/*public CreateVoucherHelper prepareVoucherRequest(RevaluationRequest revaluationRequest){
		
		String url = null;
		restTemplate.postForObject(url, revaluationRequest.getRevaluation().toVoucher(getAccountDetails(revaluationRequest)), 
				responseType);
		return null;
	}*/
	
	public List<VouchercreateAccountCodeDetails> getAccountDetails(RevaluationRequest revaluationRequest){
		Asset asset = assetCurrentAmountService.
				getAsset(revaluationRequest.getRevaluation().getAssetId(), 
						revaluationRequest.getRevaluation().getTenantId(), revaluationRequest.getRequestInfo());
		
		AssetCategory assetCategory = asset.getAssetCategory();
		
		List<VouchercreateAccountCodeDetails> accountCodeDetails = new ArrayList<VouchercreateAccountCodeDetails>();
		if(revaluationRequest.getRevaluation().getTypeOfChange().equals(TypeOfChangeEnum.INCREASED)){
			accountCodeDetails.add(getGlCodes(revaluationRequest, assetCategory.getAssetAccount(), false, true));
			accountCodeDetails.add(getGlCodes(revaluationRequest, assetCategory.getRevaluationReserveAccount(), true, false));
		} else if(revaluationRequest.getRevaluation().getTypeOfChange().equals(TypeOfChangeEnum.DECREASED)) {
			accountCodeDetails.add(getGlCodes(revaluationRequest, assetCategory.getAssetAccount(), true, false));
			accountCodeDetails.add(getGlCodes(revaluationRequest, assetCategory.getRevaluationReserveAccount(), false, true));
		}
		
		return accountCodeDetails;
	}
	
	public VouchercreateAccountCodeDetails getGlCodes(RevaluationRequest revaluationRequest,Long accountId, Boolean iscredit,Boolean isDebit){
		
		VouchercreateAccountCodeDetails debitAccountCodeDetail = new VouchercreateAccountCodeDetails();
		ChartOfAccountContractResponse chartOfAccountContractResponse = null;
		String url = null;
		try{
			 chartOfAccountContractResponse = 
					restTemplate.postForObject(url, revaluationRequest.getRequestInfo(), ChartOfAccountContractResponse.class);
			
			debitAccountCodeDetail.setGlcode(chartOfAccountContractResponse.getChartOfAccount().getGlcode());
			
			if(iscredit)
				debitAccountCodeDetail.setCreditAmount(revaluationRequest.getRevaluation().getRevaluationAmount());
			if(isDebit)
				debitAccountCodeDetail.setDebitAmount(revaluationRequest.getRevaluation().getRevaluationAmount());
			
		} catch(Exception ex) {
			logger.info("RevaluationService getAccountDetails:",ex);
			throw new RuntimeException(ex);
		}
		
		return debitAccountCodeDetail;
	}
	
	private RevaluationResponse getRevaluationResponse(List<Revaluation> revaluations) {
		RevaluationResponse revaluationResponse = new RevaluationResponse();
		revaluationResponse.setRevaluations(revaluations);
		return revaluationResponse;
	}
	

}
