package org.egov.pt.calculator.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.pt.calculator.web.models.CalculationCriteria;
import org.egov.pt.calculator.web.models.property.BillingSlab;
import org.egov.pt.calculator.web.models.property.Property;
import org.egov.pt.calculator.web.models.property.Unit;
import org.springframework.stereotype.Service;

@Service
public class EstimationService {

	
	/**
	 * Generates a map with tax head code as key and the amount to be collected
	 * against respective codes
	 * 
	 * @param criteria
	 * @return Map<String, Double>
	 */
	Map<String, Double> getEstimationMap(CalculationCriteria criteria, List<BillingSlab> billingSlabs) {

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
