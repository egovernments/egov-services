package org.egov.pt.calculator.service;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.pt.calculator.producer.Producer;
import org.egov.pt.calculator.repository.PTCalculatorDBRepository;
import org.egov.pt.calculator.util.BillingSlabUtils;
import org.egov.pt.calculator.util.Configurations;
import org.egov.pt.calculator.util.ResponseInfoFactory;
import org.egov.pt.calculator.web.models.property.BillingSlab;
import org.egov.pt.calculator.web.models.property.BillingSlabReq;
import org.egov.pt.calculator.web.models.property.BillingSlabRes;
import org.egov.pt.calculator.web.models.property.BillingSlabSearcCriteria;
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
	
	@Autowired
	private PTCalculatorDBRepository dbRepository;
	
	@Autowired
	private ResponseInfoFactory factory;

	public BillingSlabRes createBillingSlab(BillingSlabReq billingSlabReq) {
		producer.push(configurations.getBillingSlabSavePersisterTopic(), billingSlabReq);
		return billingSlabUtils.getBillingSlabResponse(billingSlabReq);
	}
	
	public BillingSlabRes updateBillingSlab(BillingSlabReq billingSlabReq) {
		producer.push(configurations.getBillingSlabUpdatePersisterTopic(), billingSlabReq);
		return billingSlabUtils.getBillingSlabResponse(billingSlabReq);
	}
	
	public BillingSlabRes searchBillingSlabs(RequestInfo requestInfo, BillingSlabSearcCriteria billingSlabSearcCriteria) {
		List<BillingSlab> billingSlabs = new ArrayList<>();
		try {
			billingSlabs = dbRepository.searchBillingSlab(billingSlabSearcCriteria);
		}catch(Exception e) {
			log.error("Exception while fetching billing slabs from db: "+e);
			if(null == billingSlabs) 
				billingSlabs = new ArrayList<>();
		}
		return BillingSlabRes.builder().responseInfo(factory.createResponseInfoFromRequestInfo(requestInfo, true))
				.billingSlab(billingSlabs).build();
	}
}
 
