package org.egov.pt.calculator.util;

import org.springframework.stereotype.Component;

@Component
public class BillingSlabConstants {
	
	public static final String MDMS_PT_MOD_NAME = "PropertyTax";
	public static final String MDMS_PROPERTYSUBTYPE_MASTER_NAME = "PropertySubType";
	public static final String MDMS_USAGEMINOR_MASTER_NAME = "UsageCategoryMinor";
	public static final String MDMS_USAGESUBMINOR_MASTER_NAME = "UsageCategorySubMinor";
	public static final String MDMS_SUBOWNERSHIP_MASTER_NAME = "SubOwnerShipCategory";
	
	
	public static final String MDMS_PROPERTYSUBTYPE_JSONPATH = "$.MdmsRes.PropertyTax.PropertySubType";
	public static final String MDMS_USAGEMINOR_JSONPATH = "$.MdmsRes.PropertyTax.UsageCategoryMinor";
	public static final String MDMS_USAGESUBMINOR_JSONPATH = "$.MdmsRes.PropertyTax.UsageCategorySubMinor";
	public static final String MDMS_SUBOWNERSHIP_JSONPATH = "$.MdmsRes.PropertyTax.SubOwnerShipCategory";
}
