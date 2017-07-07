package org.egov.collection.web.contract;


import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;


@Setter
@Getter
@ToString
public class BillDetails   {
  
	private String id;

	private String businessDetailsCode;

	private String refNo;

	private String bill;

	private LocalDate billDate;
	
	private String billDescription;

	private String billNumber;

	private String consumerCode;

	private String consumerType;

	private BigDecimal minimumAmount;

	private BigDecimal totalAmount;
	
	private BigDecimal amountPaid;

	private List<String> collectionModesNotAllowed = new ArrayList<String>();
  
	private String event;

	private String receiptNumber;

	private Timestamp receiptDate;
	
	private String receiptType;

	private String channel;
  
	private String voucherHeader;
    
	private String collectionType;
  
	private String boundary;

	private String reasonForCancellation;
  
	@JsonProperty("BillAccountDetails")
	private List<BillAccountDetails> billAccountDetails = new ArrayList<BillAccountDetails>();
	
	private String tenantId;
	
	private String businessService;

	private String displayMessage;

	private Boolean callBackForApportioning;

	private Boolean partPaymentAllowed;

}

