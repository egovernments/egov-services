package org.egov.pt.calculator.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.egov.common.contract.request.RequestInfo;
import org.egov.mdms.model.MdmsCriteriaReq;
import org.egov.mdms.model.MdmsResponse;
import org.egov.pt.calculator.repository.Repository;
import org.egov.pt.calculator.util.CalculatorConstants;
import org.egov.pt.calculator.util.CalculatorUtils;
import org.egov.pt.calculator.web.models.demand.TaxHeadMaster;
import org.egov.pt.calculator.web.models.demand.TaxHeadMasterResponse;
import org.egov.pt.calculator.web.models.demand.TaxPeriod;
import org.egov.pt.calculator.web.models.demand.TaxPeriodResponse;
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
	public List<TaxHeadMaster> getTaxHeadMasterMap(RequestInfo requestInfo, String tenantId) {

		StringBuilder uri = calculatorUtils.getTaxHeadSearchUrl(tenantId);
		TaxHeadMasterResponse res = mapper.convertValue(
				repository.fetchResult(uri, RequestInfoWrapper.builder().requestInfo(requestInfo).build()),
				TaxHeadMasterResponse.class);
		return res.getTaxHeadMasters();
	}

	/**
	 * Fetch Tax Head Masters From billing service
	 * @param requestInfo
	 * @param tenantId
	 * @return
	 */
	public List<TaxPeriod> getTaxPeriodList(RequestInfoWrapper requestInfoWrapper, String tenantId) {

		StringBuilder uri = calculatorUtils.getTaxPeriodSearchUrl(tenantId);
		TaxPeriodResponse res = mapper.convertValue(
				repository.fetchResult(uri, requestInfoWrapper),
				TaxPeriodResponse.class);
		return res.getTaxPeriods();
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
	public Map<String, Object> getApplicableMaster(String assessmentYear, List<Object> masterList) {
		
		Map<String, Object> objToBeReturned = null;
		String maxYearFromTheList = "0";

		for (Object object : masterList) {

			Map<String, Object> objMap = (Map<String, Object>) object;
			String objFinYear = ((String) objMap.get(CalculatorConstants.FROMFY_FIELD_NAME)).split("-")[0];

			if (objFinYear.compareTo(assessmentYear.split("-")[0]) == 0) 
				return  objMap;
				
			else if (assessmentYear.split("-")[0].compareTo(objFinYear) > 0 && maxYearFromTheList.compareTo(objFinYear) <= 0) {
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
	 * @param timeBasedMasterMap 
	 * @param assessmentYear 
	 * @return
	 */
	public BigDecimal getFireCess(BigDecimal payableTax, String assessmentYear,
			Map<String, JSONArray> timeBasedMasterMap) {
		BigDecimal fireCess = BigDecimal.ZERO;

		if (payableTax.doubleValue() == 0.0)
			return fireCess;

		Map<String, Object> fireCessMap = getApplicableMaster(assessmentYear,
				timeBasedMasterMap.get(CalculatorConstants.FIRE_CESS_MASTER));

		return calculateApplicables(payableTax, fireCessMap);
	}
	
	/**
	 * Method to calculate exmeption based on the Amount and exemption map
	 * 
	 * @param applicableAmount
	 * @param configMap
	 * @return
	 */
	public BigDecimal calculateApplicables(BigDecimal applicableAmount, Object config) {

		BigDecimal currentApplicable = BigDecimal.ZERO;

		if (null == config)
			return currentApplicable;

		@SuppressWarnings("unchecked")
		Map<String, Object> configMap = (Map<String, Object>) config;

		BigDecimal rate = null != configMap.get(CalculatorConstants.RATE_FIELD_NAME)
				? BigDecimal.valueOf(((Number) configMap.get(CalculatorConstants.RATE_FIELD_NAME)).doubleValue())
				: null;

		BigDecimal maxAmt = null != configMap.get(CalculatorConstants.MAX_AMOUNT_FIELD_NAME)
				? BigDecimal.valueOf(((Number) configMap.get(CalculatorConstants.MAX_AMOUNT_FIELD_NAME)).doubleValue())
				: null;

		BigDecimal minAmt = null != configMap.get(CalculatorConstants.MIN_AMOUNT_FIELD_NAME)
				? BigDecimal.valueOf(((Number) configMap.get(CalculatorConstants.MIN_AMOUNT_FIELD_NAME)).doubleValue())
				: null;

		BigDecimal flatAmt = null != configMap.get(CalculatorConstants.FLAT_AMOUNT_FIELD_NAME)
				? BigDecimal.valueOf(((Number) configMap.get(CalculatorConstants.FLAT_AMOUNT_FIELD_NAME)).doubleValue())
				: BigDecimal.ZERO;

		if (null == rate)
			currentApplicable = flatAmt.compareTo(applicableAmount) > 0 ? applicableAmount : flatAmt;
		else {
			currentApplicable = applicableAmount.multiply(rate.divide(CalculatorConstants.HUNDRED));

			if (null != maxAmt && currentApplicable.compareTo(maxAmt) > 0)
				currentApplicable = maxAmt;
			else if (null != minAmt && currentApplicable.compareTo(minAmt) < 0)
				currentApplicable = minAmt;
		}
		return currentApplicable;
	}
	
}
