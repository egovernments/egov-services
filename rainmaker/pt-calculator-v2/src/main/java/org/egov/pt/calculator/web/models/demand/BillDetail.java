package org.egov.pt.calculator.web.models.demand;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillDetail {

	private String id;

	private String bill;

	private Long billDate;

	private String billDescription;

	private String billNumber; // refNo

	private String consumerCode;

	private String consumerType;

	private BigDecimal minimumAmount;

	private BigDecimal totalAmount;

	private List<String> collectionModesNotAllowed;

	private String tenantId;

	private String businessService; // buisnessDetailsCode

	private String displayMessage;

	private Boolean callBackForApportioning;

	private String receiptNumber;

	private Long receiptDate;

	private String receiptType;

	private String channel;

	private String voucherHeader;

	private String boundary;

	private String reasonForCancellation;

	private BigDecimal amountPaid;

	private String cancellationRemarks;

	private String status;

	@JsonProperty("billAccountDetails")
	private List<BillAccountDetail> billAccountDetails;

	private String manualReceiptNumber;

	private Long stateId;

}