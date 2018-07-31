package org.egov.pt.calculator.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.egov.common.contract.request.RequestInfo;
import org.egov.mdms.model.MdmsCriteriaReq;
import org.egov.mdms.model.MdmsResponse;
import org.egov.pt.calculator.repository.Repository;
import org.egov.pt.calculator.util.CalculatorConstants;
import org.egov.pt.calculator.util.CalculatorUtils;
import org.egov.pt.calculator.web.models.demand.TaxHeadMaster;
import org.egov.pt.calculator.web.models.demand.TaxHeadMasterResponse;
import org.egov.pt.calculator.web.models.property.RequestInfoWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.minidev.json.JSONArray;

@Service
public class MasterDataService {

	@Autowired
	private Repository repository;
	
	@Autowired
	private ObjectMapper mapper;
	
	@Autowired
	private CalculatorUtils calculatorUtils;
	
	/**
	 * Fetches Financial Year from Mdms Api
	 * 
	 * @param requestInfo
	 * @param assesmentYear
	 * @param tenantId
	 * @return
	 */
	@SuppressWarnings("unchecked") 
	public Map<String, Object> getfinancialYear(RequestInfo requestInfo, String assesmentYear, String tenantId) {

		MdmsCriteriaReq mdmsCriteriaReq = calculatorUtils.getFinancialYearRequest(requestInfo, assesmentYear, tenantId);
		StringBuilder url = calculatorUtils.getMdmsSearchUrl();
		MdmsResponse res = mapper.convertValue(repository.fetchResult(url, mdmsCriteriaReq), MdmsResponse.class);
		return (Map<String, Object>) res.getMdmsRes().get(CalculatorConstants.FINANCIAL_MODULE)
				.get(CalculatorConstants.FINANCIAL_YEAR_MASTER).get(0);
	}
	
	/**
	 * Fetch Tax Head Masters From billing service
	 * @param requestInfo
	 * @param tenantId
	 * @return
	 */
	public Map<String, TaxHeadMaster> getTaxHeadMasterMap(RequestInfo requestInfo, String tenantId) {

		
		StringBuilder uri = calculatorUtils.getTaxHeadSearchUrl(tenantId);
		TaxHeadMasterResponse res = mapper.convertValue(
				repository.fetchResult(uri, RequestInfoWrapper.builder().requestInfo(requestInfo).build()),
				TaxHeadMasterResponse.class);
		return res.getTaxHeadMasters().parallelStream()
				.collect(Collectors.toMap(TaxHeadMaster::getCode, Function.identity()));
	}
	
	/**
	 * Method to enrich the property Master data Map
	 * 
	 * @param requestInfo
	 * @param tenantId
	 */
	public void setPropertyMasterValues(RequestInfo requestInfo, String tenantId,
			Map<String, Map<String, List<Object>>> propertyBasedExemptionMasterMap, Map<String, JSONArray> timeBasedExemptionMasterMap) {

		MdmsResponse response = mapper.convertValue(repository.fetchResult(calculatorUtils.getMdmsSearchUrl(),
				calculatorUtils.getPropertyModuleRequest(requestInfo, tenantId)), MdmsResponse.class);
		Map<String, JSONArray> res = response.getMdmsRes().get(CalculatorConstants.PROPERTY_TAX_MODULE);
		for (Entry<String, JSONArray> entry : res.entrySet()) {

			String masterName = entry.getKey();
			
			/* Masters which need to be parsed will be contained in the list */
			if (CalculatorConstants.PROPERTY_BASED_EXEMPTION_MASTERS.contains(entry.getKey()))
				propertyBasedExemptionMasterMap.put(masterName, getParsedMaster(entry));
			
			/* Master not contained in list will be stored as it is  */
			timeBasedExemptionMasterMap.put(entry.getKey(), entry.getValue());
		}
	}
	
	/**
	 * Parses the master which has an exemption in them
	 * @param entry
	 * @return
	 */
	private Map<String, List<Object>> getParsedMaster(Entry<String, JSONArray> entry) {
		
		JSONArray values = entry.getValue();
		Map<String, List<Object>> codeValueListMap = new HashMap<>();
		for (Object object : values) {

			@SuppressWarnings("unchecked")
			Map<String, Object> objectMap = (Map<String, Object>) object;
			String code = (String) objectMap.get(CalculatorConstants.CODE_FIELD_NAME);
			if (null == codeValueListMap.get(code)) {

				List<Object> valuesList = new ArrayList<>();
				valuesList.add(objectMap);
				codeValueListMap.put(code, valuesList);
			} else {
				codeValueListMap.get(code).add(objectMap);
			}
		}
		return codeValueListMap;
	}

	
	/**
	 * Returns the 'APPLICABLE' master object from the list of inputs
	 * 
	 * filters the Input based on their effective financial year
	 * 
	 * If an object is found with effective year same as assessment year that master entity will be returned
	 * 
	 * If exact match is not found then the entity with latest effective financial year which should be lesser than the assessment year
	 * 
	 * NOTE : applicable points to single object  out of all the entries for a given master which fits the period of the property being assessed
	 *  
	 * @param assessmentYear
	 * @param masterListMap
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getApplicableMasterFromList(String assessmentYear, List<Object> masterList) {
		
		Map<String, Object> objToBeReturned = null;
		String maxYearFromTheList = (String) ((Map<String, Object>) masterList.get(0))
				.get(CalculatorConstants.FROMFY_FIELD_NAME);

		for (Object object : masterList) {

			Map<String, Object> objMap = (Map<String, Object>) object;
			String objFinYear = (String) objMap.get(CalculatorConstants.FROMFY_FIELD_NAME);

			if (objFinYear.compareTo(assessmentYear) == 0)
				return  objMap;
			else if (assessmentYear.compareTo(objFinYear) > 0 && maxYearFromTheList.compareTo(objFinYear) <= 0) {
				maxYearFromTheList = objFinYear;
				objToBeReturned = objMap;
			}
		}
		return objToBeReturned;
	}
	
	/**
	 * Estimates the fire cess that needs to be paid for the given tax amount
	 * 
	 * Returns Zero if no data is found for the given criteria
	 * 
	 * @param payableTax
	 * @param timeBasedExmeptionMasterMap 
	 * @param assessmentYear 
	 * @return
	 */
	public BigDecimal getFireCess(BigDecimal payableTax, String assessmentYear,
			Map<String, JSONArray> timeBasedExmeptionMasterMap) {

		BigDecimal fireCess = BigDecimal.ZERO;
		Map<String, Object> fireCessMap = getApplicableMasterFromList(assessmentYear,
				timeBasedExmeptionMasterMap.get(CalculatorConstants.FIRE_CESS_MASTER));
		if (null == fireCessMap) return fireCess;

		BigDecimal rate = null != fireCessMap.get(CalculatorConstants.RATE_FIELD_NAME) ? BigDecimal.valueOf(
					((Number) fireCessMap.get(CalculatorConstants.RATE_FIELD_NAME)).doubleValue()) : null;

		BigDecimal flatAmt = null != fireCessMap.get(CalculatorConstants.FLAT_AMOUNT_FIELD_NAME) ? BigDecimal.valueOf(		
					((Number) fireCessMap.get(CalculatorConstants.FLAT_AMOUNT_FIELD_NAME)).doubleValue()) : null;

		BigDecimal minAmt = null != fireCessMap.get(CalculatorConstants.MIN_AMOUNT_FIELD_NAME) ? BigDecimal.valueOf(
					((Number) fireCessMap.get(CalculatorConstants.MIN_AMOUNT_FIELD_NAME)).doubleValue()) : null;
					
		BigDecimal maxAmt = null != fireCessMap.get(CalculatorConstants.MAX_AMOUNT_FIELD_NAME) ? BigDecimal.valueOf(
					((Number) fireCessMap.get(CalculatorConstants.MAX_AMOUNT_FIELD_NAME)).doubleValue()) : null;

		/*
		 * Applying rate if value is not null
		 * 
		 *  if the applied value is lesser than min value then take min value
		 *  
		 *  else if the applied value is greater than max value then take max value
		 *  
		 * else apply flat amount in case of rate is null and flat amt is not null 
		 */
			if (null != rate) {
				fireCess = payableTax.multiply(rate.divide(CalculatorConstants.HUNDRED));

				if (null != minAmt && fireCess.compareTo(minAmt) < 0)
					fireCess = minAmt;

				else if (null != maxAmt && fireCess.compareTo(maxAmt) > 0)
					fireCess = maxAmt;
			} else
				fireCess = flatAmt;
			
		return fireCess;
	}
	
}
