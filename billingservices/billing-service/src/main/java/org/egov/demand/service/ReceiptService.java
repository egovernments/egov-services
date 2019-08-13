package org.egov.demand.service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Comparator;
import java.util.Set;
import java.util.Collections;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.egov.common.contract.request.RequestInfo;
import org.egov.demand.model.Bill;
import org.egov.demand.model.BillAccountDetail;
import org.egov.demand.model.BillDetail;
import org.egov.demand.model.BillDetail.StatusEnum;
import org.egov.demand.model.Demand;
import org.egov.demand.model.DemandCriteria;
import org.egov.demand.model.DemandDetail;
import org.egov.demand.web.contract.BillRequest;
import org.egov.demand.web.contract.DemandRequest;
import org.egov.demand.web.contract.ReceiptRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ReceiptService {

	@Autowired
	private DemandService demandService;
	

	public void updateDemandFromReceipt(ReceiptRequest receiptRequest, StatusEnum status, Boolean isReceiptCancellation) {

		if (null != receiptRequest && CollectionUtils.isEmpty(receiptRequest.getReceipt())
				|| Objects.isNull(receiptRequest)
				|| CollectionUtils.isEmpty(receiptRequest.getReceipt().get(0).getBill())) {

			log.info(" no data found in receipt for update : {} " + receiptRequest);
			return;
		}

		List<Bill> bills = receiptRequest.getReceipt().get(0).getBill();
		
		Set<String> demandIds = new HashSet<>();
		bills.forEach(bill -> bill.getBillDetails().forEach(billDetail -> {
			demandIds.add(billDetail.getDemandId());
			billDetail.setStatus(status);
		}));

		BillRequest billRequest = BillRequest.builder().requestInfo(receiptRequest.getRequestInfo()).bills(bills)
				.build();
		updateDemandFromBill(billRequest,demandIds, isReceiptCancellation);
	}

	/**
	 * Update the demand collection details based on the bill
	 * 
	 * @param billRequest
	 * @param demandIds
	 * @param isReceiptCancellation
	 * @return
	 */
	public void updateDemandFromBill(BillRequest billRequest,Set<String> demandIds, Boolean isReceiptCancellation) {

		List<Bill> bills = billRequest.getBills();
		String tenantId = bills.get(0).getTenantId();
		RequestInfo requestInfo = billRequest.getRequestInfo();

		DemandCriteria demandCriteria = DemandCriteria.builder().demandId(demandIds).tenantId(tenantId).build();
		List<Demand> demandsToBeUpdated = demandService.getDemands(demandCriteria, requestInfo);
		Map<String, Demand> demandIdMap = demandsToBeUpdated.stream()
				.collect(Collectors.toMap(Demand::getId, Function.identity()));
		
		for(Bill bill : bills) {
			
			for (BillDetail billDetail : bill.getBillDetails())
				updateDemandFromBillDetail(billDetail, demandIdMap.get(billDetail.getDemandId()), isReceiptCancellation);
			
		}
		
		demandService.updateAsync(DemandRequest.builder().demands(demandsToBeUpdated).requestInfo(billRequest.getRequestInfo()).build());
	}

	/**
	 * Updates the collection amount in the incoming demand object based on the billDetail object
	 * 
	 * @param demandIdMap
	 * @param billDetail
	 */
	private void updateDemandFromBillDetail(BillDetail billDetail, Demand demand, Boolean isReceiptCancellation) {

		Map<String, List<DemandDetail>> taxHeadCodeDemandDetailgroup = demand.getDemandDetails().stream()
				.collect(Collectors.groupingBy(DemandDetail::getTaxHeadMasterCode));

		for (BillAccountDetail billAccDetail : billDetail.getBillAccountDetails()) {

			/* Ignoring billaccount details with no taxhead codes
			 * to avoid saving collection only information
			 */ 
			if(null == billAccDetail.getTaxHeadCode()) return;
			
			
			List<DemandDetail> currentDetails = taxHeadCodeDemandDetailgroup.get(billAccDetail.getTaxHeadCode());
			int length = 0;
			
			if (!CollectionUtils.isEmpty(currentDetails))
				length = currentDetails.size();

			// Sort all the tax amounts so the collection for -ve amounts happens first
			Collections.sort(currentDetails, Comparator.comparing(DemandDetail::getTaxAmount));

			/* 
			 * if single demand detail corresponds to single billAccountDetail then update directly
			 */
			if (length == 1) {

				updateSingleDemandDetail(currentDetails.get(0), billAccDetail, isReceiptCancellation);
			}
			/*
			 * if multiple demandDetails point to one BillAccountDetial
			 */
			else if (length > 1) {

				updateMultipleDemandDetails(currentDetails, billAccDetail, isReceiptCancellation);
			} else {

				/*
				 * if no demand detail found for the corresponding billAccountDetail 
				 * then add the new DemandDetail in the demand
				 */
				DemandDetail newAdvanceDetail = DemandDetail.builder()
						.taxHeadMasterCode(billAccDetail.getTaxHeadCode())
						.taxAmount(billAccDetail.getAmount())
						.collectionAmount(BigDecimal.ZERO)
						.tenantId(demand.getTenantId())
						.demandId(demand.getId())
						.build();
				
				demand.getDemandDetails().add(newAdvanceDetail);
			}
		}
	}

	
	/**
	 * Method to handle update if single demandDetail is presnt for a BillAccountDetail
	 * 
	 * @param currentDetail         the demand detail object to be updated
	 * 
	 * @param billAccDetail         bill account detail object from which values
	 *                              needs to fetched
	 *                              
	 * @param isReceiptCancellation if the call is made for payment or cancellation
	 */
	private void updateSingleDemandDetail(DemandDetail currentDetail, BillAccountDetail billAccDetail,
			Boolean isReceiptCancellation) {
		
		BigDecimal oldCollectedAmount = currentDetail.getCollectionAmount();
		BigDecimal newAmount = billAccDetail.getAdjustedAmount();

		if (isReceiptCancellation)
			currentDetail.setCollectionAmount(oldCollectedAmount.subtract(newAmount));
		else
			currentDetail.setCollectionAmount(oldCollectedAmount.add(newAmount));
	}
	
	
	/**
	 * Method to handle update if multiple demand details are present for a single Bill account detail
	 * 
	 * @param demandDetails List of demand details to updated
	 * @param billAccDetail the bill account detail with the paid-amount/Adjusted amount
	 * @param isReceiptCancellation to identify if the method call is for payment or cancellation
	 */
	private void updateMultipleDemandDetails (List<DemandDetail> demandDetails, BillAccountDetail billAccDetail, Boolean isReceiptCancellation) {

		BigDecimal incomingAmount = billAccDetail.getAdjustedAmount();
		Boolean isNegativeDetail = incomingAmount.compareTo(BigDecimal.ZERO) < 0;
		
		BigDecimal negatedIncomingAmount = isNegativeDetail ? incomingAmount.negate() : incomingAmount;
		
		if (!isReceiptCancellation)
			updateDetailsForPayment(demandDetails, isNegativeDetail, negatedIncomingAmount);
		else
			updateDetailsForCancellation(demandDetails, isNegativeDetail, negatedIncomingAmount);

	}

	/**
	 * Method to handle receipt cancellation in case of  multiple Demand detail present for a single billAccountDetail
	 * 
	 * @param demandDetails        List of details to be updated
	 * 
	 * @param isNegativeDetail     boolean field to represent whether the demand
	 *                             detail is greater than or lesser than zero
	 * 
	 * @param negatedIncomingAmount Adjusted amount from bill detail negated to be
	 *                             positive if it was negative
	 */
	private void updateDetailsForCancellation(List<DemandDetail> demandDetails, Boolean isNegativeDetail,
			BigDecimal negatedIncomingAmount) {

		for (DemandDetail detail : demandDetails) {

			if (negatedIncomingAmount.compareTo(BigDecimal.ZERO) == 0)
				return;

			/*
			 * amount to be set in collectionAmount field of demandDetail after adjustments
			 */
			BigDecimal resultantCollectionAmt;

			/*
			 * Changing the collection amount to positive in case of negative demandDetail
			 */
			BigDecimal currentDetailCollectionAmt = isNegativeDetail ? detail.getCollectionAmount().negate()
					: detail.getCollectionAmount();

			if (currentDetailCollectionAmt.compareTo(negatedIncomingAmount) >= 0) {
				
				resultantCollectionAmt = currentDetailCollectionAmt.subtract(negatedIncomingAmount);
				negatedIncomingAmount = BigDecimal.ZERO;
			} else {

				resultantCollectionAmt = BigDecimal.ZERO;
				negatedIncomingAmount = negatedIncomingAmount.subtract(currentDetailCollectionAmt);
			}

			/* Changing the sign of result amount for negative demand details */
			resultantCollectionAmt = isNegativeDetail ? resultantCollectionAmt.negate() : resultantCollectionAmt;

			detail.setCollectionAmount(resultantCollectionAmt);
		}
	}

	/**
	 *  Method to handle payment in case of multiple Demand details present for a single billAccountDetail
	 * 
	 * @param demandDetails        List of details to be updated
	 * 
	 * @param isNegativeDetail     boolean field to represent whether the demand
	 *                             detail is greater than or lesser than zero
	 *                             
	 * @param negatedIncomingAmount Adjusted amount from bill detail negated to be
	 *                             positive if it was negative
	 */
	private void updateDetailsForPayment(List<DemandDetail> demandDetails, Boolean isNegativeDetail,
			BigDecimal negatedIncomingAmount) {

		for (DemandDetail detail : demandDetails) {

			if (negatedIncomingAmount.compareTo(BigDecimal.ZERO) == 0)
				return;

			/*
			 * amount to be set in collectionAmount field of demandDetail after adjustments
			 */
			BigDecimal resultantCollectionAmt;

			/* Changing the tax values to positive in case of negative demandDetail */
			BigDecimal currentDetailTaxAmount = isNegativeDetail ? detail.getTaxAmount().negate()
					: detail.getTaxAmount();
			BigDecimal currentDetailCollectionAmt = isNegativeDetail ? detail.getCollectionAmount().negate()
					: detail.getCollectionAmount();

			BigDecimal currentDetailTaxCollectionDifference = currentDetailTaxAmount
					.subtract(currentDetailCollectionAmt);

			/*
			 * if current demandDetail difference is lesser than incoming amount of
			 * 
			 * BillAccountDetail, then add the whole value to result
			 */
			if (currentDetailTaxCollectionDifference.compareTo(negatedIncomingAmount) >= 0) {

				resultantCollectionAmt = currentDetailCollectionAmt.add(negatedIncomingAmount);
				negatedIncomingAmount = BigDecimal.ZERO;
			} else {
				/*
				 * if difference of demandDetail is lesser than Incoming amount, then add the
				 * 
				 * difference to resulantAmount and subtract the same from incoming amount
				 */
				resultantCollectionAmt = currentDetailCollectionAmt.add(currentDetailTaxCollectionDifference);
				negatedIncomingAmount = negatedIncomingAmount.subtract(currentDetailTaxCollectionDifference);
			}

			/* Changing the sign of result amount for negative demand details */
			resultantCollectionAmt = isNegativeDetail ? resultantCollectionAmt.negate() : resultantCollectionAmt;

			detail.setCollectionAmount(resultantCollectionAmt);
		}
	}

}
