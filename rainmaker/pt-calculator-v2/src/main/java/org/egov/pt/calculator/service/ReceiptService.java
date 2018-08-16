package org.egov.pt.calculator.service;

import java.util.List;

import org.egov.pt.calculator.repository.Repository;
import org.egov.pt.calculator.util.CalculatorConstants;
import org.egov.pt.calculator.util.CalculatorUtils;
import org.egov.pt.calculator.web.models.collections.Receipt;
import org.egov.pt.calculator.web.models.collections.ReceiptRes;
import org.egov.pt.calculator.web.models.demand.BillDetail;
import org.egov.pt.calculator.web.models.demand.Demand;
import org.egov.pt.calculator.web.models.property.RequestInfoWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ReceiptService {

	@Autowired
	private Repository repository;
	
	@Autowired
	private CalculatorUtils utils;
	
	@Autowired
	private ObjectMapper mapper;
	
	/**
	 * searches the latest collected date of the particular demand 
	 * 
	 * @param demand
	 * @param requestInfoWrapper
	 * @return latestCollectedTime
	 */
	public Long getInterestLatestCollectedTime(Demand demand, RequestInfoWrapper requestInfoWrapper) {

		List<Receipt> receipts = getReceipts(demand, requestInfoWrapper);
		long latestCollectedDate = 0l;

		if (CollectionUtils.isEmpty(receipts)) return latestCollectedDate;

		for (Receipt receipt : receipts) {
			
			BillDetail detail = receipt.getBill().get(0).getBillDetails().get(0);
			if (detail.getStatus().equalsIgnoreCase(CalculatorConstants.RECEIPT_STATUS_APPROVED)
					&& detail.getReceiptDate().compareTo(latestCollectedDate) > 0)
				latestCollectedDate = detail.getReceiptDate();
		}
		return latestCollectedDate;
	}

	/**
	 * private method to search receipts
	 * @param demand
	 * @param requestInfoWrapper
	 */
	private List<Receipt> getReceipts(Demand demand, RequestInfoWrapper requestInfoWrapper) {

		StringBuilder url = utils.getReceiptSearchUrl(demand.getTenantId(), demand.getConsumerCode());
		return mapper.convertValue(repository.fetchResult(url, requestInfoWrapper), ReceiptRes.class).getReceipts();
	}
}
