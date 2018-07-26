package org.egov.pt.calculator.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.pt.calculator.util.CalculatorConstants;
import org.egov.pt.calculator.util.Configurations;
import org.egov.pt.calculator.web.models.BillingSlab;
import org.egov.pt.calculator.web.models.BillingSlabSearchCriteria;
import org.egov.pt.calculator.web.models.Calculation;
import org.egov.pt.calculator.web.models.CalculationCriteria;
import org.egov.pt.calculator.web.models.CalculationReq;
import org.egov.pt.calculator.web.models.CalculationRes;
import org.egov.pt.calculator.web.models.TaxHeadEstimate;
import org.egov.pt.calculator.web.models.demand.Category;
import org.egov.pt.calculator.web.models.demand.TaxHeadMaster;
import org.egov.pt.calculator.web.models.property.OwnerInfo;
import org.egov.pt.calculator.web.models.property.Property;
import org.egov.pt.calculator.web.models.property.PropertyDetail;
import org.egov.pt.calculator.web.models.property.Unit;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;

@Service
@Slf4j
public class EstimationService {

	@Autowired
	private BillingSlabService billingSlabService;

	@Autowired
	private PayService payService;

	@Autowired
	private Configurations configs;

	@Autowired
	private MasterDataService mstrDataService;

	/**
	 * Generates a map with assessmentnumber of property as key and estimation
	 * 
	 * map(taxhead code as key, amount to be paid as value) as value
	 * 
	 * NOTE : This method updates the master data map in masterDataService
	 * 
	 * @param CalculationReq
	 * @return Map<String, Calculation>
	 */
	public Map<String, Calculation> getEstimationPropertyMap(CalculationReq request) {

		RequestInfo requestInfo = request.getRequestInfo();
		List<CalculationCriteria> criterias = request.getCalculationCriteria();
		Map<String, Calculation> calculationPropertyMap = new HashMap<>();
		for (CalculationCriteria criteria : criterias) {
			Property property = criteria.getProperty();
			String assesmentNumber = property.getPropertyDetails().get(0).getAssessmentNumber();
			Calculation calculation = getcalculation(requestInfo, criteria, getEstimationMap(criteria, requestInfo));
			calculation.setServiceNumber(assesmentNumber);
			calculationPropertyMap.put(assesmentNumber, calculation);
		}
		return calculationPropertyMap;
	}

	/**
	 * Method to estimate the tax to be paid for given property
	 * 
	 * @param request
	 * @return CalculationRes
	 */
	public CalculationRes getTaxCalculation(CalculationReq request) {

		CalculationCriteria criteria = request.getCalculationCriteria().get(0);
		return new CalculationRes(new ResponseInfo(), Arrays.asList(getcalculation(request.getRequestInfo(), criteria,
				getEstimationMap(criteria, request.getRequestInfo()))));
	}

	/**
	 * Generates a List of Estimates containing object with tax head code as key and
	 * the amount to be collected as value against the keys
	 * 
	 * @param criteria
	 * @return Map<String, Double>
	 */
	private List<TaxHeadEstimate> getEstimationMap(CalculationCriteria criteria, RequestInfo requestInfo) {

		List<TaxHeadEstimate> estimates = new ArrayList<>();
		String assessmentYear = criteria.getAssessmentYear();
		BigDecimal taxAmt = BigDecimal.ZERO;
		BigDecimal usageExemption = BigDecimal.ZERO;
		Property property = criteria.getProperty();
		PropertyDetail detail = property.getPropertyDetails().get(0);

		List<BillingSlab> filteredbillingSlabs = getSlabsFiltered(property, requestInfo);
		Map<String, Map<String, List<Object>>> propertyBasedExemptionMasterMap = new HashMap<>();
		Map<String, JSONArray> timeBasedExmeptionMasterMap = new HashMap<>();
		mstrDataService.setPropertyMasterValues(requestInfo, criteria.getTenantId(), propertyBasedExemptionMasterMap,
				timeBasedExmeptionMasterMap);

		for (Unit unit : detail.getUnits()) {

			boolean isUnitCommercial = unit.getUsageCategoryMajor()
					.equalsIgnoreCase(configs.getUsageMajorNonResidential());
			boolean isUnitRented = unit.getOccupancyType().equalsIgnoreCase(configs.getOccupancyTypeRented());
			BigDecimal currentUnitTax = null;

			BillingSlab slab = getSlabForCalc(filteredbillingSlabs, unit);
			if (null == slab) {

				Map<String, String> map = new HashMap<>();
				map.put(CalculatorConstants.BILLING_SLAB_MATCH_ERROR_CODE,
						CalculatorConstants.BILLING_SLAB_MATCH_ERROR_MESSAGE
								.replace(CalculatorConstants.BILLING_SLAB_MATCH_AREA, unit.getUnitArea().toString())
								.replace(CalculatorConstants.BILLING_SLAB_MATCH_FLOOR, unit.getFloorNo())
								.replace(CalculatorConstants.BILLING_SLAB_MATCH_USAGE_DETAIL,
										unit.getUsageCategoryDetail()));
				throw new CustomException(map);
			}

			if (isUnitCommercial && isUnitRented) {

				BigDecimal multiplier = null;
				if (null != slab.getArvPercent())
					multiplier = BigDecimal.valueOf(slab.getArvPercent() / 100);
				else
					multiplier = BigDecimal.valueOf(configs.getArvPercent() / 100);
				currentUnitTax = unit.getArv().multiply(multiplier);
			} else {
				currentUnitTax = BigDecimal.valueOf(unit.getUnitArea() * slab.getUnitRate());
			}

			taxAmt = taxAmt.add(currentUnitTax);
			usageExemption = usageExemption
					.add(getExemption(unit, currentUnitTax, assessmentYear, propertyBasedExemptionMasterMap));
		}

		estimates.add(TaxHeadEstimate.builder().taxHeadCode(CalculatorConstants.PT_TAX).estimateAmount(taxAmt).build());
		estimates.add(TaxHeadEstimate.builder().taxHeadCode(CalculatorConstants.PT_UNIT_USAGE_EXEMPTION)
				.estimateAmount(usageExemption).build());
		BigDecimal payableTax = taxAmt.subtract(usageExemption);

		/*
		 * FIXME ADD CESS FIRE
		 */
/*		BigDecimal fireCess = getFireCess(payableTax);
		estimates.add(TaxHeadEstimate.builder().taxHeadCode(CalculatorConstants.PT_FIRE_CESS)
				.estimateAmount(BigDecimal.ZERO).build());
		payableTax = payableTax.add(BigDecimal.ZERO);*/

		BigDecimal userExemption = getExemption(detail.getOwners(), payableTax, assessmentYear,
				propertyBasedExemptionMasterMap);
		estimates.add(TaxHeadEstimate.builder().taxHeadCode(CalculatorConstants.PT_OWNER_EXEMPTION)
				.estimateAmount(userExemption).build());
		payableTax = payableTax.subtract(userExemption);
		/*
		 * get applicable rebate and penalty
		 */
		Map<String, BigDecimal> rebatePenaltyMap = payService.applyPenaltyAndRebate(payableTax, assessmentYear,
				timeBasedExmeptionMasterMap);
		estimates.add(TaxHeadEstimate.builder().taxHeadCode(CalculatorConstants.PT_TIME_REBATE)
				.estimateAmount(rebatePenaltyMap.get(CalculatorConstants.PT_TIME_REBATE)).build());
		estimates.add(TaxHeadEstimate.builder().taxHeadCode(CalculatorConstants.PT_TIME_PENALTY)
				.estimateAmount(rebatePenaltyMap.get(CalculatorConstants.PT_TIME_PENALTY)).build());
		return estimates;
	}

	/**
	 * Prepares Calculation Response based on the provided tax head-Amount map
	 * 
	 * @param criteria
	 * @param calculatedMap
	 * @return
	 */
	private Calculation getcalculation(RequestInfo requestInfo, CalculationCriteria criteria,
			List<TaxHeadEstimate> estimates) {

		String tenantId = criteria.getTenantId();
		Map<String, Object> finYearMap = mstrDataService.getfinancialYear(requestInfo, criteria.getAssessmentYear(),
				tenantId);
		Long fromDate = (Long) finYearMap.get(CalculatorConstants.FINANCIAL_YEAR_STARTING_DATE);
		Long toDate = (Long) finYearMap.get(CalculatorConstants.FINANCIAL_YEAR_ENDING_DATE);
		Map<String, TaxHeadMaster> taxHeadCategoryMap = mstrDataService.getTaxHeadMasterMap(requestInfo, tenantId);
		
		BigDecimal taxAmt = BigDecimal.ZERO;
		BigDecimal penalty = BigDecimal.ZERO;
		BigDecimal exemption = BigDecimal.ZERO;
		BigDecimal rebate = BigDecimal.ZERO;

		for (TaxHeadEstimate estimate : estimates) {

			Category category = taxHeadCategoryMap.get(estimate.getTaxHeadCode()).getCategory();
			estimate.setCategory(category);

			switch (category) {

			case TAX:
				taxAmt = taxAmt.add(estimate.getEstimateAmount());
				break;

			case PENALTY:
				penalty = penalty.add(estimate.getEstimateAmount());
				break;

			case REBATE:
				rebate = rebate.add(estimate.getEstimateAmount());
				break;

			case EXEMPTION:
				exemption = exemption.add(estimate.getEstimateAmount());
				break;

			default:
				taxAmt = taxAmt.add(estimate.getEstimateAmount());
				break;
			}
		}
		return Calculation.builder().totalAmount(taxAmt.add(penalty).subtract(rebate).subtract(exemption))
				.taxAmount(taxAmt).penalty(penalty).exemption(exemption).rebate(rebate).fromDate(fromDate)
				.toDate(toDate).tenantId(criteria.getTenantId()).serviceNumber(criteria.getAssesmentNumber())
				.taxHeadEstimates(estimates).build();
	}

	/**
	 * method to filter the slabs based on the values present in Property detail
	 * object
	 * 
	 */
	private List<BillingSlab> getSlabsFiltered(Property property, RequestInfo requestInfo) {

		PropertyDetail detail = property.getPropertyDetails().get(0);
		String tenantId = property.getTenantId();
		BillingSlabSearchCriteria slabSearchCriteria = BillingSlabSearchCriteria.builder().tenantId(tenantId).build();
		List<BillingSlab> billingSlabs = billingSlabService.searchBillingSlabs(requestInfo, slabSearchCriteria)
				.getBillingSlab();

		log.debug(" the slabs count : " + billingSlabs.size());
		final String all = configs.getSlabValueAll();
		/*
		 * check when the land area should be used
		 */
		Double plotSize = detail.getBuildUpArea();

		final String dtlPtType = detail.getPropertyType();
		final String dtlPtSubType = detail.getPropertySubType();
		final String dtlOwnerShipCat = detail.getOwnershipCategory();
		final String dtlSubOwnerShipCat = detail.getSubOwnershipCategory();
		final String dtlareaType = property.getAddress().getLocality().getArea();
		final Boolean dtlIsMultiFloored = detail.getNoOfFloors() > 1;

		return billingSlabs.stream().filter(slab -> {

			Boolean slabMultiFloored = slab.getIsPropertyMultiFloored();
			String  slabAreatype = slab.getAreaType();
			String  slabPropertyType = slab.getPropertyType();
			String  slabPropertySubType = slab.getPropertySubType();
			String  slabOwnerShipCat = slab.getOwnerShipCategory();
			String  slabSubOwnerShipCat = slab.getSubOwnerShipCategory();
			Double  slabAreaFrom = slab.getFromPlotSize();
			Double  slabAreaTo = slab.getToPlotSize();
			
			boolean isPropertyMultiFloored = dtlIsMultiFloored.equals(slabMultiFloored);
			
			boolean isAreaMatching = dtlareaType.equalsIgnoreCase(slabAreatype);

			boolean isPtTypeMatching = dtlPtType.equalsIgnoreCase(slabPropertyType)
					|| all.equalsIgnoreCase(slabPropertyType);
			boolean isPtSubTypeMactching = dtlPtSubType.equalsIgnoreCase(slabPropertySubType)
					|| all.equalsIgnoreCase(dtlPtSubType);
			boolean isOwnerShipMatching = dtlOwnerShipCat.equalsIgnoreCase(slabOwnerShipCat)
					|| all.equalsIgnoreCase(slabOwnerShipCat);
			boolean isSubOwnerShipMatching = dtlSubOwnerShipCat.equalsIgnoreCase(slabSubOwnerShipCat)
					|| all.equalsIgnoreCase(slabSubOwnerShipCat);
			boolean isPlotMatching = slabAreaFrom <= plotSize && slabAreaTo >= plotSize;

			log.debug(" the slab is : " + slab);
			
			log.debug(" the match is isPtTypeMatching && isPtSubTypeMactching && isOwnerShipMatching && isSubOwnerShipMatching\r\n" + 
					"					&& isPlotMatching && isAreaMatching && isPropertyMultiFloored  :  " + isPtTypeMatching + "  :  "  + isPtSubTypeMactching + "  :  "  + isOwnerShipMatching + "  :  "  + isSubOwnerShipMatching
					+ "  :  "  + isPlotMatching + "  :  "  + isAreaMatching + "  :  "  + isPropertyMultiFloored);
			
			return isPtTypeMatching && isPtSubTypeMactching && isOwnerShipMatching && isSubOwnerShipMatching
					&& isPlotMatching && isAreaMatching && isPropertyMultiFloored;
		}).collect(Collectors.toList());
	}

	/**
	 * Method to get the matching billing slab for the respective unit
	 * 
	 * @param billingSlabs
	 * @param unit
	 * @return
	 */
	private BillingSlab getSlabForCalc(List<BillingSlab> billingSlabs, Unit unit) {

		log.debug(" the remaining slabs : ------------------------ : "+ billingSlabs);
		
		final String all = configs.getSlabValueAll();

		for (BillingSlab billSlb : billingSlabs) {

			Double floorNo = Double.parseDouble(unit.getFloorNo());

			boolean isMajorMatching = billSlb.getUsageCategoryMajor().equalsIgnoreCase(unit.getUsageCategoryMajor())
					|| (billSlb.getUsageCategoryMajor().equalsIgnoreCase(all));

			boolean isMinorMatching = billSlb.getUsageCategoryMinor().equalsIgnoreCase(unit.getUsageCategoryMinor())
					|| (billSlb.getUsageCategoryMinor().equalsIgnoreCase(all));

			boolean isSubMinorMatching = billSlb.getUsageCategorySubMinor().equalsIgnoreCase(
					unit.getUsageCategorySubMinor()) || (billSlb.getUsageCategorySubMinor().equalsIgnoreCase(all));

			boolean isDetailsMatching = billSlb.getUsageCategoryDetail().equalsIgnoreCase(unit.getUsageCategoryDetail())
					|| (billSlb.getUsageCategoryDetail().equalsIgnoreCase(all));

			boolean isFloorMatching = billSlb.getFromFloor() <= floorNo && billSlb.getToFloor() >= floorNo;

			boolean isOccupancyTypeMatching = billSlb.getOccupancyType().equalsIgnoreCase(unit.getOccupancyType());

			log.debug(" the match for each slab :  isMajorMatching && isMinorMatching && isSubMinorMatching && isDetailsMatching && isFloorMatching\r\n" + 
					"					&& isOccupancyTypeMatching " + isMajorMatching + "  :   " + isMinorMatching + "  :   " + isSubMinorMatching + "  :   " + isDetailsMatching + "  :   " + isFloorMatching
					+ "  :   " + isOccupancyTypeMatching);
			
			if (isMajorMatching && isMinorMatching && isSubMinorMatching && isDetailsMatching && isFloorMatching
					&& isOccupancyTypeMatching) {
				log.debug(" The Id of the matching slab : "+ billSlb.getId());
				return billSlb;
			}
		}
		return null;
	}

	/**
	 * Usage based exemptions applied on unit
	 * 
	 * The exemption discount will be applied based on the exemption rate of the
	 * usage types
	 * 
	 * @param unit
	 * @param currentUnitTax
	 * @return
	 */
	private BigDecimal getExemption(Unit unit, BigDecimal currentUnitTax, String financialYear,
			Map<String, Map<String, List<Object>>> propertyMasterDataMap) {

		Map<String, Object> exemption = getExemptionFromUsage(unit, financialYear, propertyMasterDataMap);

		if (null != exemption) {

			Double exempRate = (Double) exemption.get(CalculatorConstants.RATE_FIELD_NAME);
			Double exempMaxRate = (Double) exemption.get(CalculatorConstants.MAX_AMOUNT_FIELD_NAME);
			BigDecimal exempted = null;
			if (null != exempRate) {
				exempted = currentUnitTax.multiply(BigDecimal.valueOf(exempRate / 100));
				if (null != exempMaxRate && !exempMaxRate.equals(100.0)
						&& exempted.compareTo(BigDecimal.valueOf(exempMaxRate)) > 0)
					exempted = BigDecimal.valueOf(exempMaxRate);
			} else
				exempted = BigDecimal.valueOf((Double) exemption.get(CalculatorConstants.FLAT_AMOUNT_FIELD_NAME));
			return exempted;
		} else
			return BigDecimal.ZERO;

	}

	/**
	 * Estimates the fire cess that needs to be paid for the given tax amount
	 * 
	 * @param payableTax
	 * @return
	 */
	private BigDecimal getFireCess(BigDecimal payableTax) {

		
		return null;
	}
	
	/**
	 * Applies discount on Total tax amount OwnerType based exemptions
	 * 
	 * @param owners
	 * @param taxAmt
	 * @return
	 */
	private BigDecimal getExemption(Set<OwnerInfo> owners, BigDecimal taxAmt, String financialYear,
			Map<String, Map<String, List<Object>>> propertyBasedExemptionMasterMap) {

		Map<String, List<Object>> ownerTypeMap = propertyBasedExemptionMasterMap
				.get(CalculatorConstants.OWNER_TYPE_MASTER);
		BigDecimal userExemption = BigDecimal.ZERO;
		final int userCount = owners.size();
		BigDecimal share = taxAmt.divide(BigDecimal.valueOf(userCount));
		for (OwnerInfo owner : owners) {
			
			if(null == owner.getOwnerType())
				continue;
			Map<String, Object> applicableOwnerType = mstrDataService.getApplicableMasterFromList(financialYear,
					ownerTypeMap.get(owner.getOwnerType()));
			if (null != applicableOwnerType) {
				@SuppressWarnings("unchecked")
				Map<String, Object> exemption = (Map<String, Object>) applicableOwnerType
						.get(CalculatorConstants.EXEMPTION_FIELD_NAME);

				Double exemptionRate = (Double) exemption.get(CalculatorConstants.RATE_FIELD_NAME);
				Double exempMaxRate = (Double) exemption.get(CalculatorConstants.MAX_AMOUNT_FIELD_NAME);
				userExemption = userExemption.add(share.multiply(BigDecimal.valueOf(exemptionRate / 100)));
				if (null != exempMaxRate && !exemptionRate.equals(100.0)
						&& userExemption.compareTo(BigDecimal.valueOf(exempMaxRate)) > 0)
					userExemption = BigDecimal.valueOf(exempMaxRate);
			}
		}
		return userExemption;
	}

	/**
	 * Returns the appropriate exemption object from the usage masters
	 * 
	 * Search happens from child (usageCategoryDetail) to parent
	 * (usageCategoryMajor)
	 * 
	 * if any appropriate match is found in getApplicableMasterFromList, then the
	 * exemption object from that master will be returned
	 * 
	 * if no match found null will be returned
	 * 
	 * @param unit
	 * @param financialYear
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Object> getExemptionFromUsage(Unit unit, String financialYear,
			Map<String, Map<String, List<Object>>> proeprtyBasedExemptionMasterMap) {

		Map<String, List<Object>> usageDetails = proeprtyBasedExemptionMasterMap
				.get(CalculatorConstants.USAGE_DETAIL_MASTER);
		Map<String, List<Object>> usageSubMinors = proeprtyBasedExemptionMasterMap
				.get(CalculatorConstants.USAGE_SUB_MINOR_MASTER);
		Map<String, List<Object>> usageMinors = proeprtyBasedExemptionMasterMap
				.get(CalculatorConstants.USAGE_MINOR_MASTER);
		Map<String, List<Object>> usageMajors = proeprtyBasedExemptionMasterMap
				.get(CalculatorConstants.USAGE_MAJOR_MASTER);
		
		Map<String, Object> applicableUsageMaster = null;
		
		if(null != usageDetails.get(unit.getUsageCategoryDetail()))
		 applicableUsageMaster = mstrDataService.getApplicableMasterFromList(financialYear,
				usageDetails.get(unit.getUsageCategoryDetail()));

		if ( null != applicableUsageMaster &&  null == applicableUsageMaster.get(CalculatorConstants.EXEMPTION_FIELD_NAME) && null != usageSubMinors.get(unit.getUsageCategorySubMinor()))
			applicableUsageMaster = mstrDataService.getApplicableMasterFromList(financialYear,
					usageSubMinors.get(unit.getUsageCategorySubMinor()));

		if (null != applicableUsageMaster && null == applicableUsageMaster.get(CalculatorConstants.EXEMPTION_FIELD_NAME) && null != usageMinors.get(unit.getUsageCategoryMinor()))
			applicableUsageMaster = mstrDataService.getApplicableMasterFromList(financialYear,
					usageMinors.get(unit.getUsageCategoryMinor()));
		
		if (null != applicableUsageMaster && null == applicableUsageMaster.get(CalculatorConstants.EXEMPTION_FIELD_NAME) && null != usageMajors.get(unit.getUsageCategoryMajor()))
			applicableUsageMaster = mstrDataService.getApplicableMasterFromList(financialYear,
					usageMajors.get(unit.getUsageCategoryMajor()));

		if (null != applicableUsageMaster)
			return (Map<String, Object>) applicableUsageMaster.get(CalculatorConstants.EXEMPTION_FIELD_NAME);
		return applicableUsageMaster;
	}

}
