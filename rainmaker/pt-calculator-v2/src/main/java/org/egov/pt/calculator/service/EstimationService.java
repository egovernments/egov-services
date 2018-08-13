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
import org.springframework.util.CollectionUtils;

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
	 * will be called by calculate api
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
	 * will be called by estimate api 
	 * 
	 * @param request
	 * @return CalculationRes
	 */
	public CalculationRes getTaxCalculation(CalculationReq request) {

		CalculationCriteria criteria = request.getCalculationCriteria().get(0);
		
		Property property = criteria.getProperty();
		PropertyDetail detail = property.getPropertyDetails().get(0);
		
		Map<String, String> error = new HashMap<>();
		boolean isLandProperty = CalculatorConstants.PT_TYPE_VACANT_LAND.equalsIgnoreCase(detail.getPropertyType());

		if (null == detail.getLandArea() && null == detail.getBuildUpArea())
			error.put(CalculatorConstants.PT_ESTIMATE_AREA_NULL, CalculatorConstants.PT_ESTIMATE_AREA_NULL_MSG);

		if (isLandProperty && null == detail.getLandArea())
			error.put(CalculatorConstants.PT_ESTIMATE_VACANT_LAND_NULL, CalculatorConstants.PT_ESTIMATE_VACANT_LAND_NULL_MSG);

		if (!isLandProperty && CollectionUtils.isEmpty(detail.getUnits()))
			error.put(CalculatorConstants.PT_ESTIMATE_NON_VACANT_LAND_UNITS, CalculatorConstants.PT_ESTIMATE_NON_VACANT_LAND_UNITS_MSG);

		if(!CollectionUtils.isEmpty(error))
			throw new CustomException(error);
		
		return new CalculationRes(new ResponseInfo(), Arrays.asList(getcalculation(request.getRequestInfo(), criteria,
				getEstimationMap(criteria, request.getRequestInfo()))));
	}

	/**
	 * Generates a List of Tax head estimates with tax head code, 
	 * 
	 * tax head category and the amount to be collected for the key.
	 * 
	 * @param criteria
	 * @return Map<String, Double>
	 */
	private List<TaxHeadEstimate> getEstimationMap(CalculationCriteria criteria, RequestInfo requestInfo) {

		BigDecimal taxAmt = BigDecimal.ZERO;
		BigDecimal usageExemption = BigDecimal.ZERO;
		Property property = criteria.getProperty();
		PropertyDetail detail = property.getPropertyDetails().get(0);
		String assessmentYear =  detail.getFinancialYear();
		String tenantId       =  property.getTenantId();
		
		List<BillingSlab> filteredbillingSlabs = getSlabsFiltered(property, requestInfo);
		
		Map<String, Map<String, List<Object>>> propertyBasedExemptionMasterMap = new HashMap<>();
		Map<String, JSONArray> timeBasedExmeptionMasterMap = new HashMap<>();
		mstrDataService.setPropertyMasterValues(requestInfo, tenantId, propertyBasedExemptionMasterMap,
				timeBasedExmeptionMasterMap);

		/*
		 * by default land should get only one slab from database per tenantId
		 */
		if (CalculatorConstants.PT_TYPE_VACANT_LAND.equalsIgnoreCase(detail.getPropertyType())
				&& filteredbillingSlabs.size() != 1)
			throw new CustomException(CalculatorConstants.PT_ESTIMATE_BILLINGSLABS_UNMATCH_VACANCT,
					CalculatorConstants.PT_ESTIMATE_BILLINGSLABS_UNMATCH_VACANT_MSG.replace("{count}",
							String.valueOf(filteredbillingSlabs.size())));

		else if (CalculatorConstants.PT_TYPE_VACANT_LAND.equalsIgnoreCase(detail.getPropertyType())) {
			taxAmt = taxAmt.add(BigDecimal.valueOf(filteredbillingSlabs.get(0).getUnitRate() * detail.getLandArea()));
		} else {

			double unBuiltRate = 0.0;
			int groundUnitsCount = 0;
			Double groundUnitsArea = 0.0;
			
			for (Unit unit : detail.getUnits()) {
				
				BillingSlab slab = getSlabForCalc(filteredbillingSlabs, unit);
				BigDecimal currentUnitTax = getTaxForUnit(slab, unit);
				
				/*
				 * counting the number of units & total area in ground floor for unbuilt area tax calculation
				 */
				if (unit.getFloorNo().equalsIgnoreCase("0")) {
					groundUnitsCount += 1;
					groundUnitsArea += unit.getUnitArea();
					if (null != slab.getUnBuiltUnitRate())
						unBuiltRate += slab.getUnBuiltUnitRate();
				}

				taxAmt = taxAmt.add(currentUnitTax);
				usageExemption = usageExemption
						.add(getExemption(unit, currentUnitTax, assessmentYear, propertyBasedExemptionMasterMap));
			}
			/*
			 * making call to get unbuilt area tax estimate
			 */
			taxAmt = taxAmt.add(getUnBuiltRate( detail, unBuiltRate, groundUnitsCount, groundUnitsArea));
		}
		return getEstimatesForTax(assessmentYear, taxAmt, usageExemption, detail, propertyBasedExemptionMasterMap,
				timeBasedExmeptionMasterMap);
	}

	/**
	 * Private method to calculate the unbuilt area tax estimate
	 * 
	 * gives the subtraction of landArea and buildUpArea if both are present.
	 * 
	 * on absence of landArea Zero will be given.
	 * 
	 * on absence of buildUpArea sum of all unit areas of ground floor
	 * 
	 * will be subtracted from the landArea.
	 * 
	 * the unBuilUnitRate is the average of unBuilt rates from ground units.
	 * 
	 * @param detail
	 * @param unBuiltRate
	 * @param groundUnitsCount
	 * @param groundUnitsArea
	 * @return
	 */
	private BigDecimal getUnBuiltRate(PropertyDetail detail, double unBuiltRate, int groundUnitsCount,
			Double groundUnitsArea) {

		BigDecimal unBuiltAmt = BigDecimal.ZERO;

		if (0.0 < unBuiltRate && null != detail.getLandArea() && groundUnitsCount > 0) {

			Double diffArea = null != detail.getBuildUpArea() ? detail.getLandArea() - detail.getBuildUpArea()
					: detail.getLandArea() - groundUnitsArea;
			unBuiltAmt = unBuiltAmt.add(BigDecimal.valueOf((unBuiltRate/groundUnitsCount) * (diffArea)));
		}
		return unBuiltAmt;
	}

	/**
	 * Returns Tax amount value for the unit from the list of slabs passed
	 * 
	 * The tax is dependent on the unit rate and unit area for all cases
	 * 
	 * except for commercial units which is rented, for this a percent will
	 * 
	 * be applied on the annual rent value from the slab.
	 * 
	 * arvPercent is not provided in the slab, it will be picked from the config
	 * 
	 * which is common for the slab.
	 * 
	 * @param filteredbillingSlabs
	 * @param unit
	 * @return
	 */
	private BigDecimal getTaxForUnit(BillingSlab slab, Unit unit) {
		boolean isUnitCommercial = unit.getUsageCategoryMajor()
				.equalsIgnoreCase(configs.getUsageMajorNonResidential());
		boolean isUnitRented = unit.getOccupancyType().equalsIgnoreCase(configs.getOccupancyTypeRented());
		BigDecimal currentUnitTax = null;

		if (null == slab) {

			Map<String, String> map = new HashMap<>();
			map.put(CalculatorConstants.BILLING_SLAB_MATCH_ERROR_CODE,
					CalculatorConstants.BILLING_SLAB_MATCH_ERROR_MESSAGE
							.replace(CalculatorConstants.BILLING_SLAB_MATCH_AREA, unit.getUnitArea().toString())
							.replace(CalculatorConstants.BILLING_SLAB_MATCH_FLOOR, unit.getFloorNo())
							.replace(CalculatorConstants.BILLING_SLAB_MATCH_USAGE_DETAIL,
									null!= unit.getUsageCategoryDetail() ? unit.getUsageCategoryDetail() : "nill"));
			throw new CustomException(map);
		}

		if (isUnitCommercial && isUnitRented) {

			if (unit.getArv() == null) {
				Map<String, String> map = new HashMap<>();
				map.put("EG_PT_ESTIMATE_ARV_NULL", "Arv field is required for Commercial plus Rented properties");
				throw new CustomException(map);
			}

			BigDecimal multiplier = null;
			if (null != slab.getArvPercent())
				multiplier = BigDecimal.valueOf(slab.getArvPercent() / 100);
			else
				multiplier = BigDecimal.valueOf(configs.getArvPercent() / 100);
			currentUnitTax = unit.getArv().multiply(multiplier);
		} else {
			currentUnitTax = BigDecimal.valueOf(unit.getUnitArea() * slab.getUnitRate());
		}
		return currentUnitTax;
	}

	/**
	 * Return an Estimate list containing all the required tax heads
	 * mapped with respective amt to be paid.
	 * 
	 * @param estimates
	 * @param assessmentYear
	 * @param taxAmt
	 * @param usageExemption
	 * @param detail propertyDetail
	 * @param propertyBasedExemptionMasterMap
	 * @param timeBasedExmeptionMasterMap
	 */
	private List<TaxHeadEstimate> getEstimatesForTax(String assessmentYear, BigDecimal taxAmt, BigDecimal usageExemption, PropertyDetail detail,
			Map<String, Map<String, List<Object>>> propertyBasedExemptionMasterMap,
			Map<String, JSONArray> timeBasedExmeptionMasterMap) {

		BigDecimal payableTax = taxAmt;
		List<TaxHeadEstimate> estimates = new ArrayList<>();

		// AdHoc Values (additional rebate or penalty manually entered by the employee)
		if (null != detail.getAdhocPenalty())
			estimates.add(TaxHeadEstimate.builder().taxHeadCode(CalculatorConstants.PT_ADHOC_PENALTY)
					.estimateAmount(detail.getAdhocPenalty()).build());

		// taxes
		estimates.add(TaxHeadEstimate.builder().taxHeadCode(CalculatorConstants.PT_TAX)
				.estimateAmount(taxAmt.setScale(2, 2)).build());

		// usage exemption
		estimates.add(TaxHeadEstimate.builder().taxHeadCode(CalculatorConstants.PT_UNIT_USAGE_EXEMPTION)
				.estimateAmount(usageExemption.setScale(2, 2)).build());
		payableTax = payableTax.subtract(usageExemption);

		// owner exemption
		BigDecimal userExemption = getExemption(detail.getOwners(), payableTax, assessmentYear,
				propertyBasedExemptionMasterMap);
		estimates.add(TaxHeadEstimate.builder().taxHeadCode(CalculatorConstants.PT_OWNER_EXEMPTION)
				.estimateAmount(userExemption.setScale(2, 2)).build());
		payableTax = payableTax.subtract(userExemption);
		
		// Fire cess
		BigDecimal fireCess = mstrDataService.getFireCess(payableTax, assessmentYear, timeBasedExmeptionMasterMap);
		estimates.add(TaxHeadEstimate.builder().taxHeadCode(CalculatorConstants.PT_FIRE_CESS)
				.estimateAmount(fireCess.setScale(2, 2)).build());
		payableTax = payableTax.add(fireCess);

		if (null != detail.getAdhocExemption() && detail.getAdhocExemption().compareTo(payableTax) <= 0) {
			estimates.add(TaxHeadEstimate.builder().taxHeadCode(CalculatorConstants.PT_ADHOC_REBATE)
					.estimateAmount(detail.getAdhocExemption()).build());
		} else if (null != detail.getAdhocExemption()) {
			throw new CustomException(CalculatorConstants.PT_ADHOC_REBATE_INVALID_AMOUNT,
					CalculatorConstants.PT_ADHOC_REBATE_INVALID_AMOUNT_MSG + taxAmt);
		}
		
		/*
		 * get applicable rebate and penalty
		 */
		Map<String, BigDecimal> rebatePenaltyMap = payService.applyPenaltyRebateAndInterest(payableTax, BigDecimal.ZERO, 0l, assessmentYear,
				timeBasedExmeptionMasterMap);

		if(null == rebatePenaltyMap) return estimates;
		
		estimates.add(TaxHeadEstimate.builder().taxHeadCode(CalculatorConstants.PT_TIME_REBATE)
				.estimateAmount(rebatePenaltyMap.get(CalculatorConstants.PT_TIME_REBATE)).build());
		estimates.add(TaxHeadEstimate.builder().taxHeadCode(CalculatorConstants.PT_TIME_PENALTY)
				.estimateAmount(rebatePenaltyMap.get(CalculatorConstants.PT_TIME_PENALTY)).build());
		estimates.add(TaxHeadEstimate.builder().taxHeadCode(CalculatorConstants.PT_TIME_INTEREST)
				.estimateAmount(rebatePenaltyMap.get(CalculatorConstants.PT_TIME_INTEREST)).build());

		return estimates;
	}

	/**
	 * Prepares Calculation Response based on the provided TaxHeadEstimate List
	 * 
	 * All the credit taxHeads will be payable and all debit tax heads will be deducted.
	 * 
	 * @param criteria
	 * @param calculatedMap
	 * @return
	 */
	private Calculation getcalculation(RequestInfo requestInfo, CalculationCriteria criteria,
			List<TaxHeadEstimate> estimates) {

		Property property = criteria.getProperty();
		PropertyDetail detail = property.getPropertyDetails().get(0);
		
		String assessmentYear = null != criteria.getAssessmentYear() ? criteria.getAssessmentYear() : detail.getFinancialYear();
		String assessmentNumber = null != criteria.getAssesmentNumber() ? criteria.getAssesmentNumber() : detail.getAssessmentNumber();
		String tenantId       = null != criteria.getTenantId() ? criteria.getTenantId() : property.getTenantId();
		

		Map<String, Object> finYearMap = mstrDataService.getfinancialYear(requestInfo, assessmentYear,
				tenantId);
		Long fromDate = (Long) finYearMap.get(CalculatorConstants.FINANCIAL_YEAR_STARTING_DATE);
		Long toDate = (Long) finYearMap.get(CalculatorConstants.FINANCIAL_YEAR_ENDING_DATE);
		Map<String, Category> taxHeadCategoryMap = mstrDataService.getTaxHeadMasterMap(requestInfo, tenantId).stream()
				.collect(Collectors.toMap(TaxHeadMaster::getCode, TaxHeadMaster::getCategory));

		BigDecimal taxAmt = BigDecimal.ZERO;
		BigDecimal penalty = BigDecimal.ZERO;
		BigDecimal exemption = BigDecimal.ZERO;
		BigDecimal rebate = BigDecimal.ZERO;

		for (TaxHeadEstimate estimate : estimates) {

			Category category = taxHeadCategoryMap.get(estimate.getTaxHeadCode());
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
		
		TaxHeadEstimate decimalEstimate = payService.roundOfDecimals(taxAmt.add(penalty), rebate.add(exemption));
		
		if (null != decimalEstimate) {

			estimates.add(decimalEstimate);
			if (CalculatorConstants.PT_DECIMAL_CEILING_CREDIT.equalsIgnoreCase(decimalEstimate.getTaxHeadCode()))
				taxAmt = taxAmt.add(decimalEstimate.getEstimateAmount());
			else
				rebate = rebate.add(decimalEstimate.getEstimateAmount());
		}

		return Calculation.builder().totalAmount(taxAmt.add(penalty).subtract(rebate).subtract(exemption))
				.taxAmount(taxAmt).penalty(penalty).exemption(exemption).rebate(rebate).fromDate(fromDate)
				.toDate(toDate).tenantId(tenantId).serviceNumber(assessmentNumber)
				.taxHeadEstimates(estimates).build();
	}

	/**
	 * method to do a first level filtering on the slabs based on the values present in Property detail
	 */
	private List<BillingSlab> getSlabsFiltered(Property property, RequestInfo requestInfo) {

		PropertyDetail detail = property.getPropertyDetails().get(0);
		String tenantId = property.getTenantId();
		BillingSlabSearchCriteria slabSearchCriteria = BillingSlabSearchCriteria.builder().tenantId(tenantId).build();
		List<BillingSlab> billingSlabs = billingSlabService.searchBillingSlabs(requestInfo, slabSearchCriteria)
				.getBillingSlab();

		log.debug(" the slabs count : " + billingSlabs.size());
		final String all = configs.getSlabValueAll();

		Double plotSize = null != detail.getLandArea() ? detail.getLandArea() : detail.getBuildUpArea();

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
			
			boolean isPropertyMultiFloored = slabMultiFloored.equals(dtlIsMultiFloored);
			
			boolean isAreaMatching = slabAreatype.equalsIgnoreCase(dtlareaType) || all.equalsIgnoreCase(slab.getAreaType());

			boolean isPtTypeMatching = slabPropertyType.equalsIgnoreCase(dtlPtType);
			
			boolean isPtSubTypeMactching = slabPropertySubType.equalsIgnoreCase(dtlPtSubType)
					|| all.equalsIgnoreCase(slabPropertySubType);
			
			boolean isOwnerShipMatching = slabOwnerShipCat.equalsIgnoreCase(dtlOwnerShipCat)
					|| all.equalsIgnoreCase(slabOwnerShipCat);
			
			boolean isSubOwnerShipMatching = slabSubOwnerShipCat.equalsIgnoreCase(dtlSubOwnerShipCat)
					|| all.equalsIgnoreCase(slabSubOwnerShipCat);
			
			boolean isPlotMatching = slabAreaFrom <= plotSize && slabAreaTo >= plotSize;
			
			log.debug(" the match is isPtTypeMatching && isPtSubTypeMactching && isOwnerShipMatching && isSubOwnerShipMatching\r\n" 
					+ "	&& isPlotMatching && isAreaMatching && isPropertyMultiFloored  :  " + isPtTypeMatching 
					+ "  :  "  + isPtSubTypeMactching + "  :  "  + isOwnerShipMatching + "  :  " 
					+ isSubOwnerShipMatching + "  :  "  + isPlotMatching + "  :  "  + isAreaMatching 
					+ "  :  "  + isPropertyMultiFloored);
			
			return isPtTypeMatching && isPtSubTypeMactching && isOwnerShipMatching && isSubOwnerShipMatching
					&& isPlotMatching && isAreaMatching && isPropertyMultiFloored;
		}).collect(Collectors.toList());
	}

	/**
	 * Second level filtering to get the matching billing slab for the respective unit
	 * will return only one slab per unit.
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

			log.debug( " the match for each slab :  isMajorMatching && isMinorMatching && isSubMinorMatching &&"
					+ " isDetailsMatching &&" + " isFloorMatching\r\n" + " && isOccupancyTypeMatching " 
					+ isMajorMatching + "  :  "	+ isMinorMatching + "  :  " + isSubMinorMatching 
					+ "  :  " + isDetailsMatching + "  :   "+ isFloorMatching + "  :   " + isOccupancyTypeMatching);

			if (isMajorMatching && isMinorMatching && isSubMinorMatching && isDetailsMatching && isFloorMatching
					&& isOccupancyTypeMatching) {
				log.debug(" The Id of the matching slab : " + billSlb.getId());
				return billSlb;
			}
		}
		return null;
	}

	/**
	 * Usage based exemptions applied on unit.
	 * 
	 * The exemption discount will be applied based on the exemption rate of the
	 * usage master types.
	 */
	private BigDecimal getExemption(Unit unit, BigDecimal currentUnitTax, String financialYear,
			Map<String, Map<String, List<Object>>> propertyMasterMap) {

		Map<String, Object> exemption = getExemptionFromUsage(unit, financialYear, propertyMasterMap);
		return mstrDataService.calculateApplicables(currentUnitTax, exemption);
	}
	
	/**
	 * Applies discount on Total tax amount OwnerType based on exemptions.
	 */
	private BigDecimal getExemption(Set<OwnerInfo> owners, BigDecimal taxAmt, String financialYear,
			Map<String, Map<String, List<Object>>> propertyMasterMap) {

		Map<String, List<Object>> ownerTypeMap = propertyMasterMap.get(CalculatorConstants.OWNER_TYPE_MASTER);
		BigDecimal userExemption = BigDecimal.ZERO;
		final int userCount = owners.size();
		BigDecimal share = taxAmt.divide(BigDecimal.valueOf(userCount));

		for (OwnerInfo owner : owners) {

			if (null == ownerTypeMap.get(owner.getOwnerType()))
				continue;

			Map<String, Object> applicableOwnerType = mstrDataService.getApplicableMaster(financialYear,
					ownerTypeMap.get(owner.getOwnerType()));

			if (null != applicableOwnerType) {

				BigDecimal currentExemption = mstrDataService.calculateApplicables(share,
						applicableOwnerType.get(CalculatorConstants.EXEMPTION_FIELD_NAME));

				userExemption = userExemption.add(currentExemption);
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
	 * if no match found(for all the four usages) then null will be returned
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
		
		Map<String, Object> applicableUsageMasterexemption = null;
		
		if (null != usageDetails.get(unit.getUsageCategoryDetail()))
			applicableUsageMasterexemption = mstrDataService.getApplicableMaster(financialYear,
					usageDetails.get(unit.getUsageCategoryDetail()));

		if (isExemptionNull(applicableUsageMasterexemption)
				&& null != usageSubMinors.get(unit.getUsageCategorySubMinor()))
			applicableUsageMasterexemption = mstrDataService.getApplicableMaster(financialYear,
					usageSubMinors.get(unit.getUsageCategorySubMinor()));

		if (isExemptionNull(applicableUsageMasterexemption) && null != usageMinors.get(unit.getUsageCategoryMinor()))
			applicableUsageMasterexemption = mstrDataService.getApplicableMaster(financialYear,
					usageMinors.get(unit.getUsageCategoryMinor()));

		if (isExemptionNull(applicableUsageMasterexemption) && null != usageMajors.get(unit.getUsageCategoryMajor()))
			applicableUsageMasterexemption = mstrDataService.getApplicableMaster(financialYear,
					usageMajors.get(unit.getUsageCategoryMajor()));

		if (null != applicableUsageMasterexemption)
			return (Map<String, Object>) applicableUsageMasterexemption.get(CalculatorConstants.EXEMPTION_FIELD_NAME);
		return applicableUsageMasterexemption;
	}
	
	private boolean isExemptionNull(Map<String, Object> applicableUsageMasterexemption) {

		return (null != applicableUsageMasterexemption
				&& null == applicableUsageMasterexemption.get(CalculatorConstants.EXEMPTION_FIELD_NAME))
				|| null == applicableUsageMasterexemption;
	}
}
