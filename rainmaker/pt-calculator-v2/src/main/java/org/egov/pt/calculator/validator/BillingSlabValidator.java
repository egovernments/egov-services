package org.egov.pt.calculator.validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.egov.mdms.model.MdmsCriteriaReq;
import org.egov.pt.calculator.web.models.property.BillingSlab;
import org.egov.pt.calculator.web.models.property.BillingSlabReq;
import org.egov.tracer.model.CustomException;
import org.egov.pt.calculator.repository.PTCalculatorRepository;
import org.egov.pt.calculator.util.BillingSlabConstants;
import org.egov.pt.calculator.util.BillingSlabUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.jayway.jsonpath.JsonPath;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BillingSlabValidator {
	
	@Autowired
	private BillingSlabUtils billingSlabUtils;
	
	@Autowired
	private PTCalculatorRepository repository;


	public void validateCreate(BillingSlabReq billingSlabReq) {
		Map<String, String> errorMap = new HashMap<>();
		fetchAndvalidateMDMSCodes(billingSlabReq, errorMap);
		
		if(!CollectionUtils.isEmpty(errorMap)) {
			throw new CustomException(errorMap);
		}
		
		
	}
	
	public void fetchAndvalidateMDMSCodes(BillingSlabReq billingSlabReq, Map<String, String> errorMap) {
		StringBuilder uri = new StringBuilder();
		MdmsCriteriaReq request = billingSlabUtils.prepareRequest(uri, billingSlabReq.getBillingSlab().get(0).getTenantId(), billingSlabReq.getRequestInfo());
		Object response = null;
		try {
			response = repository.fetchResult(uri, request);
			if(null == response) {
				log.info("MDMS data couldn't be fetched. Skipping code validation.....");
				return;
			}
			validateMDMSCodes(billingSlabReq, errorMap, response);
		}catch(Exception e) {
			log.error("MDMS data couldn't be fetched. Skipping code validation.....", e);
			return;
		}
	}
	
	public void validateMDMSCodes(BillingSlabReq billingSlabReq, Map<String, String> errorMap, Object mdmsResponse) {
		List<Object> propertySubtypes = new ArrayList<>();
		List<Object> usageCategoryMinors = new ArrayList<>();
		List<Object> usageCategorySubMinor = new ArrayList<>();
		List<Object> subOwnerShipCategory = new ArrayList<>();
		try {
			propertySubtypes = JsonPath.read(mdmsResponse, BillingSlabConstants.MDMS_PROPERTYSUBTYPE_JSONPATH);
			usageCategoryMinors = JsonPath.read(mdmsResponse, BillingSlabConstants.MDMS_USAGEMINOR_JSONPATH);
			usageCategorySubMinor = JsonPath.read(mdmsResponse, BillingSlabConstants.MDMS_USAGESUBMINOR_JSONPATH);
			subOwnerShipCategory = JsonPath.read(mdmsResponse, BillingSlabConstants.MDMS_SUBOWNERSHIP_JSONPATH);
		}catch(Exception e) {
			if(CollectionUtils.isEmpty(propertySubtypes) && CollectionUtils.isEmpty(usageCategoryMinors) 
					&& CollectionUtils.isEmpty(usageCategorySubMinor) && CollectionUtils.isEmpty(subOwnerShipCategory)) {
				log.error("MDMS data couldn't be fetched. Skipping code validation.....", e);
				return;
			}
		}
		for(BillingSlab billingSlab: billingSlabReq.getBillingSlab()) {
			if(!StringUtils.isEmpty(billingSlab.getPropertySubType())) {
				if(!CollectionUtils.isEmpty(propertySubtypes)) {
					List<String> allowedPropertySubTypes = JsonPath.read(propertySubtypes, "filter");
					if(!allowedPropertySubTypes.contains(billingSlab.getPropertySubType())) {
						errorMap.put("INVALID_PROPERTY_SUBTYPE", 
								"Allowed property subtype for this property type: "+billingSlab.getPropertyType()+" are: "+allowedPropertySubTypes);
					}
				}
				if(!CollectionUtils.isEmpty(subOwnerShipCategory)) {
					List<String> allowedsubOwnerShipCategories = JsonPath.read(propertySubtypes, "filter");
					if(!allowedsubOwnerShipCategories.contains(billingSlab.getSubOwnerShipCategory())) {
						errorMap.put("INVALID_SUBOWNERSHIP_CATEGORY", 
								"Allowed subownership category for this ownership category: "+billingSlab.getOwnerShipCategory()+" are: "+allowedsubOwnerShipCategories);
					}
				}
				if(!CollectionUtils.isEmpty(usageCategoryMinors)) {
					List<String> allowedUsageCategoryMinors = JsonPath.read(propertySubtypes, "filter");
					if(!allowedUsageCategoryMinors.contains(billingSlab.getUsageCategoryMinor())) {
						errorMap.put("INVALID_USAGE_CATEGORY_MINOR", 
								"Allowed Usage category minor for this usage category major: "+billingSlab.getUsageCategoryMajor()+" are: "+allowedUsageCategoryMinors);
					}else {
						List<String> usageCategorySubMinors = JsonPath.read(propertySubtypes, "filter");
						if(!usageCategorySubMinors.contains(billingSlab.getUsageCategorySubMinor())) {
							errorMap.put("INVALID_USAGE_CATEGORY_SUBMINOR", 
									"Allowed Usage category sub minor for this usage category minor: "+billingSlab.getUsageCategoryMinor()+" are: "+usageCategorySubMinors);
						}
						
					}
				}
			}
		}
	}
}
