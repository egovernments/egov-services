package org.egov.collection.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.egov.collection.web.contract.Bill;
import org.egov.collection.web.contract.BillAccountDetail;
import org.egov.collection.web.contract.BillDetail;
import org.egov.collection.web.contract.Purpose;
import org.egov.collection.web.contract.Receipt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class ReceiptCommonModel {

	private List<ReceiptHeader> receiptHeaders;

	private List<ReceiptDetail> receiptDetails;

	public List<Receipt> toDomainContract() {
		List<Receipt> receipts = new ArrayList<>();

		for (ReceiptHeader receiptHeader : receiptHeaders) {
			
			List<ReceiptDetail> listReceiptDetail = new ArrayList<>();
			for (ReceiptDetail receiptDetail : receiptDetails) {
				if (receiptDetail.getReceiptHeader().getId().equals(receiptHeader.getId())) {
					listReceiptDetail.add(receiptDetail);
				}
			}
			List<BillAccountDetail> billAccountDetails = new ArrayList<>();
			for (ReceiptDetail rctDetail : listReceiptDetail) {
				billAccountDetails.add(BillAccountDetail.builder().isActualDemand(rctDetail.getIsActualDemand())
						.id(rctDetail.getId().toString()).tenantId(rctDetail.getTenantId())
						.billDetail(rctDetail.getReceiptHeader().getId().toString())
						.creditAmount(BigDecimal.valueOf(rctDetail.getCramount()))
						.debitAmount(BigDecimal.valueOf(rctDetail.getDramount()))
						.purpose(Purpose.valueOf(rctDetail.getPurpose())).build());
			}
			BillDetail billDetail = BillDetail.builder().id(receiptHeader.getId().toString())
					.consumerCode(receiptHeader.getConsumerCode()).consumerType(receiptHeader.getConsumerType())
					.minimumAmount(BigDecimal.valueOf(receiptHeader.getMinimumAmount()))
					.totalAmount(BigDecimal.valueOf(receiptHeader.getTotalAmount()))
					.collectionModesNotAllowed(Arrays.asList(receiptHeader.getCollModesNotAllwd()))
					.tenantId(receiptHeader.getTenantId()).displayMessage(receiptHeader.getDisplayMsg())
					.billAccountDetails(billAccountDetails).build();
			List<BillAccountDetail> listOfBillAccountDetails = new ArrayList<>();
			
			for (BillAccountDetail billAccountDetail : billAccountDetails) {
				listOfBillAccountDetails.add(billAccountDetail);
			}
			BillDetail detailsWrap = BillDetail.builder()
					.businessService(receiptHeader.getBusinessDetails()).billNumber(receiptHeader.getReferenceNumber())
					.receiptNumber(receiptHeader.getReceiptNumber())
					.receiptDate(new Timestamp(receiptHeader.getReceiptDate().getTime()))
					.receiptType(receiptHeader.getReceiptType()).channel(receiptHeader.getChannel())
					.voucherHeader(receiptHeader.getVoucherheader()).collectionType(receiptHeader.getCollectionType())
					.boundary(receiptHeader.getBoundary())
					.reasonForCancellation(receiptHeader.getReasonForCancellation())
					.billAccountDetails(listOfBillAccountDetails).build();
			
			Bill billInfo = Bill.builder().payeeName(receiptHeader.getPayeename())
					.payeeAddress(receiptHeader.getPayeeAddress()).payeeEmail(receiptHeader.getPayeeEmail())
					.tenantId(receiptHeader.getTenantId()).billDetails(Collections.singletonList(billDetail)).build();
			Bill bill = Bill.builder().paidBy(receiptHeader.getPaidBy())
					.billDetails(Collections.singletonList(detailsWrap)).build();
			Receipt receipt =new Receipt();
			receipt = Receipt.builder().tenantId(receiptHeader.getTenantId()).bill(bill)
					.build();
			receipts.add(receipt);
		}

		return receipts;
	}
}
