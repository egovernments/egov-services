package org.egov.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.egov.common.contract.request.RequestInfo;
import org.egov.config.ApplicationProperties;
import org.egov.contract.AssetCurrentValueRequest;
import org.egov.contract.DepreciationRequest;
import org.egov.contract.DepreciationResponse;
import org.egov.contract.FinancialYear;
import org.egov.model.AssetCategory;
import org.egov.model.CurrentValue;
import org.egov.model.Depreciation;
import org.egov.model.DepreciationDetail;
import org.egov.model.DepreciationInputs;
import org.egov.model.criteria.DepreciationCriteria;
import org.egov.model.enums.DepreciationStatus;
import org.egov.model.enums.ReasonForFailure;
import org.egov.model.enums.Sequence;
import org.egov.model.enums.TransactionType;
import org.egov.repository.DepreciationRepository;
import org.egov.repository.MasterDataRepository;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;

@Service
@Slf4j
public class DepreciationService {

	// @Autowired private AssetService assetService;

	@Autowired
	private AssetCommonService assetCommonService;

	@Autowired
	private MasterDataService mDService;
	
	@Autowired
	private MasterDataRepository mDRepo;

	@Autowired
	private ApplicationProperties applicationProperties;

	@Autowired
	private DepreciationRepository depreciationRepository;

	@Autowired
	private CurrentValueService currentValueService;

	@Autowired
	private SequenceGenService genService;

	@Autowired
	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

	public DepreciationResponse createDepreciationAsync(DepreciationRequest depreciationRequest) {

		DepreciationCriteria criteria = depreciationRequest.getDepreciationCriteria();
		RequestInfo requestInfo = depreciationRequest.getRequestInfo();
		/*Date date=new Date();
		date.setHours(23);
		date.setMinutes(59);
		date.setSeconds(59);
		*/
		
		
		  Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(applicationProperties.getTimeZone()));
		    calendar.setTimeInMillis(new Date().getTime());
		    calendar.set(Calendar.HOUR_OF_DAY,23);
	        calendar.set(Calendar.MINUTE, 59);
		    calendar.set(Calendar.SECOND, 59);
		    calendar.set(Calendar.MILLISECOND, 755);
		    Date addMilliSeconds = calendar.getTime();
		
		System.err.println(criteria.getToDate()+"criteria.getToDate()");
/*
		System.err.println(addMilliSeconds+"new Date().getTime()");
		
		System.err.println(addMilliSeconds.getTime()+"epoc");*/
		
		System.err.println(addMilliSeconds.getTime()+"new Date().getTime()");
		
	

		if (criteria.getToDate() > addMilliSeconds.getTime() ) {
			Map<String, String> map = new HashMap<>();
			map.put(applicationProperties.getDepreciationDate(), "Assets cannot be depreciated for future dates");
			throw new CustomException(map);
		}
		getFinancialYearData(depreciationRequest);

		Depreciation depreciation = depreciateAssets(depreciationRequest);

		depreciation.setAuditDetails(assetCommonService.getAuditDetails(requestInfo));

		// TODO put depreciation in kafka topic
		kafkaTemplate.send(applicationProperties.getSaveDepreciationTopic(), depreciation);
		
		DepreciationResponse depreciationResponse = DepreciationResponse.builder().depreciation(depreciation).responseInfo(null).build();
         System.err.println("depreciationResponse"+depreciationResponse);
		
		return depreciationResponse;
	}

	/***
	 * Calculates the Depreciation and returns , Calculates the CurrentValue request
	 * and sends it to currentValue Service
	 * 
	 * @param depreciationCriteria
	 * @param requestInfo
	 * @return
	 */
	public Depreciation depreciateAssets(DepreciationRequest depreciationRequest) {

		DepreciationCriteria depreciationCriteria = depreciationRequest.getDepreciationCriteria();
		RequestInfo requestInfo = depreciationRequest.getRequestInfo();

		List<DepreciationInputs> depreciationInputsList = depreciationRepository
				.getDepreciationInputs(depreciationCriteria);
		
		log.info("the list of assets fetched : "+ depreciationInputsList);

		List<DepreciationDetail> depreciationDetailsList = new ArrayList<>();
		List<CurrentValue> currentValues = new ArrayList<>();
		Depreciation depreciation = null;

		if (!depreciationInputsList.isEmpty())
			enrichDepreciationInputs(depreciationInputsList, requestInfo, depreciationCriteria.getTenantId());

		// calculating the depreciation and adding the currenVal and DepDetail to the
		// lists
		calculateDepreciationAndCurrentValue(depreciationInputsList, depreciationDetailsList, currentValues,
				depreciationCriteria.getFromDate(), depreciationCriteria.getToDate());

		// FIXME TODO voucher integration

		getDepreciationdetailsId(depreciationDetailsList);
		// sending dep/currval objects to respective create async methods
		depreciation = Depreciation.builder().depreciationCriteria(depreciationCriteria)
				.depreciationDetails(depreciationDetailsList).build();
		depreciation.setTenantId(depreciationCriteria.getTenantId());

		currentValueService.createCurrentValueAsync(
				AssetCurrentValueRequest.builder().assetCurrentValue(currentValues).requestInfo(requestInfo).build());

		return depreciation;
	}

	private void getDepreciationdetailsId(List<DepreciationDetail> depreciationDetailsList) {

		final List<Long> idList = genService.getIds(depreciationDetailsList.size(),
				Sequence.DEPRECIATIONSEQUENCE.toString());
		int i = 0;
		for (DepreciationDetail depreciationDetail : depreciationDetailsList)
			depreciationDetail.setId(idList.get(i++));
	}

	/***
	 * Calculate the Depreciation value and the current and populate the respective
	 * lists for the values
	 * 
	 * @param depreciationInputsList
	 * @param depDetList
	 * @param currValList
	 * @param fromDate
	 * @param toDate
	 */
	
	
	private void calculateDepreciationAndCurrentValue(List<DepreciationInputs> depreciationInputsList,
			List<DepreciationDetail> depDetList, List<CurrentValue> currValList, Long fromDate, Long toDate) {
		log.info("depreciationInputsList.size()"+depreciationInputsList.size());

		depreciationInputsList.forEach(a -> {

			log.info("the current depreciation input object : " + a);
			// TODO get value form master
			BigDecimal minValue = BigDecimal.ONE;
			ReasonForFailure reason = null;
			DepreciationStatus status = DepreciationStatus.FAIL;
			BigDecimal amtToBeDepreciated = null;
			BigDecimal valueAfterDep = null;
			
			BigDecimal valueAfterDepRounded=null;
			BigDecimal amtToBeDepreciatedRounded=null;
			
			// getting the indvidual fromDate
			Long invidualFromDate = getFromDateForIndvidualAsset(a, fromDate);

			if (a.getCurrentValue().compareTo(minValue) <= 0) {

				reason = ReasonForFailure.ASSET_IS_FULLY_DEPRECIATED_TO_MINIMUN_VALUE;
			}
			else if(null==a.getDepreciationRate()) {
				
				reason = ReasonForFailure.DEPRECIATION_RATE_NOT_FOUND;
				}
			
			else {
				
				status = DepreciationStatus.SUCCESS;

				// getting the amt to be depreciated
				amtToBeDepreciated = getAmountToBeDepreciated(a, invidualFromDate, toDate);
				
				amtToBeDepreciatedRounded=new BigDecimal(amtToBeDepreciated.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
				System.err.println("amtToBeDepreciatedRounded------------"+amtToBeDepreciatedRounded);

				// calculating the valueAfterDepreciation 
				valueAfterDep = a.getCurrentValue().subtract(amtToBeDepreciated);
				
				valueAfterDepRounded=new BigDecimal(valueAfterDep.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
				System.err.println("valueAfterDepRounded------------"+valueAfterDepRounded);
	

				// adding currval to the currval list
				currValList.add(CurrentValue.builder().assetId(a.getAssetId())
						.assetTranType(TransactionType.DEPRECIATION).currentAmount(valueAfterDepRounded)
						.transactionDate(toDate).tenantId(a.getTenantId()).build());
			}

			// adding the depreciation detail object to list
			depDetList.add(DepreciationDetail.builder().assetId(a.getAssetId()).reasonForFailure(reason).assetCode(a.getAssetCode())
					.depreciationRate(a.getDepreciationRate()).depreciationValue(amtToBeDepreciatedRounded).fromDate(invidualFromDate)
					.valueAfterDepreciation(valueAfterDepRounded).valueBeforeDepreciation(a.getCurrentValue()).status(status)
					.build());
		});
	}

	/***
	 * to find the Amount to be depreciated for every Asset from the
	 * DepreciationInput Object
	 * 
	 * @param depInputs
	 * @param indvidualFromDate
	 * @param toDate
	 * @return
	 */
	private BigDecimal getAmountToBeDepreciated(DepreciationInputs depInputs, Long indvidualFromDate, Long toDate) {
		
		System.err.println("depInputs"+depInputs);

		// getting the no of days betweeen the from and todate (including both from and
		// to date)
		Long noOfDays = ((toDate - indvidualFromDate) / 1000 / 60 / 60 / 24) + 1;
		log.info("no of days between fromdate : " + indvidualFromDate + " and todate : " + toDate + " is : " + noOfDays);
		 

			// deprate for the no of days = no of days * calculated dep rate per day
		Double depRateForGivenPeriod = noOfDays * depInputs.getDepreciationRate() / 365;
			log.info("dep rate for given period is : " + depRateForGivenPeriod);
		
		// returning the calculated amt to be depreciated using the currentvalue from
		// dep inputs and depreciation rate for given period
			
		
		if (depInputs.getDepreciationSum() != null)
			return BigDecimal.valueOf((depInputs.getCurrentValue()).add(depInputs.getDepreciationSum()).doubleValue()
					* (depRateForGivenPeriod / 100));
		else
			return BigDecimal.valueOf((depInputs.getCurrentValue()).doubleValue() * (depRateForGivenPeriod / 100));
		
	}
	
	private Long getFromDateForIndvidualAsset(DepreciationInputs depInputs, Long fromDate) {
		
		// deciding the from date from the last depreciation date
		if (depInputs.getLastDepreciationDate()!=null && depInputs.getLastDepreciationDate().compareTo(fromDate) >= 0) {
			fromDate = depInputs.getLastDepreciationDate();
			fromDate += 86400000l; // adding one day in milli seconds to start depreciation from next day
		} else if (depInputs.getDateOfCreation() > fromDate) {
			fromDate = depInputs.getDateOfCreation();
			//fromDate += 86400000l;
		}
		return fromDate;
	}

	private void getFinancialYearData(DepreciationRequest depreciationRequest) {

		DepreciationCriteria criteria = depreciationRequest.getDepreciationCriteria();
		Long todate = criteria.getToDate();

		
		FinancialYear financialYear = mDService.getFinancialYear(todate, depreciationRequest.getRequestInfo(),
				criteria.getTenantId());
		
		if(financialYear==null)
		{
			Map<String, String> errormap = new HashMap<>();
			errormap.put(applicationProperties.getFinancialYear(), " No Financial Found For The Given ToDate");
			throw new CustomException(errormap);
		}
		// setting the toDate hours to 23 and mins to 59
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(todate);
		calendar.setTimeZone(TimeZone.getTimeZone(applicationProperties.getTimeZone()));
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);

		log.info(" to date set to 23 59... : "+todate);
		criteria.setToDate(calendar.getTimeInMillis());
		criteria.setFinancialYear(financialYear.getFinYearRange());
		criteria.setFromDate(financialYear.getStartingDate());
	}

	/***
	 * Enrich DepreciationInputs using the masterDataService
	 * 
	 * @param depreciationInputsList
	 * @param requestInfo
	 * @param tenantId
	 */
	private void enrichDepreciationInputs(List<DepreciationInputs> depreciationInputsList, RequestInfo requestInfo,
			String tenantId) {
		
		Map<String, String> errorMap = new HashMap<>();
		Set<Long> assetCategoryIds = depreciationInputsList.stream().map(dil -> dil.getAssetCategory())
				.collect(Collectors.toSet());

		Map<String, Map<String, Map<String, String>>> moduleMap = new HashMap<>();
		Map<String, Map<String, String>> masterMap = new HashMap<>();
		Map<String, String> fieldMap = new HashMap<>();

		fieldMap.put("id", assetCommonService.getIdQuery(assetCategoryIds));
		masterMap.put("AssetCategory", fieldMap);
		moduleMap.put("ASSET", masterMap);

		if(!tenantId.equals("default"))
			tenantId=tenantId.split("\\.")[0];
		Map<String,JSONArray>  assetMap= mDRepo.getMastersByListParams(moduleMap, requestInfo, tenantId).get("ASSET");
		
		if(!assetMap.get("AssetCategory").isEmpty()) {
		  Map<Long, AssetCategory> assetCatMap = mDService.getAssetCategoryMapFromJSONArray(assetMap.get("AssetCategory"));
		  

		depreciationInputsList.forEach(a -> a.setDepreciationRate(assetCatMap.get(a.getAssetCategory()).getDepreciationRate()));
		}else {
			errorMap.put(applicationProperties.getDepreciationAssetCategory(), "No Asset Categories found for the chosen Assets");
			throw new CustomException(errorMap);
		}
	}

}
