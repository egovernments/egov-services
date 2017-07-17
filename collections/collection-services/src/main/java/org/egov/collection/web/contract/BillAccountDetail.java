package org.egov.collection.web.contract;


import java.math.BigDecimal;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Setter
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BillAccountDetail   {
  
	private String glcode;

	private Integer order;

	private String accountDescription;

	private Boolean isActualDemand;
    
	private String id;

	private String tenantId;

	private String billDetail;
	
	private BigDecimal creditAmount;

	private BigDecimal debitAmount;
	
	private Purpose purpose;
	  
}

