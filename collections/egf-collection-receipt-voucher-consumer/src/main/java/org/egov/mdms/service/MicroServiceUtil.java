package org.egov.mdms.service;

import java.util.List;

import org.egov.receipt.consumer.model.BusinessService;
import org.egov.receipt.consumer.model.TaxHeadMaster;
import org.egov.receipt.custom.exception.VoucherCustomException;

public interface MicroServiceUtil {
	public List<BusinessService> getPropertyTaxBusinessService(String tenantId, String code)  throws Exception;
	public List<TaxHeadMaster> getTaxHeadMasters(String tenantId, String code) throws Exception;
}
