package org.egov.lams.web.contract;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BillReceiptInfo {

	private String billReferenceNum;
	private String event;
	private String receiptNum;
	private Date receiptDate;
	private String payeeName;
	private String payeeAddress;
	private Set<ReceiptAccountInfo> accountDetails;
	/*
	 * private Set<ReceiptInstrumentInfo> instrumentDetails; private
	 * Set<ReceiptInstrumentInfo> bouncedInstruments;
	 */
	private String serviceName;
	private String paidBy;
	private String description;
	private BigDecimal totalAmount;
	private String receiptURL;
	private String collectionType;
	private Boolean legacy;
	private String additionalInfo;
	private String source;
	private String receiptInstrumentType;
	private String tenantId = null;
	private String receiptStatus;
}
