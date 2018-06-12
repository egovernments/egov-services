package org.egov.pt.calculator.validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.egov.mdms.model.MdmsCriteriaReq;
import org.egov.pt.calculator.web.models.property.BillingSlab;
import org.egov.pt.calculator.web.models.property.BillingSlabReq;
import org.egov.pt.calculator.web.models.property.BillingSlabRes;
import org.egov.pt.calculator.web.models.property.BillingSlabSearchCriteria;
import org.egov.tracer.model.CustomException;
import org.egov.pt.calculator.repository.PTCalculatorRepository;
import org.egov.pt.calculator.service.BillingSlabService;
import org.egov.pt.calculator.util.BillingSlabConstants;
import org.egov.pt.calculator.util.BillingSlabUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.jayway.jsonpath.JsonPath;

import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;

@Service
@Slf4j
public class BillingSlabValidator {

	@Autowired
	private BillingSlabUtils billingSlabUtils;

	@Autowired
	private PTCalculatorRepository repository;

	@Autowired
	private BillingSlabService billingSlabService;

	public void validateCreate(BillingSlabReq billingSlabReq) {
		Map<String, String> errorMap = new HashMap<>();
		runCommonChecks(billingSlabReq, errorMap);
		validateDuplicateBillingSlabs(billingSlabReq, errorMap);
		fetchAndvalidateMDMSCodes(billingSlabReq, errorMap);
		if (!CollectionUtils.isEmpty(errorMap)) {
			throw new CustomException(errorMap);
		}
	}

	public void validateUpdate(BillingSlabReq billingSlabReq) {
		Map<String, String> errorMap = new HashMap<>();
		runCommonChecks(billingSlabReq, errorMap);
		checkIfBillingSlabsExist(billingSlabReq, errorMap);
		fetchAndvalidateMDMSCodes(billingSlabReq, errorMap);
		if (!CollectionUtils.isEmpty(errorMap)) {
			throw new CustomException(errorMap);
		}
	}
	
	public void runCommonChecks(BillingSlabReq billingSlabReq, Map<String, String> errorMap) {
		Set<String> tenantIds = billingSlabReq.getBillingSlab().parallelStream().map(BillingSlab::getTenantId)
				.collect(Collectors.toSet());
		if(tenantIds.size() < 1)
			errorMap.put("EG_PT_INVALID_INPUT", "Input must have atleast once billing slab");
		else if(tenantIds.size() > 1)
			errorMap.put("EG_PT_INVALID_INPUT", "All billing slabs must belong to same tenant");
		
		if (!CollectionUtils.isEmpty(errorMap)) {
			throw new CustomException(errorMap);
		}
	}

	public void checkIfBillingSlabsExist(BillingSlabReq billingSlabReq, Map<String, String> errorMap) {
		List<String> ids = billingSlabReq.getBillingSlab().parallelStream().map(BillingSlab::getId)
				.collect(Collectors.toList());
		BillingSlabRes billingSlabRes = billingSlabService.searchBillingSlabs(billingSlabReq.getRequestInfo(),
				BillingSlabSearchCriteria.builder().id(ids).tenantId(billingSlabReq.getBillingSlab().get(0).getTenantId()).build());
		if (CollectionUtils.isEmpty(billingSlabRes.getBillingSlab())) {
			errorMap.put("EG_PT_INVALID_IDS", "Following records are unavailable, IDs: "+ ids);
		} else {
			List<String> idsAvailableintheDB = billingSlabRes.getBillingSlab().parallelStream().map(BillingSlab::getId)
					.collect(Collectors.toList());
			if (idsAvailableintheDB.size() != ids.size()) {
				List<String> invalidIds = new ArrayList<>();
				for (String id : ids) {
					if (!idsAvailableintheDB.contains(id))
						invalidIds.add(id);
				}
				errorMap.put("EG_PT_INVALID_IDS", "Following records are unavailable, IDs: " + invalidIds);
			}
		}
		
		if (!CollectionUtils.isEmpty(errorMap))
			throw new CustomException(errorMap);
	}

	public void validateDuplicateBillingSlabs(BillingSlabReq billingSlabReq, Map<String, String> errorMap) {
		List<String> ids = billingSlabReq.getBillingSlab().parallelStream().map(BillingSlab::getId)
				.collect(Collectors.toList());
		BillingSlabRes billingSlabRes = billingSlabService.searchBillingSlabs(billingSlabReq.getRequestInfo(),
				BillingSlabSearchCriteria.builder().id(ids).build());
		if (!CollectionUtils.isEmpty(billingSlabRes.getBillingSlab())) {
			List<String> duplicateIds = billingSlabRes.getBillingSlab().parallelStream().map(BillingSlab::getId)
					.collect(Collectors.toList());

			errorMap.put("EG_PT_DUPLICATE_IDS", "Following records are duplicate, IDs: [" + duplicateIds + "]");
		}
		
		if (!CollectionUtils.isEmpty(errorMap))
			throw new CustomException(errorMap);
	}

	public void fetchAndvalidateMDMSCodes(BillingSlabReq billingSlabReq, Map<String, String> errorMap) {
		StringBuilder uri = new StringBuilder();
		MdmsCriteriaReq request = billingSlabUtils.prepareRequest(uri,
				billingSlabReq.getBillingSlab().get(0).getTenantId(), billingSlabReq.getRequestInfo());
		Object response = null;
		try {
			response = repository.fetchResult(uri, request);
			if (null == response) {
				log.info("MDMS data couldn't be fetched. Skipping code validation.....");
				return;
			}
			validateMDMSCodes(billingSlabReq, errorMap, response);
		} catch (Exception e) {
			log.error("MDMS data couldn't be fetched. Skipping code validation.....", e);
			return;
		}
	}

	public void validateMDMSCodes(BillingSlabReq billingSlabReq, Map<String, String> errorMap, Object mdmsResponse) {
		List<Object> propertyTypes = new ArrayList<>();
		List<Object> propertySubtypes = new ArrayList<>();
		List<Object> usageCategoryMajor = new ArrayList<>();
		List<Object> usageCategoryMinors = new ArrayList<>();
		List<Object> usageCategorySubMinor = new ArrayList<>();
		List<Object> usageCategoryDetail = new ArrayList<>();
		List<Object> ownerShipCategory = new ArrayList<>();
		List<Object> subOwnerShipCategory = new ArrayList<>();
		List<Object> occupancyType = new ArrayList<>();
		
		try {
			propertyTypes = JsonPath.read(mdmsResponse, BillingSlabConstants.MDMS_PROPERTYTAX_JSONPATH + BillingSlabConstants.MDMS_PROPERTYTYPE_MASTER_NAME);
			propertySubtypes = JsonPath.read(mdmsResponse, BillingSlabConstants.MDMS_PROPERTYTAX_JSONPATH + BillingSlabConstants.MDMS_PROPERTYSUBTYPE_MASTER_NAME);
			
			usageCategoryMajor = JsonPath.read(mdmsResponse, BillingSlabConstants.MDMS_PROPERTYTAX_JSONPATH + BillingSlabConstants.MDMS_USAGEMAJOR_MASTER_NAME);
			usageCategoryMinors = JsonPath.read(mdmsResponse, BillingSlabConstants.MDMS_PROPERTYTAX_JSONPATH + BillingSlabConstants.MDMS_USAGEMINOR_MASTER_NAME);
			usageCategorySubMinor = JsonPath.read(mdmsResponse, BillingSlabConstants.MDMS_PROPERTYTAX_JSONPATH + BillingSlabConstants.MDMS_USAGESUBMINOR_MASTER_NAME);
			usageCategoryDetail = JsonPath.read(mdmsResponse, BillingSlabConstants.MDMS_PROPERTYTAX_JSONPATH + BillingSlabConstants.MDMS_USAGEDETAIL_MASTER_NAME);
			
			ownerShipCategory = JsonPath.read(mdmsResponse, BillingSlabConstants.MDMS_PROPERTYTAX_JSONPATH + BillingSlabConstants.MDMS_OWNERSHIP_MASTER_NAME);
			subOwnerShipCategory = JsonPath.read(mdmsResponse, BillingSlabConstants.MDMS_PROPERTYTAX_JSONPATH + BillingSlabConstants.MDMS_SUBOWNERSHIP_MASTER_NAME);
			
			occupancyType = JsonPath.read(mdmsResponse, BillingSlabConstants.MDMS_PROPERTYTAX_JSONPATH + BillingSlabConstants.MDMS_OCCUPANCYTYPE_MASTER_NAME);
		} catch (Exception e) {
			if (CollectionUtils.isEmpty(propertySubtypes) && CollectionUtils.isEmpty(usageCategoryMinors)
					&& CollectionUtils.isEmpty(usageCategorySubMinor)
					&& CollectionUtils.isEmpty(subOwnerShipCategory)) {
				log.error("MDMS data couldn't be fetched. Skipping code validation.....", e);
				return;
			}
		}
		for (BillingSlab billingSlab : billingSlabReq.getBillingSlab()) {
			if(!StringUtils.isEmpty(billingSlab.getOccupancyType())) {
				List<String> allowedOccupancyTypes = JsonPath.read(occupancyType, BillingSlabConstants.MDMS_CODE_JSONPATH);
				if(!allowedOccupancyTypes.contains(billingSlab.getOccupancyType()))
					errorMap.put("INVALID_OCCUPANCY_TYPE","Occupancy Type provided is invalid");
			}
			if(!StringUtils.isEmpty(billingSlab.getPropertyType())) {
				List<String> allowedPropertyTypes = JsonPath.read(propertyTypes,BillingSlabConstants.MDMS_CODE_JSONPATH);
				if(!allowedPropertyTypes.contains(billingSlab.getPropertyType())) {
					errorMap.put("INVALID_PROPERTY_TYPE","Property Type provided is invalid");
				}else {
					if (!StringUtils.isEmpty(billingSlab.getPropertySubType()) && !billingSlab.getPropertySubType().equalsIgnoreCase(BillingSlabConstants.ALL_PLACEHOLDER_BILLING_SLAB)) {
						if (!CollectionUtils.isEmpty(propertySubtypes)) {
							List<String> allowedPropertySubTypes = JsonPath.read(propertySubtypes,"$.*.[?(@.propertyType=='" + billingSlab.getPropertyType() + "')].code");
							if (!allowedPropertySubTypes.contains(billingSlab.getPropertySubType())) {
								errorMap.put("INVALID_PROPERTY_SUBTYPE", "Property Subtype provided is invalid");
							}
						}
					}
				}
			}
			if(!StringUtils.isEmpty(billingSlab.getOwnerShipCategory())) {
				List<String> allowedOwnerShipCategories = JsonPath.read(ownerShipCategory,BillingSlabConstants.MDMS_CODE_JSONPATH);
				if(!allowedOwnerShipCategories.contains(billingSlab.getOwnerShipCategory())) {
					errorMap.put("INVALID_OWNERSHIP_CATEGORY","Ownership category is invalid");

				}else {
					if (!CollectionUtils.isEmpty(subOwnerShipCategory)) {
						List<String> allowedsubOwnerShipCategories = JsonPath.read(subOwnerShipCategory,"$.*.[?(@.ownerShipCategoryCode=='" + billingSlab.getOwnerShipCategory() + "')].code");
						if (!allowedsubOwnerShipCategories.contains(billingSlab.getSubOwnerShipCategory())) {
							errorMap.put("INVALID_SUBOWNERSHIP_CATEGORY","Subownership category is invalid");

						}
					}
				}
			}
			if(!StringUtils.isEmpty(billingSlab.getUsageCategoryMajor())) {
				List<String> allowedUsageCategoryMajor = JsonPath.read(usageCategoryMajor, BillingSlabConstants.MDMS_CODE_JSONPATH);
				if(!allowedUsageCategoryMajor.contains(billingSlab.getUsageCategoryMajor())) {
					errorMap.put("INVALID_USAGECATEGORY_MAJOR","Usage category Major is invalid");
				}else {
					if (!CollectionUtils.isEmpty(usageCategoryMinors) && !StringUtils.isEmpty(billingSlab.getUsageCategoryMinor()) && !billingSlab.getUsageCategoryMinor().equalsIgnoreCase(BillingSlabConstants.ALL_PLACEHOLDER_BILLING_SLAB)) {
						List<String> allowedUsageCategoryMinors = JsonPath.read(usageCategoryMinors, "$.*.[?(@.usageCategoryMajor=='" + billingSlab.getUsageCategoryMajor() + "')].code");
						if (!allowedUsageCategoryMinors.contains(billingSlab.getUsageCategoryMinor())) {
							errorMap.put("INVALID_USAGE_CATEGORY_MINOR","Usage category minor is invalid");
						} else {
							if (!CollectionUtils.isEmpty(allowedUsageCategoryMinors) && !StringUtils.isEmpty(billingSlab.getUsageCategorySubMinor()) && !billingSlab.getUsageCategorySubMinor().equalsIgnoreCase(BillingSlabConstants.ALL_PLACEHOLDER_BILLING_SLAB)) {
								List<String> allowedUsageCategorySubMinors = JsonPath.read(usageCategorySubMinor,"$.*.[?(@.usageCategoryMinor=='" + billingSlab.getUsageCategoryMinor() + "')].code");
								if (!allowedUsageCategorySubMinors.contains(billingSlab.getUsageCategorySubMinor())) {
									errorMap.put("INVALID_USAGE_CATEGORY_SUBMINOR","Provided Usage category sub minor is invalid");
								}else {
									if (!CollectionUtils.isEmpty(allowedUsageCategorySubMinors) && !StringUtils.isEmpty(billingSlab.getUsageCategoryDetail()) && !billingSlab.getUsageCategoryDetail().equalsIgnoreCase(BillingSlabConstants.ALL_PLACEHOLDER_BILLING_SLAB)) {
										List<String> allowedUsageCategoryDetail = JsonPath.read(usageCategoryDetail ,"$.*.[?(@.usageCategorySubMinor=='" + billingSlab.getUsageCategorySubMinor() + "')].code");
										if (!allowedUsageCategoryDetail.contains(billingSlab.getUsageCategoryDetail())) {
											errorMap.put("INVALID_USAGE_CATEGORY_DETAIL","Provided Usage category detail is invalid");
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
}
