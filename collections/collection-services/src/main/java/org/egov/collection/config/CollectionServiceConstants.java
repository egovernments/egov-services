package org.egov.collection.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class CollectionServiceConstants {
	
	public static final String INVALID_RECEIPT_REQUEST = "Receipt create request is invalid";
	
	public static final String TENANT_ID_MISSING_CODE = "egcl_001";
	public static final String TENANT_ID_MISSING_FIELD = "tenantId";
	public static final String TENANT_ID_MISSING_MESSAGE = "Tenant id is mising";
	
	public static final String PAYEE_NAME_MISSING_CODE = "egcl_002";
	public static final String PAYEE_NAME_MISSING_FIELD = "payeeName";
	public static final String PAYEE_NAME_MISSING_MESSAGE = "Payee Name is mising";
	
	public static final String PAID_BY_MISSING_CODE = "egcl_003";
	public static final String PAID_BY_MISSING_FIELD = "paidBy";
	public static final String PAID_BY_MISSING_MESSAGE = "Paid by id is mising";
	
	public static final String RECEIPT_TYPE_MISSING_CODE = "egcl_004";
	public static final String RECEIPT_TYPE_MISSING_FIELD = "receiptType";
	public static final String RECEIPT_TYPE_MISSING_MESSAGE = "Receipt Type is mising";
	
	public static final String RECEIPT_DATE_MISSING_CODE = "egcl_005";
	public static final String RECEIPT_DATE_MISSING_FIELD = "receiptDate";
	public static final String RECEIPT_DATE_MISSING_MESSAGE = "Receipt Date is mising";
	
	public static final String BD_CODE_MISSING_CODE = "egcl_006";
	public static final String BD_CODE_MISSING_FIELD = "businessDetailsCode";
	public static final String BD_CODE_MISSING_MESSAGE = "Business Details Code is mising";
	
	public static final String COA_MISSING_CODE = "egcl_007";
	public static final String COA_MISSING_FIELD = "glcode";
	public static final String COA_MISSING_MESSAGE = "Chart of Account Code/ GL Code is mising";
	
	public static final String PURPOSE_MISSING_CODE = "egcl_008";
	public static final String PURPOSE_MISSING_FIELD = "purpose";
	public static final String PURPOSE_MISSING_MESSAGE = "Purpose is mising";
	
	
	
	//URIs for other micro services collection depends on
	public static final String BD_SEARCH_URI = "http://egov-micro-dev.egovernments.org/egov-common-masters/businessDetails/_search";
	public static final String COA_SEARCH_URI = "http://egov-micro-dev.egovernments.org/egf-masters/chartofaccounts/_search";




}
