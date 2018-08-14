package org.egov.demand.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.egov.demand.config.ApplicationProperties;
import org.egov.demand.model.Bill;
import org.egov.demand.web.contract.Receipt;
import org.egov.demand.web.contract.ReceiptRequest;
import org.egov.demand.web.contract.factory.ResponseFactory;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DemandAdjustmentService {
	
	private static final Logger logger = LoggerFactory.getLogger(DemandAdjustmentService.class);

	@Autowired
	private DemandService demandService;

	@Autowired
	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

	@Autowired
	private ApplicationProperties applicationProperties;

	@Autowired
	private ResponseFactory responseInfoFactory;
	
	public void adjustDemandOnReceiptCancellation(ReceiptRequest receiptRequest) {
		for(Receipt receipt: receiptRequest.getReceipt()) {
			Map<String, Map<String, BigDecimal>> mapOfBillDetailsAndCreditAmount = new HashMap<>();
			for(Bill bill: receipt.getBill()) {
				
			}
		}
		
	}

}
