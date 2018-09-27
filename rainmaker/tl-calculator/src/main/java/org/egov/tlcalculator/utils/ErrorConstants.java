package org.egov.tlcalculator.utils;

import org.springframework.stereotype.Component;

@Component
public class ErrorConstants {
	
	public static final String MULTIPLE_TENANT_CODE = "MULTIPLE_TENANTS";
	public static final String MULTIPLE_TENANT_MSG = "All Billingslabs being created must belong to one single tenant";
	
	public static final String INVALID_TRADETYPE_CODE = "INVALID_TRADETYPE";
	public static final String INVALID_TRADETYPE_MSG = "The following TradeType is invalid";
	
	public static final String INVALID_ACCESSORIESCATEGORY_CODE = "INVALID_ACCESSORIESCATEGORY";
	public static final String INVALID_ACCESSORIESCATEGORY_MSG = "The following AccessoriesCategory is invalid";
	
	public static final String INVALID_STRUCTURETYPE_CODE = "INVALID_STRUCTURETYPE";
	public static final String INVALID_STRUCTURETYPE_MSG = "The following StructureType is invalid";
	

}
