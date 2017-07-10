package org.egov.collection.web.contract;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class BillDetailsWrapper {

	@JsonProperty("BillDetails")
	private BillDetails billDetails;
	
	private String businessDetailsCode;

	private String refNo;

	private String event;

	private String receiptNumber;

	private Timestamp receiptDate;
	
	private String receiptType;

	private String channel;
  
	private String voucherHeader;
    
	private String collectionType;
  
	private String boundary;

	private String reasonForCancellation;
	
	private BigDecimal amountPaid;

}
