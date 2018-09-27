package org.egov.tlcalculator.service;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.egov.mdms.model.MdmsCriteriaReq;
import org.egov.tlcalculator.kafka.broker.TLCalculatorProducer;
import org.egov.tlcalculator.utils.BillingslabConstants;
import org.egov.tlcalculator.utils.BillingslabUtils;
import org.egov.tlcalculator.utils.ResponseInfoFactory;
import org.egov.tlcalculator.web.models.AuditDetails;
import org.egov.tlcalculator.web.models.BillingSlab;
import org.egov.tlcalculator.web.models.BillingSlabReq;
import org.egov.tlcalculator.web.models.BillingSlabRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.jayway.jsonpath.JsonPath;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BillingslabService {
	
	@Autowired
	private BillingslabUtils util;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private TLCalculatorProducer producer;
	
	@Autowired
	private ResponseInfoFactory factory;
	
	public BillingSlabRes createSlabs(BillingSlabReq billingSlabReq) {
		enrichSlabsForCreate(billingSlabReq);
		billingSlabReq.getBillingSlab().parallelStream().forEach(slab -> { producer.push("topic", slab); });
		return BillingSlabRes.builder().responseInfo(factory.createResponseInfoFromRequestInfo(billingSlabReq.getRequestInfo(), true))
				.billingSlab(billingSlabReq.getBillingSlab()).build();
	}
	
	public BillingSlabRes updateSlabs(BillingSlabReq billingSlabReq) {
		return null;
	}
	
	public Object searchSlabs() {
		return null;
	}
	
	public void enrichSlabsForCreate(BillingSlabReq billingSlabReq) {
		AuditDetails audit = AuditDetails.builder().createdBy(billingSlabReq.getRequestInfo().getUserInfo().getUuid())
				.createdTime(new Date().getTime()).lastModifiedBy(billingSlabReq.getRequestInfo().getUserInfo().getUuid()).lastModifiedTime(new Date().getTime()).build();
		for(BillingSlab slab: billingSlabReq.getBillingSlab()) {
			slab.setId(UUID.randomUUID().toString());
			slab.setAuditDetails(audit);
		}
	}
	
	public Map<String, List<String>> getMDMSDataForValidation(BillingSlabReq billingSlabReq){
		Map<String, List<String>> mdmsMap = new HashMap<>();
		String[] masters = {BillingslabConstants.TL_MDMS_TRADETYPE, BillingslabConstants.TL_MDMS_ACCESSORIESCATEGORY, BillingslabConstants.TL_MDMS_STRUCTURETYPE};
		for(String master: Arrays.asList(masters)) {
			StringBuilder uri = new StringBuilder();
			MdmsCriteriaReq request = util.prepareMDMSSearchReq(uri, billingSlabReq.getBillingSlab().get(0).getTenantId(), 
					BillingslabConstants.TL_MDMS_MODULE_NAME, master, null, billingSlabReq.getRequestInfo());
			try {
				Object response = restTemplate.postForObject(uri.toString(), request, Map.class);
				if(null != response) {
					String jsonPath = BillingslabConstants.MDMS_JSONPATH_FOR_MASTER_CODES
							.replaceAll("#module#", BillingslabConstants.TL_MDMS_MODULE_NAME).replaceAll("#master#", master);
					List<String> data = JsonPath.read(response, jsonPath);
					mdmsMap.put(master, data);
				}
			}catch(Exception e) {
				log.error("Couldn't fetch master: "+master);
				continue;
			}
			
		}
		
		return mdmsMap;
	}

}
