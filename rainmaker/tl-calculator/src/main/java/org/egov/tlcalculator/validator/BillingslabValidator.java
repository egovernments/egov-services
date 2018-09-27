package org.egov.tlcalculator.validator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.egov.tlcalculator.service.BillingslabService;
import org.egov.tlcalculator.utils.BillingslabConstants;
import org.egov.tlcalculator.utils.BillingslabUtils;
import org.egov.tlcalculator.utils.ErrorConstants;
import org.egov.tlcalculator.web.models.BillingSlab;
import org.egov.tlcalculator.web.models.BillingSlabReq;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class BillingslabValidator {
	
	@Autowired
	private BillingslabUtils util;
	
	@Autowired
	private BillingslabService service;
	
	
	public void validateCreate(BillingSlabReq billingSlabReq) {
		Map<String, String> errorMap = new HashMap<>();
		tenantIdCheck(billingSlabReq, errorMap);
		Map<String, List<String>> mdmsDataMap = service.getMDMSDataForValidation(billingSlabReq);
		for(BillingSlab slab: billingSlabReq.getBillingSlab()) {
			validateMDMSCodes(slab, mdmsDataMap, errorMap);
		}
		if(!CollectionUtils.isEmpty(errorMap.keySet())) {
			throw new CustomException(errorMap);
		}
	}
	
	public void validateUpdate(BillingSlabReq billingSlabReq) {
		Map<String, String> errorMap = new HashMap<>();
		tenantIdCheck(billingSlabReq, errorMap);
		Map<String, List<String>> mdmsDataMap = service.getMDMSDataForValidation(billingSlabReq);
		for(BillingSlab slab: billingSlabReq.getBillingSlab()) {
			validateMDMSCodes(slab, mdmsDataMap, errorMap);
		}
		if(!CollectionUtils.isEmpty(errorMap.keySet())) {
			throw new CustomException(errorMap);
		}
	}
	
	public void tenantIdCheck(BillingSlabReq billingSlabReq, Map<String, String> errorMap) {
		Set<String> tenantIds = billingSlabReq.getBillingSlab().parallelStream().map(BillingSlab::getTenantId).collect(Collectors.toSet());
		if(tenantIds.size() > 1) {
			errorMap.put(ErrorConstants.MULTIPLE_TENANT_CODE, ErrorConstants.MULTIPLE_TENANT_MSG);
			throw new CustomException(errorMap);
		}
	}
	
	public void validateMDMSCodes(BillingSlab billingSlab, Map<String, List<String>> mdmsDataMap, Map<String, String> errorMap) {
		if(!StringUtils.isEmpty(billingSlab.getTradeType())) {
			if(!mdmsDataMap.get(BillingslabConstants.TL_MDMS_TRADETYPE).contains(billingSlab.getTradeType()))
				errorMap.put(ErrorConstants.INVALID_TRADETYPE_CODE, ErrorConstants.INVALID_TRADETYPE_MSG + ": "+billingSlab.getTradeType());
		}
		if(!StringUtils.isEmpty(billingSlab.getAccessoryCategory())) {
			if(!mdmsDataMap.get(BillingslabConstants.TL_MDMS_ACCESSORIESCATEGORY).contains(billingSlab.getAccessoryCategory()))
				errorMap.put(ErrorConstants.INVALID_ACCESSORIESCATEGORY_CODE, ErrorConstants.INVALID_ACCESSORIESCATEGORY_MSG + ": "+billingSlab.getAccessoryCategory());
		}
		if(!StringUtils.isEmpty(billingSlab.getStructureType())) {
			if(!mdmsDataMap.get(BillingslabConstants.TL_MDMS_STRUCTURETYPE).contains(billingSlab.getStructureType()))
				errorMap.put(ErrorConstants.INVALID_STRUCTURETYPE_CODE, ErrorConstants.INVALID_STRUCTURETYPE_MSG + ": "+billingSlab.getStructureType());
		}
	}

}
