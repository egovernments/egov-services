package org.egov.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.egov.common.contract.request.RequestInfo;
import org.egov.contract.AssetCurrentValueRequest;
import org.egov.contract.DepreciationRequest;
import org.egov.contract.DepreciationResponse;
import org.egov.model.AssetCategory;
import org.egov.model.CurrentValue;
import org.egov.model.Depreciation;
import org.egov.model.DepreciationDetail;
import org.egov.model.DepreciationInputs;
import org.egov.model.criteria.DepreciationCriteria;
import org.egov.model.enums.TransactionType;
import org.egov.repository.DepreciationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DepreciationService {

	@Autowired
	private AssetService assetService;

	@Autowired
	private AssetCommonService assetCommonService;
	
	@Autowired
	private DepreciationRepository depreciationRepository;
	
	@Autowired 
	private CurrentValueService currentValueService;
	
	@Autowired
	private MasterDataService masterDataService;
	
	
	public DepreciationResponse createDepreciationAsync(DepreciationRequest depreciationRequest) {
		
		DepreciationCriteria criteria = depreciationRequest.getDepreciationCriteria();
		RequestInfo requestInfo = depreciationRequest.getRequestInfo();
		
		Long todate = criteria.getToDate();
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(todate);
		//int year = calendar.get(Calendar.YEAR);
		
		//Calendar fromCal = Calendar.getInstance();
		//fromCal.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH,Calendar.APRIL);
		calendar.set(Calendar.DATE,1);
		criteria.setFromDate(calendar.getTimeInMillis());
		System.err.println("from date calculated : "+ criteria.getFromDate());
		
		Depreciation depreciation = depreciateAssets(depreciationRequest);
		
		depreciation.setAuditDetails(assetCommonService.getAuditDetails(requestInfo));
		
		//TODO put depreciation in kafka topic
		
		return DepreciationResponse.builder().depreciation(depreciation).responseInfo(null).build();
	}
	
	/***
	 * Calculates the Depreciation and returns , Calculates the CurrentValue request and sends it to currentValue Service
	 * 
	 * @param depreciationCriteria
	 * @param requestInfo
	 * @return
	 */
	public Depreciation depreciateAssets(DepreciationRequest depreciationRequest) {
		
		DepreciationCriteria depreciationCriteria = depreciationRequest.getDepreciationCriteria();
		RequestInfo requestInfo = depreciationRequest.getRequestInfo();
		
		List<DepreciationInputs> depreciationInputsList = depreciationRepository.getDepreciationInputs(depreciationCriteria);
		
		List<DepreciationDetail> depreciationDetailsList = new ArrayList<>();
		List<CurrentValue> currentValues = new ArrayList<>();
		Depreciation depreciation = null;
		
		if(!depreciationInputsList.isEmpty()) 
		enrichDepreciationInputs(depreciationInputsList,requestInfo,depreciationCriteria.getTenantId());
		
		// calculating the depreciation and adding the currenVal and DepDetail to the lists
		calculateDepreciationAndCurrentValue(depreciationInputsList,depreciationDetailsList,currentValues,depreciationCriteria.getFromDate(),
				depreciationCriteria.getToDate());
		
		// FIXME TODO voucher integration
		
		
		// sending dep/currval objects to respective create async methods
		 depreciation = Depreciation.builder().depreciationCriteria(depreciationCriteria)
				.depreciationDetails(depreciationDetailsList).build();
		
		currentValueService.createCurrentValueAsync(AssetCurrentValueRequest.builder()
				.assetCurrentValue(currentValues).requestInfo(requestInfo).build());
		
		return depreciation;
	}

	/***
	 * Calculate the Depreciation value and the current and populate the respective lists for the values
	 * @param depreciationInputsList
	 * @param depDetList
	 * @param currValList
	 * @param fromDate
	 * @param toDate
	 */
	private void calculateDepreciationAndCurrentValue(List<DepreciationInputs> depreciationInputsList,
			List<DepreciationDetail> depDetList, List<CurrentValue> currValList, Long fromDate, Long toDate) {

		depreciationInputsList.forEach(a -> {
			// getting the amt to be depreciated
			BigDecimal amtToBeDepreciated = getAmountToBeDepreciated(a, fromDate, toDate);

			// calculating the valueAfterDepreciation
			BigDecimal valueAfterDep = a.getCurrentValue().subtract(amtToBeDepreciated);

			// adding the depreciation detail object to list
			depDetList.add(DepreciationDetail.builder().assetId(a.getAssetId())
					.depreciationRate(a.getDepreciationRate()).depreciationValue(amtToBeDepreciated)
					.valueAfterDepreciation(valueAfterDep).valueBeforeDepreciation(a.getCurrentValue()).build());

			// adding currval to the currval list
			currValList.add(CurrentValue.builder().assetId(a.getAssetId()).assetTranType(TransactionType.DEPRECIATION)
					.currentAmount(valueAfterDep).build());
		});
	}

	/***
	 * to find the Amount to be depreciated for every Asset from the DepreciationInput Object
	 * @param depInputs
	 * @param fromDate
	 * @param toDate
	 * @return
	 */
	private BigDecimal getAmountToBeDepreciated(DepreciationInputs depInputs,Long fromDate,Long toDate) {
		
		// deciding the from date for the current depreciation from the last depreciation date
		if(depInputs.getLastDepreciationDate().compareTo(toDate) >= 0)
			fromDate = depInputs.getLastDepreciationDate();
		
		// getting the no of days betweeen the from and todate using ChronoUnit
		LocalDate fromEpoch = LocalDate.ofEpochDay(fromDate);
		LocalDate toEpoch = LocalDate.ofEpochDay(toDate);
		Long noOfDays = ChronoUnit.DAYS.between(fromEpoch, toEpoch);

		// deprate for the no of days = no of days * calculated dep rate per day
		Double depRateForGivenPeriod = noOfDays * depInputs.getDepreciationRate()/365;
		
		// returning the calculated amt to be depreciated using the currentvalue from dep inputs and depreciation rate for given period
		return BigDecimal.valueOf(depInputs.getCurrentValue().doubleValue()*(depRateForGivenPeriod/100));
	}


	/***
	 * Enrich DepreciationInputs using the masterDataService
	 * 
	 * @param depreciationInputsList
	 * @param requestInfo
	 * @param tenantId
	 */
	private void enrichDepreciationInputs(List<DepreciationInputs> depreciationInputsList,RequestInfo requestInfo,String tenantId) {
		
		Set<Long> assetCategoryIds = depreciationInputsList.stream().map(dil -> dil.getAssetCategory()).collect(Collectors.toSet());
		
		Map<Long, AssetCategory> assetCatMap = masterDataService.getAssetCategoryMap(assetCategoryIds, requestInfo, tenantId);
		
		depreciationInputsList.forEach(a -> a.setDepreciationRate(assetCatMap.get(a.getAssetCategory()).getDepreciationRate()));
	}
	
}
