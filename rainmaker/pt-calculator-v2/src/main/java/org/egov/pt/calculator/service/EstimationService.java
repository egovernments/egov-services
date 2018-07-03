package org.egov.pt.calculator.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.mdms.model.MdmsCriteriaReq;
import org.egov.mdms.model.MdmsResponse;
import org.egov.pt.calculator.repository.Repository;
import org.egov.pt.calculator.util.CalculatorConstants;
import org.egov.pt.calculator.util.CalculatorUtils;
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
import org.egov.pt.calculator.web.models.demand.TaxHeadMasterResponse;
import org.egov.pt.calculator.web.models.property.Property;
import org.egov.pt.calculator.web.models.property.PropertyDetail;
import org.egov.pt.calculator.web.models.property.RequestInfoWrapper;
import org.egov.pt.calculator.web.models.property.Unit;
import org.egov.tracer.model.ServiceCallException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EstimationService {

	@Autowired
	private BillingSlabService billingSlabService;

	@Autowired
	private Repository repository;

	@Autowired
	private CalculatorUtils calculatorUtils;

	@Autowired
	private ObjectMapper mapper;
	
	@Autowired
	private Configurations configs;
	
	/**
	 * Generates a map with assessmentnumber of property as key and estimation
	 * map(taxhead code as key, amount to be paid as value) as value
	 * 
	 * @param criteria
	 * @return Map<String, Map<String, Double>>
	 */
	public Map<String, Calculation> getEstimationPropertyMap(CalculationReq request) {

		RequestInfo requestInfo = request.getRequestInfo();
		List<CalculationCriteria> criterias = request.getCalculationCriteria();
		BillingSlabSearchCriteria slabSearcCriteria = BillingSlabSearchCriteria.builder()
				.tenantId(criterias.get(0).getTenantId()).build();

		List<BillingSlab> billingslabs = billingSlabService
				.searchBillingSlabs(request.getRequestInfo(), slabSearcCriteria).getBillingSlab();

		Map<String, Calculation> calculationPropertyMap = new HashMap<>();

		for (CalculationCriteria criteria : criterias) {

			Property property = criteria.getProperty();
			String assesmentNumber = property.getPropertyDetails().get(0).getAssessmentNumber();
			Calculation calculation = getcalculation(requestInfo, criteria, getEstimationMap(criteria, billingslabs));
			calculation.setServiceNumber(assesmentNumber);
			calculationPropertyMap.put(assesmentNumber,calculation);
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
		BillingSlabSearchCriteria slabSearchCriteria = BillingSlabSearchCriteria.builder()
				.tenantId(criteria.getTenantId()).build();
		List<BillingSlab> slabs = billingSlabService.searchBillingSlabs(request.getRequestInfo(), slabSearchCriteria)
				.getBillingSlab();
		return new CalculationRes(new ResponseInfo(),
				Arrays.asList(getcalculation(request.getRequestInfo(), criteria, getEstimationMap(criteria, slabs))));
	}

	/**
	 * Generates a map with tax head code as key and the amount to be collected
	 * against respective codes
	 * 
	 * @param criteria
	 * @return Map<String, Double>
	 */
	List<TaxHeadEstimate> getEstimationMap(CalculationCriteria criteria, List<BillingSlab> billingSlabs) {

		List<TaxHeadEstimate> estimates = new ArrayList<>();

		BigDecimal taxAmt = BigDecimal.ZERO;
		Property property = criteria.getProperty();
		PropertyDetail detail = property.getPropertyDetails().get(0);
		List<BillingSlab> filteredbillingSlabs = getSlabsFiltered(detail, billingSlabs);

		for (Unit unit : detail.getUnits()) {

			boolean isUnitCommercial = unit.getUsageCategoryMajor().equalsIgnoreCase(configs.getUsageMajorNonResidential());
			boolean isUnitRented = unit.getOccupancyType().equalsIgnoreCase(configs.getOccupancyTypeRented());
			BigDecimal currentUnitTax = null;
			BigDecimal currentUsageExemption = null;
			
			if (isUnitCommercial && isUnitRented) {
				currentUnitTax = unit.getArv().multiply(BigDecimal.valueOf(configs.getArvPercent()/ 100));
			} else {
				BillingSlab slab = getSlabForCalc(filteredbillingSlabs, unit);
				if (null == slab)
					throw new ServiceCallException("No Billing slab found for the current unit");
				Double a = unit.getUnitArea() * slab.getUnitRate();
				currentUnitTax = BigDecimal.valueOf(a);
			}
			//currentUsageExemption = currentUnitTax.multiply();
			taxAmt = taxAmt.add(currentUnitTax);
		}

		estimates.add(TaxHeadEstimate.builder().taxHeadCode("PT_TAX").estimateAmount(taxAmt.doubleValue()).build());
		// add logic for rebate and penalty
		// how to choose the map keys?
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
		Map<String, Object> finYearMap = getfinancialYear(requestInfo, criteria.getAssesmentYear(), tenantId);
		Long fromDate = (Long) finYearMap.get(CalculatorConstants.FINANCIAL_YEAR_STARTING_DATE);
		Long toDate = (Long) finYearMap.get(CalculatorConstants.FINANCIAL_YEAR_ENDING_DATE);
		Map<String, TaxHeadMaster> taxHeadCategoryMap = getTaxHeadMasterMap(requestInfo, tenantId);

		Double taxAmt = Double.valueOf(0.0);
		Double penalty = Double.valueOf(0.0);
		Double exemption = Double.valueOf(0.0);
		Double rebate = Double.valueOf(0.0);

		for (TaxHeadEstimate estimate : estimates) {

			Category category = taxHeadCategoryMap.get(estimate.getTaxHeadCode()).getCategory();
			estimate.setCategory(category);
			switch (category) {

			case TAX:
				taxAmt += estimate.getEstimateAmount();
				break;

			case PENALTY:
				penalty += estimate.getEstimateAmount();
				break;

			case REBATE:
				rebate += estimate.getEstimateAmount();
				break;

			case EXEMPTION:
				exemption += estimate.getEstimateAmount();
				break;

			default:
				taxAmt += estimate.getEstimateAmount();
				break;
			}
		}
		return Calculation.builder().totalAmount(taxAmt + penalty - rebate - exemption).taxAmount(taxAmt)
				.penalty(penalty).exemption(exemption).rebate(rebate).fromDate(fromDate).toDate(toDate)
				.tenantId(criteria.getTenantId()).serviceNumber(criteria.getAssesmentNumber())
				.taxHeadEstimates(estimates).build();
	}
	
	/**
	 * method to filter the slabs based on the values present in Property detail object
	 * 
	 */
	private List<BillingSlab> getSlabsFiltered(PropertyDetail detail, List<BillingSlab> billingSlabs) {

		final String all = configs.getSlabValueAll();
		/*
		 *  check when the land area should be used
		 */
		Float plotSize = detail.getBuildUpArea();

		final String dtlPtType = detail.getPropertyType();
		final String dtlPtSubType = detail.getPropertySubType();
		final String dtlOwnerShipCat = detail.getOwnershipCategory();
		final String dtlSubOwnerShipCat = detail.getSubOwnershipCategory();

		billingSlabs.parallelStream().filter(slab -> {

			String slabPropertyType = slab.getPropertyType();
			String slabPropertySubType = slab.getPropertySubType();
			String slabOwnerShipCat = slab.getOwnerShipCategory();
			String slabSubOwnerShipCat = slab.getSubOwnerShipCategory();
			Double slabAreaFrom = slab.getFromPlotSize();
			Double slabAreaTo = slab.getToPlotSize();

			boolean isPtTypeMatching = dtlPtType.equalsIgnoreCase(slabPropertyType)
											|| all.equalsIgnoreCase(slabPropertyType);
			boolean isPtSubTypeMactching = dtlPtSubType.equalsIgnoreCase(slabPropertySubType)
											|| all.equalsIgnoreCase(dtlPtSubType);
			boolean isOwnerShipMatching = dtlOwnerShipCat.equalsIgnoreCase(slabOwnerShipCat)
											|| all.equalsIgnoreCase(slabOwnerShipCat);
			boolean isSubOwnerShipMatching = dtlSubOwnerShipCat.equalsIgnoreCase(slabSubOwnerShipCat)
											|| all.equalsIgnoreCase(slabSubOwnerShipCat);
			boolean isPlotMatching	=	slabAreaFrom<=plotSize && slabAreaTo>=plotSize;
			
			return isPtTypeMatching && isPtSubTypeMactching && isOwnerShipMatching && isSubOwnerShipMatching
					&& isPlotMatching;
		}).collect(Collectors.toList());

		return billingSlabs;
	}

	private BillingSlab getSlabForCalc(List<BillingSlab> billingSlabs, Unit unit) {

		final String all = configs.getSlabValueAll();
		
		for (BillingSlab billSlb : billingSlabs) {

			Double floorNo = Double.parseDouble(unit.getFloorNo());

			boolean isMajorMatching = billSlb.getUsageCategoryMajor().equalsIgnoreCase(unit.getUsageCategoryMajor())
					|| (billSlb.getUsageCategoryMajor().equalsIgnoreCase(all));

			boolean isMinorMatching = billSlb.getUsageCategoryMinor().equalsIgnoreCase(unit.getUsageCategoryMinor())
					|| (billSlb.getUsageCategoryMinor().equalsIgnoreCase(all));

			boolean isSubMinorMatching = billSlb.getUsageCategorySubMinor()
					.equalsIgnoreCase(unit.getUsageCategorySubMinor())
					|| (billSlb.getUsageCategorySubMinor().equalsIgnoreCase(all));

			boolean isDetailsMatching = billSlb.getUsageCategoryDetail().equalsIgnoreCase(
					unit.getUsageCategoryDetail()) || (billSlb.getUsageCategoryDetail().equalsIgnoreCase(all));

			boolean isFloorMatching = billSlb.getFromFloor() <= floorNo && billSlb.getToFloor() >= floorNo;

			boolean isOccupancyTypeMatching = billSlb.getOccupancyType().equalsIgnoreCase(unit.getOccupancyType());

			log.info(" is major matching : " + isMajorMatching + " isMinorMatching : " + isMinorMatching
					+ " isSubMinorMatching : " + isSubMinorMatching + " isDetailsMatching : " + isDetailsMatching
					+ " isFloorMatching : " + isFloorMatching + " isOccupancyTypeMatching : "
					+ isOccupancyTypeMatching);
			
			if (isMajorMatching && isMinorMatching && isSubMinorMatching && isDetailsMatching && isFloorMatching
					&& isOccupancyTypeMatching)
				return billSlb;
		}
		return null;
	}


	@SuppressWarnings("unchecked")
	private Map<String, Object> getfinancialYear(RequestInfo requestInfo, String assesmentYear, String tenantId) {

		// FIXME remove harcoding after putting data for pb
		tenantId = "mh";

		MdmsCriteriaReq mdmsCriteriaReq = calculatorUtils.getFinancialYearRequest(requestInfo, assesmentYear, tenantId);
		StringBuilder url = calculatorUtils.getMdmsSearchUrl();
		MdmsResponse res = mapper.convertValue(repository.fetchResult(url, mdmsCriteriaReq), MdmsResponse.class);
		return (Map<String, Object>) res.getMdmsRes().get(CalculatorConstants.FINANCIAL_MODULE)
				.get(CalculatorConstants.FINANCIAL_YEAR_MASTER).get(0);
	}

	private Map<String, TaxHeadMaster> getTaxHeadMasterMap(RequestInfo requestInfo, String tenantId) {

		// FIXME remove harcoding after putting data for pb
		tenantId = "mh.roha";

		StringBuilder uri = calculatorUtils.getTaxHeadSearchUrl(tenantId);
		TaxHeadMasterResponse res = mapper.convertValue(
				repository.fetchResult(uri, RequestInfoWrapper.builder().requestInfo(requestInfo).build()),
				TaxHeadMasterResponse.class);
		return res.getTaxHeadMasters().parallelStream()
				.collect(Collectors.toMap(TaxHeadMaster::getCode, Function.identity()));
	}

}
