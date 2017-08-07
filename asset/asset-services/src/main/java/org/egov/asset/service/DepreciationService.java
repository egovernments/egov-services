package org.egov.asset.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.egov.asset.config.ApplicationProperties;
import org.egov.asset.contract.AssetCurrentValueRequest;
import org.egov.asset.contract.DepreciationRequest;
import org.egov.asset.contract.DepreciationResponse;
import org.egov.asset.contract.FinancialYearContract;
import org.egov.asset.contract.FinancialYearContractResponse;
import org.egov.asset.contract.RequestInfoWrapper;
import org.egov.asset.domain.CalculationAssetDetails;
import org.egov.asset.domain.CalculationCurrentValue;
import org.egov.asset.model.AssetCurrentValue;
import org.egov.asset.model.AuditDetails;
import org.egov.asset.model.Depreciation;
import org.egov.asset.model.DepreciationCriteria;
import org.egov.asset.model.DepreciationDetail;
import org.egov.asset.model.enums.Sequence;
import org.egov.asset.repository.DepreciationRepository;
import org.egov.asset.web.wrapperfactory.ResponseInfoFactory;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DepreciationService {
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private DepreciationRepository depreciationRepository;
	
	@Autowired
	private CurrentValueService currentValueService;
	
	@Autowired
	private ResponseInfoFactory responseInfoFactory;

	@Autowired
	private AssetDepreciator assetDepreciator;
	
	@Autowired
	private SequenceGenService sequenceGenService;
	
	@Autowired
	private ApplicationProperties applicationProperties;
	
	@Autowired
	private AssetCommonService assetCommonService;
	
	@Autowired
	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;
	
	public DepreciationResponse depreciateAsset(DepreciationRequest depreciationRequest) {

		RequestInfo requestInfo = depreciationRequest.getRequestInfo();
		DepreciationCriteria depreciationCriteria = depreciationRequest.getDepreciationCriteria();
		String tenantId = depreciationCriteria.getTenantId();
		if (depreciationCriteria.getFinancialYear() == null
				&& (depreciationCriteria.getFromDate() == null && depreciationCriteria.getToDate() == null))
			throw new RuntimeException("financialyear and (time period)fromdate,todate both "
					+ "cannot be null please provide atleast one value");
		else if (depreciationCriteria.getFinancialYear() == null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(depreciationCriteria.getFromDate());
			int from = calendar.get(Calendar.YEAR);
			calendar.setTimeInMillis(depreciationCriteria.getToDate());
			int to = calendar.get(Calendar.YEAR);
			depreciationCriteria.setFinancialYear(from + "-" + Integer.toString(to).substring(2, 4));
			log.info("financial year value -- " + depreciationCriteria.getFinancialYear());
		} else if(depreciationCriteria.getFromDate() == null && depreciationCriteria.getToDate() == null){
			
			String url = applicationProperties.getEgfServiceHostName() + applicationProperties.getEgfFinancialYearSearchPath()
					+ "?tenantId =" + tenantId + "&finYearRange=" + depreciationCriteria.getFinancialYear();
			FinancialYearContract financialYearContract = restTemplate.postForObject(url, new RequestInfoWrapper(
					requestInfo), FinancialYearContractResponse.class).getFinancialYears().get(0);
			
			depreciationCriteria.setToDate(financialYearContract.getEndingDate().getTime());
			depreciationCriteria.setFromDate(financialYearContract.getStartingDate().getTime());
		}
		
		AuditDetails auditDetails = assetCommonService.getAuditDetails(requestInfo); 

		List<CalculationAssetDetails> calculationAssetDetailList = depreciationRepository.getCalculationAssetDetails(
				depreciationCriteria);
		Map<Long, CalculationCurrentValue> calculationCurrentValues = depreciationRepository.getCalculationCurrentvalue(
				depreciationCriteria).stream().collect(Collectors.toMap(CalculationCurrentValue::getAssetId, Function.identity()));
		Map<Long, BigDecimal> depreciationSumMap = depreciationRepository.getdepreciationSum(depreciationCriteria.getTenantId());
		
		List<AssetCurrentValue> assetCurrentValues = new ArrayList<>();
		List<DepreciationDetail> depreciationDetails = new ArrayList<>();
		
		assetDepreciator.depreciateAsset(depreciationRequest, calculationAssetDetailList, calculationCurrentValues
				, depreciationSumMap, assetCurrentValues, depreciationDetails);
		
		Long voucherReference = null;
	/*	 TODO get voucherreference do integration 
		if (assetConfigurationService.getEnabledVoucherGeneration(AssetConfigurationKeys.ENABLEVOUCHERGENERATION,
				depreciationCriteria.getTenantId()))
			try {
				// TODO VOUCHER GEN voucherReference =
				// createVoucherForRevaluation(revaluationRequest);
			} catch (final Exception e) {
				throw new RuntimeException("Voucher Generation is failed due to :" + e.getMessage());
			}*/

		for (AssetCurrentValue assetCurrentValue :assetCurrentValues) { assetCurrentValue.setTenantId(tenantId);}
		
		Depreciation depreciation = Depreciation.builder().depreciationCriteria(depreciationCriteria).depreciationDetails(
				depreciationDetails).voucherReference(voucherReference).auditDetails(auditDetails).build();
		saveAsync(depreciation);
		currentValueService.createCurrentValueAsync(AssetCurrentValueRequest.builder().assetCurrentValues(
				assetCurrentValues).requestInfo(requestInfo).build());

		return new DepreciationResponse(responseInfoFactory.createResponseInfoFromRequestHeaders(requestInfo),
				depreciation);
	}

	public void saveAsync(Depreciation depreciation) {

		List<DepreciationDetail> depreciationDetails = depreciation.getDepreciationDetails();
		List<Long> depreciationDetailsId = sequenceGenService.getIds(depreciationDetails.size(),
				Sequence.DEPRECIATIONSEQUENCE.toString());
		int depreciationCount = 0;
		for (DepreciationDetail depreciationDetail : depreciationDetails) {
			depreciationDetail.setId(depreciationDetailsId.get(depreciationCount++));

		}
		kafkaTemplate.send(applicationProperties.getSaveDepreciationTopic(),depreciation);
	}
	
	public void save(Depreciation depreciation) {
		depreciationRepository.saveDepreciation(depreciation);
	}
}
