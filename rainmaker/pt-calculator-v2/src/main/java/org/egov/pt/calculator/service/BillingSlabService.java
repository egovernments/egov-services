package org.egov.pt.calculator.service;

import org.egov.pt.calculator.producer.Producer;
import org.egov.pt.calculator.util.BillingSlabUtils;
import org.egov.pt.calculator.util.Configurations;
import org.egov.pt.calculator.web.models.property.BillingSlabReq;
import org.egov.pt.calculator.web.models.property.BillingSlabRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BillingSlabService {
	
	@Autowired
	private BillingSlabUtils billingSlabUtils;
	
	@Autowired
	private Producer producer;
	
	@Autowired
	private Configurations configurations;

	public BillingSlabRes createBillingSlab(BillingSlabReq billingSlabReq) {
		producer.push(configurations.getBillingSlabSavePersisterTopic(), billingSlabReq);
		return billingSlabUtils.getBillingSlabResponse(billingSlabReq);
	}
	
	public BillingSlabRes updateBillingSlab(BillingSlabReq billingSlabReq) {
		producer.push(configurations.getBillingSlabUpdatePersisterTopic(), billingSlabReq);
		return billingSlabUtils.getBillingSlabResponse(billingSlabReq);
	}
}
