package org.egov.collection.indexer.contract;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.joda.time.LocalDate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Setter
@Getter
@ToString
public class BillDetail {
  
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
		
	private String tenantId;
	
	private String businessService;

	private String displayMessage;

	private Boolean callBackForApportioning;	

}

