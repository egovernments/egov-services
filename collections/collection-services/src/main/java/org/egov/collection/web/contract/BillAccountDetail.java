package org.egov.collection.web.contract;


import java.math.BigDecimal;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode
public class BillAccountDetail   {
    
        private String billNo;

        private Long billId;

        private Long billYear;

        private Long taxId;
        
        private Long billDate;
        
        private BigDecimal currDemand;

        private BigDecimal arrDemand;

        private BigDecimal currCollection;

        private BigDecimal arrCollection;

        private BigDecimal currBalance;

        private BigDecimal arrBalance;
  
	private String glcode;

	private Integer order;

	private String accountDescription;

	private Boolean isActualDemand;
    
	private String id;

	private String tenantId;

	private String billDetail;
	
	private BigDecimal crAmountToBePaid = BigDecimal.ZERO;
	
	private BigDecimal creditAmount = BigDecimal.ZERO;

	private BigDecimal debitAmount = BigDecimal.ZERO;
	
	private Purpose purpose;
	  
}

