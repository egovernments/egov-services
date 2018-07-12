package org.egov.pt.calculator.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.common.contract.request.RequestInfo;
import org.egov.pt.calculator.repository.Repository;
import org.egov.pt.calculator.util.CalculatorConstants;
import org.egov.pt.calculator.util.CalculatorUtils;
import org.egov.pt.calculator.util.Configurations;
import org.egov.pt.calculator.web.models.Assessment;
import org.egov.pt.calculator.web.models.Calculation;
import org.egov.pt.calculator.web.models.CalculationCriteria;
import org.egov.pt.calculator.web.models.CalculationReq;
import org.egov.pt.calculator.web.models.GetBillCriteria;
import org.egov.pt.calculator.web.models.TaxHeadEstimate;
import org.egov.pt.calculator.web.models.demand.BillResponse;
import org.egov.pt.calculator.web.models.demand.Demand;
import org.egov.pt.calculator.web.models.demand.DemandDetail;
import org.egov.pt.calculator.web.models.demand.DemandRequest;
import org.egov.pt.calculator.web.models.demand.DemandResponse;
import org.egov.pt.calculator.web.models.property.OwnerInfo;
import org.egov.pt.calculator.web.models.property.Property;
import org.egov.pt.calculator.web.models.property.PropertyDetail;
import org.egov.pt.calculator.web.models.property.RequestInfoWrapper;
import org.egov.tracer.model.ServiceCallException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;

@Service
@Slf4j
public class DemandService {

	@Autowired
	private EstimationService estimationService;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private Configurations configs;

	@Autowired
	private AssessmentService assessmentService;

	@Autowired
	private CalculatorUtils utils;

	@Autowired
	private Repository repository;

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private PayService payService;

	@Autowired
	private MasterDataService mstrDataService;

	/**
	 * Generates and persists the demand to billing service for the given property
	 * 
	 * if the property has been assessed already for the given financial year then
	 * 
	 * it carry forwards the old collection amount to the new demand as advance
	 * 
	 * @param request
	 * @return
	 */
	public Map<String, Calculation> generateDemands(CalculationReq request) {

		List<CalculationCriteria> criterias = request.getCalculationCriteria();
		List<Demand> demands = new ArrayList<>();
		Map<String, Calculation> propertyCalculationMap = null;
		for (CalculationCriteria criteria : criterias) {

			BigDecimal carryForwardCollectedAmount = getCarryForwardAndCancelOldDemand(criteria,
					request.getRequestInfo());
			if (carryForwardCollectedAmount.doubleValue() >= 0.0) {
				Property property = criteria.getProperty();
				propertyCalculationMap = estimationService.getEstimationPropertyMap(request);

				Demand demand = prepareDemand(property,
						propertyCalculationMap.get(property.getPropertyDetails().get(0).getAssessmentNumber()));
				if (carryForwardCollectedAmount.doubleValue() > 0.0)
					demand.getDemandDetails()
							.add(DemandDetail.builder().taxAmount(carryForwardCollectedAmount)
									.tenantId(criteria.getTenantId()).demandId(demand.getId())
									.taxHeadMasterCode(CalculatorConstants.PT_ADVANCE_CARRYFORWARD).build());
				demands.add(demand);
			}else {
				// FIXME if carry forward is in negative then add the property exception
				// assessment cannot be
			}
		}
		DemandRequest dmReq = DemandRequest.builder().demands(demands).requestInfo(request.getRequestInfo()).build();
		String url = new StringBuilder().append(configs.getBillingServiceHost())
				.append(configs.getDemandCreateEndPoint()).toString();
		DemandResponse res = new DemandResponse();

		try {
			res = restTemplate.postForObject(url, dmReq, DemandResponse.class);
		} catch (HttpClientErrorException e) {
			throw new ServiceCallException(e.getResponseBodyAsString());
		}
		log.info(" The demand Response is : " + res);
		assessmentService.saveAssessments(res.getDemands(), request.getRequestInfo());
		return propertyCalculationMap;
	}

	/**
	 * Generates and returns bill from billing service
	 * 
	 * updates the demand with penalty and rebate if applicable before generating
	 * bill
	 * 
	 * NOTE : This method updates the master data map in masterDataService
	 * 
	 * @param getBillCriteria
	 * @param requestInfo
	 * @return
	 */
	public BillResponse getBill(GetBillCriteria getBillCriteria, RequestInfoWrapper requestInfoWrapper) {

		RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();
		Map<String, Map<String, List<Object>>> propertyBasedExemptionMasterMap = new HashMap<>();
		Map<String, JSONArray> timeBasedExmeptionMasterMap = new HashMap<>();
		mstrDataService.setPropertyMasterValues(requestInfo, getBillCriteria.getTenantId(),
				propertyBasedExemptionMasterMap, timeBasedExmeptionMasterMap);

		DemandResponse res = mapper.convertValue(
				repository.fetchResult(utils.getDemandSearchUrl(getBillCriteria), requestInfoWrapper),
				DemandResponse.class);
		
		Demand demand = res.getDemands().get(0);
		applyPenaltyAndRebate(demand, getBillCriteria.getAssessmentYear(), timeBasedExmeptionMasterMap);
		DemandRequest request = DemandRequest.builder().demands(Arrays.asList(demand)).requestInfo(requestInfo).build();
		StringBuilder updateDemandUrl = utils.getUpdateDemandUrl();
		repository.fetchResult(updateDemandUrl, request);
		StringBuilder billGenUrl = utils.getBillGenUrl(getBillCriteria.getTenantId(), demand.getId(), demand.getConsumerCode());
		return mapper.convertValue(repository.fetchResult(billGenUrl, requestInfoWrapper), BillResponse.class);
	}

	/**
	 * if any previous assessments and demands associated with it exists for the
	 * same financial year
	 * 
	 * Then Returns the collected amount of previous demand if the current
	 * assessment is for the current year
	 * 
	 * and cancels the previous demand by updating it's status to inactive
	 * 
	 * @param criteria
	 * @return
	 */
	private BigDecimal getCarryForwardAndCancelOldDemand(CalculationCriteria criteria, RequestInfo requestInfo) {

		Property property = criteria.getProperty();

		BigDecimal carryForward = BigDecimal.ZERO;

		Assessment assessment = Assessment.builder().propertyId(property.getPropertyId())
				.tenantId(property.getTenantId())
				.assessmentYear(property.getPropertyDetails().get(0).getFinancialYear()).build();

		List<Assessment> assessments = assessmentService.getMaxAssessment(assessment);

		if (!CollectionUtils.isEmpty(assessments)) {

			Assessment latestAssessment = assessments.get(0);
			DemandResponse res = mapper.convertValue(
					repository.fetchResult(utils.getDemandSearchUrl(latestAssessment), requestInfo), DemandResponse.class);
			Demand demand = res.getDemands().get(0);

			carryForward = utils.getTotalCollectedAmount(demand);
			// FIXME set the demand status to inactive and update it

			// update demand with status inactive
			DemandRequest request = DemandRequest.builder().demands(Arrays.asList(demand)).requestInfo(requestInfo)
					.build();
			StringBuilder updateDemandUrl = utils.getUpdateDemandUrl();
			repository.fetchResult(updateDemandUrl, request);
		}
		return carryForward;
	}

	/**
	 * Prepares Demand object based on the incoming calculation object and property
	 * 
	 * @param property
	 * @param calculation
	 * @return
	 */
	private Demand prepareDemand(Property property, Calculation calculation) {

		String tenantId = property.getTenantId();
		PropertyDetail detail = property.getPropertyDetails().get(0);
		String propertyType = detail.getPropertyType();
		String consumerCode = property.getPropertyId() + "-" + detail.getAssessmentNumber();
		OwnerInfo owner = detail.getCitizenInfo();
		List<DemandDetail> details = new ArrayList<>();

		for (TaxHeadEstimate estimate : calculation.getTaxHeadEstimates())
			details.add(DemandDetail.builder().taxHeadMasterCode(estimate.getTaxHeadCode())
					.taxAmount(estimate.getEstimateAmount()).tenantId(tenantId).build());

		return Demand.builder().tenantId(tenantId).businessService(configs.getPtModuleCode()).consumerType(propertyType)
				.consumerCode(consumerCode).owner(owner).taxPeriodFrom(calculation.getFromDate())
				.taxPeriodTo(calculation.getToDate())
				.minimumAmountPayable(BigDecimal.valueOf(configs.getPtMinAmountPayable())).demandDetails(details)
				.build();
	}

	/**
	 * Applies Penalty/Rebate to the incoming demands
	 * 
	 * If applied already then the demand details will be updated
	 * 
	 * @param demand
	 * @param assessmentYear
	 * @return
	 */
	private void applyPenaltyAndRebate(Demand demand, String assessmentYear,
			Map<String, JSONArray> timeBasedExmeptionMasterMap) {

		String tenantId = demand.getTenantId();
		String demandId = demand.getId();
		BigDecimal taxAmt = utils.getTaxAmtFromDemand(demand);

		boolean isRebateUpdated = false;
		boolean isPenaltyUpdated = false;
		List<DemandDetail> details = demand.getDemandDetails();
		Map<String, BigDecimal> rebatePenaltyEstimates = payService.applyPenaltyAndRebate(taxAmt, assessmentYear,
				timeBasedExmeptionMasterMap);
		BigDecimal rebate = rebatePenaltyEstimates.get(CalculatorConstants.PT_TIME_REBATE);
		BigDecimal penalty = rebatePenaltyEstimates.get(CalculatorConstants.PT_TIME_PENALTY);

		for (DemandDetail detail : details) {
			if (detail.getTaxHeadMasterCode().equalsIgnoreCase(CalculatorConstants.PT_TIME_REBATE)) {
				detail.setTaxAmount(rebate);
				isRebateUpdated = true;
			} else if (detail.getTaxHeadMasterCode().equalsIgnoreCase(CalculatorConstants.PT_TIME_PENALTY)) {
				detail.setTaxAmount(rebate);
				isPenaltyUpdated = true;
			}
		}
		if (!isPenaltyUpdated && penalty.compareTo(BigDecimal.ZERO) > 0)
			details.add(DemandDetail.builder().taxAmount(penalty).taxHeadMasterCode(CalculatorConstants.PT_TIME_PENALTY)
					.demandId(demandId).tenantId(tenantId).build());
		if (!isRebateUpdated && rebate.compareTo(BigDecimal.ZERO) > 0)
			details.add(DemandDetail.builder().taxAmount(rebate).taxHeadMasterCode(CalculatorConstants.PT_TIME_REBATE)
					.demandId(demandId).tenantId(tenantId).build());
	}
}
