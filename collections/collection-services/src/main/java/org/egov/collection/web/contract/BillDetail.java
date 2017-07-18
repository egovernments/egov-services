package org.egov.collection.web.contract;


import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BillDetail   {
  
	private String id;

	private String bill;

	private LocalDate billDate;
	
	private String billDescription;

	private String billNumber; //refNo

	private String consumerCode;

	private String consumerType;

	private BigDecimal minimumAmount;

	private BigDecimal totalAmount;
	
	private List<String> collectionModesNotAllowed = new ArrayList<String>();
		
	private String tenantId;
	
	private String businessService; //buisnessDetailsCode

	private String displayMessage;

	private Boolean callBackForApportioning;	
	
	private String receiptNumber;

	private Timestamp receiptDate;
	
	private String receiptType;

	private String channel;
  
	private String voucherHeader;
    
	private String collectionType;
  
	private String boundary;

	private String reasonForCancellation;
	
	private BigDecimal amountPaid;

	@JsonProperty("BillAccountDetail")
	private List<BillAccountDetail> billAccountDetails = new ArrayList<BillAccountDetail>();//for billing-service
}

