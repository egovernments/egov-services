package org.egov.collection.web.contract;


import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;


@Setter
@Getter
@ToString
public class BillDetails   {
  
	private String id;

	private String bill;

	private LocalDate billDate;
	
	private String billDescription;

	private String billNumber;

	private String consumerCode;

	private String consumerType;

	private BigDecimal minimumAmount;

	private BigDecimal totalAmount;
	
	private List<String> collectionModesNotAllowed = new ArrayList<String>();
    
	@JsonProperty("BillAccountDetails")
	private List<BillAccountDetails> billAccountDetails = new ArrayList<BillAccountDetails>();//for billing-service
	
	@JsonProperty("BillAccountDetailsWrapper")
	private List<BillAccountDetailsWrapper> billAccountDetailsWrapper = new ArrayList<BillAccountDetailsWrapper>();//for collection-service
	
	private String tenantId;
	
	private String businessService;

	private String displayMessage;

	private Boolean callBackForApportioning;

	private Boolean partPaymentAllowed;
	

}

