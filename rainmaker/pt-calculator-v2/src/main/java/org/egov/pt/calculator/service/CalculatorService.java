package org.egov.pt.calculator.service;

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
import org.egov.pt.calculator.web.models.property.BillingSlab;
import org.egov.pt.calculator.web.models.property.BillingSlabSearchCriteria;
import org.egov.pt.calculator.web.models.property.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CalculatorService {

	@Autowired
	private EstimationService estimationService;

	@Autowired
	private BillingSlabService billingSlabService;

	/**
	 * Generates a map with assessmentnumber of property as key and estimation
	 * map(taxhead code as key, amt to be paid as value) as value
	 * 
	 * @param criteria
	 * @return Map<String, Map<String, Double>>
	 */
	public Map<String, Map<String, Double>> getEstimationPropertyMap(CalculationReq request) {

		List<CalculationCriteria> criterias = request.getCalculationCriteria();
		BillingSlabSearchCriteria slabSearcCriteria = BillingSlabSearchCriteria.builder()
				.tenantId(criterias.get(0).getTenantId()).build();

		List<BillingSlab> billingslabs = billingSlabService
				.searchBillingSlabs(request.getRequestInfo(), slabSearcCriteria).getBillingSlab();

		Map<String, Map<String, Double>> estimationPropertyMap = new HashMap<>();

		for (CalculationCriteria criteria : criterias) {

			Property property = criteria.getProperty();
			estimationPropertyMap.put(property.getAssessmentNumber(), estimationService.getEstimationMap(criteria, billingslabs));
		}
		return estimationPropertyMap;
	}

	/**
	 * Method to generate the estimated tax to be paid for the given property
	 * 
	 * @param request
	 * @return CalculationRes
	 */
	public CalculationRes getTaxCalculation(CalculationReq request) {

		CalculationCriteria criteria = request.getCalculationCriteria().get(0);
		List<Calculation> calcList = new ArrayList<>();
		calcList.add(getcalculation(criteria));
		return CalculationRes.builder().responseInfo(new ResponseInfo()).calculation(calcList).build();
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
		
		BillingSlabSearchCriteria slabSearcCriteria = BillingSlabSearchCriteria.builder()
				.tenantId(criteria.getTenantId()).build();

		List<BillingSlab> billingslabs = billingSlabService.searchBillingSlabs(null, slabSearcCriteria)
				.getBillingSlab();
		
		Map<String, Double> calculatedMap = estimationService.getEstimationMap(criteria, billingslabs);

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
	
}
