package org.egov.pt.calculator.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.egov.pt.calculator.config.CalculatorConfig;
import org.egov.pt.calculator.web.models.Calculation;
import org.egov.pt.calculator.web.models.CalculationCriteria;
import org.egov.pt.calculator.web.models.CalculationReq;
import org.egov.pt.calculator.web.models.TaxHeadEstimate;
import org.egov.pt.calculator.web.models.demand.Demand;
import org.egov.pt.calculator.web.models.demand.DemandDetail;
import org.egov.pt.calculator.web.models.demand.DemandRequest;
import org.egov.pt.calculator.web.models.property.OwnerInfo;
import org.egov.pt.calculator.web.models.property.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DemandService {

	@Autowired
	private EstimationService calculator;

	public Map<String, Calculation> generateDemands(CalculationReq request) {

		List<CalculationCriteria> criterias = request.getCalculationCriteria();
		List<Demand> demands = new ArrayList<>();
		Map<String, Calculation> propertyCalculationMap = null;
		for (CalculationCriteria criteria : criterias) {
			Property property = criteria.getProperty();
			propertyCalculationMap = calculator.getEstimationPropertyMap(request);
			demands.add(getDemand(property,
					propertyCalculationMap.get(property.getPropertyDetails().get(0).getAssessmentNumber())));
		}
		DemandRequest.builder().demands(demands).requestInfo(request.getRequestInfo()).build();
		/*
		 * post demand Request to billing service
		 */
		return propertyCalculationMap;
	}

	private Demand getDemand(Property property, Calculation calculation) {

		String tenantId = property.getTenantId();
		String propertyType = property.getPropertyDetails().get(0).getPropertyType();
		String assessmentNumber = property.getPropertyDetails().get(0).getAssessmentNumber();
		OwnerInfo owner = property.getPropertyDetails().get(0).getOwners().iterator().next();
		List<DemandDetail> details = new ArrayList<>();

		for (TaxHeadEstimate estimate : calculation.getTaxHeadEstimates())
			details.add(DemandDetail.builder().taxHeadMasterCode(estimate.getTaxHeadCode())
					.taxAmount(BigDecimal.valueOf(estimate.getEstimateAmount())).tenantId(tenantId).build());

		/*
		 * change owner info logic to a better one considering all the scenarios
		 */
		return Demand.builder().tenantId(tenantId).businessService(CalculatorConfig.SERVICE_NAME)
				.consumerType(propertyType).consumerCode(assessmentNumber).owner(owner)
				.taxPeriodFrom(calculation.getFromDate()).taxPeriodTo(calculation.getToDate())
				.minimumAmountPayable(CalculatorConfig.MIN_AMT_PAYABLE).demandDetails(details).build();
	}
}
