package org.egov.demand.service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.egov.common.contract.request.RequestInfo;
import org.egov.demand.model.Bill;
import org.egov.demand.model.BillAccountDetail;
import org.egov.demand.model.BillDetail;
import org.egov.demand.model.Demand;
import org.egov.demand.model.DemandCriteria;
import org.egov.demand.model.DemandDetail;
import org.egov.demand.repository.DemandRepository;
import org.egov.demand.web.contract.BillRequest;
import org.egov.demand.web.contract.DemandResponse;
import org.egov.demand.web.contract.ReceiptRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ReceiptService {

	@Autowired
	private DemandRepository demandRepository;

	
	public DemandResponse updateDemandFromReceipt(ReceiptRequest receiptRequest,
			org.egov.demand.model.BillDetail.StatusEnum status) {
		
		/*
		 * FIXME TODO post the error data in a new topic or elastic
		 */
		if (null != receiptRequest && CollectionUtils.isEmpty(receiptRequest.getReceipt())
				|| Objects.isNull(receiptRequest)
				|| CollectionUtils.isEmpty(receiptRequest.getReceipt().get(0).getBill()))
			return null;

		List<Bill> bills = receiptRequest.getReceipt().get(0).getBill();
		
		Set<String> demandIds = new HashSet<>();
		bills.forEach(bill -> bill.getBillDetails().forEach(billDetail -> {
			demandIds.add(billDetail.getDemandId());
			billDetail.setStatus(status);
		}));

		BillRequest billRequest = BillRequest.builder().requestInfo(receiptRequest.getRequestInfo()).bills(bills)
				.build();
		return updateDemandFromBill(billRequest,demandIds, false);
	}

	/**
	 * Update the demand collection details based on the bill
	 * 
	 * @param billRequest
	 * @param demandIds
	 * @param isReceiptCancellation
	 * @return
	 */
	public DemandResponse updateDemandFromBill(BillRequest billRequest,Set<String> demandIds, Boolean isReceiptCancellation) {

		List<Bill> bills = billRequest.getBills();
		RequestInfo requestInfo = billRequest.getRequestInfo();
		String tenantId = bills.get(0).getTenantId();

		DemandCriteria demandCriteria = DemandCriteria.builder().demandId(demandIds).tenantId(tenantId).build();
		List<Demand> demandsToBeUpdated = demandRepository.getDemands(demandCriteria);
		Map<String, Demand> demandIdMap = demandsToBeUpdated.stream()
				.collect(Collectors.toMap(Demand::getId, Function.identity()));
		
		for(Bill bill : bills) {
			
			for (BillDetail billDetail : bill.getBillDetails())
				updateDemandFromBillDetail(billDetail, demandIdMap.get(billDetail.getDemandId()), isReceiptCancellation);
			
		}
		return null;
	}

	/**
	 * Updates the collection amount in the incoming demand object based on the billDetail object
	 * 
	 * @param demandIdMap
	 * @param billDetail
	 */
	private void updateDemandFromBillDetail(BillDetail billDetail, Demand demand, Boolean isRecieptCancellation) {

		Map<String, DemandDetail> demandDetailIdMap = demand.getDemandDetails().stream()
				.collect(Collectors.toMap(DemandDetail::getId, Function.identity()));

		for (BillAccountDetail billAccDetail : billDetail.getBillAccountDetails()) {

			DemandDetail currentDetail = demandDetailIdMap.get(billAccDetail.getDemandDetailId());
			BigDecimal oldCollectedAmount = currentDetail.getCollectionAmount();
			BigDecimal newAmount = billAccDetail.getAdjustedAmount();

			if (isRecieptCancellation)
				currentDetail.setCollectionAmount(oldCollectedAmount.subtract(newAmount));
			else
				currentDetail.setCollectionAmount(oldCollectedAmount.add(newAmount));
		}
	}
}
