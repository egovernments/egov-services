package org.egov.collection.web.contract;


import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
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

	private String billNumber;

	private String consumerCode;

	private String consumerType;

	private BigDecimal minimumAmount;

	private BigDecimal totalAmount;
	
	private List<String> collectionModesNotAllowed = new ArrayList<String>();
    
	@JsonProperty("BillAccountDetail")
	private List<BillAccountDetail> billAccountDetails = new ArrayList<BillAccountDetail>();//for billing-service
		
	private String tenantId;
	
	private String businessService;

	private String displayMessage;

	private Boolean callBackForApportioning;	

}

