package org.egov.pt.calculator.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.egov.common.contract.response.ResponseInfo;
import org.egov.pt.calculator.web.models.Calculation;
import org.egov.pt.calculator.web.models.CalculationCriteria;
import org.egov.pt.calculator.web.models.CalculationReq;
import org.egov.pt.calculator.web.models.CalculationRes;
import org.egov.pt.calculator.web.models.demand.Category;
import org.egov.pt.calculator.web.models.property.Property;
import org.egov.pt.calculator.web.models.property.Unit;
import org.springframework.stereotype.Service;

@Service
public class CalculatorService {

	/**
	 * Method to generate the estimated tax to be paid for the given property
	 * 
	 * @param request
	 * @return CalculationRes
	 */
	public CalculationRes getEstimates(CalculationReq request) {

		CalculationCriteria criteria = request.getCalculationCriteria().get(0);
		List<Calculation> calcList = new ArrayList<>();
		calcList.add(getcalculation(criteria));
		return CalculationRes.builder().responseInfo(new ResponseInfo()).calculation(calcList).build();
	}

	/**
	 * Generates a map with tax head code as key and the amount to be collected
	 * against respective codes
	 * 
	 * @param criteria
	 * @return Map<String, Map<String, Double>>
	 */
	public Map<String, Map<String, Double>> getTaxMap(CalculationReq request) {

		List<CalculationCriteria> criterias = request.getCalculationCriteria();
		// get slabs from mdms

		Map<String, Map<String, Double>> estimationMap = new HashMap<>();

		for (CalculationCriteria criteria : criterias) {

			Property property = criteria.getProperty();
			estimationMap.put(property.getAssessmentNumber(), getCalculatedMap(criteria));
		}
		return estimationMap;
	}


	/**
	 * Prepares Calculation Response based on the provided tax head-Amount map
	 * 
	 * @param criteria
	 * @param calculatedMap
	 * @return
	 */
	private Calculation getcalculation(CalculationCriteria criteria) {

		// fetch one financial year AND use financial year from arguments
		Long fromDate = new Date().getTime();
		Long toDate = new Date().getTime();

		// fetch tax heads and create tax head category map
		Map<String, Category> taxHeadCategoryMap = new HashMap<>();
		// remove after integration
		taxHeadCategoryMap.put("PT_TAX", Category.TAX);
		
		
		// fetch billing slabs
		
		Map<String, Double> calculatedMap = getCalculatedMap(criteria);

		Double totalAmt = Double.valueOf(0.0);
		Double penalty = Double.valueOf(0.0);
		Double exemption = Double.valueOf(0.0);
		Double rebate = Double.valueOf(0.0);

		for (Entry<String, Double> entry : calculatedMap.entrySet()) {

			Category category = taxHeadCategoryMap.get(entry.getKey());
			switch (category) {

			case TAX:
				totalAmt += entry.getValue();
				break;

			case PENALTY:
				penalty += entry.getValue();
				break;

			case REBATE:
				rebate += entry.getValue();
				break;

			case EXEMPTION:
				exemption += entry.getValue();
				break;

			default:
				break;
			}
		}
		return Calculation.builder().totalAmount(totalAmt).penalty(penalty).exemption(exemption).rebate(rebate)
				.fromDate(fromDate).toDate(toDate).tenantId(criteria.getTenantId()).build();
	}
	
	/**
	 * Generates a map with tax head code as key and the amount to be collected
	 * against respective codes
	 * 
	 * @param criteria
	 * @return Map<String, Double>
	 */
	private Map<String, Double> getCalculatedMap(CalculationCriteria criteria /* put billing slabs */) {

		// get slabs from mdms
		Map<String, Double> map = new HashMap<>();
		BigDecimal taxAmt = BigDecimal.ZERO;
		Property property = criteria.getProperty();

		for (Unit unit : property.getPropertyDetail().getUnits()) {
			
			// loop the slabs for each unit until a match is found
			float a = unit.getUnitArea() * 1;
			taxAmt = taxAmt.add(BigDecimal.valueOf(a));
		}
		map.put("PT_TAX", taxAmt.doubleValue());
		// add logic for rebate and penalty
		// how to choose the map keys?
		return map;
	}
}
