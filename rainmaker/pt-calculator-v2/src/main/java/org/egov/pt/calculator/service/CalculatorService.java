package org.egov.pt.calculator.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.egov.common.contract.response.ResponseInfo;
import org.egov.pt.calculator.web.models.Calculation;
import org.egov.pt.calculator.web.models.CalculationCriteria;
import org.egov.pt.calculator.web.models.CalculationReq;
import org.egov.pt.calculator.web.models.CalculationRes;
import org.springframework.stereotype.Service;

@Service
public class CalculatorService {

	public CalculationRes generateTaxes(CalculationReq request) {

		List<CalculationCriteria> criterias = request.getCalculationCriteria();
		List<Calculation> calculations = new ArrayList<>();
		criterias.forEach(criteria -> calculations.add(getcalculationFromMap(criteria, getCalculatedMap(criteria))));
		return CalculationRes.builder().responseInfo(new ResponseInfo()).calculation(calculations).build();
	}

	public Map<String, Double> getCalculatedMap(CalculationCriteria criteria) {

		/*
		 * map will be generated on a lot of factors based on year,location,units,usages etc..,
		 */
		Map<String, Double> map = new HashMap<>();
		map.put("PT_TAX", 1000.0);
		map.put("EMP_GUA_CESS", 1500.0);
		map.put("EDU_CESS", 1000.0);
		return map;
	}

	private Calculation getcalculationFromMap(CalculationCriteria criteria, Map<String, Double> calculatedMap) {

		Set<String> taxHeadCodeSet = calculatedMap.keySet();
		/*
		 * get all the taxheads based on the key set from map and find out the category
		 * to fill in the calculation object
		 * 
		 * replace the current logic with taxhead getCategory logic
		 */

		Double totalAmt = Double.valueOf(0.0);
		for (String code : taxHeadCodeSet) {
			if (code != null)
				totalAmt += calculatedMap.get(code);
		}

		/*
		 * get financial year object to get the from and to date
		 */
		Long fromDate = 0l;
		Long toDate = 0l;
		return Calculation.builder().totalAmount(totalAmt).fromDate(fromDate).toDate(toDate)
				.connectionNumber(criteria.getProperty().getId()).tenantId(criteria.getTenantId()).build();
	}
}
