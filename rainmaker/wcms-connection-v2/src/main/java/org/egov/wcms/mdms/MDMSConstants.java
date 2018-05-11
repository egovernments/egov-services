package org.egov.wcms.mdms;

import org.springframework.stereotype.Component;

@Component
public class MDMSConstants {
	
	private MDMSConstants() {};
	
	public static final String WCMS_MODULE_NAME = "WaterCharges";
	public static final String APPLICATIONTYPE_MASTER_NAME = "ApplicationType";
	public static final String BILLINGSLAB_MASTER_NAME = "BillingSlab";
	public static final String BILLINGTYPE_MASTER_NAME = "BillingType";
	public static final String DOCUMENTTYPE_MASTER_NAME = "DocumentType";
	public static final String PIPESIZE_MASTER_NAME = "PipeSize";
	
	public static final String WCMS_MDMS_RES_JSONPATH = "$.MdmsRes.WaterCharges.";
	public static final String WCMS_MDMS_RES_CODE_JSONPATH = "$.*.code";
	public static final String WCMS_MDMS_RES_NAME_JSONPATH = "$.*.name";
	public static final String WCMS_MDMS_RES_VALIDDOCTYPE_JSONPATH = "$.[?(@.code=='{$}')].documentType";
	
}
