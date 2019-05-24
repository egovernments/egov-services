package org.egov.mdms.service;

import java.util.List;

import org.egov.receipt.consumer.model.BusinessService;
import org.egov.receipt.consumer.model.FinanceMdmsModel;
import org.egov.receipt.consumer.model.RequestInfo;
import org.egov.receipt.consumer.model.TaxHeadMaster;
import org.egov.receipt.custom.exception.VoucherCustomException;

public interface MicroServiceUtil {
	public List<TaxHeadMaster> getTaxHeadMasters(String tenantId, String code, RequestInfo requestInfo, FinanceMdmsModel finSerMdms) throws Exception;
	public List<BusinessService> getBusinessService(String tenantId, String code, RequestInfo requestInfo, FinanceMdmsModel finSerMdms)  throws Exception;
	String getBusinessServiceName(String tenantId, String code, RequestInfo requestInfo, FinanceMdmsModel finSerMdms)
			throws VoucherCustomException;
}
